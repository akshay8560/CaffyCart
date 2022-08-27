package com.example.caffycart;

import androidx.annotation.DrawableRes;

public class CoffeeBucket {
    String name, description;
    int image;

    public CoffeeBucket(String name, String description, @DrawableRes int image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }
}
