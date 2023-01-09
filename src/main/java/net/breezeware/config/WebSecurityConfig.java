package net.breezeware.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.breezeware.exception.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * WebSecurityConfig is used for Security Configuration
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtFilter jwtFilter;

    @Value("${client.cors.allowedOrigins}")
    private String[] allowedOrigins;

    /**
     * Configure the Security using OAuth2.0
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("Entering configure HttpSecurity");
        http.authorizeRequests()
                .antMatchers("/", "/user","/swagger-ui/**", "/swagger/**",
                        "/v3/api-docs/**")
                .permitAll().anyRequest().authenticated().and()
                .oauth2ResourceServer((oauth2ResourceServer) -> oauth2ResourceServer.jwt().and()
                        .authenticationEntryPoint(authenticationEntryPoint()))
                .addFilterBefore(jwtFilter, BasicAuthenticationFilter.class).exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler()).authenticationEntryPoint(authenticationEntryPoint()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().cors().and().csrf()
                .disable().headers().cacheControl();
        log.info("Leaving configure HttpSecurity");
    }

    //Cors Configuration
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS","DELETE", "PUT"));
        configuration.setAllowedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        configuration.setExposedHeaders(List.of("*"));
        // System.out.println(configuration.getAllowedOrigins());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        log.info("Leaving corsConfigurationSource() with Allowed Origins{}", configuration.getAllowedOrigins());
        // System.out.println(source);
        return source;
    }

    //Access Denied Handler
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ErrorResponse errorResponse = buildErrorResponse(response, accessDeniedException);
            buildServletOutputStream(response, errorResponse);
        };
    }

    //Authentication Entry Point
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {

        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ErrorResponse errorResponse = buildErrorResponse(response, authException);
            buildServletOutputStream(response, errorResponse);
        };
    }

    private void buildServletOutputStream(HttpServletResponse response, ErrorResponse errorResponse)
            throws IOException {
        ServletOutputStream out = response.getOutputStream();
        System.out.println(out);
        new ObjectMapper().writeValue(out, errorResponse);
        out.flush();
    }

    private ErrorResponse buildErrorResponse(HttpServletResponse httpServletResponse, Exception ex) {
        List<String> details = new ArrayList();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(httpServletResponse.getStatus());
        errorResponse.setMessage(HttpStatus.valueOf(httpServletResponse.getStatus()).name());
        details.add(ex.getLocalizedMessage().split(":")[0]);
        System.out.println(ex.getLocalizedMessage());
        System.out.println(ex.getLocalizedMessage().split(":")[0]);
        errorResponse.setDetails(details);
        return errorResponse;
    }

}
