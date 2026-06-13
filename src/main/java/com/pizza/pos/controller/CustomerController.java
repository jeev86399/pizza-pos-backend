package com.pizza.pos.controller;

import com.pizza.pos.model.Customer;
import com.pizza.pos.repository.CustomerRepository;
import com.pizza.pos.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
//@CrossOrigin(origins = "*")
public class CustomerController {

    private String emailForReset;
    private String generatedOTP; 

    private final AdminController adminController;
    private final CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    // Constructor injection
    CustomerController(CustomerRepository customerRepository, AdminController adminController) {
        this.customerRepository = customerRepository;
        this.adminController = adminController;
    }

    // ================= REGISTER =================
    @PostMapping("/register")
    public String register(@RequestBody Customer customer) {
        return customerService.registerCustomer(customer);
    }

    // ================= LOGIN (UPDATED + SAFE) =================
    @PostMapping("/login")
    public String login(@RequestBody Customer loginData) {

        // Debug logs (visible in Railway logs)
        System.out.println("--- LOGIN ATTEMPT ---");
        System.out.println("Received Email: " + loginData.getEmailID());
        System.out.println("Received Password: " + loginData.getPassword());

        return customerRepository.findAll().stream()
                .filter(user ->
                        user.getEmailID() != null &&
                        loginData.getEmailID() != null &&
                        user.getEmailID().equalsIgnoreCase(loginData.getEmailID()) &&
                        user.getPassword() != null &&
                        user.getPassword().equals(loginData.getPassword())
                )
                .findFirst()
                .map(user -> "SUCCESS")
                .orElse("FAIL");
    }

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {

        return customerRepository.findAll().stream()
                .filter(c -> c.getEmailID() != null &&
                             c.getEmailID().equalsIgnoreCase(email))
                .findFirst()
                .map(user -> {

                    generatedOTP = String.valueOf((int)((Math.random() * 900000) + 100000));
                    emailForReset = email;

                    System.out.println("OTP for " + email + " is: " + generatedOTP);

                    return ResponseEntity.ok("OTP sent to your email!");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found"));
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String otp,
                                                @RequestParam String newPassword) {

        if (generatedOTP != null && generatedOTP.equals(otp)) {

            return customerRepository.findAll().stream()
                    .filter(c -> c.getEmailID() != null &&
                                 c.getEmailID().equalsIgnoreCase(emailForReset))
                    .findFirst()
                    .map(user -> {
                        user.setPassword(newPassword);
                        customerRepository.save(user);
                        generatedOTP = null;

                        return ResponseEntity.ok("Password changed successfully!");
                    })
                    .orElse(ResponseEntity.badRequest().body("Error updating password"));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP!");
    }

    // ================= VIEW PROFILE =================
    @GetMapping("/viewProfile/{email}")
    public ResponseEntity<Customer> viewProfile(@PathVariable String email) {

        return customerRepository.findAll().stream()
                .filter(user -> user.getEmailID() != null &&
                                user.getEmailID().equalsIgnoreCase(email))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= UPDATE PROFILE =================
    @PutMapping("/updateProfile/{email}")
    public ResponseEntity<String> updateProfile(@PathVariable String email,
                                                @RequestBody Customer updatedData) {

        return customerRepository.findAll().stream()
                .filter(user -> user.getEmailID() != null &&
                                user.getEmailID().equalsIgnoreCase(email))
                .findFirst()
                .map(customer -> {

                    // Personal Info
                    customer.setFirstName(updatedData.getFirstName());
                    customer.setLastName(updatedData.getLastName());
                    customer.setGender(updatedData.getGender());
                    customer.setDateOfBirth(updatedData.getDateOfBirth());

                    // Address Info
                    customer.setStreet(updatedData.getStreet());
                    customer.setCity(updatedData.getCity());
                    customer.setState(updatedData.getState());
                    customer.setPincode(updatedData.getPincode());
                    customer.setMobileNo(updatedData.getMobileNo());

                    customerRepository.save(customer);

                    return ResponseEntity.ok("Profile Updated Successfully!");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }
}