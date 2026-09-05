package com.project.SupportFlow.service;

import com.project.SupportFlow.dto.TicketRequestDTO;
import com.project.SupportFlow.dto.TicketResponseDTO;
import com.project.SupportFlow.dto.UserRequestDTO;
import com.project.SupportFlow.dto.UserResponseDTO;
import com.project.SupportFlow.enums.TicketPriority;
import com.project.SupportFlow.enums.TicketStatus;
import com.project.SupportFlow.model.Ticket;
import com.project.SupportFlow.model.User;
import com.project.SupportFlow.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public UserResponseDTO createUser(UserRequestDTO dto) {
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
                .orElseThrow(() -> new RuntimeException("User not found"));

        return toDTO(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    private void createdEntity(User user, UserRequestDTO dto){
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
    }

    private UserResponseDTO toDTO(User user){
        UserResponseDTO dto = new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );

        return dto;
    }
}
