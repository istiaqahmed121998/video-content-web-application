package com.example.video_sharing_web_application.video;

import com.example.video_sharing_web_application.appuser.AppUser;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.Map;


@RestController
@RequestMapping(path = "api/v1/video")
@AllArgsConstructor
@Slf4j
public class VideoContentController {
    private final VideoContentService videoContentService;


    @GetMapping(path = "me")
    public String getName(Authentication authentication, Principal principal) {
        log.info(authentication.getName());
        log.info(principal.getName());
        return "adsdsada";
    }

    @PostMapping(path = "add")
    public ResponseEntity<Object> saveContent(@RequestBody Map<String,Object> payload){
        if(payload.containsKey("url") && payload.get("url") instanceof String){
            return videoContentService.addVideoContent((String) payload.get("url"),new AppUser());
        }
        return null ;
    }
    @GetMapping(value = "{videoId}")
    public ResponseEntity<Object> getByVideoId(@PathVariable("videoId") String videoId){
        videoContentService.videoContentByVideoId(videoId);
        return null;
    }
    @GetMapping()
    public ResponseEntity<Object> getAllVideos(){
        videoContentService.videoContentList();
        return null;
    }
}
