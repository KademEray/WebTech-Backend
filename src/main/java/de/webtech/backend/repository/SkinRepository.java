package de.webtech.backend.repository;

import de.webtech.backend.model.Skin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository-Interface für die Skin-Entität.
 * Erweitert JpaRepository, um CRUD-Operationen und zusätzliche Methoden zur Interaktion mit der Skin-Tabelle zu ermöglichen.
 */
@Repository
public interface SkinRepository extends JpaRepository<Skin, Long> {

    /**
     * Findet alle Skins, die einem bestimmten Benutzernamen zugeordnet sind.
     *
     * @param username Der Benutzername, nach dem Skins gefiltert werden sollen.
     * @return Eine Liste von Skins, die dem angegebenen Benutzernamen zugeordnet sind.
     */
    List<Skin> findByUsername(String username);
}
