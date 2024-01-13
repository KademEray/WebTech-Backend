package de.webtech.backend;

import de.webtech.backend.Service.UserService;
import de.webtech.backend.model.User;
import de.webtech.backend.model.UserDTO;
import de.webtech.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testklasse für den UserService.
 * Enthält Unit-Tests zur Überprüfung der Funktionen des UserService.
 */
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    /**
     * Initialisiert die Mock-Objekte vor jedem Test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Testet, ob createUser einen neuen Benutzer erfolgreich erstellt.
     */
    @Test
    public void createUser_ShouldCreateUserSuccessfully() {
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setPassword("Password123.");

        when(userRepository.findByUsername("newUser")).thenReturn(null);
        when(passwordEncoder.encode("Password123.")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User resultUser = userService.createUser(newUser);

        assertNotNull(resultUser, "Erstellter Benutzer sollte nicht null sein");
        assertEquals("newUser", resultUser.getUsername());
        assertEquals("encodedPassword", resultUser.getPassword());
        verify(userRepository).findByUsername("newUser");
        verify(userRepository).save(any(User.class));
    }

    /**
     * Testet, ob createUser eine Ausnahme wirft, wenn der Benutzername bereits existiert.
     */
    @Test
    public void createUser_ShouldThrowExceptionForExistingUsername() {
        User newUser = new User();
        newUser.setUsername("existingUser");
        newUser.setPassword("Password123.");

        when(userRepository.findByUsername("existingUser")).thenReturn(new User());

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(newUser));
        verify(userRepository).findByUsername("existingUser");
        verify(userRepository, never()).save(any(User.class));
    }

    /**
     * Testet, ob getAllUsers eine Liste aller Benutzer zurückgibt.
     */
    @Test
    public void getAllUsers_ShouldReturnAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("Password123.");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setPassword("Password123.");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size(), "Sollte zwei Benutzer zurückgeben");
        verify(userRepository).findAll();
    }

    /**
     * Testet, ob getUserById einen UserDTO für eine gegebene Benutzer-ID zurückgibt.
     */
    @Test
    public void getUserById_ShouldReturnUserDTO() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("user1");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getUserById(userId);

        assertNotNull(userDTO, "UserDTO sollte nicht null sein");
        assertEquals(userId, userDTO.getId());
        assertEquals("user1", userDTO.getUsername());
    }

    /**
     * Testet, ob updateUser einen Benutzer aktualisiert und zurückgibt.
     */
    @Test
    public void updateUser_ShouldUpdateAndReturnUser() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("existingUser");
        existingUser.setPassword("OldPassword123.");

        User updatedUserDetails = new User();
        updatedUserDetails.setUsername("updatedUser");
        updatedUserDetails.setPassword("NewPassword123.");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("NewPassword123.")).thenReturn("encodedNewPassword");
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User updatedUser = userService.updateUser(userId, updatedUserDetails);

        assertNotNull(updatedUser, "Aktualisierter Benutzer sollte nicht null sein");
        assertEquals("updatedUser", updatedUser.getUsername());
        assertEquals("encodedNewPassword", updatedUser.getPassword());
    }

    /**
     * Testet, ob deleteUser einen Benutzer löscht.
     */
    @Test
    public void deleteUser_ShouldDeleteUser() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        doNothing().when(userRepository).delete(existingUser);

        userService.deleteUser(userId);

        verify(userRepository).findById(userId);
        verify(userRepository).delete(existingUser);
    }

    /**
     * Testet, ob findByUsername einen Benutzer anhand des Benutzernamens zurückgibt.
     */
    @Test
    public void findByUsername_ShouldReturnUser() {
        String username = "user1";
        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword("Password123.");

        when(userRepository.findByUsername(username)).thenReturn(user);

        User foundUser = userService.findByUsername(username);

        assertNotNull(foundUser, "Gefundener Benutzer sollte nicht null sein");
        assertEquals(username, foundUser.getUsername());
        verify(userRepository).findByUsername(username);
    }

}
