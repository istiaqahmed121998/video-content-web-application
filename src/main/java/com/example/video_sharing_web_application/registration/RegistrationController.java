package com.example.video_sharing_web_application.registration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.video_sharing_web_application.appuser.AppUser;
import com.example.video_sharing_web_application.appuser.AppUserRole;
import com.example.video_sharing_web_application.exception.ApiError;
import com.example.video_sharing_web_application.exception.ApiRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.video_sharing_web_application.security.SecurityConstants.*;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(path = "api/v1")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    @PostMapping(path = "/registration")
    public ResponseEntity<Object> register(@RequestBody @Valid RegistrationRequest request) {
        String confirmToken=registrationService.register(request);
        if(confirmToken.length()>0){
            return new ResponseEntity<>(Map.of("status",200,"token",confirmToken,"message","check your inbox"),HttpStatus.OK);
        }
        else {
            throw new ApiRequestException(String.format("%s There is a complication of creating a token",request.email()));
        }
    }
    @GetMapping(path = "confirm")
    public ResponseEntity<Object> confirm(@RequestParam("token") String token){
         if(registrationService.confirmToken(token)){
             return new ResponseEntity<>(Map.of("status",200,"message","Confirmed your email"),HttpStatus.OK);
         }
         return null;
    }

    @GetMapping(path = "token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie accessTokenCookie=registrationService.checkRequestCookie(request.getCookies(),"refresh_token");
        if(accessTokenCookie!=null && accessTokenCookie.getValue()!=null){
            String refresh_token = accessTokenCookie.getValue();
            try {
                Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
                AppUser user = registrationService.jwtDecode(algorithm,refresh_token);
                if(user!=null){
                    String access_token = JWT.create()
                            .withSubject(user.getEmail())
                            .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                            .withIssuer(JWT_ISSUER)
                            .withClaim("roles", Arrays.stream(user.getAppUserRole().values()).map(AppUserRole::toString).collect(Collectors.toList()))
                            .sign(algorithm);
                    Map<String, Object> tokens = new HashMap<>();
                    tokens.put("status", 200);
                    tokens.put("access_token", access_token);
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
                }
                response.setStatus(SC_FORBIDDEN);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),  new ApiError(HttpStatus.UNAUTHORIZED,"User is not found","User is not found"));
            } catch (JWTVerificationException exception){
                response.setStatus(SC_FORBIDDEN);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),  new ApiError(HttpStatus.UNAUTHORIZED,exception.getLocalizedMessage(),exception.getMessage()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            new ObjectMapper().writeValue(response.getOutputStream(),  new ApiError(HttpStatus.UNAUTHORIZED,"Refresh token is invalid","Refresh token is invalid"));
        }

    }
    @GetMapping(path = "logout")
    public void logOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie accessTokenCookie=registrationService.checkRequestCookie(request.getCookies(),"refresh_token");
        if(accessTokenCookie!=null && accessTokenCookie.getValue()!=null){
            String refresh_token = accessTokenCookie.getValue();
            try {
                Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
                AppUser user = registrationService.jwtDecode(algorithm,refresh_token);
                if(user!=null){
                    Map<String, Object> responses = new HashMap<>();
                    Cookie cookie = new Cookie("refresh_token", null);
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    responses.put("status", 200);
                    responses.put("message", "Successfully logged out");
                    response.setStatus(SC_OK);
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), responses);
                }
                response.setStatus(SC_FORBIDDEN);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), new ApiError(HttpStatus.UNAUTHORIZED,"User is not found","User is not found"));
            } catch (JWTVerificationException exception){
                response.setStatus(SC_FORBIDDEN);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),  new ApiError(HttpStatus.UNAUTHORIZED,exception.getLocalizedMessage(),exception.getMessage()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            new ObjectMapper().writeValue(response.getOutputStream(),  new ApiError(HttpStatus.UNAUTHORIZED,"Refresh token is invalid","Refresh token is invalid"));
        }

    }
}
