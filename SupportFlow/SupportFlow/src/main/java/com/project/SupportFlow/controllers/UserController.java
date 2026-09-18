package com.project.SupportFlow.controllers;

import com.project.SupportFlow.dto.UserRequestDTO;
import com.project.SupportFlow.dto.UserResponseDTO;
import com.project.SupportFlow.repositories.UserRepository;
import com.project.SupportFlow.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@Valid @RequestParam Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findUserById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@Valid @RequestParam Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestParam Long id, @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(id, dto));
    }
}
