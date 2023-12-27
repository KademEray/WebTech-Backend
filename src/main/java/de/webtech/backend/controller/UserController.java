package de.webtech.backend.controller;

import de.webtech.backend.Service.UserService;
import de.webtech.backend.exception.ResourceNotFoundException;
import de.webtech.backend.model.User;
import de.webtech.backend.model.UserDTO;
import de.webtech.backend.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    String jws = Jwts.builder().subject("username").signWith(key).compact();

    // Get all users
    @GetMapping("/")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    @GetMapping("/me")
    public UserDetails getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }

    // Create a new user
    @PostMapping("/")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Spezifische Fehlermeldung für bereits vergebene Benutzernamen
            if (e.getMessage().equals("Der Benutzername ist bereits vergeben")) {
                return new ResponseEntity<>("Benutzername bereits vergeben", HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return new ResponseEntity<>("Ungültiger Benutzername oder Passwort", HttpStatus.UNAUTHORIZED);
        }

        String token = Jwts.builder().setSubject(existingUser.getId().toString()).signWith(key).compact();


        return new ResponseEntity<>(token, HttpStatus.OK);
    }


    // Update a user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    // Delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get user points by username
    @GetMapping("/{username}/points")
    public ResponseEntity<?> getUserPoints(@PathVariable String username) {
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(user.getPoints(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Update user points
    @PutMapping("/{username}/points")
    public ResponseEntity<?> updateUserPoints(@PathVariable String username, @RequestBody Map<String, Integer> pointsData) {
        try {
            User user = userService.findByUsername(username);
            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            int newPoints = pointsData.get("points");
            if (newPoints > user.getPoints()) {
                user.setPoints(newPoints);
                userRepository.save(user);
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/highscores")
    public ResponseEntity<List<User>> getHighscores() {
        List<User> users = userService.getAllUsers();
        // Sortierung der Benutzer nach Punkten
        users.sort((u1, u2) -> Integer.compare(u2.getPoints(), u1.getPoints()));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }





}
