package de.webtech.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import de.webtech.backend.model.Person;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByUsername(String username);

}

