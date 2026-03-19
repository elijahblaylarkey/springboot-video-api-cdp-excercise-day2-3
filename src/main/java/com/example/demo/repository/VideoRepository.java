package com.example.demo.repository;

import com.example.demo.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    @Query("SELECT v FROM Video v WHERE LOWER(v.title) = LOWER(:title)")
    Optional<Video> findByTitleIgnoreCase(@Param("title") String title);
}
