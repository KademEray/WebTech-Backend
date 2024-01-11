package de.webtech.backend;

import de.webtech.backend.Service.SkinService;
import de.webtech.backend.model.Skin;
import de.webtech.backend.repository.SkinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
public class SkinServiceTests {


    @Mock
    private SkinRepository skinRepository;

    @InjectMocks
    private SkinService skinService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllSkins_ShouldReturnListOfSkins() {
        Skin skin1 = new Skin(); // Annahme, dass Skin ein einfaches POJO ist
        skin1.setId(1L);
        Skin skin2 = new Skin();
        skin2.setId(2L);
        List<Skin> expectedSkins = Arrays.asList(skin1, skin2);

        when(skinRepository.findAll()).thenReturn(expectedSkins);

        List<Skin> actualSkins = skinService.getAllSkins();

        assertEquals(expectedSkins, actualSkins, "Die zurückgegebene Liste von Skins sollte mit der erwarteten Liste übereinstimmen");
    }
}
