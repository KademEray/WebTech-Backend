package de.webtech.backend;

import de.webtech.backend.Service.UserService;
import de.webtech.backend.controller.UserController;
import de.webtech.backend.model.User;
import de.webtech.backend.model.UserDTO;
import de.webtech.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class UserControllerTests {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllUsers_ShouldReturnListOfUsers() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("Passwort123.");
        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("Passwort123.");
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        List<User> result = userController.getAllUsers();

        assertNotNull(result, "Die Liste sollte nicht null sein");
        assertEquals(2, result.size(), "Die Liste sollte zwei Benutzer enthalten");
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void getUserById_ShouldReturnUserDTO() {
        Long userId = 1L;
        UserDTO mockUserDTO = new UserDTO();
        mockUserDTO.setUsername("user1");
        mockUserDTO.setPassword("Passwort123.");
        when(userService.getUserById(userId)).thenReturn(mockUserDTO);

        ResponseEntity<UserDTO> response = userController.getUserById(userId);

        assertNotNull(response.getBody(), "UserDTO sollte nicht null sein");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status sollte OK sein");
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    public void getCurrentUser_ShouldReturnCurrentUserDetails() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user1");

        UserDetails result = userController.getCurrentUser(userDetails);

        assertNotNull(result, "UserDetails sollte nicht null sein");
        assertEquals("user1", result.getUsername(), "Benutzername sollte übereinstimmen");
    }

    @Test
    public void createUser_ShouldCreateAndReturnUser() {
        User newUser = new User();
        newUser.setUsername("user1");
        newUser.setPassword("Passwort123.");
        when(userService.createUser(any(User.class))).thenReturn(newUser);

        ResponseEntity<?> response = userController.createUser(newUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status sollte CREATED sein");
        assertEquals(newUser, response.getBody(), "Erstellter Benutzer sollte zurückgegeben werden");
    }

    @Test
    public void login_ShouldReturnTokenForValidUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setPassword("Passwort123.");

        when(userService.findByUsername("user1")).thenReturn(user);
        when(passwordEncoder.matches(anyString(), eq("Passwort123."))).thenReturn(true);

        ResponseEntity<?> response = userController.login(user);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status sollte OK sein");
        assertNotNull(response.getBody(), "Token sollte nicht null sein");
    }



    @Test
    public void login_ShouldReturnUnauthorizedForInvalidUser() {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("Passwort123.");
        when(userService.findByUsername("invalidUser")).thenReturn(null);

        ResponseEntity<?> response = userController.login(user);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Status sollte UNAUTHORIZED sein");
        assertEquals("Ungültiger Benutzername oder Passwort", response.getBody(), "Antwort sollte eine Fehlermeldung enthalten");
    }

    @Test
    public void updateUser_ShouldUpdateAndReturnUser() {
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setUsername("user1");
        updatedUser.setPassword("Passwort123.");
        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(updatedUser);

        ResponseEntity<?> response = userController.updateUser(userId, updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status sollte OK sein");
        assertEquals(updatedUser, response.getBody(), "Aktualisierter Benutzer sollte zurückgegeben werden");
    }

    @Test
    public void deleteUser_ShouldDeleteUser() {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status sollte OK sein");
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    public void getUserPoints_ShouldReturnUserPoints() {
        String username = "user1";
        User user = new User();
        user.setUsername("user1");
        user.setPassword("Passwort123.");
        user.setPoints(100);
        when(userService.findByUsername(username)).thenReturn(user);

        ResponseEntity<?> response = userController.getUserPoints(username);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status sollte OK sein");
        assertEquals(100, response.getBody(), "Punkte sollten übereinstimmen");
    }

    @Test
    public void updateUserPoints_ShouldUpdateAndReturnUser() {
        String username = "user1";
        User user = new User();
        user.setUsername("user1");
        user.setPassword("Passwort123.");
        user.setPoints(100);
        when(userService.findByUsername(username)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        ResponseEntity<?> response = userController.updateUserPoints(username, Map.of("points", 150));

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status sollte OK sein");
        assertEquals(user, response.getBody(), "Aktualisierter Benutzer sollte zurückgegeben werden");
    }

    @Test
    public void getHighscores_ShouldReturnSortedUsers() {
        User user1 = new User(); // Konfigurieren Sie das User-Objekt
        user1.setUsername("user1");
        user1.setPassword("Passwort123.");
        user1.setPoints(150);
        User user2 = new User(); // Konfigurieren Sie das User-Objekt
        user2.setUsername("user2");
        user2.setPassword("Passwort123.");
        user2.setPoints(100);
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        ResponseEntity<List<User>> response = userController.getHighscores();

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status sollte OK sein");
        assertEquals(Arrays.asList(user1, user2), response.getBody(), "Liste sollte sortierte Benutzer enthalten");
    }




}
