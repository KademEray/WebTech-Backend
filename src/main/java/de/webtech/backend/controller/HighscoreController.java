package de.webtech.backend.controller;

import de.webtech.backend.model.Highscore;
import de.webtech.backend.repository.HighscoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/highscores")
public class HighscoreController {
    @Autowired
    private HighscoreRepository highscoreRepository;

    @PostMapping
    public Highscore createHighscore(@RequestBody Highscore highscore) {
        return highscoreRepository.save(highscore);
    }

    @GetMapping
    public List<Highscore> getAllHighscores() {
        return highscoreRepository.findAll();
    }

    @GetMapping("/highscores")
    public String viewAllHighscores(Model model) {
        List<Highscore> highscores = highscoreRepository.findAll();
        model.addAttribute("highscores", highscores);
        return "highscores";
    }


}
