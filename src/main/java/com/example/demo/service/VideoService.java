package com.example.demo.service;

import com.example.demo.dto.CreateMovieRequest;
import com.example.demo.dto.VideoDto;
import com.example.demo.model.Movie;
import com.example.demo.model.Series;
import com.example.demo.model.Video;
import com.example.demo.repository.VideoRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class VideoService {
    private final VideoRepository repository;

    public VideoService(VideoRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void initializeData() {
        // seed data
        if (!repository.findByTitleIgnoreCase("Inception").isPresent()) {
            repository.save(new Movie("Inception", "Sci-Fi"));
        }
        if (!repository.findByTitleIgnoreCase("Stranger Things").isPresent()) {
            repository.save(new Series("Stranger Things", "Sci-Fi"));
        }
    }

    public List<VideoDto> getAllVideos() {
        return repository.findAll().stream()
                .map(VideoDto::fromVideo)
                .collect(Collectors.toList());
    }

    public List<VideoDto> getAvailableVideos() {
        return repository.findAll().stream()
                .filter(Video::isAvailable)
                .map(VideoDto::fromVideo)
                .collect(Collectors.toList());
    }

    public VideoDto upsertMovie(CreateMovieRequest req) {
        String title = req.getTitle();
        String genre = req.getGenre();
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("title must not be blank");
        }

        title = title.trim();
        genre = genre == null ? "" : genre.trim();

        Movie movie = new Movie(title, genre);
        repository.findByTitleIgnoreCase(title).ifPresent(existing -> {
            if (existing instanceof Movie) {
                movie.setId(existing.getId());
            }
        });

        Movie saved = repository.save(movie);
        return VideoDto.fromVideo(saved);
    }

    public VideoDto rentVideo(String title) {
        Video v = findVideoOrThrow(title);
        v.rentVideo();
        Video updated = repository.save(v);
        return VideoDto.fromVideo(updated);
    }

    public VideoDto returnVideo(String title) {
        Video v = findVideoOrThrow(title);
        v.returnVideo();
        Video updated = repository.save(v);
        return VideoDto.fromVideo(updated);
    }

    public boolean existsByTitle(String title) {
        return repository.findByTitleIgnoreCase(title).isPresent();
    }

    private Video findVideoOrThrow(String title) {
        return repository.findByTitleIgnoreCase(title)
                .orElseThrow(() -> new NoSuchElementException(title));
    }
}
