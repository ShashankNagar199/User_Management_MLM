package com.usermanagement.controllers;

import com.usermanagement.dto.ChangePasswordWithTokenDto;
import com.usermanagement.dto.ForgotPasswordRequestDto;
import com.usermanagement.dto.UserResponseDto;
import com.usermanagement.models.User;
import com.usermanagement.services.ForgotPasswordService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/forgotPassword")
@Validated
public class ForgotPasswordController {
    private final ForgotPasswordService forgotPasswordService;

    @Value("${redirect.url}")
    private String redirectUrl;

    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    @ResponseStatus(HttpStatus.OK)
//    @Operation(description = "generates token and sends to user over mail for password reset", tags = {"Forgot Password Controller"})
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "token generated and sent over mail"),
//            @ApiResponse(code = 400, message = "Invalid Request", response = ApiExceptionResponse.class
//                    , examples = @Example({@ExampleProperty(mediaType = "application/json",
//                    value = JsonExampleConstants.ERROR)}))
 //   })
    @PostMapping("/begin")
    public ResponseEntity<?> generatePasswordResetToken(@RequestBody final ForgotPasswordRequestDto requestDto,
                                                        final HttpServletRequest request) {
        forgotPasswordService.generatePasswordResetToken(requestDto, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
//    @Operation(description = "resets the password", tags = {"Forgot Password Controller"})
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "password has been reset", response = UserResponseDto.class
//                    , examples = @Example({@ExampleProperty(
//                    mediaType = "application/json", value = JsonExampleConstants.USER_PASSWORD_CHANGED)}) ),
//            @ApiResponse(code = 400, message = "Invalid Request", response = ApiExceptionResponse.class
//                    , examples = @Example({@ExampleProperty(mediaType = "application/json",
//                    value = JsonExampleConstants.ERROR)}))
//    })
    @PostMapping("/reset")
    public ResponseEntity<?> generatePasswordResetToken(@RequestBody ChangePasswordWithTokenDto requestDto) {
        final User userResponseDto = forgotPasswordService.resetPasswordWithToken(requestDto);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
//    @Operation(description = "checks if the token is valid and associated to an account", tags = {"Forgot Password Controller"})
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "valid token"),
//            @ApiResponse(code = 400, message = "Invalid Request", response = ApiExceptionResponse.class
//                    , examples = @Example({@ExampleProperty(mediaType = "application/json",
//                    value = JsonExampleConstants.ERROR)}))
//    })
    @GetMapping("/validate/{token}")
    public ResponseEntity<String> checkToken(@PathVariable("token") final String token) {
        forgotPasswordService.checkIfTokenValid(token);
        //using redirect.url property to find out if the request came in for Dev Env, Prod Env or Local Env
        //And redirecting the UI accordingly
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl+"/api/forgotPassword/validate/"+token)).build();
    }
}
