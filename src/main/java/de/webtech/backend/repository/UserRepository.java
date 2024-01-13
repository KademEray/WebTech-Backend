package de.webtech.backend.repository;

import de.webtech.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository-Interface für die User-Entität.
 * Erweitert JpaRepository, um CRUD-Operationen und zusätzliche Methoden zur Interaktion mit der User-Tabelle zu ermöglichen.
 * Ermöglicht den Zugriff auf Benutzerdaten in der Datenbank.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Findet einen Benutzer anhand seines Benutzernamens.
     *
     * @param username Der Benutzername des gesuchten Benutzers.
     * @return Ein User-Objekt, wenn ein Benutzer mit dem angegebenen Benutzernamen gefunden wird, andernfalls null.
     */
    User findByUsername(String username);
}
