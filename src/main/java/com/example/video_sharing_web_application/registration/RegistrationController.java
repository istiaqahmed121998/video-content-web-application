package com.example.video_sharing_web_application.registration;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/registration")
@CrossOrigin("http://localhost:3000")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    @PostMapping
    public ResponseEntity<Object> register(@RequestBody RegistrationRequest request){
        String confirmToken=registrationService.register(request);
        if(confirmToken.length()>0){
            return new ResponseEntity<>(Map.of("status",200,"token",confirmToken,"message","check your inbox"),HttpStatus.OK);
        }
        return null;
    }
    @GetMapping(path = "confirm")
    public ResponseEntity<Object> confirm(@RequestParam("token") String token){
         if(registrationService.confirmToken(token)){
             return new ResponseEntity<>(Map.of("status",200,"message","Confirmed your email"),HttpStatus.OK);
         }
         return null;
    }
}
