package net.breezeware.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.breezeware.exception.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("Entering configure HttpSecurity");
        http.authorizeRequests().antMatchers("/","/user").permitAll().anyRequest().authenticated().and()
                .oauth2ResourceServer((oauth2ResourceServer) -> oauth2ResourceServer.jwt().and()
                        .authenticationEntryPoint(authenticationEntryPoint()))
                .addFilterBefore(jwtFilter, BasicAuthenticationFilter.class).exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler()).authenticationEntryPoint(authenticationEntryPoint()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().cors().and().csrf()
                .disable().headers().cacheControl();
        log.info("Leaving configure HttpSecurity");
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ErrorResponse errorResponse = buildErrorResponse(response, accessDeniedException);
            buildServletOutputStream(response, errorResponse);
        };
    }

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
