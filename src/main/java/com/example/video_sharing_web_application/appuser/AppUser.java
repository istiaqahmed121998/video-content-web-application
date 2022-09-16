package com.example.video_sharing_web_application.appuser;

import com.example.video_sharing_web_application.video.VideoContent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private long id;

    private String firstname;
    private String lastname;

    private String email;
    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;
    private String password;
    private Boolean locked=false;
    private Boolean enabled=false;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "liked_videos",
            joinColumns =
                    {
                    @JoinColumn(name = "app_user_id", referencedColumnName = "id",
                            nullable = false, updatable = false)
                    },
            inverseJoinColumns =
                    {
                    @JoinColumn(name = "video_content_id", referencedColumnName = "id",
                            nullable = false, updatable = false)
                    })
    Set<VideoContent> likedVideoContents;

    public AppUser(String firstname, String lastname, String email, AppUserRole appUserRole, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.appUserRole = appUserRole;
        this.password = password;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
