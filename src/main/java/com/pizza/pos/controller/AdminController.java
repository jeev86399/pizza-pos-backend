package com.pizza.pos.controller;
import com.pizza.pos.model.Food;
import com.pizza.pos.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin") // All admin tasks start with /admin[cite: 1, 2]
//@CrossOrigin(origins = "*") // This allows React to talk to Spring Boot
public class AdminController {

    @Autowired
    private FoodService foodService;

    // Endpoint to add a pizza[cite: 1, 2]
    @PostMapping("/addFood")
    public String addFood(@RequestBody Food food) {
        return foodService.addFood(food);
    }

    // Endpoint to see all pizzas[cite: 1, 2]
    @GetMapping("/viewAllFood")
    public List<Food> viewAllFood() {
        return foodService.viewAllFood();
    }
    @DeleteMapping("/deleteFood/{id}")
    public String deleteFood(@PathVariable String id) {
        foodService.deleteFood(id);
        return "SUCCESS";
    }

    @PutMapping("/updateFood/{id}")
    public String updateFood(@PathVariable String id, @RequestBody Food food) {
        return foodService.updateFood(id, food);
    }
}