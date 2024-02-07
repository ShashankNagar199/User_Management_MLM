package com.usermanagement.controllers;

import java.util.List;

import com.usermanagement.dto.LoginUserRequest;
import com.usermanagement.dto.LoginUserResponse;
import com.usermanagement.dto.UserCreationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.usermanagement.exceptions.UserNotFoundException;
import com.usermanagement.models.Referral;
import com.usermanagement.models.User;
import com.usermanagement.services.UserService;

@RestController
@RequestMapping(value="/api",produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class UserController {

	@Value("${spring.datasource.password}")
	private Long dbPass;

	    @Autowired
	    private UserService userService;
		@PostMapping(value="/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<User> registerUser(@RequestBody UserCreationDto user) throws Exception {
			return new ResponseEntity<>(userService.registerUser(user), HttpStatus.CREATED);
	    }

		@PostMapping(value="/login")
		public ResponseEntity<LoginUserResponse> generateToken(@RequestBody LoginUserRequest loginUserRequest) {
		final LoginUserResponse loginUserResponse = userService.login(loginUserRequest);
		return new ResponseEntity<>(loginUserResponse, HttpStatus.OK);
		}

	    @GetMapping("/allUsers")
	    public List<User> getAllUsers() {
	        return userService.getAllUsers();
	    }

	    @GetMapping("/users/{userId}")
	    public User getUserByEmail(@PathVariable String userId) {
	        return userService.getUserByUserId(userId);
	    }

	    @GetMapping("/referrals/{email}")
	    public List<Referral> getReferralsByUser(@PathVariable String email) throws UserNotFoundException {
	        return userService.getReferralsByUser(email);
	    }

//	    @PostMapping("/calculate-commission")
//	    public void calculateCommission(@RequestBody User user) throws UserNotFoundException {
//	        userService.calculateCommissions(user);
//	    }
}
