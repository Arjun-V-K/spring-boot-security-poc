package com.example.demo.security;

import com.example.demo.service.UserServiceImpl;
import com.example.demo.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTH_HEADER);
        if(authHeader != null && authHeader.contains(PREFIX)) {
            String token = authHeader.substring(7);
            log.info("Found Bearer token {}", token);
            boolean valid = jwtUtil.verifyToken(token);
            if(!valid) {
                log.info("Invalid token found. Unauthorized.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            var username = jwtUtil.getClaim(token, "sub");
            log.info("Username {} found. Setting SecurityContext.", username);
            var user = userService.loadUserByUsername(username);
            var context = SecurityContextHolder.createEmptyContext();
            SecurityContextHolder.setContext(context);
            context.setAuthentication(new UsernamePasswordAuthenticationToken(user,"",user.getAuthorities()));
            log.info("Continuing to next filters");
            filterChain.doFilter(request,response);
        }
        else {
            log.info("No Bearer token found. Continue to Basic authentication logic");
            filterChain.doFilter(request,response);
        }
    }
}
