package org.example.controllers;

import org.example.models.dto.AuthRequest;
import org.example.models.dto.AuthResponse;
import org.example.utils.JwtUtil;
import org.example.services.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log-in")
public class LoginController{

    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public LoginController(AuthenticationManager authenticationManager,
                          MyUserDetailsService userDetailsService,
                          JwtUtil jwtUtil) {
        System.out.println("Created login controller");
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public AuthResponse createAuthenticationToken(@RequestBody AuthRequest request) {
        System.out.println("Creating auth token");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        // Load user details
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        // Generate JWT
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        // Return token
        return new AuthResponse(jwt);
    }
}
