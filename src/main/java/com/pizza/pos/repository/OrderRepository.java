package com.pizza.pos.repository;

import com.pizza.pos.model.Order;
import com.pizza.pos.model.OrderStatsDTO; // ADD THIS
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // ADD THIS
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    
    List<Order> findByUserId(String userId);

    @Query("SELECT new com.pizza.pos.model.OrderStatsDTO(" +
    	       "count(o), coalesce(sum(o.totalCost), 0.0), " + 
    	       "sum(case when o.status = 'PENDING' then 1 else 0 end), " + 
    	       "sum(case when o.status = 'Dispatched' then 1 else 0 end)) " + 
    	       "FROM Order o")
    	OrderStatsDTO getOrderStats();
}