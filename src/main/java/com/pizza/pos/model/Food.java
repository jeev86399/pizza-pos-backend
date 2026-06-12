package com.pizza.pos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pos_tbl_food")
public class Food {
    @Id
    private String foodId;
    private String name;
    private String description;
    private double price;
    private String category;    // Pizza, Burger, Sandwitch, etc.
    private String imageUrl;    // URL for the product image
    private String foodSize;    // Small, Medium, Large, etc.
    private String type;        // Veg or Non-Veg
    private boolean isAvailable;

    // Standard Getters and Setters
    public String getFoodId() { return foodId; }
    public void setFoodId(String foodId) { this.foodId = foodId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getFoodSize() { return foodSize; }
    public void setFoodSize(String foodSize) { this.foodSize = foodSize; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean isAvailable) { this.isAvailable = isAvailable; }
}