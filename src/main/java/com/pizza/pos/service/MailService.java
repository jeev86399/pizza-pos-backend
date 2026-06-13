package com.pizza.pos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtpEmail(String to, String otp) {

        try {

            System.out.println("=================================");
            System.out.println("MAIL SERVICE STARTED");
            System.out.println("TO: " + to);
            System.out.println("FROM: " + fromEmail);
            System.out.println("OTP: " + otp);

            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("Pizza Ordering System - Password Reset OTP");
            message.setText(
                    "Dear User,\n\n" +
                    "Your OTP for password reset is: " + otp +
                    "\n\nThis OTP is valid for a limited time.\n\n" +
                    "Regards,\nPizza Ordering System"
            );

            System.out.println("Attempting to send email...");

            mailSender.send(message);

            System.out.println("MAIL SENT SUCCESSFULLY");
            System.out.println("=================================");

        } catch (Exception e) {

            System.err.println("=================================");
            System.err.println("FATAL MAIL ERROR");
            System.err.println("------------------------");

            e.printStackTrace();

            System.err.println("------------------------");
            System.err.println("=================================");
        }
    }
}