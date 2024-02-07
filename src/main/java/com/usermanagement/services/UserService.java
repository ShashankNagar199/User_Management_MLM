package com.usermanagement.services;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.usermanagement.dto.LoginUserRequest;
import com.usermanagement.dto.LoginUserResponse;
import com.usermanagement.dto.UserCount;
import com.usermanagement.dto.UserCreationDto;
import com.usermanagement.exceptions.*;
import com.usermanagement.models.UserRank;
import com.usermanagement.repositories.UserRankRepository;
import com.usermanagement.util.*;
import com.usermanagement.models.UserVerification;
import com.usermanagement.repositories.UserVerificationRepository;
import com.usermanagement.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.usermanagement.models.Referral;
import com.usermanagement.models.User;
import com.usermanagement.repositories.ReferralRepository;
import com.usermanagement.repositories.UserRepository;
import org.springframework.web.bind.annotation.RequestBody;

//import javax.transaction.Transactional;

@Service
public class UserService {
	    
	    @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private ReferralRepository referralRepository;

	    @Autowired
		private  BCryptPasswordEncoder passwordEncoder;

		@Autowired
		private  UserDetailsService userDetailsService;

		@Autowired
		private UserVerificationRepository userVerificationRepository;

		@Autowired
		private UserRankRepository userRankRepository;
        @Autowired
        private sendMailUtils sendMailUtils;

		@Autowired
		private  AuthenticationManager authenticationManager;

		@Autowired
		private  JwtUtils jwtUtil;

		@Autowired
		private WalletService walletService;
		@Autowired
		private TronWalletService tronWalletService;

        @Value("${redirect.url}")
        private String redirectUrl;
        @Value("${mlm.account.verification.url}")
        private String requestMappingForEmailValidation;

