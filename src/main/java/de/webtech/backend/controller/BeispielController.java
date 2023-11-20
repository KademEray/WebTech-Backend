//package de.webtech.backend.controller;
//
//import de.webtech.backend.model.Beispiel;  // Import Ihrer eigenen Beispiel-Klasse
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//import de.webtech.backend.repository.BeispielRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//@CrossOrigin(origins = "http://localhost:8081")
//@RestController
//@RequestMapping("/api/users")
//public class BeispielController {
//
//
//
//    @Autowired
//    private BeispielRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    // CREATE
//    @PostMapping("/create")
//    public ResponseEntity<Beispiel> createUser(@RequestBody Beispiel user) {
//        try {
//            // Verschlüsseln des Passworts
//            String encodedPassword = passwordEncoder.encode(user.getPassword());
//            user.setPassword(encodedPassword);
//
//            // Speichern des Benutzers
//            Beispiel newUser = userRepository.save(user);
//
//            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // READ (All)
//    @GetMapping("/")
//    public ResponseEntity<List<Beispiel>> getAllUsers() {
//        try {
//            List<Beispiel> users = userRepository.findAll();
//            if (users.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//            return new ResponseEntity<>(users, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // READ (Single)
//    @GetMapping("/{id}")
//    public ResponseEntity<Beispiel> getUserById(@PathVariable("id") Long id) {
//        Optional<Beispiel> userData = userRepository.findById(id);
//        return userData.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    // UPDATE
//    @PutMapping("/{id}")
//    public ResponseEntity<Beispiel> updateUser(@PathVariable("id") Long id, @RequestBody Beispiel user) {
//        Optional<Beispiel> userData = userRepository.findById(id);
//
//        if (userData.isPresent()) {
//            Beispiel updatedUser = userData.get();
//            updatedUser.setUsername(user.getUsername());
//
//            // Verschlüsseln des neuen Passworts
//            String encodedPassword = passwordEncoder.encode(user.getPassword());
//            updatedUser.setPassword(encodedPassword);
//
//            return new ResponseEntity<>(userRepository.save(updatedUser), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
//    // DELETE
//    @DeleteMapping("/{id}")
//    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") Long id) {
//        try {
//            userRepository.deleteById(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//}
//