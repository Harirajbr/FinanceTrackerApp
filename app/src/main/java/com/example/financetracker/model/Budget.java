package com.example.financetracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "budget_table")
public class Budget {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String category;
    private double monthlyLimit;
    private double totalSpent;  // Track total spent per category
    private long monthTimestamp; // Identify the month

    public Budget(String category, double monthlyLimit, double totalSpent, long monthTimestamp) {
        this.category = category;
        this.monthlyLimit = monthlyLimit;
        this.totalSpent = totalSpent;
        this.monthTimestamp = monthTimestamp;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getMonthlyLimit() { return monthlyLimit; }
    public void setMonthlyLimit(double monthlyLimit) { this.monthlyLimit = monthlyLimit; }
    public double getTotalSpent() { return totalSpent; }
    public void setTotalSpent(double totalSpent) { this.totalSpent = totalSpent; }
    public long getMonthTimestamp() { return monthTimestamp; }
    public void setMonthTimestamp(long monthTimestamp) { this.monthTimestamp = monthTimestamp; }
}
