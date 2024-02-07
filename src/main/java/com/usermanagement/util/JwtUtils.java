package com.usermanagement.util;

import com.usermanagement.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.function.Function;

public class JwtUtils implements Serializable {
    private static final long serialVersionUID = 234234523523L;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expirationDateInMs}")
    private Long expirationDateInMs;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }


    //generate token for user
    public String generateToken(UserDetails userDetails, User user) {
        Map<String, Object> claims = new HashMap<>();
//        if(user.getRole() != null && (user.getRole().getName().equalsIgnoreCase("ADMIN") ||
//                user.getRole().getName().equalsIgnoreCase("USER")))
//            return doGenerateTokenWithFiniteExpiration(claims, userDetails.getUsername());
        return doGenerateToken(claims, userDetails.getUsername());
    }


    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expirationDateInMs * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    private String doGenerateTokenWithFiniteExpiration(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationDateInMs * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }


    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername());
    }
}
