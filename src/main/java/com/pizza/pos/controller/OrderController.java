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

    // ================= CANCEL ORDER =================
    @PutMapping("/cancelOrder/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable String orderId) {
        return orderRepository.findById(orderId).map(order -> {

            if ("PENDING".equalsIgnoreCase(order.getOrderStatus())) {
                order.setOrderStatus("CANCELLED");
                orderRepository.save(order);
                return ResponseEntity.ok("SUCCESS");
            }

            return ResponseEntity.badRequest().body("Order already dispatched or cancelled.");

        }).orElse(ResponseEntity.notFound().build());
    }

    // ================= PLACE ORDER =================
    @PostMapping("/placeOrder")
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {

        return customerRepo.findAll().stream()
                .filter(c -> c.getEmailID() != null &&
                             c.getEmailID().equalsIgnoreCase(order.getUserId()))
                .findFirst()
                .map(customer -> {

                    // Profile validation (with ADMIN bypass)
                    if (isProfileIncomplete(customer)) {
                        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                                .body("PROFILE_INCOMPLETE: Please update your Address and Mobile number.");
                    }

                    // Attach delivery details
                    order.setStreet(customer.getStreet());
                    order.setCity(customer.getCity());
                    order.setMobileNo(customer.getMobileNo());

                    String result = orderService.placeOrder(order);
                    return ResponseEntity.ok(result);

                }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("ERROR: Customer with Email " + order.getUserId() + " not found."));
    }

    // ================= HELPER METHOD (UPDATED) =================
    private boolean isProfileIncomplete(Customer c) {

        // ✅ ADMIN bypass (NEW LOGIC)
        if (c.getRole() != null && "ADMIN".equalsIgnoreCase(c.getRole())) {
            return false;
        }

        return c.getStreet() == null || c.getStreet().trim().isEmpty() ||
               c.getCity() == null || c.getCity().trim().isEmpty() ||
               c.getMobileNo() == null || c.getMobileNo().trim().isEmpty();
    }

    // ================= VIEW MY ORDERS =================
    @GetMapping("/viewMyOrders/{userId}")
    public List<Order> getMyOrders(@PathVariable String userId) {
        return orderService.getMyOrders(userId);
    }

    // ================= VIEW ALL ORDERS =================
    @GetMapping("/viewAllOrders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // ================= UPDATE STATUS =================
    @PutMapping("/updateStatus/{orderId}")
    public ResponseEntity<String> updateStatus(@PathVariable String orderId,
                                               @RequestParam String status) {

        orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok("Status updated successfully");
    }

    // ================= STATS =================
    @GetMapping("/stats")
    public OrderStatsDTO getStats() {
        return orderService.getOrderStats();
    }
}