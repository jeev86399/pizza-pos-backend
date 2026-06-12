package com.pizza.pos.controller;

import com.pizza.pos.model.Order;
import com.pizza.pos.model.Customer;
import com.pizza.pos.model.OrderStatsDTO;
import com.pizza.pos.repository.OrderRepository;
import com.pizza.pos.service.OrderService;
import com.pizza.pos.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
//@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderRepository orderRepository;

	@Autowired
    private OrderService orderService;

    @Autowired
    private CustomerRepository customerRepo;

	OrderController(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}
    
	@PutMapping("/cancelOrder/{orderId}")
	public ResponseEntity<String> cancelOrder(@PathVariable String orderId) {
	    return orderRepository.findById(orderId).map(order -> {
	        // Requirement AD-009: Only PENDING orders can be changed/cancelled [cite: 523, 638]
	        if ("PENDING".equalsIgnoreCase(order.getOrderStatus())) {
	            order.setOrderStatus("CANCELLED"); // [cite: 523, 641]
	            orderRepository.save(order);
	            return ResponseEntity.ok("SUCCESS");
	        }
	        return ResponseEntity.badRequest().body("Order already dispatched or cancelled.");
	    }).orElse(ResponseEntity.notFound().build());
	}
    
    
    
    
    @PostMapping("/placeOrder")
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        // --- UPDATED LOGIC: Find customer by Email instead of Primary Key ID ---
        // This ensures compatibility with the frontend 'currentUser' (email)
        return customerRepo.findAll().stream()
                .filter(c -> c.getEmailID().equalsIgnoreCase(order.getUserId()))
                .findFirst()
                .map(customer -> {
            
            // 1. The Gatekeeper: Validate that the profile is actually complete
            if (isProfileIncomplete(customer)) {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                        .body("PROFILE_INCOMPLETE: Please update your Address and Mobile number.");
            }

            // 2. Attach current customer delivery details to this specific order
            order.setStreet(customer.getStreet());
            order.setCity(customer.getCity());
            order.setMobileNo(customer.getMobileNo());

            // 3. Proceed to save the order
            String result = orderService.placeOrder(order);
            return ResponseEntity.ok(result);

        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("ERROR: Customer with Email " + order.getUserId() + " not found."));
    }

    // Helper method to check if essential delivery fields are missing
    private boolean isProfileIncomplete(Customer c) {
        return c.getStreet() == null || c.getStreet().trim().isEmpty() ||
               c.getCity() == null || c.getCity().trim().isEmpty() ||
               c.getMobileNo() == null || c.getMobileNo().trim().isEmpty();
    }

    @GetMapping("/viewMyOrders/{userId}")
    public List<Order> getMyOrders(@PathVariable String userId) {
        return orderService.getMyOrders(userId);
    }

    @GetMapping("/viewAllOrders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/updateStatus/{orderId}")
    public ResponseEntity<String> updateStatus(@PathVariable String orderId, @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok("Status updated successfully");
    }

    @GetMapping("/stats")
    public OrderStatsDTO getStats() {
        return orderService.getOrderStats();
    }
}