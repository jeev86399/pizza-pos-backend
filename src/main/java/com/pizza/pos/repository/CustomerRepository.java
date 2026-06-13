package com.pizza.pos.repository;

import com.pizza.pos.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    // This will handle saving customer data to database (already handled by JpaRepository)

    // ✅ Custom method to find customer by email
    Customer findByEmailID(String emailID);
}