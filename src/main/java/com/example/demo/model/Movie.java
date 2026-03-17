package com.example.demo.model;

public class Movie extends Video {

    public Movie(String title, String genre) {
        super(title, genre);
    }

    @Override
    public void play() {
        // simple placeholder
        System.out.println("Playing movie: " + getTitle());
    }

    // optional overload
    public void play(String quality) {
        System.out.println("Playing movie: " + getTitle() + " in " + quality);
    }
}
