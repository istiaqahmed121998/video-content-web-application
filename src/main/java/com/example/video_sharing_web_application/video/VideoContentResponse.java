package com.example.video_sharing_web_application.video;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.util.List;
@Getter
@Setter
@Builder
public class VideoContentResponse {
    private Integer status;
    private Integer totalResults;
    private List<VideoContent> data;
}
