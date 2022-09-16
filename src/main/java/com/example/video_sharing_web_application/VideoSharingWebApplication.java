package com.example.video_sharing_web_application;

import com.example.video_sharing_web_application.appuser.AppUser;
import com.example.video_sharing_web_application.appuser.AppUserRepository;
import com.example.video_sharing_web_application.appuser.AppUserRole;
import com.example.video_sharing_web_application.video.VideoContent;
import com.example.video_sharing_web_application.video.VideoContentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class VideoSharingWebApplication{

    public static void main(String[] args) {
        SpringApplication.run(VideoSharingWebApplication.class, args);
    }


    @Bean
    @Transactional
    CommandLineRunner initDatabase(VideoContentService videoService, AppUserRepository appUserRepository) {
        return args -> {
            AppUser user1=new AppUser("Zenith", "Jhony","zjhony@gmail.com", AppUserRole.USER, "aa");
            VideoContent video= new VideoContent("https://www.youtube.com/watch?v=9SGDpanrc8U","9SGDpanrc8U",LocalDateTime.now(), user1);
            Set<VideoContent> videos = new HashSet<>();
            videos.add(video);
            user1.setLikedVideoContents(videos);
            appUserRepository.save(user1);
        };
    }
}
