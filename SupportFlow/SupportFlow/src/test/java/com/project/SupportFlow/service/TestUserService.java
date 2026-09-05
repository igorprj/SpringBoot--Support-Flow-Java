package com.project.SupportFlow.service;

import com.project.SupportFlow.dto.UserRequestDTO;
import com.project.SupportFlow.dto.UserResponseDTO;
import com.project.SupportFlow.model.User;
import com.project.SupportFlow.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestUserService {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void deveCriarUsuarioComSucesso() {
        UserRequestDTO dto = new UserRequestDTO(
                "test",
                "test@gmail.com",
                "123"
        );

        User user = new User();
        user.setId(1L);
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(dto.password());

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO resultado = userService.register(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.name()).isEqualTo(dto.name());
        assertThat(resultado.email()).isEqualTo(dto.email());
    }

    @Test
    void deveListarTodosUsuariosComSucesso() {
        User user = new User();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test@gmail.com");
        user.setPassword("123");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("test2");
        user.setEmail("test2@gmail.com");
        user2.setPassword("123");

        when(userRepository.findAll()).thenReturn(List.of(user, user2));

        List<UserResponseDTO> resultado = userService.getAllUsers();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).id()).isEqualTo(user.getId());
        assertThat(resultado.get(0).name()).isEqualTo(user.getName());
        assertThat(resultado.get(0).email()).isEqualTo(user.getEmail());
        assertThat(resultado.get(1).id()).isEqualTo(user2.getId());
        assertThat(resultado.get(1).name()).isEqualTo(user2.getName());
        assertThat(resultado.get(1).email()).isEqualTo(user2.getEmail());

    }

    @Test
    void deveRetornarUmaListaVazia() {

        when(userRepository.findAll()).thenReturn(List.of());

        List<UserResponseDTO> resultado = userService.getAllUsers();

        assertThat(resultado).isEmpty();
    }

    @Test
    void deveAcharUsuarioPorIDComSucesso() {
        User user = new User();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test@gmail.com");
        user.setPassword("123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO resultado = userService.findUserById(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.name()).isEqualTo(user.getName());
        assertThat(resultado.email()).isEqualTo(user.getEmail());
    }

    @Test
    void deveLancarUmaExcecaoSeNaoAcharUsuarioPorIDComSucesso() {
        Long idInexistente = 999L;
        when(userRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findUserById(idInexistente));
    }

    @Test
    void deveDeletarUsuarioComSucesso() {
        User user = new User();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test@gmail.com");
        user.setPassword("123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void deveLancarExcecaoAoDeletarUsuarioInexistente() {
        Long idInexistente = 999L;

        when(userRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.deleteUser(idInexistente));
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        User user = new User();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test@gmail.com");
        user.setPassword("123");

        UserRequestDTO dto = new UserRequestDTO(
                "testtttttt",
                "test2@gmail.com",
                "123456"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO resultado = userService.updateUser(1L, dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.name()).isEqualTo(user.getName());
        assertThat(resultado.email()).isEqualTo(user.getEmail());
    }

    @Test
    void deveLancarExcecaoSeUsuarioNaoExistente() {
        Long idInexistente = 999L;

        when(userRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(idInexistente, null));
    }
}
