package com.example.tallybook.model;

public class Record {
    private long id;
    private double amount;
    private String type;
    private String category;
    private String date;
    private String remark;

    public Record() {}

    public Record(double amount, String type, String category, String date, String remark) {
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
        this.remark = remark;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
} 