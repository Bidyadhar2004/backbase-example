package com.backbase.example.accountservice.security;

import com.backbase.example.accountservice.service.TokenBlacklistService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ComponentScan(basePackages = "com.backbase.example.accountservice.security")
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.secret}")
    private String secret;

    @MockBean
    private TokenBlacklistService tokenBlacklistService;

    @Configuration
    @PropertySource("classpath:test.properties")
    static class TestConfig {
        @Bean
        public JwtUtil jwtUtil() {
            return new JwtUtil();
        }
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateToken() {
        User userDetails = new User("user", "password", new ArrayList<>());
        String token = jwtUtil.generateToken(userDetails.getUsername());

        Assertions.assertNotNull(token);
        Assertions.assertTrue(jwtUtil.validateToken(token, userDetails.getUsername()));
    }

    @Test
    public void testExtractUsername() {
        User userDetails = new User("user", "password", new ArrayList<>());
        String token = jwtUtil.generateToken(userDetails.getUsername());

        String username = jwtUtil.extractUsername(token);
        Assertions.assertEquals(userDetails.getUsername(), username);
    }

    @Test
    public void testValidateToken() {
        User userDetails = new User("user", "password", new ArrayList<>());
        String token = jwtUtil.generateToken(userDetails.getUsername());

        Assertions.assertTrue(jwtUtil.validateToken(token, userDetails.getUsername()));
    }

    @Test
    public void testExtractExpiration() {
        User userDetails = new User("user", "password", new ArrayList<>());
        String token = jwtUtil.generateToken(userDetails.getUsername());

        Date expiration = jwtUtil.extractExpiration(token);
        Assertions.assertNotNull(expiration);
    }
}
