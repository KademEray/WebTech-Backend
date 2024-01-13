package de.webtech.backend.Service;

import de.webtech.backend.exception.ResourceNotFoundException;
import de.webtech.backend.model.Skin;
import de.webtech.backend.repository.SkinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service-Klasse für die Verwaltung von Skin-Objekten.
 * Diese Klasse bietet Methoden zur Interaktion mit der SkinRepository und zur Verwaltung von Skin-Daten.
 */
@Service
public class SkinService {

    @Autowired
    private SkinRepository skinRepository;

    /**
     * Ruft alle Skins aus der Datenbank ab.
     *
     * @return Eine Liste aller Skins.
     */
    public List<Skin> getAllSkins() {
        return skinRepository.findAll();
    }

    /**
     * Sucht nach einem Skin anhand seiner ID.
     *
     * @param id Die ID des gesuchten Skins.
     * @return Ein Optional, das den gefundenen Skin enthält, oder ein leeres Optional, wenn kein Skin gefunden wurde.
     */
    public Optional<Skin> getSkinById(Long id) {
        return skinRepository.findById(id);
    }

    /**
     * Erstellt einen neuen Skin und speichert ihn in der Datenbank.
     *
     * @param skin Der zu speichernde Skin.
     * @param username Der Benutzername des Skin-Besitzers.
     * @return Der gespeicherte Skin.
     */
    public Skin createSkin(Skin skin, String username) {
        // Debugging-Ausgabe
        System.out.println(skin.getColor());
        System.out.println(skin.getShape());

        skin.setUsername(username);
        System.out.println(skin.getUsername());
        return skinRepository.save(skin);
    }

    /**
     * Ruft alle Skins ab, die einem bestimmten Benutzer zugeordnet sind.
     *
     * @param username Der Benutzername, zu dem die Skins gehören.
     * @return Eine Liste von Skins, die dem angegebenen Benutzernamen zugeordnet sind.
     */
    public List<Skin> getSkinsByUsername(String username) {
        return skinRepository.findByUsername(username);
    }

    /**
     * Aktualisiert einen vorhandenen Skin anhand seiner ID.
     *
     * @param id Die ID des zu aktualisierenden Skins.
     * @param skinDetails Die neuen Details für den Skin.
     * @return Der aktualisierte Skin.
     * @throws ResourceNotFoundException Wenn kein Skin mit der angegebenen ID gefunden wurde.
     */
    public Skin updateSkin(Long id, Skin skinDetails) {
        Skin skin = skinRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Skin not found"));
        skin.setColor(skinDetails.getColor());
        skin.setShape(skinDetails.getShape());
        return skinRepository.save(skin);
    }

    /**
     * Löscht einen Skin anhand seiner ID.
     *
     * @param id Die ID des zu löschenden Skins.
     * @throws ResourceNotFoundException Wenn kein Skin mit der angegebenen ID gefunden wurde.
     */
    public void deleteSkin(Long id) {
        Skin skin = skinRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Skin not found"));
        skinRepository.delete(skin);
    }

}
