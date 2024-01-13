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


/**
 * Controller-Klasse für Benutzeroperationen.
 * Stellt Endpunkte für die Verwaltung von Benutzerdaten bereit, inklusive Erstellung, Aktualisierung,
 * Löschung und Abfrage von Benutzerinformationen. Unterstützt auch Authentifizierungsfunktionen.
 */
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

    // Schlüssel für die Signatur von JWTs
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);


    /**
     * Gibt alle Benutzer zurück.
     *
     * @return Eine Liste aller Benutzer.
     */
    @GetMapping("/")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    /**
     * Ruft einen Benutzer anhand seiner ID ab.
     *
     * @param id Die ID des Benutzers.
     * @return Eine ResponseEntity mit UserDTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    /**
     * Gibt die UserDetails des aktuell eingeloggten Benutzers zurück.
     *
     * @param userDetails Die UserDetails des eingeloggten Benutzers.
     * @return UserDetails des eingeloggten Benutzers.
     */
    @GetMapping("/me")
    public UserDetails getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }

    /**
     * Erstellt einen neuen Benutzer.
     *
     * @param user Der zu erstellende Benutzer.
     * @return Eine ResponseEntity mit dem erstellten Benutzer.
     */
    @PostMapping("/")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Der Benutzername ist bereits vergeben")) {
                return new ResponseEntity<>("Benutzername bereits vergeben", HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Loggt einen Benutzer ein und gibt ein JWT zurück.
     *
     * @param user Die Anmeldedaten des Benutzers.
     * @return Ein JWT-Token.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return new ResponseEntity<>("Ungültiger Benutzername oder Passwort", HttpStatus.UNAUTHORIZED);
        }
        String token = Jwts.builder().setSubject(existingUser.getId().toString()).signWith(key).compact();
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


    /**
     * Aktualisiert einen vorhandenen Benutzer.
     *
     * @param id Die ID des zu aktualisierenden Benutzers.
     * @param userDetails Die aktualisierten Daten des Benutzers.
     * @return Eine ResponseEntity mit dem aktualisierten Benutzer.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Löscht einen Benutzer.
     *
     * @param id Die ID des zu löschenden Benutzers.
     * @return Eine ResponseEntity, die den Erfolg der Operation bestätigt.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Ruft die Punkte eines Benutzers ab.
     *
     * @param username Der Benutzername.
     * @return Eine ResponseEntity mit den Punkten des Benutzers.
     */
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


    /**
     * Aktualisiert die Punkte eines Benutzers.
     *
     * @param username Der Benutzername.
     * @param pointsData Die neuen Punktedaten.
     * @return Eine ResponseEntity mit dem aktualisierten Benutzer.
     */
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

    /**
     * Gibt eine Liste der Benutzer nach ihren Highscores sortiert zurück.
     *
     * @return Eine Liste der Benutzer nach Punkten sortiert.
     */
    @GetMapping("/highscores")
    public ResponseEntity<List<User>> getHighscores() {
        List<User> users = userService.getAllUsers();
        users.sort((u1, u2) -> Integer.compare(u2.getPoints(), u1.getPoints()));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }





}
