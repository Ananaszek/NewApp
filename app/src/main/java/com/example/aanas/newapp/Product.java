package com.example.aanas.newapp;

public class Product {

    private int id;
    private String name;
    private String price;
    private String amount;
    private boolean bought;

    public Product(String name, String price, String amount, boolean bought){
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.bought = bought;
    }

    public Product(int id,String name, String price, String amount, boolean bought) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.bought = bought;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}