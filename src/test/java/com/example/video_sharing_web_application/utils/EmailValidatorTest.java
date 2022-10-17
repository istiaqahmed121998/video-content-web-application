package com.example.video_sharing_web_application.utils;

import com.example.video_sharing_web_application.registration.EmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class EmailValidatorTest {

    private EmailValidator underTest;

    @BeforeEach
    void setUp(){
        underTest=new EmailValidator();
    }

    @ParameterizedTest
    @CsvSource(
            {"alice@example.com,true",
                    "zenithzo@c.com,false",
                    "zenithzone@gmail.com,true",
                    "aada@gma.com,true",
                    "zenith@live.com,true"
            }
    )
    @DisplayName("Email validation check")
    void itShouldValidateEmailAddress(String email, Boolean expected ){
        boolean isValid = underTest.test(email);
        assertThat(isValid).isEqualTo((expected));
    }
}
