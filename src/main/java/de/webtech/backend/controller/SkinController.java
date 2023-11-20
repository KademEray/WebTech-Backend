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


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/skins")
public class SkinController {



    @Autowired
    private SkinService skinService;

    @Autowired
    private UserService userService;

    // Methode, um alle Skins abzurufen
    @GetMapping
    public List<Skin> getAllSkins() {
        return skinService.getAllSkins();
    }

    // Methode, um Skins für den eingeloggten Benutzer abzurufen
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


    // Methode, um Skins für einen bestimmten Benutzer abzurufen
    @GetMapping("/forUser/{username}")
    public ResponseEntity<List<Skin>> getSkinsByUsername(@PathVariable String username) {
        List<Skin> skins = skinService.getSkinsByUsername(username);
        return ResponseEntity.ok(skins);
    }

    @GetMapping("/api/private/data")
    public String getPrivateData(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            // Der Benutzer ist authentifiziert, tun Sie etwas mit dem Benutzernamen
            return "Hallo " + username;
        } else {
            // Der Benutzer ist nicht authentifiziert
            return "Sie müssen sich anmelden, um auf diese Ressource zuzugreifen.";
        }
    }

    // Methode, um einen neuen Skin zu erstellen
    @PostMapping("/createSkin")
    public ResponseEntity<Skin> createSkin(@RequestBody Skin skin) {
        String username = skin.getUsername();  // Benutzername aus dem Request-Body extrahieren
        // Übergebe den Benutzernamen an die Methode createSkin deines Services
        Skin createdSkin = skinService.createSkin(skin, username);
        return new ResponseEntity<>(createdSkin, HttpStatus.CREATED);
    }


    // Methode, um einen vorhandenen Skin zu aktualisieren
    @PutMapping("/{id}")
    public ResponseEntity<Skin> updateSkin(@PathVariable Long id, @RequestBody Skin skinDetails) {
        Skin updatedSkin = skinService.updateSkin(id, skinDetails);
        return ResponseEntity.ok(updatedSkin);
    }

    // Methode, um einen Skin zu löschen
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkin(@PathVariable Long id) {
        skinService.deleteSkin(id);
        return ResponseEntity.ok().build();
    }


}
