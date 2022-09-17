package com.example.video_sharing_web_application.video;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoContentRepository extends JpaRepository<VideoContent,Long> {

    Optional<VideoContent> findVideoContentById(Long id);



}
