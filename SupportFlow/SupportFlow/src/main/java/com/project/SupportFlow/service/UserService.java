package com.project.SupportFlow.service;

import com.project.SupportFlow.dto.*;
import com.project.SupportFlow.enums.Role;
import com.project.SupportFlow.exceptions.UserNotFoundException;
import com.project.SupportFlow.model.User;
import com.project.SupportFlow.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public UserResponseDTO register(UserRequestDTO dto) {
        User  user = new User();

        createdEntity(user, dto);

        User saved = userRepository.save(user);

        return toDTO(saved);
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::toDTO)
                .toList();
    }

    public UserResponseDTO findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return toDTO(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        updateEntity(user, dto);

        User saved = userRepository.save(user);
        return toDTO(saved);
    }

    private void createdEntity(User user, UserRequestDTO dto){
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.USER);
    }

    private UserResponseDTO toDTO(User user){
        UserResponseDTO dto = new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );

        return dto;
    }

    private void updateEntity(User user, UserRequestDTO dto){
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
    }
}
