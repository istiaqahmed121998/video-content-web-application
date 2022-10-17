package com.example.video_sharing_web_application.video;

import com.example.video_sharing_web_application.user.User;
import com.fasterxml.jackson.annotation.JsonGetter;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "video_content_seq_gen")
    @SequenceGenerator(name = "video_content_seq_gen", sequenceName = "VIDEO_CONTENT_SEQ")
    private Long id;
    @Column(nullable = false)
    private String videoId;
    @Column(nullable = false)
    private LocalDateTime addedAt;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User addedByUser;


    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade({SAVE_UPDATE, MERGE, PERSIST})
    @JoinTable(name = "liked_videos",
            joinColumns =
                    {
                            @JoinColumn(name = "video_content_id", referencedColumnName = "id",
                                    nullable = false, updatable = false)
                    },
            inverseJoinColumns =
                    {
                            @JoinColumn(name = "user_id", referencedColumnName = "id",
                                    nullable = false, updatable = false)
                    })
    @JsonIgnore
    Set<User> likes = new HashSet<>();


    public VideoContent(String videoId, LocalDateTime addedAt, User addedByUser) {
        this.videoId = videoId;
        this.addedAt = addedAt;
        this.addedByUser = addedByUser;
    }

    public void addLikedVideo(User user) {
        likes.add(user);
    }

    public void removeLikedVideo(User user) {
        likes.remove(user);
    }

    @JsonGetter
    public int getLikeCount() {
        return likes.size();
    }
}
