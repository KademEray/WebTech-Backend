package de.webtech.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.webtech.backend.Service.SkinService;
import de.webtech.backend.controller.SkinController;
import de.webtech.backend.model.Skin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;

public class SkinControllerTests {

    @Mock
    private SkinService skinService;

    @InjectMocks
    private SkinController skinController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllSkins_ShouldReturnListOfSkins() {
        Skin skin1 = new Skin(); // Ersetzen Sie dies durch geeignete Test-Instanzen von Skin
        Skin skin2 = new Skin();
        when(skinService.getAllSkins()).thenReturn(Arrays.asList(skin1, skin2));

        List<Skin> result = skinController.getAllSkins();

        assertNotNull(result, "Die Liste sollte nicht null sein");
        assertEquals(2, result.size(), "Die Liste sollte zwei Skins enthalten");
        verify(skinService, times(1)).getAllSkins();
    }

}
