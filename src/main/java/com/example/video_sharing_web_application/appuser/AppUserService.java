package com.example.video_sharing_web_application.appuser;

import com.example.video_sharing_web_application.exception.ApiRequestException;
import com.example.video_sharing_web_application.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG="User with email %s not found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)));
    }


    public AppUser signUpUser(@NotNull AppUser appUser){
        boolean isExist=appUserRepository.findByEmail(appUser.getEmail())
                .isPresent();
        if(isExist){
            throw new ApiRequestException(String.format("%s Email is already taken",appUser.getEmail()));
        }
        String encodedPassword=bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);

        return appUserRepository.save(appUser);
    }
    public boolean enableAppUser(String email) {
        return appUserRepository.enableAppUser(email)==1;
    }

    public Optional<AppUser> findByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }
}
