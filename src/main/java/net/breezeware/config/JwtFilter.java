package net.breezeware.config;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jwt.SignedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    // @Value(value = "${userdetail.attribute}")
    // private String userAttr;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        log.info("Entering JwtFilter() {} ");
        String header = req.getHeader("Authorization");
        System.out.println(header);
        log.debug("Authorization header {}", header);
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
          //  return;
        }

        Optional<UsernamePasswordAuthenticationToken> authentication = getAuthentication(req);
        System.out.println(authentication);
        System.out.println(SecurityContextHolder.getContext());
        if (SecurityContextHolder.getContext() != null && authentication.isPresent()) {
            SecurityContextHolder.getContext().setAuthentication(authentication.get());
        }

        log.info("Leaving JwtFilter()");
        chain.doFilter(req, res);
    }

    // Reads the JWT from the Authorization header, and then uses JWT to get the
    // user roles.
    private Optional<UsernamePasswordAuthenticationToken> getAuthentication(HttpServletRequest request) {
        log.info("Entering getAuthentication() ");
        String token = request.getHeader("Authorization");
        Optional<UsernamePasswordAuthenticationToken> optToken = Optional.empty();
        System.out.println(optToken);
        if (token != null) {
            // parse the token.
            SignedJWT signedJwt;
            try {
                signedJwt = SignedJWT.parse(token.replace("Bearer ", ""));

                // Retrieve the JWT claims according to the app requirements
                // String user = signedJwt.getJWTClaimsSet().getSubject();
                String user = signedJwt.getJWTClaimsSet().getStringClaim("email");
                System.out.println(user);

                if (signedJwt.getJWTClaimsSet().getClaims() != null) {
                    Map<String, Object> claims = signedJwt.getJWTClaimsSet().getClaims();
                    System.out.println(claims);
                    JSONArray jsonArray = (JSONArray) claims.get("roles");
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            authorities.add(
                                    new SimpleGrantedAuthority("ROLE_" + jsonArray.get(i).toString().toUpperCase()));
                        }

                    }

                    log.info("Leaving getAuthentication() with UsernamePasswordAuthenticationToken for {} ", user);
                    return Optional.of(new UsernamePasswordAuthenticationToken(user, null, authorities));
                }

            } catch (ParseException e) {
                log.info("Error while parsing JWT Token {} ", e.getMessage());
            }

        }

        log.info("Leaving getAuthentication() without UsernamePasswordAuthenticationToken");
        return optToken;
    }
}
