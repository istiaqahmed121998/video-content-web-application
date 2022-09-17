package com.example.video_sharing_web_application.appuser;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString
public class AppUserResponse {
    private Integer status;
    private Integer totalResults;
    private List<AppUser> data;
}
