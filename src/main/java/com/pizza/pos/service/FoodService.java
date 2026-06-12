package com.pizza.pos.service;

import java.util.Optional;
import com.pizza.pos.model.Food;
import com.pizza.pos.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepo;

    public String addFood(Food food) {
        try {
            long count = foodRepo.count();
            
            // LOGIC FIX: Generate ID based on Category (PI for Pizza, DR for Drink, etc.)
            String prefix = "FD"; 
            if (food.getCategory() != null && food.getCategory().length() >= 2) {
                prefix = food.getCategory().substring(0, 2).toUpperCase();
            }
            
            String generatedId = prefix + (1001 + count); 
            
            food.setFoodId(generatedId);
            food.setAvailable(true); // Default new items to available
            foodRepo.save(food);
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
    }

    public List<Food> viewAllFood() {
        return foodRepo.findAll();
    }

    public void deleteFood(String id) {
        foodRepo.deleteById(id);
    }

    public String updateFood(String id, Food foodDetails) {
        Optional<Food> foodOpt = foodRepo.findById(id);
        if (foodOpt.isPresent()) {
            Food food = foodOpt.get();
            
            // SYNC ALL FIELDS: Ensure Cocola, Desserts, and Burgers update correctly
            food.setName(foodDetails.getName());
            food.setPrice(foodDetails.getPrice());
            food.setCategory(foodDetails.getCategory());
            food.setDescription(foodDetails.getDescription());
            food.setFoodSize(foodDetails.getFoodSize());
            food.setType(foodDetails.getType());
            food.setImageUrl(foodDetails.getImageUrl());
            food.setAvailable(foodDetails.isAvailable());

            foodRepo.save(food);
            return "SUCCESS";
        }
        return "FAIL";
    }
}