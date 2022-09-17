package com.example.video_sharing_web_application.video;

import com.example.video_sharing_web_application.appuser.AppUser;
import com.example.video_sharing_web_application.appuser.AppUserResponse;
import com.example.video_sharing_web_application.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(path = "api/v1/video")
@CrossOrigin("http://localhost:3000")
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
    @GetMapping
    public VideoContentResponse getAllVideos(){
        List videoContents =videoContentService.videoContentList();
        VideoContentResponse response = new VideoContentResponse ();
        response.setStatus(200);
        response.setData(videoContents);
        response.setTotalResults(videoContents.size());
        return response;
    }

    @GetMapping(value = "{videoId}")
    public VideoContentResponse getByVideoId(@PathVariable("videoId") Long videoId){
        VideoContent videoContent =videoContentService.videoContentByVideoId(videoId);
        VideoContentResponse response = new VideoContentResponse ();
        response.setStatus(200);
        response.setData(List.of(videoContent));
        response.setTotalResults(1);
        return response;
    }

    @GetMapping(value = "{videoId}/like")
    public ResponseEntity<Object> likeVideoContent(@PathVariable("videoId") Long videoId,Authentication authentication){
        videoContentService.likeVideoContent(videoId,authentication.getName());
        return new ResponseEntity<>(Map.of("status",200,"message","Video has been liked"),HttpStatus.OK);
    }
    @GetMapping(value = "{videoId}/users")
    public AppUserResponse getLikedAppUserByVideoId(@PathVariable("videoId") Long videoId){
        List<AppUser> users = videoContentService.getLikedAppUserByVideoId(videoId);
        AppUserResponse response = new AppUserResponse();
        response.setStatus(200);
        response.setData(users);
        response.setTotalResults(users.size());
        return response;
    }

    @PostMapping(path = "add")
    public ResponseEntity<Object> saveContent(@RequestBody Map<String,Object> payload,Authentication authentication){
        log.info((String) payload.get("url"));
        log.info(authentication.getName());
        if(payload.containsKey("url") && payload.get("url") instanceof String){
            if(videoContentService.addVideoContent((String) payload.get("url"),authentication.getName())){
                return new ResponseEntity<>(Map.of("status",200,"message","Video has been added"),HttpStatus.OK);
            }
            return new ResponseEntity<>(Map.of("status",403,"message","there is some problem"),HttpStatus.EXPECTATION_FAILED);
        }
        else {
            throw new ResourceNotFoundException("there is a problem"+authentication.getPrincipal());
        }
    }
}
