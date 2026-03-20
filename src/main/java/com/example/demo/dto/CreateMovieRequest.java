package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateMovieRequest {
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("genre")
    private String genre;

    public CreateMovieRequest() {
    }

    public CreateMovieRequest(String title, String genre) {
        this.title = title;
        this.genre = genre;
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
}
