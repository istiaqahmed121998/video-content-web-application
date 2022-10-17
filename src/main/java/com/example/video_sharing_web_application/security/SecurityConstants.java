package com.example.video_sharing_web_application.security;

import java.util.concurrent.TimeUnit;

public class SecurityConstants {
//    public static final int ACCESS_TOKEN_EXPIRATION_TIME = 10 * 60*1000;
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = TimeUnit.HOURS.toMillis(4);
//    public static final int REFRESH_TOKEN_EXPIRATION_TIME = 30 * 60*1000;
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = TimeUnit.DAYS.toMillis(1);
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/v1/registration";
    public static final String LOGIN_URL = "/api/v1/login";
    public static final String REFRESH_TOKEN_URL = "/api/v1/token/refresh";
    public static final String JWT_SECRET = "jhony";

    public static final String JWT_ISSUER= "jhony";
}