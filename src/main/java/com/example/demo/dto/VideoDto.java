package com.example.demo.dto;

import com.example.demo.model.Movie;
import com.example.demo.model.Video;

public class VideoDto {
    private String title;
    private String genre;
    private boolean available;
    private String type;

    public VideoDto() {
    }

    public VideoDto(String title, String genre, boolean available, String type) {
        this.title = title;
        this.genre = genre;
        this.available = available;
        this.type = type;
    }

    public static VideoDto fromVideo(Video v) {
        String t = (v instanceof Movie) ? "MOVIE" : "SERIES";
        return new VideoDto(v.getTitle(), v.getGenre(), v.isAvailable(), t);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
