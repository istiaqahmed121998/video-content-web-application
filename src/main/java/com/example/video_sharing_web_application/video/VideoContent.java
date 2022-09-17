package com.example.video_sharing_web_application.video;

import com.example.video_sharing_web_application.appuser.AppUser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.hibernate.annotations.CascadeType.*;

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
    @Column(nullable = false,unique = true)
    private String videoId;
    @Column(nullable = false)
    private String videoUrl;
    @Column(nullable = false)
    private LocalDateTime addedAt;
    @JsonIgnore
    @ManyToOne(optional = false,fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    private AppUser appUser;
//    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade({ SAVE_UPDATE,MERGE,PERSIST})
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
