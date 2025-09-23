package org.example.controllers;

import org.example.lua.LuaModManager;
import org.example.models.responses_requests.AuthRequest;
import org.example.models.responses_requests.AuthResponse;
import org.example.models.responses_requests.GenericErrorResponse;
import org.example.utils.JwtUtil;
import org.example.services.security.MyUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log-in")
public class LoginController{

    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final LuaModManager luaManager;

    public LoginController(AuthenticationManager authenticationManager,
                           MyUserDetailsService userDetailsService,
                           JwtUtil jwtUtil,
                           LuaModManager luaManager) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.luaManager = luaManager;
    }

    @PostMapping
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        if (userDetails == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericErrorResponse("Wrong credentials!"));
        }

        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        return  ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(jwt));
    }
}
