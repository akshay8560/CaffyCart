package com.example.caffycart;

import java.io.Serializable;

public class OrderValues implements Serializable {

    String name;
    boolean wippedCream;
    boolean chocolate;
    String coffeeType;
    String cupSize;
    String quantity;
    String price;

    public OrderValues(String name, boolean wippedCream, boolean chocolate, String coffeeType, String cupSize, String quantity, String price) {
        this.name = name;
        this.wippedCream = wippedCream;
        this.chocolate = chocolate;
        this.coffeeType = coffeeType;
        this.cupSize = cupSize;
        this.quantity = quantity;
        this.price = price;
    }
}
