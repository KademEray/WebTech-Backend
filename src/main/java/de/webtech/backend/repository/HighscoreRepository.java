package de.webtech.backend.repository;


import de.webtech.backend.model.Highscore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HighscoreRepository extends JpaRepository<Highscore, Long> {
}
