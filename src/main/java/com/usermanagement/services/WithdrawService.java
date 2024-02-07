package com.usermanagement.services;

import com.usermanagement.models.User;
import com.usermanagement.models.UserVerification;
import com.usermanagement.repositories.UserRepository;
import com.usermanagement.repositories.UserVerificationRepository;
import com.usermanagement.util.CustomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class WithdrawService {

    @Autowired
    private UserVerificationRepository userVerificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomUtils customUtils;

    private final String withdrawTronUrl = "http://localhost:4005/api/wallet/withdraw-tron";

    @Autowired
    private RestTemplate restTemplate;

    public UserVerification saveVerificationCodeForWithdraw(String userId){
        User user=userRepository.findByUserId(userId);
        UserVerification userVerificationEntity = new UserVerification();
        String randomCode = customUtils.generateRandomToken();
        userVerificationEntity.setVerificationCode(randomCode);
        userVerificationEntity.setUser(user);
        userVerificationEntity.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        userVerificationEntity.setIsVerified("N");
        return userVerificationRepository.saveAndFlush(userVerificationEntity);
    }
    public String withdrawTron(String fromAddress, String toAddress, String amount) {
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create request body
        String requestBody = String.format("{\"from_address\":\"%s\",\"to_address\":\"%s\",\"amount\":\"%s\"}", fromAddress, toAddress, amount);

        // Create request entity
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Make the HTTP request
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                withdrawTronUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Check if the request was successful (HTTP status 200)
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // Parse the response or return it as needed
            return responseEntity.getBody();
        } else {
            // Handle error scenarios
            System.err.println("Error: " + responseEntity.getStatusCode());
            return null;
        }
    }
}
