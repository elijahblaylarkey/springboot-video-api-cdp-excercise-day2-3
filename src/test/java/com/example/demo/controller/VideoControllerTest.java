package com.example.demo.controller;

import com.example.demo.dto.VideoDto;
import com.example.demo.service.VideoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VideoController.class)
@AutoConfigureMockMvc
public class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoService videoService;

    @Test
    @DisplayName("GET /api/videos returns list and JSON fields")
    void getAllVideos_returnsListAndJsonFields() throws Exception {
        VideoDto dto = new VideoDto("Inception", "Sci-Fi", true, "MOVIE");
        given(videoService.getAllVideos()).willReturn(List.of(dto));

        mockMvc.perform(get("/api/videos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Inception"))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[0].type").value("MOVIE"));
    }

    @Test
    @DisplayName("GET /api/videos/available returns available list")
    void getAvailableVideos_returnsAvailableList() throws Exception {
        VideoDto dto = new VideoDto("Inception", "Sci-Fi", true, "MOVIE");
        given(videoService.getAvailableVideos()).willReturn(List.of(dto));

        mockMvc.perform(get("/api/videos/available"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/videos/add/movie returns created")
    void addMovie_returnsCreated() throws Exception {
        String body = "{\"title\":\"Inception\",\"genre\":\"Sci-Fi\"}";
        VideoDto dto = new VideoDto("Inception", "Sci-Fi", true, "MOVIE");

        given(videoService.existsByTitle("Inception")).willReturn(false);
        given(videoService.upsertMovie(any())).willReturn(dto);

        mockMvc.perform(post("/api/videos/add/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.type").value("MOVIE"));

        then(videoService).should().upsertMovie(any());
    }

    @Test
    @DisplayName("PUT /api/videos/{title}/rent returns ok")
    void rentVideo_returnsOk() throws Exception {
        VideoDto dto = new VideoDto("Inception", "Sci-Fi", false, "MOVIE");
        given(videoService.rentVideo("Inception")).willReturn(dto);

        mockMvc.perform(put("/api/videos/Inception/rent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false));
    }
}
