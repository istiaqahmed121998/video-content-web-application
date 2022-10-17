package com.example.video_sharing_web_application.util;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
@Builder
@JsonPropertyOrder({ "httpHeaders", "httpStatusCode", "message", "data" })
public class ApiResponse<T> {
    public   int httpStatusCode;
    public  String message;
    public  T data;
}