package com.example.video_sharing_web_application.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.video_sharing_web_application.appuser.AppUserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static com.example.video_sharing_web_application.security.SecurityConstants.*;
import static java.util.Arrays.stream;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals(LOGIN_URL) && request.getServletPath().equals(SIGN_UP_URL)){
            filterChain.doFilter(request,response);
        }
        else {
            String authorizationHeader = request.getHeader(HEADER_STRING);
            if(authorizationHeader!=null && authorizationHeader.startsWith(TOKEN_PREFIX)){
                String token = authorizationHeader.substring(TOKEN_PREFIX.length());
                try {
                    Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET); //use more secure key
                    JWTVerifier verifier = JWT.require(algorithm)
                            .withIssuer(JWT_ISSUER)
                            .build();
                    DecodedJWT jwt = verifier.verify(token);
                    String email= jwt.getSubject();
                    AppUserRole [] roles = jwt.getClaim("roles").asArray(AppUserRole.class);
                    Collection<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
                    stream(roles).forEach(role-> simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.name())));
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(email,null,simpleGrantedAuthorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request,response);
                } catch (JWTVerificationException exception){
                    response.setStatus(SC_FORBIDDEN);
                    response.setContentType(APPLICATION_JSON_VALUE);

                    new ObjectMapper().writeValue(response.getOutputStream(), Map.of("status",403,"message",exception.getMessage()));
                }
            }
            else{
                filterChain.doFilter(request,response);
            }

        }
    }
}
