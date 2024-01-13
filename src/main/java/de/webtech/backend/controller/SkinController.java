package de.webtech.backend.controller;

import de.webtech.backend.Service.SkinService;
import de.webtech.backend.Service.UserService;
import de.webtech.backend.exception.ResourceNotFoundException;
import de.webtech.backend.model.Skin;
import de.webtech.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * Controller für die Verwaltung von Skins.
 * Stellt Endpunkte bereit, um Skins zu erstellen, zu aktualisieren, zu löschen und abzurufen.
 * Unterstützt auch die Abfrage von Skins für bestimmte Benutzer.
 */
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/skins")
public class SkinController {



    @Autowired
    private SkinService skinService;

    @Autowired
    private UserService userService;


    /**
     * Ruft alle verfügbaren Skins ab.
     *
     * @return Eine Liste von Skins.
     */
    @GetMapping
    public List<Skin> getAllSkins() {
        return skinService.getAllSkins();
    }


    /**
     * Ruft Skins für den aktuell eingeloggten Benutzer ab.
     *
     * @param userDetails Details des eingeloggten Benutzers.
     * @return Eine Antwort-Entität mit den Skins des Benutzers oder ein UNAUTHORIZED-Status, falls nicht eingeloggt.
     */
    @GetMapping("/forUser")
    public ResponseEntity<?> getSkins(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            User user = userService.findByUsername(username);
            return new ResponseEntity<>(skinService.getSkinsByUsername(user.getUsername()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }



    /**
     * Ruft Skins für einen spezifischen Benutzer ab.
     *
     * @param username Der Benutzername, für den die Skins abgerufen werden sollen.
     * @return Eine Antwort-Entität mit den Skins des Benutzers.
     */
    @GetMapping("/forUser/{username}")
    public ResponseEntity<List<Skin>> getSkinsByUsername(@PathVariable String username) {
        List<Skin> skins = skinService.getSkinsByUsername(username);
        return ResponseEntity.ok(skins);
    }

    /**
     * Stellt private Daten für den authentifizierten Benutzer bereit.
     *
     * @param userDetails Details des eingeloggten Benutzers.
     * @return Eine Nachricht für den Benutzer.
     */
    @GetMapping("/api/private/data")
    public String getPrivateData(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            return "Hallo " + username;
        } else {
            return "Sie müssen sich anmelden, um auf diese Ressource zuzugreifen.";
        }
    }


    /**
     * Erstellt einen neuen Skin.
     *
     * @param skin Die Skin-Details für die Erstellung.
     * @return Eine Antwort-Entität mit dem erstellten Skin.
     */
    @PostMapping("/createSkin")
    public ResponseEntity<Skin> createSkin(@RequestBody Skin skin) {
        String username = skin.getUsername();
        Skin createdSkin = skinService.createSkin(skin, username);
        return new ResponseEntity<>(createdSkin, HttpStatus.CREATED);
    }



    /**
     * Aktualisiert einen vorhandenen Skin.
     *
     * @param id Die ID des zu aktualisierenden Skins.
     * @param skinDetails Die aktualisierten Skin-Details.
     * @return Eine Antwort-Entität mit dem aktualisierten Skin.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Skin> updateSkin(@PathVariable Long id, @RequestBody Skin skinDetails) {
        Skin updatedSkin = skinService.updateSkin(id, skinDetails);
        return ResponseEntity.ok(updatedSkin);
    }


    /**
     * Löscht einen Skin.
     *
     * @param id Die ID des zu löschenden Skins.
     * @return Eine leere Antwort-Entität, die den Erfolg der Operation anzeigt.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkin(@PathVariable Long id) {
        skinService.deleteSkin(id);
        return ResponseEntity.ok().build();
    }


}
