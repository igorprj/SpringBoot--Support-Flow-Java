package com.project.SupportFlow.controllers;

import com.project.SupportFlow.config.TokenProvider;
import com.project.SupportFlow.dto.UserRequestDTO;
import com.project.SupportFlow.dto.UserResponseDTO;
import com.project.SupportFlow.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
public class AuthController {

    private UserService userService;

    private AuthenticationManager authenticationManager;

    private TokenProvider tokenProvider;

    @PostMapping
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(dto));
    }

    @PostMapping
    public ResponseEntity<String> login(@RequestBody UserRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
        );

        String token = tokenProvider.gerarToken(authentication);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(token);
    }
}
