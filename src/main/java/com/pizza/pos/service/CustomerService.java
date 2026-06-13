package com.pizza.pos.service;

import com.pizza.pos.model.Customer;
import com.pizza.pos.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepo;

    public String registerCustomer(Customer customer) {
        try {
            // 1. Get total records count
            long count = customerRepo.count();

            // 2. Generate prefix safely
            String firstName = customer.getFirstName();
            String prefix;

            if (firstName != null && firstName.length() >= 2) {
                prefix = firstName.substring(0, 2).toUpperCase();
            } else if (firstName != null && firstName.length() == 1) {
                prefix = firstName.toUpperCase() + "X"; // fallback
            } else {
                prefix = "US"; // default fallback
            }

            // 3. Generate user ID (e.g., US1001)
            String generatedId = prefix + (1001 + count);

            // 4. Set ID
            customer.setUserId(generatedId);

            // 5. Save to database
            customerRepo.save(customer);

            return "SUCCESS";

        } catch (Exception e) {
            // Helps debugging in Railway / logs
            e.printStackTrace();
            return "FAIL";
        }
    }
}