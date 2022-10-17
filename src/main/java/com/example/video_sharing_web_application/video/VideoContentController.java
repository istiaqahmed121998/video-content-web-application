package com.example.video_sharing_web_application.video;

import com.example.video_sharing_web_application.exception.ApiRequestException;
import com.example.video_sharing_web_application.util.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;


@RestController
@RequestMapping(path = "api/v1/video")
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
@Slf4j
public class VideoContentController {
    private final VideoContentService videoContentService;


    @GetMapping(path = "")
    public ResponseEntity<ApiResponse> getAllVideoContent(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "3") int size){
        return videoContentService.findAll(page,size);
    }

    @GetMapping(path = "{id}")
    public  ResponseEntity<ApiResponse> getByVideoById(@PathVariable("id") Long id,HttpSession session,Authentication authentication){
        return videoContentService.findVideoContentById(id,session,authentication);
    }

    @GetMapping(path ="like/{id}")
    public ResponseEntity<ApiResponse> likeVideoContent(@PathVariable("id") Long id,Authentication authentication){
        return videoContentService.likeVideoContent(id,authentication.getName());
    }

    @DeleteMapping(path = "delete/{id}")
    public ResponseEntity<ApiResponse> deleteVideoContent(@PathVariable("id") Long id,Authentication authentication){
        return videoContentService.deleteVideoContent(id,authentication.getName());
    }



    @PostMapping(path = "add")
    public  ResponseEntity<Object> saveContent(@RequestBody @Valid Content content,Authentication authentication){
        if(authentication!=null) {
            videoContentService.addVideoContent(content.url(), authentication.getName());
            return new ResponseEntity<>(Map.of("status",HttpStatus.CREATED,"message","Video has been successfully added"),HttpStatus.CREATED);
        }
        throw new ApiRequestException("Authentication Failed");
    }



}
record Content(@NotBlank(message = "url can not be blank") String url){
}
