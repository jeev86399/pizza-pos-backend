package com.pizza.pos.service;

import com.pizza.pos.model.Order;
import com.pizza.pos.model.OrderStatsDTO;
import com.pizza.pos.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;

    public String placeOrder(Order order) {
        // Requirement US-005: Order ID should be auto-generated [cite: 526, 643]
        int randomNum = (int)(Math.random() * 1000);
        String uniqueId = "OR" + System.currentTimeMillis() + randomNum; 
        
        order.setOrderId(uniqueId); 
        order.setOrderDate(new Date());
        
        // FIX: Using 'orderStatus' to match React frontend property 
        order.setOrderStatus("PENDING"); 
        
        try {
            orderRepo.save(order);
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL";
        }
    }

    public List<Order> getMyOrders(String userId) {
        return orderRepo.findByUserId(userId);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public void updateOrderStatus(String orderId, String newStatus) {
        Order order = orderRepo.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        
        // Requirement AD-009: Admin can change status (Dispatched/Cancelled) [cite: 523, 638]
        order.setOrderStatus(newStatus); 
        orderRepo.save(order);
    }

    public OrderStatsDTO getOrderStats() {
        return orderRepo.getOrderStats();
    }
}