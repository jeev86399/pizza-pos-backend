package com.pizza.pos.controller;

import com.pizza.pos.model.Customer;
import com.pizza.pos.repository.CustomerRepository;
import com.pizza.pos.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.Optional;

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

    // Constructor injection for repository
    CustomerController(CustomerRepository customerRepository, AdminController adminController) {
        this.customerRepository = customerRepository;
		this.adminController = adminController;
    }

    @PostMapping("/register")
    public String register(@RequestBody Customer customer) {
        return customerService.registerCustomer(customer);
    }

    @PostMapping("/login")
    public String login(@RequestBody Customer loginData) {
        // Search for the user by email
        return customerRepository.findAll().stream()
                .filter(user -> user.getEmailID().equals(loginData.getEmailID()) && 
                                user.getPassword().equals(loginData.getPassword()))
                .findFirst()
                .map(user -> "SUCCESS") 
                .orElse("FAIL"); 
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return customerRepository.findAll().stream()
                .filter(c -> c.getEmailID().equalsIgnoreCase(email))
                .findFirst()
                .map(user -> {
                    // Generate a random 6-digit OTP
                    generatedOTP = String.valueOf((int)((Math.random() * 900000) + 100000));
                    emailForReset = email;
                    
                    // PRINT TO CONSOLE (In real life, you'd send an email here)
                    System.out.println("OTP for " + email + " is: " + generatedOTP);
                    
                    return ResponseEntity.ok("OTP sent to your email!");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String otp, @RequestParam String newPassword) {
        if (otp.equals(generatedOTP)) {
            return customerRepository.findAll().stream()
                    .filter(c -> c.getEmailID().equalsIgnoreCase(emailForReset))
                    .findFirst()
                    .map(user -> {
                        user.setPassword(newPassword);
                        customerRepository.save(user);
                        generatedOTP = null; // Clear OTP after use
                        return ResponseEntity.ok("Password changed successfully!");
                    })
                    .orElse(ResponseEntity.badRequest().body("Error updating password"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP!");
    }
    
    
    
    
    
    
    
    
    // --- NEW: VIEW PROFILE ENDPOINT ---
 // --- UPDATE: VIEW PROFILE BY EMAIL ---
    @GetMapping("/viewProfile/{email}")
    public ResponseEntity<Customer> viewProfile(@PathVariable String email) {
        return customerRepository.findAll().stream()
                .filter(user -> user.getEmailID().equalsIgnoreCase(email))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- UPDATE: UPDATE PROFILE BY EMAIL ---
    @PutMapping("/updateProfile/{email}")
    public ResponseEntity<String> updateProfile(@PathVariable String email, @RequestBody Customer updatedData) {
        return customerRepository.findAll().stream()
                .filter(user -> user.getEmailID().equalsIgnoreCase(email))
                .findFirst()
                .map(customer -> {
                    // Update Personal Info
                    customer.setFirstName(updatedData.getFirstName());
                    customer.setLastName(updatedData.getLastName());
                    customer.setGender(updatedData.getGender());
                    customer.setDateOfBirth(updatedData.getDateOfBirth());
                    
                    // Update Address Info
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