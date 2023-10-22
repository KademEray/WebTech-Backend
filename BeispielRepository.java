package de.webtech.backend.repository;


import de.webtech.backend.model.Beispiel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeispielRepository extends JpaRepository<Beispiel, Long> {
}
