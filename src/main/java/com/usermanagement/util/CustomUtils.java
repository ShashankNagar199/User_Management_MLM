package com.usermanagement.util;

import org.springframework.stereotype.Service;

import java.io.Serializable;
import net.bytebuddy.utility.RandomString;

@Service
public class CustomUtils implements Serializable {

    //Generate token
    public String generateRandomToken() {
        return RandomString.make(64);
    }
    public Long convertStringToLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public Double convertStringToDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
