package com.example.demo.model;

public abstract class Video {
    private String title;
    private String genre;
    private boolean available = true;

    public Video(String title, String genre) {
        this.title = title;
        this.genre = genre;
        this.available = true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void rentVideo() {
        this.available = false;
    }

    public void returnVideo() {
        this.available = true;
    }

    public abstract void play();
}
