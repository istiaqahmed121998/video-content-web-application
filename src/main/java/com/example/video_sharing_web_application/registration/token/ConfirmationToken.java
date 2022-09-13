package com.example.video_sharing_web_application.registration.token;

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
public class ConfirmationToken {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;
    private String token;
    private LocalDateTime createAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;


    public ConfirmationToken(String token, LocalDateTime createAt, LocalDateTime expiresAt,AppUser appUser) {
        this.token = token;
        this.createAt = createAt;
        this.expiresAt = expiresAt;
        this.appUser=appUser;
    }
}
