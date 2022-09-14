package com.example.video_sharing_web_application.video;

import lombok.AllArgsConstructor;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/video")
@AllArgsConstructor
public class VideoContentController {
    private final VideoContentService videoContentService;

    @PostMapping(path = "add")
    public ResponseEntity<Object> saveContent(@RequestBody Map<String,Object> payload){
        if(payload.containsKey("url") && payload.get("url") instanceof String){
            return videoContentService.addVideoContent((String) payload.get("url"));
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
