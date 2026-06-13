package com.pizza.pos.service;

import com.pizza.pos.model.Customer;
import com.pizza.pos.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CustomerService {

    private final CustomerRepository customerRepo;
    private final MailService mailService;

    @Autowired
    public CustomerService(CustomerRepository customerRepo,
                           MailService mailService) {
        this.customerRepo = customerRepo;
        this.mailService = mailService;
    }

    // ================= REGISTER =================
    public String registerCustomer(Customer customer) {
        try {
            long count = customerRepo.count();

            String firstName = customer.getFirstName();
            String prefix;

            if (firstName != null && firstName.length() >= 2) {
                prefix = firstName.substring(0, 2).toUpperCase();
            } else if (firstName != null && firstName.length() == 1) {
                prefix = firstName.toUpperCase() + "X";
            } else {
                prefix = "US";
            }

            String generatedId = prefix + (1001 + count);
            customer.setUserId(generatedId);

            customerRepo.save(customer);

            return "SUCCESS";

        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
    }

    // ================= GENERATE OTP =================
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // ================= FORGOT PASSWORD =================
    public String processForgotPassword(String email) {

        System.out.println("DEBUG: Entering processForgotPassword for: " + email);

        try {

            Customer customer = customerRepo.findByEmailID(email);

            if (customer == null) {
                System.out.println("DEBUG: Customer not found");
                return "USER_NOT_FOUND";
            }

            String otp = generateOtp();
            System.out.println("DEBUG: Generated OTP = " + otp);

            customer.setOtp(otp);
            customerRepo.save(customer);

            System.out.println("DEBUG: OTP saved to database");
            System.out.println("DEBUG: About to call mailService.sendOtpEmail");

            try {

                mailService.sendOtpEmail(email, otp);

                System.out.println("DEBUG: MailService call finished");

            } catch (Exception e) {

                System.err.println("DEBUG: MailService call THREW AN EXCEPTION");
                e.printStackTrace();
            }

            return "OTP_SENT";

        } catch (Exception e) {

            System.err.println("DEBUG: processForgotPassword FAILED");
            e.printStackTrace();

            return "FAIL";
        }
    }

    // ================= VERIFY OTP =================
    public String verifyOtp(String email, String enteredOtp) {
        try {

            Customer customer = customerRepo.findByEmailID(email);

            if (customer == null) {
                return "USER_NOT_FOUND";
            }

            if (customer.getOtp() != null &&
                customer.getOtp().equals(enteredOtp)) {

                return "OTP_VALID";

            } else {

                return "INVALID_OTP";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
    }

    // ================= RESET PASSWORD =================
    public String resetPassword(String email, String newPassword) {
        try {

            Customer customer = customerRepo.findByEmailID(email);

            if (customer == null) {
                return "USER_NOT_FOUND";
            }

            customer.setPassword(newPassword);
            customer.setOtp(null);

            customerRepo.save(customer);

            return "PASSWORD_UPDATED";

        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
    }
}