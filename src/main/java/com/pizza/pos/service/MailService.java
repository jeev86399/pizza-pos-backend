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

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    public void sendOtpEmail(String toEmail, String otp) {
        
        System.out.println("DEBUG: Sending email via Brevo API to: " + toEmail);
        
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.brevo.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", brevoApiKey);
        headers.set("accept", "application/json");

        // Use the email you signed up with for Brevo as the sender
        Map<String, Object> sender = new HashMap<>();
        sender.put("name", "Pizza POS");
        sender.put("email", "sj631957@gmail.com"); 

        Map<String, Object> to = new HashMap<>();
        to.put("email", toEmail);

        Map<String, Object> body = new HashMap<>();
        body.put("sender", sender);
        body.put("to", List.of(to));
        body.put("subject", "Your Password Reset OTP");
        body.put("htmlContent", "<h2>Pizza Ordering System</h2><p>Your OTP is: <strong>" + otp + "</strong></p>");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(url, request, String.class);
            System.out.println("DEBUG: Brevo API Success!");
        } catch (Exception e) {
            System.err.println("DEBUG: Brevo API Failed.");
            e.printStackTrace();
        }
    }
}
