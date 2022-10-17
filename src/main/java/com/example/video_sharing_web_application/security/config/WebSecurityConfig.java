package com.example.video_sharing_web_application.security.config;


import com.example.video_sharing_web_application.appuser.AppUserRole;
import com.example.video_sharing_web_application.appuser.AppUserService;
import com.example.video_sharing_web_application.security.filter.CustomAccessDeniedHandler;
import com.example.video_sharing_web_application.security.filter.CustomAuthenticationFilter;
import com.example.video_sharing_web_application.security.filter.CustomAuthorizationFilter;
import com.example.video_sharing_web_application.security.filter.CustomHttp403ForbiddenEntryPoint;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {
    private final AppUserService appUserService;
    private final AuthenticationConfiguration configuration;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(appUserService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        authenticationFilter.setFilterProcessesUrl("/api/v1/login");
        http.cors();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/v1/login/**").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/registration/**").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/confirm/**").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/token/refresh/**").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/logout/**").permitAll();
        http.anonymous().and().authorizeRequests().antMatchers("/api/v1/video").permitAll();
        http.anonymous().and().authorizeRequests().antMatchers("/api/v1/video/").permitAll();
        http.anonymous().and().authorizeRequests().antMatchers("/api/v1/video/latest/**").permitAll();
        http.anonymous().and().authorizeRequests().antMatchers("/api/v1/video/{\\d+}").permitAll();
        http.anonymous().and().authorizeRequests().antMatchers("/api/v1/video/{\\d+}/").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/video/add/**").hasAuthority(AppUserRole.USER.name());
        http.authorizeRequests().antMatchers("/api/v1/video/add/**").hasAuthority(AppUserRole.USER.name());
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(authenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        http.exceptionHandling().authenticationEntryPoint(customHttp403ForbiddenEntryPoint());
        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return configuration.getAuthenticationManager();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://192.168.68.104:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public CustomHttp403ForbiddenEntryPoint customHttp403ForbiddenEntryPoint(){
        return new CustomHttp403ForbiddenEntryPoint();
    }
}
