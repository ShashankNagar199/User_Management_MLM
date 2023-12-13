package com.usermanagement.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usermanagement.exceptions.ReferralException;
import com.usermanagement.exceptions.UserAlreadyExistsException;
import com.usermanagement.exceptions.UserNotFoundException;
import com.usermanagement.models.Referral;
import com.usermanagement.models.User;
import com.usermanagement.services.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	    @Autowired
	    private UserService userService;

	    @PostMapping("/users")
	    public User registerUser(@RequestBody User user) throws UserAlreadyExistsException {
	        return userService.registerUser(user);
	    }

	    @GetMapping("/users")
	    public List<User> getAllUsers() {
	        return userService.getAllUsers();
	    }

	    @GetMapping("/users/{email}")
	    public User getUserByEmail(@PathVariable String email) {
	        return userService.getUserByEmail(email);
	    }

	    @PostMapping("/referrals")
	    public Referral recordReferral(@RequestBody Referral referral) throws ReferralException {
	         return userService.recordReferral(referral);
	    }

	    @GetMapping("/referrals/{email}")
	    public List<Referral> getReferralsByUser(@PathVariable String email) throws UserNotFoundException {
	        return userService.getReferralsByUser(email);
	    }

	    @PostMapping("/calculate-commission")
	    public void calculateCommission(@RequestBody User user) throws UserNotFoundException {
	        userService.calculateCommissions(user);
	    }

	    @GetMapping("/commission/{email}")
	    public int getCommissionByUser(@PathVariable String email) {
	        User user = userService.getUserByEmail(email);
	        return user.getCommission();
	    }
	
}
