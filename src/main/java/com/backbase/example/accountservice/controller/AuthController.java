package com.backbase.example.accountservice.controller;

import com.backbase.example.accountservice.security.JwtUtil;
import com.backbase.example.accountservice.service.SessionManagementService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is used for a for user to do login and logout operations
 */
@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SessionManagementService sessionManagementService;


    /**
     * This method handles login of a user
     * @param authRequest
     * @return token It returns JWT token if a user successfully logged in
     * @throws Exception
     */
    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) throws Exception {
        if (sessionManagementService.isSessionActive(authRequest.getUsername())) {
            throw new Exception("User already logged in.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String token = jwtUtil.generateToken(userDetails.getUsername());
        sessionManagementService.addSession(authRequest.getUsername(), token);
        return token;
    }

    /**
     * This method is used for logout operation of a user
     * @param authRequest
     * @param authorizationHeader
     * @return It returns successful message for logout of a user
     */
    @PostMapping("/logout")
    public String logout(@RequestBody AuthRequest authRequest, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        sessionManagementService.removeSession(authRequest.getUsername());
        jwtUtil.invalidateToken(token);
        return "User logged out successfully.";
    }
}

@Getter
@Setter
@AllArgsConstructor
class AuthRequest {
    private String username;
    private String password;
}
