package com.pizza.pos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    public void sendOtpEmail(String toEmail, String otp) {
        
        System.out.println("DEBUG: Sending email via Resend API to: " + toEmail);
        
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.resend.com/emails";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(resendApiKey);

        Map<String, Object> body = new HashMap<>();
        // Note: Using the free testing email address provided by Resend
        body.put("from", "Pizza POS <onboarding@resend.dev>");
        body.put("to", List.of(toEmail)); 
        body.put("subject", "Your Password Reset OTP");
        body.put("html", "<h2>Pizza Ordering System</h2><p>Your OTP is: <strong>" + otp + "</strong></p>");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(url, request, String.class);
            System.out.println("DEBUG: Resend API Success!");
        } catch (Exception e) {
            System.err.println("DEBUG: Resend API Failed.");
            e.printStackTrace();
        }
    }
}
