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

    // Keeping this to support your React frontend
    private String emailForReset;

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
        System.out.println("--- LOGIN ATTEMPT ---");
        System.out.println("Received Email: " + loginData.getEmailID());
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
        System.out.println("--- CONTROLLER: FORGOT PASSWORD ENDPOINT HIT ---");
        
        // Save email temporarily for the reset step
        this.emailForReset = email; 

        // CALLING YOUR ACTUAL SERVICE HERE!
        String status = customerService.processForgotPassword(email);

        if ("OTP_SENT".equals(status)) {
            return ResponseEntity.ok("OTP sent to your email!");
        } else if ("USER_NOT_FOUND".equals(status)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request");
        }
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String otp,
                                                @RequestParam String newPassword) {

        System.out.println("--- CONTROLLER: RESET PASSWORD ENDPOINT HIT ---");

        if (emailForReset == null) {
            return ResponseEntity.badRequest().body("Session expired. Please request OTP again.");
        }

        // Verify OTP using your service
        String verifyStatus = customerService.verifyOtp(emailForReset, otp);

        if ("OTP_VALID".equals(verifyStatus)) {
            
            // Update the password using your service
            String resetStatus = customerService.resetPassword(emailForReset, newPassword);

            if ("PASSWORD_UPDATED".equals(resetStatus)) {
                emailForReset = null; // Clear it out after success
                return ResponseEntity.ok("Password changed successfully!");
            } else {
                return ResponseEntity.badRequest().body("Error updating password");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP!");
        }
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
                    customer.setFirstName(updatedData.getFirstName());
                    customer.setLastName(updatedData.getLastName());
                    customer.setGender(updatedData.getGender());
                    customer.setDateOfBirth(updatedData.getDateOfBirth());
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
