package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
public class TokenAuthFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Token";
    private static final String STATIC_TOKEN_USER = "VALIDUSERTOKEN";
    private static final String STATIC_TOKEN_ADMIN = "VALIDADMINTOKEN";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get token from header
        String token = request.getHeader(AUTH_HEADER);
        if(token != null && token.equals(STATIC_TOKEN_USER)) {
            log.info("Valid user token detected. Setting Security Context");

            var context = SecurityContextHolder.createEmptyContext();
            SecurityContextHolder.setContext(context);
            context.setAuthentication(new UsernamePasswordAuthenticationToken(
                    getUserDetailsObject(), "", List.of(new SimpleGrantedAuthority("ROLE_user"))));

            log.info("Continuing to next filters");
            filterChain.doFilter(request, response);
        }
        else if(token != null && token.equals(STATIC_TOKEN_ADMIN)) {
            log.info("Valid admin token detected. Setting Security Context");

            var context = SecurityContextHolder.createEmptyContext();
            SecurityContextHolder.setContext(context);
            context.setAuthentication(new UsernamePasswordAuthenticationToken(
                    getAdminUserDetailsObject(), "", List.of(new SimpleGrantedAuthority("ROLE_admin"))));

            log.info("Continuing to next filters");
            filterChain.doFilter(request, response);
        }
        else {
            log.info("Invalid token detected. Returning now");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private UserDetails getUserDetailsObject() {
        return User.builder().username("user").password("password").authorities("ROLE_user").build();
    }

    private UserDetails getAdminUserDetailsObject() {
        return User.builder().username("admin").password("password").authorities("ROLE_admin").build();
    }
}
