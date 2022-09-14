package com.example.video_sharing_web_application.video;

import com.example.video_sharing_web_application.appuser.AppUser;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;


import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class VideoContentService {
    private final VideoContentRepository videoContentRepository;
    private final VideoValidation validation;
    @PostMapping

    public List<VideoContent> videoContentList(){
        return videoContentRepository.findAll();
    }

    public VideoContent videoContentByVideoId(String id){
        return videoContentRepository.findVideoContentByVideoId(id).orElseThrow(()->{
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

    public ResponseEntity<Object> addVideoContent(String url) {
        if(validation.test(url)){
            String videoId=getVideoId(url);
            VideoContent videoContent = new VideoContent(url,videoId, LocalDateTime.now(),new AppUser());
            videoContentRepository.save(videoContent);

        }
        return null;
    }
}
