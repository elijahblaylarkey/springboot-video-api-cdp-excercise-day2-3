package com.example.demo.service;

import com.example.demo.dto.CreateMovieRequest;
import com.example.demo.dto.VideoDto;
import com.example.demo.model.Movie;
import com.example.demo.model.Series;
import com.example.demo.model.Video;

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
        store.put(normalizeTitle("Stranger Things"), new Series("Stranger Things", "Sci-Fi"));
    }

    private String normalizeTitle(String title) {
        if (title == null) return "";
        return title.trim().toLowerCase();
    }

    private void upsertMovieInternal(Movie m) {
        store.put(normalizeTitle(m.getTitle()), m);
    }

    public List<VideoDto> getAllVideos() {
        return store.values().stream().map(VideoDto::fromVideo).collect(Collectors.toList());
    }

    public List<VideoDto> getAvailableVideos() {
        return store.values().stream().filter(Video::isAvailable).map(VideoDto::fromVideo).collect(Collectors.toList());
    }

    public VideoDto upsertMovie(CreateMovieRequest req) {
        String title = req.getTitle();
        String genre = req.getGenre();
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("title must not be blank");
        }

        String key = normalizeTitle(title);
        Movie m = new Movie(title.trim(), genre == null ? "" : genre.trim());
        store.put(key, m);
        return VideoDto.fromVideo(m);
    }

    public VideoDto rentVideo(String title) {
        Video v = findVideoOrThrow(title);
        v.rentVideo();
        return VideoDto.fromVideo(v);
    }

    public VideoDto returnVideo(String title) {
        Video v = findVideoOrThrow(title);
        v.returnVideo();
        return VideoDto.fromVideo(v);
    }

    public boolean existsByTitle(String title) {
        return store.containsKey(normalizeTitle(title));
    }

    private Video findVideoOrThrow(String title) {
        String key = normalizeTitle(title);
        Video v = store.get(key);
        if (v == null) {
            throw new NoSuchElementException(title);
        }
        return v;
    }
}
