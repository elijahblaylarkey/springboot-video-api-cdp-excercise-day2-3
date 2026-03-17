package com.example.demo.controller;

import com.example.demo.dto.CreateMovieRequest;
import com.example.demo.dto.VideoDto;
import com.example.demo.service.VideoService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService service;

    public VideoController(VideoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<VideoDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/available")
    public ResponseEntity<List<VideoDto>> getAvailable() {
        return ResponseEntity.ok(service.getAvailable());
    }

    @PostMapping("/add/movie")
    public ResponseEntity<?> addMovie(@RequestBody CreateMovieRequest req) {
        if (req.getTitle() == null || req.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "title must not be blank"));
        }
        boolean existed = service.existsByTitle(req.getTitle());
        VideoDto dto = service.upsertMovie(req);
        if (existed) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        }
    }

    @PutMapping("/{title}/rent")
    public ResponseEntity<?> rent(@PathVariable String title) {
        try {
            VideoDto dto = service.rentByTitle(URLDecoder.decode(title, StandardCharsets.UTF_8));
            return ResponseEntity.ok(dto);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Video not found: " + title));
        }
    }

    @PutMapping("/{title}/return")
    public ResponseEntity<?> returnVideo(@PathVariable String title) {
        try {
            VideoDto dto = service.returnByTitle(URLDecoder.decode(title, StandardCharsets.UTF_8));
            return ResponseEntity.ok(dto);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Video not found: " + title));
        }
    }
}
