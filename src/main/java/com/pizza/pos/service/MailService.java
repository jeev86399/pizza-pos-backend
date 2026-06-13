package com.pizza.pos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {

        System.out.println("MAIL SERVICE STARTED");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Pizza Ordering System - Password Reset OTP");
        message.setText("Your OTP is: " + otp);

        mailSender.send(message);

        System.out.println("MAIL SENT SUCCESSFULLY");
    }
}