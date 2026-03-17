package com.example.demo.service;

import com.example.demo.dto.CreateMovieRequest;
import com.example.demo.dto.VideoDto;
import com.example.demo.model.Movie;
import com.example.demo.model.Series;
import com.example.demo.model.Video;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class VideoService {
    private final ConcurrentHashMap<String, Video> store = new ConcurrentHashMap<>();

    public VideoService() {
        // seed data
        upsertMovieInternal(new Movie("Inception", "Sci-Fi"));
        store.put(normalizeKey("Stranger Things"), new Series("Stranger Things", "Sci-Fi"));
    }

    private String normalizeKey(String title) {
        if (title == null) return "";
        return title.trim().toLowerCase();
    }

    private void upsertMovieInternal(Movie m) {
        store.put(normalizeKey(m.getTitle()), m);
    }

    public List<VideoDto> getAll() {
        return store.values().stream().map(VideoDto::fromVideo).collect(Collectors.toList());
    }

    public List<VideoDto> getAvailable() {
        return store.values().stream().filter(Video::isAvailable).map(VideoDto::fromVideo).collect(Collectors.toList());
    }

    public VideoDto upsertMovie(CreateMovieRequest req) {
        String title = req.getTitle();
        String genre = req.getGenre();
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("title must not be blank");
        }

        String key = normalizeKey(title);
        // create or replace with a Movie
        Movie m = new Movie(title.trim(), genre == null ? "" : genre.trim());
        store.put(key, m);
        return VideoDto.fromVideo(m);
    }

    public VideoDto rentByTitle(String rawTitle) {
        String title = URLDecoder.decode(rawTitle == null ? "" : rawTitle, StandardCharsets.UTF_8);
        String key = normalizeKey(title);
        Video v = store.get(key);
        if (v == null) throw new NoSuchElementException(title);
        v.rentVideo();
        return VideoDto.fromVideo(v);
    }

    public VideoDto returnByTitle(String rawTitle) {
        String title = URLDecoder.decode(rawTitle == null ? "" : rawTitle, StandardCharsets.UTF_8);
        String key = normalizeKey(title);
        Video v = store.get(key);
        if (v == null) throw new NoSuchElementException(title);
        v.returnVideo();
        return VideoDto.fromVideo(v);
    }

    public boolean existsByTitle(String title) {
        return store.containsKey(normalizeKey(title));
    }
}
