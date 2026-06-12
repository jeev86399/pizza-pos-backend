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
            long count = customerRepo.count();
            String prefix = customer.getFirstName().substring(0, 2).toUpperCase();
            String generatedId = prefix + (1001 + count); // US1001 format
            
            customer.setUserId(generatedId);
            customerRepo.save(customer);
            return "SUCCESS";
        } catch (Exception e) {
            return "FAIL";
        }
    }
}