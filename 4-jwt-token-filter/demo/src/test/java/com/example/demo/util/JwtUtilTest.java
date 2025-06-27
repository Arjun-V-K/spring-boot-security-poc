package com.example.demo.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void generateToken() {
        String token = jwtUtil.generateToken("user");
        System.out.println(token);
    }

    @Test
    void verifyToken() {
        String token = jwtUtil.generateToken("user");
        System.out.println(token);
        assertTrue(jwtUtil.verifyToken(token));
    }

    @Test
    void verifyToken_Expired() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNzUwOTQwMjExLCJleHAiOjE3NTA5NDAyNzF9.vbzDZp9wT4SpgI6BVaVEKybyCPBMtti8ZkSJpYjiST4";
        System.out.println(token);
        assertFalse(jwtUtil.verifyToken(token));
    }

    @Test
    void getClaim() {
        String token = jwtUtil.generateToken("user");
        System.out.println(token);
        String username = jwtUtil.getClaim(token, "sub");
        assertEquals("user", username);
    }
}