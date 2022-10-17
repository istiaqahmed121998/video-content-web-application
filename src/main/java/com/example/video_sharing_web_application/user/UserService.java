package com.example.video_sharing_web_application.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public User create(User user){
        return userRepository.save(user);
    }
}
