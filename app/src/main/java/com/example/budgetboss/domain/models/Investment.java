package com.example.budgetboss.domain.models;

import java.io.Serializable;

public class Investment implements Serializable {
    public enum InvestmentType {
        LOAN_GIVEN,
        LOAN_TAKEN,
        SIP,
        FD,
        STOCK,
        MUTUAL_FUND,
        OTHER
    }

    private String id;
    private String name;
    private double amount;
    private InvestmentType type;
    private double interestRate; // Optional
    private long startDate;
    private String notes;

    public Investment() {}

    public Investment(String id, String name, double amount, InvestmentType type, double interestRate, long startDate, String notes) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.type = type;
        this.interestRate = interestRate;
        this.startDate = startDate;
        this.notes = notes;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public InvestmentType getType() { return type; }
    public void setType(InvestmentType type) { this.type = type; }
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    public long getStartDate() { return startDate; }
    public void setStartDate(long startDate) { this.startDate = startDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
