package com.example.video_sharing_web_application.user;

import com.example.video_sharing_web_application.video.VideoContent;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", sequenceName = "USER_SEQ")
    @Column(name = "id", nullable = false)
    private Long id;
    @NotEmpty(message = "First Name is required")
    private String firstName;
    private String lastName;
    private String profileName;

    @JsonBackReference
    @OneToMany(mappedBy = "addedByUser", fetch = FetchType.LAZY, cascade = javax.persistence.CascadeType.ALL, orphanRemoval = true)
    private Set<VideoContent> videoContents = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "liked_videos",
            joinColumns =
                    {
                            @JoinColumn(name = "user_id", referencedColumnName = "id",
                                    nullable = false, updatable = false)
                    },
            inverseJoinColumns =
                    {
                            @JoinColumn(name = "video_content_id", referencedColumnName = "id",
                                    nullable = false, updatable = false)
                    })

    @JsonIgnore
    Set<VideoContent> likedVideoContents = new HashSet<>();

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        if (this.lastName == null) {
            this.setProfileName(this.firstName);
        } else {
            this.setProfileName(this.firstName + " " + this.lastName);
        }
    }


}
