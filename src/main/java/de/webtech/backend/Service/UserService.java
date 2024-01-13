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


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        return userDTO;
    }
    private boolean isValidPassword(String password) {
        final int minLength = 8;
        final boolean hasLetters = password.matches(".*[a-zA-Z].*");
        final boolean hasNumbers = password.matches(".*[0-9].*");
        final boolean hasSpecialChars = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");

        return password.length() >= minLength && hasLetters && hasNumbers && hasSpecialChars;
    }

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


    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setUsername(userDetails.getUsername());
        if (!isValidPassword(userDetails.getPassword())) {
            throw new IllegalArgumentException("Das Passwort erfüllt nicht die erforderlichen Kriterien");
        }

        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


}
