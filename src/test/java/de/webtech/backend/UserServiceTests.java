package de.webtech.backend;

import de.webtech.backend.Service.UserService;
import de.webtech.backend.model.User;
import de.webtech.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createUser_ShouldCreateUserSuccessfully() {
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setPassword("password");

        when(userRepository.findByUsername("newUser")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User resultUser = userService.createUser(newUser);

        assertNotNull(resultUser, "Erstellter Benutzer sollte nicht null sein");
        assertEquals("newUser", resultUser.getUsername());
        assertEquals("encodedPassword", resultUser.getPassword());
        verify(userRepository).findByUsername("newUser");
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void createUser_ShouldThrowExceptionForExistingUsername() {
        User newUser = new User();
        newUser.setUsername("existingUser");
        newUser.setPassword("password");

        when(userRepository.findByUsername("existingUser")).thenReturn(new User());

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(newUser));
        verify(userRepository).findByUsername("existingUser");
        verify(userRepository, never()).save(any(User.class));
    }
}
