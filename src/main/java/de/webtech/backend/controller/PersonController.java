package de.webtech.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import de.webtech.backend.model.Person;
import de.webtech.backend.repository.PersonRepository;
import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        return personRepository.save(person);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Person person) {
        if (personRepository.findByUsername(person.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Benutzername bereits vergeben");
        }
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepository.save(person);
        return ResponseEntity.ok("Registrierung erfolgreich");
    }
}

