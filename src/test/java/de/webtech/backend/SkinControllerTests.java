package de.webtech.backend;

import de.webtech.backend.Service.SkinService;
import de.webtech.backend.Service.UserService;
import de.webtech.backend.controller.SkinController;
import de.webtech.backend.model.Skin;
import de.webtech.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testklasse für den SkinController.
 * Diese Klasse enthält verschiedene Unit-Tests zur Überprüfung der Funktionalitäten des SkinControllers.
 */
public class SkinControllerTests {

    @Mock
    private SkinService skinService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SkinController skinController;

    /**
     * Initialisierungsmethode, die vor jedem Test aufgerufen wird.
     * Initialisiert die Mock-Objekte.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Testet, ob die Methode getAllSkins eine Liste von Skins zurückgibt.
     */
    @Test
    public void getAllSkins_ShouldReturnListOfSkins() {
        Skin skin1 = new Skin();
        skin1.setId(1L);
        skin1.setColor("Rot");
        skin1.setShape("Rund");
        skin1.setUsername("user1");

        Skin skin2 = new Skin();
        skin2.setId(2L);
        skin2.setColor("Blau");
        skin2.setShape("Quadratisch");
        skin2.setUsername("user2");

        when(skinService.getAllSkins()).thenReturn(Arrays.asList(skin1, skin2));

        List<Skin> result = skinController.getAllSkins();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(skinService).getAllSkins();
    }

    /**
     * Testet, ob die Methode getSkins für einen eingeloggten Benutzer Skins zurückgibt.
     */
    @Test
    public void getSkins_WhenUserDetailsNotNull_ShouldReturnSkinsForUser() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user1");

        User user = new User();
        user.setUsername("user1");
        user.setPassword("Passwort123.");

        when(userService.findByUsername("user1")).thenReturn(user); // Definieren Sie das Verhalten des userService Mocks

        Skin skin = new Skin();
        skin.setId(3L);
        skin.setColor("Grün");
        skin.setShape("Dreieckig");
        skin.setUsername("user1");

        when(skinService.getSkinsByUsername("user1")).thenReturn(Arrays.asList(skin));

        ResponseEntity<?> response = skinController.getSkins(userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(Arrays.asList(skin), response.getBody());
    }

    /**
     * Testet, ob die Methode createSkin einen neuen Skin erstellt und zurückgibt.
     */
    @Test
    public void createSkin_ShouldCreateAndReturnSkin() {
        Skin newSkin = new Skin();
        newSkin.setColor("Schwarz");
        newSkin.setShape("Stern");
        newSkin.setUsername("user3");

        Skin createdSkin = new Skin();
        createdSkin.setId(5L);
        createdSkin.setColor("Schwarz");
        createdSkin.setShape("Stern");
        createdSkin.setUsername("user3");

        when(skinService.createSkin(newSkin, "user3")).thenReturn(createdSkin);

        ResponseEntity<Skin> response = skinController.createSkin(newSkin);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdSkin, response.getBody());
    }

    /**
     * Testet, ob die Methode deleteSkin einen Skin löscht und OK zurückgibt.
     */
    @Test
    public void deleteSkin_ShouldDeleteSkinAndReturnOk() {
        Long skinId = 1L;
        doNothing().when(skinService).deleteSkin(skinId);

        ResponseEntity<Void> response = skinController.deleteSkin(skinId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(skinService).deleteSkin(skinId);
    }

    /**
     * Testet, ob die Methode getSkinsByUsername Skins für einen gegebenen Benutzernamen zurückgibt.
     */
    @Test
    public void getSkinsByUsername_ShouldReturnSkinsForGivenUsername() {
        String username = "user2";
        Skin skin1 = new Skin();
        skin1.setId(3L);
        skin1.setColor("Grün");
        skin1.setShape("Dreieckig");
        skin1.setUsername("user2");

        Skin skin2 = new Skin();
        skin2.setId(4L);
        skin2.setColor("Gelb");
        skin2.setShape("Oval");
        skin2.setUsername("user2");

        when(skinService.getSkinsByUsername(username)).thenReturn(Arrays.asList(skin1, skin2));

        ResponseEntity<List<Skin>> response = skinController.getSkinsByUsername(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Arrays.asList(skin1, skin2), response.getBody());
    }

    /**
     * Testet die getPrivateData-Methode für eingeloggte Benutzer und gibt eine Begrüßungsnachricht zurück.
     */
    @Test
    public void getPrivateData_WhenUserDetailsNotNull_ShouldReturnGreeting() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user3");

        String response = skinController.getPrivateData(userDetails);

        assertEquals("Hallo user3", response);
    }

    /**
     * Testet die getPrivateData-Methode für nicht eingeloggte Benutzer und gibt eine Aufforderung zur Anmeldung zurück.
     */
    @Test
    public void getPrivateData_WhenUserDetailsNull_ShouldReturnLoginPrompt() {
        String response = skinController.getPrivateData(null);
        assertEquals("Sie müssen sich anmelden, um auf diese Ressource zuzugreifen.", response);
    }

    /**
     * Testet, ob die Methode updateSkin einen Skin aktualisiert und den aktualisierten Skin zurückgibt.
     */
    @Test
    public void updateSkin_ShouldUpdateAndReturnSkin() {
        Long skinId = 5L;
        Skin skinToUpdate = new Skin();
        skinToUpdate.setColor("Aktualisiert Blau");
        skinToUpdate.setShape("Aktualisiert Rund");

        Skin updatedSkin = new Skin();
        updatedSkin.setId(skinId);
        updatedSkin.setColor("Aktualisiert Blau");
        updatedSkin.setShape("Aktualisiert Rund");
        updatedSkin.setUsername("user4");

        when(skinService.updateSkin(eq(skinId), any(Skin.class))).thenReturn(updatedSkin);

        ResponseEntity<Skin> response = skinController.updateSkin(skinId, skinToUpdate);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedSkin, response.getBody());
    }
}