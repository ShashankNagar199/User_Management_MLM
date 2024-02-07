package com.usermanagement.services;

import com.usermanagement.dto.ChangePasswordWithTokenDto;
import com.usermanagement.dto.ForgotPasswordRequestDto;
import com.usermanagement.exceptions.ValidationInputException;
import com.usermanagement.models.*;
import com.usermanagement.repositories.PasswordResetTokenRepository;
import com.usermanagement.repositories.UserRepository;
import com.usermanagement.util.sendMailUtils;
import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
//@Slf4j
public class ForgotPasswordService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private sendMailUtils sendMailUtils;


    @Value("${mlm.token.validation.url}")
    private String requestMappingForTokenValidation;
    public void generatePasswordResetToken(final ForgotPasswordRequestDto requestDto,
                                           final HttpServletRequest request) {

        final String userId = requestDto.getUserId();
        final String userEmail=requestDto.getEmail();
        //log.info("reset password :: get user on the basis of entered email : {}", userEmail);
        final User userEntityByEmail = userRepository.findByUserId(userId);

        //extract out the base url of the request from the current request
        final StringBuffer requestUrl = request.getRequestURL();
        final String baseUrl = requestUrl.substring(0, requestUrl.lastIndexOf("/"));

        //populate token entity and save in the DB
        PasswordResetToken tokenEntity = new PasswordResetToken();
        final String stringToken = UUID.randomUUID().toString();
        tokenEntity.setToken(stringToken);
        tokenEntity.setUser(userEntityByEmail);
        tokenEntity.setExpiryDate(LocalDateTime.now().plusDays(1));
        tokenEntity.setIsActive("Y");
        passwordResetTokenRepository.save(tokenEntity);
  //      log.debug("token entity has been saved in DB");

        //prepare url to be sent to user
        final String prepareUrlForMail = baseUrl + requestMappingForTokenValidation +stringToken;

    //    log.info("sending mail to the user : {}", userEmail);
        sendMailByCreatingThread(userEmail, prepareUrlForMail);
    }

    public User resetPasswordWithToken(final ChangePasswordWithTokenDto requestDto) {
        final PasswordResetToken passwordResetTokenEntity = checkIfTokenValid(requestDto.getToken());
        final User userEntityFromToken = passwordResetTokenEntity.getUser();
        userEntityFromToken.setPassword(encodePassword(requestDto.getNewPassword()));

        //log.info("resetting user password for user : '{}'",userEntityFromToken.getEmail());
        final User savedEntity = userRepository.saveAndFlush(userEntityFromToken);

      //  log.info("password reset complete. Deactivating the token now");
        passwordResetTokenEntity.setIsActive("N");
        passwordResetTokenRepository.saveAndFlush(passwordResetTokenEntity);

        //return the user response data
       // return userEntityDtoMapper.userEntityToDto(savedEntity);
        return savedEntity;
    }
    private String encodePassword(final String password){
        return passwordEncoder.encode(password);
    }
    public PasswordResetToken checkIfTokenValid(final String token) {
        final Optional<PasswordResetToken> byToken = passwordResetTokenRepository.findByToken(token);
        if (!byToken.isPresent()) {
          //  log.error("no entity exists for requested token");
            throw new ValidationInputException("TOKEN_NOT_FOUND");
        }
        final PasswordResetToken passwordResetTokenEntity = byToken.get();
        //if token is inactive (token.active == 'N') then it has been already used -> throw exception
        if (passwordResetTokenEntity.getIsActive().equals("IS_INACTIVE")){
            //log.error("token was already used. rejecting request");
            throw new ValidationInputException("TOKEN_ALREADY_USED");
        }
        //if the token time has expired - throw exception
        if (LocalDateTime.now().isAfter(passwordResetTokenEntity.getExpiryDate())){
            //log.error("token expired. rejecting request");
            throw new ValidationInputException("TOKEN_EXPIRED");
        }

        //log.debug("token entity found for given token and is mapped to '{}'",passwordResetTokenEntity.getUser().getEmail());
        return passwordResetTokenEntity;
    }
    private void sendMailByCreatingThread(String userEmail, String prepareUrlForMail) {
        final Runnable sendMail = () -> {
            // sendMailUtility.mailTokenToUser(prepareUrlForMail, userEmail);
            sendMailUtils.sendMail(prepareUrlForMail,userEmail,"emails/forgot-password","Reset your password");
          //  log.info("mail sent to the user");
        };
        Thread sendMailThread = new Thread(sendMail);
        sendMailThread.start();
    }
}
