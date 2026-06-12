package com.pizza.pos.repository;

import com.pizza.pos.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, String> {
    // This allows the backend to perform CRUD operations[cite: 1, 2]
}