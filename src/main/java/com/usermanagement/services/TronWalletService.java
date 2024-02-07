package com.usermanagement.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TronWalletService {
    private final String baseUrl = "http://154.19.187.84:4005/api/wallet/generate-cust-wallet-usdt";
    @Value("${tron.api.key}")
    private String apiKey;

    public String generateWallet(String userId) {
        try {
            // Set up the request headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("apiKey", apiKey);

            // Set up the request body
            String requestBody = "{\"user_id\":\"" + userId + "\"}";

            // Create the request entity
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            // Create a RestTemplate
            RestTemplate restTemplate = new RestTemplate();

            // Make the API call
            ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity, String.class);

            // Check the response status
            if (response.getStatusCode() == HttpStatus.OK) {
                return extractAddressFromJson(response.getBody());
            } else {
                // Handle other HTTP status codes
                System.out.println("API call failed with status code: " + response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
            return null;
        }
    }

//    private String extractAddressFromJson(String jsonResponse) {
//        // You can use a JSON parsing library (e.g., Jackson or Gson) for a more robust solution
//        // Here, a simple substring is used for illustration purposes
//        int startIndex = jsonResponse.indexOf("\"address\":") + 12;
//        int endIndex = jsonResponse.indexOf("\"", startIndex);
//        return jsonResponse.substring(startIndex, endIndex);
//    }
private String extractAddressFromJson(String jsonResponse) {
    try {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);
        return jsonNode.path("data").path("address").asText();
    } catch (Exception e) {
        System.err.println("Error parsing JSON: " + e.getMessage());
        return null;
    }
}
}
