package de.webtech.backend.Service;

import de.webtech.backend.exception.ResourceNotFoundException;
import de.webtech.backend.model.Skin;
import de.webtech.backend.repository.SkinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkinService {

    @Autowired
    private SkinRepository skinRepository;

    public List<Skin> getAllSkins() {
        return skinRepository.findAll();
    }

    public Optional<Skin> getSkinById(Long id) {
        return skinRepository.findById(id);
    }


    public Skin createSkin(Skin skin, String username) {
        // Debugging-Ausgabe
        System.out.println(skin.getColor());
        System.out.println(skin.getShape());

        // Speichern des Skins mit dem Benutzer
        skin.setUsername(username);  // Benutzen Sie den übergebenen Benutzernamen
        System.out.println(skin.getUsername());
        return skinRepository.save(skin);
    }


    public List<Skin> getSkinsByUsername(String username) {
        return skinRepository.findByUsername(username);
    }


    public Skin updateSkin(Long id, Skin skinDetails) {
        Skin skin = skinRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Skin not found"));
        skin.setColor(skinDetails.getColor());
        skin.setShape(skinDetails.getShape());
        return skinRepository.save(skin);
    }

    public void deleteSkin(Long id) {
        Skin skin = skinRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Skin not found"));
        skinRepository.delete(skin);
    }

}
