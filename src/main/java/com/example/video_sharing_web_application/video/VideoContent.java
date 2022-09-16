package com.example.video_sharing_web_application.video;

import com.example.video_sharing_web_application.appuser.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
    @ManyToOne(optional = false,fetch = FetchType.EAGER,cascade=CascadeType.MERGE)
    private AppUser appUser;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "liked_videos",
            joinColumns =
                    {
                            @JoinColumn(name = "video_content_id", referencedColumnName = "id",
                                    nullable = false, updatable = false)
                    },
            inverseJoinColumns =
                    {
                            @JoinColumn(name = "app_user_id", referencedColumnName = "id",
                                    nullable = false, updatable = false)
                    })
    Set<AppUser> likes = new HashSet<>();

    public VideoContent(String videoUrl,String videoId,LocalDateTime addedAt, AppUser appUser) {
        this.videoUrl = videoUrl;
        this.videoId=videoId;
        this.addedAt=addedAt;
        this.appUser = appUser;
    }
}
