package com.example.video_sharing_web_application.registration;

import javax.validation.constraints.*;


public record RegistrationRequest(@NotBlank(message = "email can not blank") @Size(min = 3, max = 15) String firstname, @NotBlank(message = "email can not blank") @Size(min = 3, max = 15) String lastname, @NotBlank(message = "email can not blank") @Email(message = "Email should be valid")String email, @Size(min = 5, max = 15) String password) {

}
