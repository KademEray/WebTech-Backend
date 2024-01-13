package de.webtech.backend.Service;

import de.webtech.backend.exception.ResourceNotFoundException;
import de.webtech.backend.model.User;
import de.webtech.backend.model.UserDTO;
import de.webtech.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service-Klasse für die Verwaltung von Benutzerdaten.
 * Diese Klasse bietet Methoden zur Interaktion mit dem UserRepository und zur Verwaltung von Benutzerdaten.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Ruft alle Benutzer aus der Datenbank ab.
     *
     * @return Eine Liste aller Benutzer.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Sucht nach einem Benutzer anhand seiner ID und gibt ihn als DTO zurück.
     *
     * @param id Die ID des gesuchten Benutzers.
     * @return Ein UserDTO mit den Daten des Benutzers.
     * @throws ResourceNotFoundException Wenn kein Benutzer mit der angegebenen ID gefunden wird.
     */
    @Transactional
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        return userDTO;
    }

    /**
     * Validiert das Passwort nach bestimmten Kriterien.
     *
     * @param password Das zu validierende Passwort.
     * @return true, wenn das Passwort den Kriterien entspricht, sonst false.
     */
    private boolean isValidPassword(String password) {
        final int minLength = 8;
        final boolean hasLetters = password.matches(".*[a-zA-Z].*");
        final boolean hasNumbers = password.matches(".*[0-9].*");
        final boolean hasSpecialChars = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");

        return password.length() >= minLength && hasLetters && hasNumbers && hasSpecialChars;
    }

    /**
     * Erstellt einen neuen Benutzer und speichert ihn in der Datenbank.
     *
     * @param user Der zu erstellende Benutzer.
     * @return Der erstellte Benutzer.
     * @throws IllegalArgumentException Wenn der Benutzername bereits vergeben ist oder das Passwort nicht den Kriterien entspricht.
     */
    public User createUser(User user) throws IllegalArgumentException {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Der Benutzername ist bereits vergeben");
        }

        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Das Passwort erfüllt nicht die erforderlichen Kriterien");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Aktualisiert die Daten eines bestehenden Benutzers.
     *
     * @param id Die ID des zu aktualisierenden Benutzers.
     * @param userDetails Die neuen Daten des Benutzers.
     * @return Der aktualisierte Benutzer.
     * @throws ResourceNotFoundException Wenn kein Benutzer mit der angegebenen ID gefunden wird.
     */
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setUsername(userDetails.getUsername());
        if (!isValidPassword(userDetails.getPassword())) {
            throw new IllegalArgumentException("Das Passwort erfüllt nicht die erforderlichen Kriterien");
        }

        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Löscht einen Benutzer anhand seiner ID.
     *
     * @param id Die ID des zu löschenden Benutzers.
     * @throws ResourceNotFoundException Wenn kein Benutzer mit der angegebenen ID gefunden wird.
     */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

    /**
     * Sucht nach einem Benutzer anhand seines Benutzernamens.
     *
     * @param username Der Benutzername des gesuchten Benutzers.
     * @return Der gefundene Benutzer oder null, wenn kein Benutzer gefunden wurde.
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


}
