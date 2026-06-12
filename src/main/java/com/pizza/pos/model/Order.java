package com.pizza.pos.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "POS_TBL_Order")
public class Order {

    @Id
    private String orderId;
    private String userId;
    private String foodId;
    private int quantity;
    
    @Column(name = "total_cost")
    private double totalCost;
    
    private String orderStatus;
    private Date orderDate;
    private String status;

    // Delivery details added for the Admin View logic
    private String street;
    private String city;
    private String mobileNo;

    // --- Standard Getters and Setters ---

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFoodId() { return foodId; }
    public void setFoodId(String foodId) { this.foodId = foodId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // --- NEW Getters and Setters for Delivery Details ---

    public String getStreet() { 
        return street; 
    }
    public void setStreet(String street) { 
        this.street = street; 
    }

    public String getCity() { 
        return city; 
    }
    public void setCity(String city) { 
        this.city = city; 
    }

    public String getMobileNo() { 
        return mobileNo; 
    }
    public void setMobileNo(String mobileNo) { 
        this.mobileNo = mobileNo; 
    }
}