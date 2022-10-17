package com.example.video_sharing_web_application.video;

import com.example.video_sharing_web_application.appuser.AppUser;
import com.example.video_sharing_web_application.appuser.AppUserService;
import com.example.video_sharing_web_application.exception.ApiRequestException;
import com.example.video_sharing_web_application.user.User;
import com.example.video_sharing_web_application.util.ApiResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class VideoContentService {
    private final VideoContentRepository videoContentRepository;
    private final AppUserService appUserService;
    private final VideoValidation validation;


    public ResponseEntity<ApiResponse> findAll(int page,int size){
        log.info(""+size);
        Pageable sortedByAddedAtDesc =
                PageRequest.of(page, size, Sort.by("addedAt").descending());
        Page<VideoContent> videoContents = videoContentRepository.findAll(sortedByAddedAtDesc);

        Map<String, Object> response = new HashMap<>();
        response.put("videoContents", videoContents.getContent());
        response.put("totalPages", videoContents.getTotalPages());
        response.put("currentPage", videoContents.getNumber());
        response.put("totalItems", videoContents.getTotalElements());

        if(videoContents.isFirst()){
            response.put("prevPage", null);
            if(videoContents.hasNext()){
                response.put("nextPage", String.format("?page=%d",videoContents.getNumber()+1));

            }
            else {
                response.put("nextPage",null);
            }
        }
        else if(videoContents.isLast()){
            response.put("nextPage", null);
            if(videoContents.hasPrevious()){
                response.put("prevPage", String.format("?page=%d",videoContents.getNumber()-1));

            }
            else {
                response.put("prevPage",null);
            }
        }
        else {
            if(videoContents.hasNext()){
                response.put("nextPage", String.format("?page=%d",videoContents.getNumber()+1));
            }
            if(videoContents.hasPrevious()){
                response.put("prevPage", String.format("?page=%d",videoContents.getNumber()-1));
            }

        }

        return new ResponseEntity<ApiResponse>(ApiResponse.builder().httpStatusCode(200).message("All Videos Details").data(response).build(), HttpStatus.OK);
    }



    public Boolean addVideoContent(String url, String email) {
        if(validation.test(url)){
           AppUser appUser=(AppUser) appUserService.loadUserByUsername(email);
           VideoContent videoContentAddedByUser = appUser.getUser().getVideoContents().stream().filter(videoContent -> videoContent.getVideoId().equals(getVideoId(url))).findFirst().orElse(null);
           if(videoContentAddedByUser!=null){
               throw new ApiRequestException(String.format("%s is already added by this user",url));
           }
            VideoContent videoContent = new VideoContent(getVideoId(url),LocalDateTime.now(),appUser.getUser());
            videoContentRepository.save(videoContent);
            return true;
        }
        else{
            throw new ApiRequestException(String.format("%s is not valid url",url));
        }
    }


    public  ResponseEntity<ApiResponse> findVideoContentById(Long id, HttpSession session, Authentication authentication){
        Optional<VideoContent> videoContent = videoContentRepository.findVideoContentById(id);

        if(authentication!=null && videoContent.isPresent()){
            AppUser appUser = (AppUser) appUserService.loadUserByUsername(authentication.getPrincipal().toString());
            Boolean isLikedVideo=videoContentRepository.findUserIsLikedVideo(appUser.getUser(),videoContent.get());
            HashMap<String, Object> res = new HashMap<>();
            if(isLikedVideo){
                res.put("isLike",true);
            }
            else{
                res.put("isLike",false);
            }
            res.put("video_details",videoContent);
            return new ResponseEntity<ApiResponse>(ApiResponse.builder().httpStatusCode(200).message("Video Details").data(res).build(), HttpStatus.OK);
        } else if (videoContent.isPresent()) {
            HashMap<String, Object> res = new HashMap<>();
            res.put("isLike",null);
            res.put("video_details",videoContent);
            return new ResponseEntity<ApiResponse>(ApiResponse.builder().httpStatusCode(200).message("Video Details").data(res).build(), HttpStatus.OK);
        }
        throw new ApiRequestException("There is not video by this id");
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
    @Transactional
    public ResponseEntity<ApiResponse> likeVideoContent(Long videoId, String email) {
        VideoContent videoContent = videoContentRepository.findVideoContentById(videoId).orElseThrow(()->new ApiRequestException("There is not video by this id"));
        AppUser appUser=(AppUser) appUserService.loadUserByUsername(email);
        User likedByUser=videoContent.getLikes().stream().filter(user -> user.equals(appUser.getUser())).findFirst().orElse(null);
        if (likedByUser==null){
            videoContent.addLikedVideo(appUser.getUser());
            videoContentRepository.save(videoContent);
            return new ResponseEntity<ApiResponse>(ApiResponse.builder().httpStatusCode(200).message(String.format("You successfully liked this video")).data(videoContent).build(), HttpStatus.OK);
        }
        else{
            videoContent.removeLikedVideo(likedByUser);
            videoContentRepository.save(videoContent);
            return new ResponseEntity<ApiResponse>(ApiResponse.builder().httpStatusCode(200).message(String.format("You successfully removed like this video")).data(videoContent).build(), HttpStatus.OK);
        }
    }

    public ResponseEntity<ApiResponse> deleteVideoContent(Long videoId, String email) {
        String userEmail=Optional.ofNullable(email).orElseThrow(()->{
            throw new ApiRequestException("You are not a user of this platform");
        });
        VideoContent videoContent = videoContentRepository.findVideoContentById(videoId).orElseThrow(()->new ApiRequestException("There is not video by this id"));
        AppUser appUser=(AppUser) appUserService.loadUserByUsername(userEmail);
        if(!videoContent.getAddedByUser().equals(appUser.getUser())){
            throw new ApiRequestException("You are not authorized to delete this video");
        }
        videoContentRepository.delete(videoContent);
        return new ResponseEntity<ApiResponse>(ApiResponse.builder().httpStatusCode(200).message(String.format("Deleted Video Details")).data(videoContent).build(), HttpStatus.OK);

    }

}
