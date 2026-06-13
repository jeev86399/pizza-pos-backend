package com.pizza.pos.service;

import com.pizza.pos.model.Customer;
import com.pizza.pos.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepo;

    // ✅ Inject Mail Service (IMPORTANT: name must match your class)
    @Autowired
    private MailService mailService;

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
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }

    // ================= FORGOT PASSWORD =================
    public String processForgotPassword(String email) {
        try {
            Customer customer = customerRepo.findByEmailID(email);

            if (customer == null) {
                return "USER_NOT_FOUND";
            }

            // ✅ Generate OTP
            String otp = generateOtp();

            // ✅ Save OTP
            customer.setOtp(otp);
            customerRepo.save(customer);

            // ✅ Send Email (MAIN STEP)
            mailService.sendOtpEmail(email, otp);

            return "OTP_SENT";

        } catch (Exception e) {
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

            if (customer.getOtp() != null && customer.getOtp().equals(enteredOtp)) {
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
            customer.setOtp(null); // clear OTP

            customerRepo.save(customer);

            return "PASSWORD_UPDATED";

        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
    }
}