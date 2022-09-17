package com.example.video_sharing_web_application.registration.email;

import com.example.video_sharing_web_application.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
@AllArgsConstructor
public class EmailService implements EmailSender{
    private final Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    @Override
    @Async

    public void send(String to, String email) {
       try {
           MimeMessage mimeMessage= mailSender.createMimeMessage();
           MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"UTF-8");
           helper.setText(email,true);
           helper.setTo(to);
           helper.setSubject("Confirm your email");
           helper.setFrom("xyz@example.com");
           mailSender.send(mimeMessage);
       }
       catch (MessagingException e){
           throw new ResourceNotFoundException(String.format("failed to send mail %s",e.getMessage()));
       }
    }
}
