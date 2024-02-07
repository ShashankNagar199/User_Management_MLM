package com.usermanagement.config;

import com.usermanagement.exceptions.ValidationInputException;
import com.usermanagement.models.User;
import com.usermanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) {
        final Optional<User> userEntityByEmail = userRepository.findByEmail(email);

        if(userEntityByEmail.isEmpty()) {
            throw new ValidationInputException("Incorrect Credentials");
        }
        final User user = userEntityByEmail.get();
        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUserEntity(user);
        //if the user has default password to its account, then we are not setting last login to the user until user changes its password.
//        if (user.getLastLogin() == null) {
//            log.info("user has to change his password");
//        }else {
//            user.setLastLogin(new Date());
//        }
        userRepository.saveAndFlush(user);
        return userDetails;
    }
}

