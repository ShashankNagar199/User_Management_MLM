package com.usermanagement.controllers;


import com.usermanagement.dto.ChangePasswordWithTokenDto;
import com.usermanagement.dto.ForgotPasswordRequestDto;
import com.usermanagement.dto.UserResponseDto;
import com.usermanagement.models.User;
import com.usermanagement.models.UserVerification;
import com.usermanagement.services.ForgotPasswordService;
import com.usermanagement.services.UserService;
import com.usermanagement.services.WithdrawService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/verification")
@Validated
public class VerificationController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final WithdrawService withdrawService;

    @Value("${redirect.url}")
    private String redirectUrl;

    public VerificationController(UserService userService,WithdrawService withdrawService) {
        this.userService = userService;
        this.withdrawService=withdrawService;
    }

    @ResponseStatus(HttpStatus.OK)
//    @Operation(description = "generates token and sends to user over mail for password reset", tags = {"Forgot Password Controller"})
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "token generated and sent over mail"),
//            @ApiResponse(code = 400, message = "Invalid Request", response = ApiExceptionResponse.class
//                    , examples = @Example({@ExampleProperty(mediaType = "application/json",
//                    value = JsonExampleConstants.ERROR)}))
    //   })
    @GetMapping("/verify")
    public ResponseEntity<String> checkVerificationCode(HttpServletRequest request, @Param("code") String code , @Param("email") String email , @Param("userId") String userId) {
        return userService.checkIfVerificationCodeIsValidAndRedirectUser(code,email,request);
    }

    @ResponseStatus(HttpStatus.OK)
//    @Operation(description = "generates token and sends to user over mail for password reset", tags = {"Forgot Password Controller"})
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "token generated and sent over mail"),
//            @ApiResponse(code = 400, message = "Invalid Request", response = ApiExceptionResponse.class
//                    , examples = @Example({@ExampleProperty(mediaType = "application/json",
//                    value = JsonExampleConstants.ERROR)}))
    //   })
    @GetMapping("/generateCode")
    public String generateVerificationCode(HttpServletRequest request, @Param("email") String email , @Param("userId") String userId) {
        UserVerification verificationCodeEntity=withdrawService.saveVerificationCodeForWithdraw(userId);
        userService.generateEmailValidation(userId,request);
        return verificationCodeEntity.getVerificationCode();
    }

}

