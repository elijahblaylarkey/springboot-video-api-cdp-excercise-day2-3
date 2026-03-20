package com.example.demo.service;

import com.example.demo.dto.CreateMovieRequest;
import com.example.demo.model.Movie;
import com.example.demo.repository.VideoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class VideoServiceTest {

    @Mock
    private VideoRepository repository;

    @InjectMocks
    private VideoService videoService;

    @Test
    @DisplayName("upsertMovie creates new movie when title not exists")
    void upsertMovie_createsNewMovieWhenTitleNotExists() {
        CreateMovieRequest request = new CreateMovieRequest("Inception", "Sci-Fi");
        given(repository.findByTitleIgnoreCase("Inception")).willReturn(Optional.empty());

        Movie movie = new Movie("Inception", "Sci-Fi");
        movie.setId(1L);
        given(repository.save(org.mockito.ArgumentMatchers.any(Movie.class))).willReturn(movie);

        var result = videoService.upsertMovie(request);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Inception");
    }

    @Test
    @DisplayName("rentVideo sets available false")
    void rentVideo_setsAvailableFalse() {
        Movie movie = new Movie("Inception", "Sci-Fi");
        movie.setId(1L);
        movie.setAvailable(true);

        given(repository.findByTitleIgnoreCase("Inception")).willReturn(Optional.of(movie));
        given(repository.save(movie)).willReturn(movie);

        var result = videoService.rentVideo("Inception");

        assertThat(result.isAvailable()).isFalse();
    }

    @Test
    @DisplayName("returnVideo sets available true")
    void returnVideo_setsAvailableTrue() {
        Movie movie = new Movie("Inception", "Sci-Fi");
        movie.setId(1L);
        movie.setAvailable(false);

        given(repository.findByTitleIgnoreCase("Inception")).willReturn(Optional.of(movie));
        given(repository.save(movie)).willReturn(movie);

        var result = videoService.returnVideo("Inception");

        assertThat(result.isAvailable()).isTrue();
    }

    @Test
    @DisplayName("rentVideo throws NotFound for missing video")
    void rentVideo_whenMissing_throwsNoSuchElementException() {
        given(repository.findByTitleIgnoreCase("Missing")).willReturn(Optional.empty());

        assertThatThrownBy(() -> videoService.rentVideo("Missing"))
                .isInstanceOf(NoSuchElementException.class);
    }
}
