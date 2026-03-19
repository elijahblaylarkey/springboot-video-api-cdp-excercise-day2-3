package com.example.demo.model;

import jakarta.persistence.Entity;

@Entity
public class Series extends Video {

    public Series() {
    }

    public Series(String title, String genre) {
        super(title, genre);
    }

    @Override
    public void play() {
        System.out.println("Playing series: " + getTitle());
    }
}
