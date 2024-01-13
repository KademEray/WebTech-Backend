package de.webtech.backend;

import de.webtech.backend.Service.SkinService;
import de.webtech.backend.exception.ResourceNotFoundException;
import de.webtech.backend.model.Skin;
import de.webtech.backend.repository.SkinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testklasse für den SkinService.
 * Diese Klasse enthält verschiedene Unit-Tests, um die Funktionalitäten des SkinService zu überprüfen.
 */
public class SkinServiceTests {

    @Mock
    private SkinRepository skinRepository;

    @InjectMocks
    private SkinService skinService;

    /**
     * Initialisiert die Mock-Objekte vor jedem Test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Testet, ob getAllSkins eine Liste von Skins zurückgibt.
     */
    @Test
    public void getAllSkins_ShouldReturnListOfSkins() {
        Skin skin1 = new Skin();
        skin1.setId(1L);
        Skin skin2 = new Skin();
        skin2.setId(2L);
        List<Skin> expectedSkins = Arrays.asList(skin1, skin2);

        when(skinRepository.findAll()).thenReturn(expectedSkins);

        List<Skin> actualSkins = skinService.getAllSkins();

        assertEquals(expectedSkins, actualSkins, "Die zurückgegebene Liste von Skins sollte mit der erwarteten Liste übereinstimmen");
    }

    /**
     * Testet, ob getSkinById einen Skin anhand seiner ID zurückgibt.
     */
    @Test
    public void getSkinById_ShouldReturnSkin() {
        Skin skin = new Skin();
        skin.setId(1L);
        when(skinRepository.findById(1L)).thenReturn(Optional.of(skin));

        Optional<Skin> foundSkin = skinService.getSkinById(1L);

        assertTrue(foundSkin.isPresent(), "Skin sollte vorhanden sein");
        assertEquals(skin, foundSkin.get(), "Gefundener Skin sollte mit dem erwarteten übereinstimmen");
    }

    /**
     * Testet, ob createSkin einen neuen Skin erstellt und zurückgibt.
     */
    @Test
    public void createSkin_ShouldCreateAndReturnSkin() {
        Skin newSkin = new Skin();
        newSkin.setColor("Blau");
        newSkin.setShape("Quadratisch");
        newSkin.setUsername("user1");

        when(skinRepository.save(any(Skin.class))).thenReturn(newSkin);

        Skin createdSkin = skinService.createSkin(newSkin, "user1");

        assertEquals("user1", createdSkin.getUsername(), "Der Benutzername sollte gesetzt werden");
        assertEquals(newSkin, createdSkin, "Erstellter Skin sollte mit dem erwarteten übereinstimmen");
    }

    /**
     * Testet, ob getSkinsByUsername eine Liste von Skins für einen gegebenen Benutzernamen zurückgibt.
     */
    @Test
    public void getSkinsByUsername_ShouldReturnListOfSkins() {
        Skin skin1 = new Skin();
        skin1.setId(1L);
        skin1.setUsername("user2");
        Skin skin2 = new Skin();
        skin2.setId(2L);
        skin2.setUsername("user2");
        List<Skin> expectedSkins = Arrays.asList(skin1, skin2);

        when(skinRepository.findByUsername("user2")).thenReturn(expectedSkins);

        List<Skin> actualSkins = skinService.getSkinsByUsername("user2");

        assertEquals(expectedSkins, actualSkins, "Die zurückgegebene Liste von Skins sollte mit der erwarteten Liste übereinstimmen");
    }

    /**
     * Testet, ob updateSkin einen bestehenden Skin aktualisiert und zurückgibt.
     */
    @Test
    public void updateSkin_ShouldUpdateAndReturnSkin() {
        Long skinId = 3L;
        Skin existingSkin = new Skin();
        existingSkin.setId(skinId);
        existingSkin.setColor("Rot");
        existingSkin.setShape("Rund");

        Skin updatedDetails = new Skin();
        updatedDetails.setColor("Grün");
        updatedDetails.setShape("Dreieckig");

        when(skinRepository.findById(skinId)).thenReturn(Optional.of(existingSkin));
        when(skinRepository.save(existingSkin)).thenReturn(existingSkin);

        Skin updatedSkin = skinService.updateSkin(skinId, updatedDetails);

        assertEquals("Grün", updatedSkin.getColor(), "Die Farbe sollte aktualisiert werden");
        assertEquals("Dreieckig", updatedSkin.getShape(), "Die Form sollte aktualisiert werden");
    }

    /**
     * Testet, ob deleteSkin einen Skin löscht.
     */
    @Test
    public void deleteSkin_ShouldDeleteSkin() {
        Long skinId = 4L;
        Skin existingSkin = new Skin();
        existingSkin.setId(skinId);

        when(skinRepository.findById(skinId)).thenReturn(Optional.of(existingSkin));
        doNothing().when(skinRepository).delete(existingSkin);

        skinService.deleteSkin(skinId);

        verify(skinRepository, times(1)).delete(existingSkin);
    }

}
