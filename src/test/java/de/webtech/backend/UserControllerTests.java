package de.webtech.backend;

import de.webtech.backend.Service.UserService;
import de.webtech.backend.controller.UserController;
import de.webtech.backend.model.User;
import de.webtech.backend.model.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class UserControllerTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllUsers_ShouldReturnListOfUsers() {
        User user1 = new User(); // Ersetzen Sie dies durch geeignete Testinstanzen
        User user2 = new User();
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        List<User> result = userController.getAllUsers();

        assertNotNull(result, "Die Liste sollte nicht null sein");
        assertEquals(2, result.size(), "Die Liste sollte zwei Benutzer enthalten");
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void getUserById_ShouldReturnUserDTO() {
        Long userId = 1L;
        UserDTO mockUserDTO = new UserDTO(); // Ersetzen Sie dies durch eine geeignete Testinstanz
        when(userService.getUserById(userId)).thenReturn(mockUserDTO);

        ResponseEntity<UserDTO> response = userController.getUserById(userId);

        assertNotNull(response.getBody(), "UserDTO sollte nicht null sein");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status sollte OK sein");
        verify(userService, times(1)).getUserById(userId);
    }


}
