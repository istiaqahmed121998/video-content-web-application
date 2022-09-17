package com.example.video_sharing_web_application.video;

import com.example.video_sharing_web_application.appuser.AppUser;
import com.example.video_sharing_web_application.appuser.AppUserRepository;
import com.example.video_sharing_web_application.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@AllArgsConstructor
public class VideoContentService {
    private final VideoContentRepository videoContentRepository;
    private final AppUserRepository appUserRepository;
    private final VideoValidation validation;

    public List<VideoContent> videoContentList(){
        return videoContentRepository.findAll();
    }

    public VideoContent videoContentByVideoId(Long id){
        return videoContentRepository.findVideoContentById(id).orElseThrow(()->{
            throw new IllegalStateException("There is not video by this id");
        });
    }
    public String getVideoId(@NonNull String videoUrl) {
        String videoId = "";
        String regex = "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(videoUrl);
        if(matcher.find()){
            videoId = matcher.group(1);
        }
        return videoId;
    }

    public Boolean addVideoContent(String url,String email) {
        Optional<AppUser> appUser = appUserRepository.findByEmail(email);
        if(!appUser.isEmpty()){
            if(validation.test(url)){
                String videoId=getVideoId(url);
                VideoContent videoContent = new VideoContent(url,videoId, LocalDateTime.now(),appUser.get());
                videoContentRepository.save(videoContent);
                return true;
            }
            else{
                throw new ResourceNotFoundException("youtube url is not correct");
            }
        }
        else {
            throw new ResourceNotFoundException("User not found");
        }
    }


    @Transactional
    public List<AppUser> getLikedAppUserByVideoId(Long videoId) {
        VideoContent videoContent=videoContentByVideoId(videoId);
        return appUserRepository.findAppUsersByLikedVideoContents(videoContent);
    }

    public Boolean likeVideoContent(Long videoId, String email) {
        Optional<AppUser> userOptional=appUserRepository.findByEmail(email);
        if(!userOptional.isEmpty()){
            Optional<VideoContent> videoContentOptional = videoContentRepository.findVideoContentById(videoId);
            if(!videoContentOptional.isEmpty()){
                VideoContent videoContent=videoContentOptional.get();
                videoContent.getLikes().add(userOptional.get());
                videoContentRepository.save(videoContent);
                return true;
            }
            else {
                throw new ResourceNotFoundException("There is no video");
            }
        }
        else {
            throw new ResourceNotFoundException("user is no found");
        }

    }
}
