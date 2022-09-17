package com.example.video_sharing_web_application.video;

import lombok.Getter;
import lombok.Setter;


import java.util.List;
@Getter
@Setter
public class VideoContentResponse {
    private Integer status;
    private Integer totalResults;
    private List<VideoContent> data;
}
