package com.pizza.pos.model;

public class OrderStatsDTO {
    private long totalOrders;
    private double totalRevenue;
    private long pendingCount;
    private long dispatchedCount;

    public OrderStatsDTO(long totalOrders, double totalRevenue, long pendingCount, long dispatchedCount) {
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.pendingCount = pendingCount;
        this.dispatchedCount = dispatchedCount;
    }

    // Getters and Setters
    public long getTotalOrders() { return totalOrders; }
    public double getTotalRevenue() { return totalRevenue; }
    public long getPendingCount() { return pendingCount; }
    public long getDispatchedCount() { return dispatchedCount; }

	public void setTotalOrders(long totalOrders) {
		this.totalOrders = totalOrders;
	}

	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public void setPendingCount(long pendingCount) {
		this.pendingCount = pendingCount;
	}

	public void setDispatchedCount(long dispatchedCount) {
		this.dispatchedCount = dispatchedCount;
	}
    
}