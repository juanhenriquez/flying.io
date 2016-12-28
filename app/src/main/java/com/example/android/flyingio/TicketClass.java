package com.example.android.flyingio;

public class TicketClass {

    private String name;
    private String price;

    TicketClass(String name) {
        this.name = name;
    }

    TicketClass(String name, String price){
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
