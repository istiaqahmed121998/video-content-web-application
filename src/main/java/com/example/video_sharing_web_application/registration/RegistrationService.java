package com.example.video_sharing_web_application.registration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.video_sharing_web_application.appuser.AppUser;
import com.example.video_sharing_web_application.appuser.AppUserRole;
import com.example.video_sharing_web_application.appuser.AppUserService;
import com.example.video_sharing_web_application.exception.ApiRequestException;
import com.example.video_sharing_web_application.registration.email.EmailSender;
import com.example.video_sharing_web_application.registration.token.ConfirmationToken;
import com.example.video_sharing_web_application.registration.token.ConfirmationTokenService;
import com.example.video_sharing_web_application.user.User;
import com.example.video_sharing_web_application.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.example.video_sharing_web_application.security.SecurityConstants.JWT_ISSUER;
import static java.util.Arrays.stream;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final AppUserService appUserService;
    private final UserService userService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;

    private final EmailSender sender;
    @Transactional
    public String register(RegistrationRequest request) {
        boolean isValidEmail=emailValidator.test(request.email());
        if(!isValidEmail){
            throw new ApiRequestException(String.format("%s Some error in your email",request.email()));
        }
        User user = userService.create(new User(request.firstname(),request.lastname()));

        if(user!=null){
            AppUser appUser=appUserService.signUpUser( new AppUser(request.email(), request.password(),AppUserRole.USER,user));
            if(appUser!=null){
                String token = UUID.randomUUID().toString();
                ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),LocalDateTime.now().plusMinutes(15),appUser);
                confirmationTokenService.saveConfirmationToken(confirmationToken);
                sender.send(
                        request.email(),
                        buildEmail(user.getFirstName(), token));
                return token;
            }
            else {
                throw new ApiRequestException(String.format("%s User is not created",request.email()));
            }
        }
        else {
            throw new ApiRequestException(String.format("%s App user is created",request.email()));
        }
    }

    private String buildEmail(String name, String token) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=http://localhost:3000/confirm/" + token + ">Activate Now</a><p>"+token+"</p> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }


    @Transactional
    public Boolean confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(()->{
            throw new ApiRequestException(String.format("%s token is not valid",token));
        });
        if(confirmationToken.getConfirmedAt()!=null){
            throw new ApiRequestException(String.format("%s token is already confirmed",token));
        }
        LocalDateTime expiresAt = confirmationToken.getExpiresAt();
        if(expiresAt.isBefore(LocalDateTime.now())){
            throw new ApiRequestException(String.format("%s token is expired",token));
        }
        confirmationTokenService.setConfirmedAt(token);
        return appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
    }

    public Cookie checkRequestCookie(Cookie[] cookies,String name) {
        if (cookies!=null){
            Optional<Cookie> refreshCookie=stream(cookies).filter(cookie -> cookie.getName().equals(name)).findFirst();
            return refreshCookie.orElse(null);
        }
        return null;
    }

    public AppUser jwtDecode(Algorithm algorithm,String refresh_token) {
 //use more secure key
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(JWT_ISSUER)
                .build();
        DecodedJWT jwt = verifier.verify(refresh_token);
        String email= jwt.getSubject();
        Optional<AppUser> appUser = appUserService.findByEmail(email);
        return appUser.orElse(null);
    }
}
