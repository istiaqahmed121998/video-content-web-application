package com.example.video_sharing_web_application.video;

import com.example.video_sharing_web_application.appuser.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class VideoContent {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(nullable = false)
    private String videoId;
    @Column(nullable = false)
    private String videoUrl;
    @Column(nullable = false)
    private LocalDateTime addedAt;
    @ManyToOne(optional = false)
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;

    public VideoContent(String videoUrl,String videoId,LocalDateTime addedAt, AppUser appUser) {
        this.videoUrl = videoUrl;
        this.videoId=videoId;
        this.addedAt=addedAt;
        this.appUser = appUser;
    }
}
