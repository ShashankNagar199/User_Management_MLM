package com.usermanagement.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usermanagement.exceptions.ReferralException;
import com.usermanagement.exceptions.UserAlreadyExistsException;
import com.usermanagement.exceptions.UserNotFoundException;
import com.usermanagement.models.Referral;
import com.usermanagement.models.Role;
import com.usermanagement.models.User;
import com.usermanagement.repositories.ReferralRepository;
import com.usermanagement.repositories.UserRepository;

@Service
public class UserService {
	    
	    @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private ReferralRepository referralRepository;

	    public User registerUser(User user) throws UserAlreadyExistsException {
	        // validate user data
	        User existingUser = userRepository.findByEmail(user.getEmail());
	        if (existingUser != null) {
	            throw new UserAlreadyExistsException("User already exists");
	        }

	        user.setPassword(user.getPassword());
	        user.setRoles(Arrays.asList(new Role("User")));
	        return userRepository.save(user);
	    }

	    public List<User> getAllUsers() {
	        return userRepository.findAll();
	    }

	    public User getUserByEmail(String email) {
	        return userRepository.findByEmail(email);
	    }

	    public Referral recordReferral(Referral referral) throws ReferralException {
	        // validate referral data
	        User referrer = userRepository.findByEmail(referral.getReferrer().getEmail());
	        if (referrer == null) {
	            throw new ReferralException("Referrer does not exist");
	        }

	        User referredUser = userRepository.findByEmail(referral.getReferredUser().getEmail());
	        if (referredUser == null) {
	            throw new ReferralException("Referred user does not exist");
	        }

	        referral.setReferrer(referrer);
	        referral.setReferredUser(referredUser);
	        return referralRepository.save(referral);
	    }

	    public List<Referral> getReferralsByUser(String email) throws UserNotFoundException {
	        User user = userRepository.findByEmail(email);
	        if (user == null) {
	            throw new UserNotFoundException("User does not exist");
	        }

	        return referralRepository.findByReferrerOrReferredUser(user);
	    }

	    public void calculateCommissions(User user) throws UserNotFoundException {
	        // calculate commissions for the user
	        List<Referral> referrals = getReferralsByUser(user.getEmail());
	        int level1Commission = calculateCommission(user, referrals, 1);
	        int level2Commission = calculateCommission(user, referrals, 2);
	        int level3Commission = calculateCommission(user, referrals, 3);

	        // update the user's commission
	        user.setCommission(level1Commission + level2Commission + level3Commission);
	        userRepository.save(user);
	    }

	    private int calculateCommission(User user, List<Referral> referrals, int level) {
	        int commission = 0;
	        for (Referral referral : referrals) {
	            if (referral.getReferredUser().getRoles().contains(new Role("User"))) {
	                commission += calculateCommission(user, referral, level);
	            }
	        }

	        return commission;
	    }

	    private int calculateCommission(User user, Referral referral, int level) {
	        // calculate the commission for the current level
	        int commission = 0;
	        if (level == 1) {
	            commission = 10;
	        } else if (level == 2) {
	            commission = 5;
	        } else if (level == 3) {
	            commission = 2;
	        }

	        // check if the referral is from the same level as the user
	        if (referral.getReferrer().getRoles().contains(new Role("User"))) {
	            commission = 0;
	        }

	        return commission;
	    }
	}