	public LoginUserResponse login(LoginUserRequest loginUserRequest) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginUserRequest.getUserId(),
							loginUserRequest.getPassword()
					)
			);
		} catch (BadCredentialsException e) {
			throw new InvalidAuthenticationException("INVALID CREDENTIALS", e);
		}
		User user = userRepository.findByUserId(loginUserRequest.getUserId());
		final UserDetails userDetails
				= userDetailsService.loadUserByUsername(loginUserRequest.getUserId());

		return new LoginUserResponse(jwtUtil.generateToken(userDetails,user));
	}

		@Transactional
	    public User registerUser(final @RequestBody UserCreationDto userdto) throws Exception {
	        // validate user data
			User user=new User();
	        User existingUser = userRepository.findByEmailId(userdto.getEmail());
			User existingUserId = userRepository.findByUserId(userdto.getUserId());
	        if (existingUser != null) {
	            throw new UserAlreadyExistsException("User already exists");
	        }
			if(existingUserId != null){
				throw new UserAlreadyExistsException("User id already exists");
			}
//			if (!userdto.getPassword().equals(userdto.getConfirmPassword())) {
//				throw new PasswordsMismatchException("Passwords do not match");
//			}
            user.setUserId(userdto.getUserId());
			user.setEmail(userdto.getEmail());
	        user.setPassword(encodePassword(userdto.getPassword()));
			//user.setPhone(userdto.getPhone());
			String referrerUserId = userdto.getReferrer();
//			User referrer = userRepository.findByUserId(referrerUserId);
//			if(referrer == null){
//				throw new UserAlreadyExistsException("Referrer user not found with id: " + referrerUserId);
//			}
//			user.setReferrer(referrerUserId);
			user.setReferrer(user.getReferrer());
			String walletAddress=tronWalletService.generateWallet(userdto.getUserId());
	        //  user.setRoles(Arrays.asList(new Role("User")));
			if(walletAddress==null){
				throw new UnableToCreateWalletAddress("Unable to create wallet address");
			}
			user.setWalletAddress(walletAddress);
			User referrerUser = userRepository.findByUserId(user.getReferrer());
			user.setLevel(referrerUser.getLevel()+1);
			user.setLevel(1);
            //Referral r= recordReferral(user.getUserId(),user.getReferrer());
			user.setBalance(0.0);
			user.setSales(0.0);
			UserCount usersCount=userRepository.getCountOfAllUsers();
			UserRank entryRank = userRankRepository.findUserRankById(6L);
			user.setUserRank(entryRank);
	        return userRepository.save(user);
	    }

	private String encodePassword(final String password){
		return passwordEncoder.encode(password);
	}
	private User getUserEntityFromUserId(final Long userId) {
		//retrieve user to be deleted using userId
		final Optional<User> userEntityById = userRepository.findById(userId);
		if (userEntityById.isEmpty()){
			//log.error("No user found with id '{}' exists", userId);
			throw new ValidationInputException("USER_NOT_FOUND");
		}
		return userEntityById.get();
	}

	    public List<User> getAllUsers() {
	        return userRepository.findAll();
	    }

	    public User getUserByUserId(String userId) {
	        return userRepository.findByUserId(userId);
	    }

	    public Referral recordReferral (String referredUser ,String referrer) throws ReferralException {
	        // validate referral data
	        User checkReferrer = userRepository.findByUserId(referrer);
	        if (checkReferrer == null) {
	            throw new ReferralException("Referrer does not exist");
	        }
            Referral referral=new Referral();
	        referral.setReferrer(referrer);
	        referral.setReferredUser(referredUser);
			referral.setReferralDate(LocalDateTime.now());
	        return referral;
	    }

	    public List<Referral> getReferralsByUser(String email) throws UserNotFoundException {
	        User user = userRepository.findByEmailId(email);
	        if (user == null) {
	            throw new UserNotFoundException("User does not exist");
	        }

	        return referralRepository.findByReferrerOrReferredUser(user.getUserId());
	    }

	public ResponseEntity<String> checkIfVerificationCodeIsValidAndRedirectUser(final String code, final String userEmail, HttpServletRequest request) {
		User user = userRepository.findByEmailId(userEmail);
		UserVerification userVerificationEntity = getUserVerificationEntityByUserId(user.getUserId());
		boolean userVerified = false;
		if (userVerificationEntity == null) {
		//	log.error("no entity exists for requested code");
			throw new ValidationInputException("USER_NOT_FOUND");
		}

		//if user is not verified (code.isVerified == 'Y') then it has been already verified -> throw exception
		if (userVerificationEntity.getIsVerified().equals("Y")) {
		//	log.error("User account is already verified. rejecting request");
			throw new ValidationInputException("USER_ALREADY_VERIFIED");
		}
		//if the verification link time has expired - throw exception
		if (LocalDateTime.now().isAfter(userVerificationEntity.getExpiryDate())) {
			//log.error("Verification code expired. rejecting request");
			userVerificationRepository.delete(userVerificationEntity);
			throw new ValidationInputException("VERIFICATION_LINK_EXPIRED");
		}
		if (userVerificationEntity.getVerificationCode().equals(code) &&
				userVerificationEntity.getIsVerified().equals("N") &&
				LocalDateTime.now().isBefore(userVerificationEntity.getExpiryDate())) {
//			user.setLastLogin(new Date());
//			user.setVerifiedOn(LocalDateTime.now());
			userVerificationRepository.delete(userVerificationEntity);
		//	log.debug("Email validation done for : {}", userVerificationEntity.getUser().getEmail());
			userVerified=true;
		}
		//extract out the request url to find out if the request came in for Dev Env or Prod Env
		//And redirecting the UI accordingly
		final StringBuffer requestUrl = request.getRequestURL();
		if (userVerified) {
			return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl+"/api/verifiedUser/"+userEmail)).build();
		} else {
			return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl+"/registration")).build();
		}
	}

	public void generateEmailValidation(final String userId,
										final HttpServletRequest request) {

		User user=userRepository.findByUserId(userId);
		final String userEmail = user.getEmail();

		//extract out the base url of the request from the current request
		final StringBuffer requestUrl = request.getRequestURL();

		final String baseUrl = requestUrl.substring(0, requestUrl.lastIndexOf("/"));
		//final Long userId = userResponseDto.getId();

		UserVerification userVerificationEntity = getUserVerificationEntityByUserId(userId);
		final String verificationCode = userVerificationEntity.getVerificationCode();

		//prepare url to be sent to user
		final String prepareUrlForMail = baseUrl + requestMappingForEmailValidation + verificationCode + "&&email=" + userEmail;

		//log.info("sending mail to the user : {}", userEmail);
		sendVerificationEmail(userEmail, prepareUrlForMail);
	}

	public UserVerification getUserVerificationEntityByUserId(String userId) {
		final Optional<UserVerification> userVerificationEntity = userVerificationRepository.findByUserId(userId);
		if (userVerificationEntity.isEmpty()) {
			//log.error("No user exists for given user id : '{}' ", userId);
			throw new ValidationInputException("USER_NOT_FOUND");
		}
		return userVerificationEntity.get();
	}

    private void sendVerificationEmail(String userEmail, String prepareUrlForMail) {
        final Runnable sendMail = () -> {
            sendMailUtils.sendMail(prepareUrlForMail, userEmail,"emails/verify-email","Please verify your account");
           // log.info("mail sent to the user");
        };
        Thread sendMailThread = new Thread(sendMail);
        sendMailThread.start();
    }
}


