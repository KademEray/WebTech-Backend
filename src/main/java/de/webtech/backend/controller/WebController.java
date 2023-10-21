package de.webtech.backend.controller;

import de.webtech.backend.model.Highscore;
import de.webtech.backend.model.Person;
import de.webtech.backend.repository.HighscoreRepository;
import de.webtech.backend.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@CrossOrigin
@Controller
public class WebController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private HighscoreRepository highscoreRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            if (personRepository.findByUsername(username).isPresent()) {
                model.addAttribute("message", "Benutzername bereits vergeben");
                return "highscores";
            }
            Person person = new Person();
            person.setUsername(username);
            person.setPassword(passwordEncoder.encode(password));
            personRepository.save(person);
            return "redirect:/highscores";
        } catch (Exception e) {
            model.addAttribute("message", "Ein Fehler ist aufgetreten: " + e.getMessage());
            return "error"; // Sie können hier auf eine spezifische Fehlerseite verweisen oder den gleichen View zurückgeben, um den Fehler anzuzeigen.
        }
    }


    @PostMapping("/addHighscore")
    public String addHighscore(@RequestParam String username, @RequestParam int highscore, Model model) {
        Person person = personRepository.findByUsername(username).orElse(null);
        if (person != null) {
            Highscore hs = new Highscore();
            hs.setPerson(person);
            hs.setScore(highscore);
            highscoreRepository.save(hs);
        } else {
            model.addAttribute("message", "Benutzername nicht gefunden");
            return "highscores";
        }
        return "redirect:/highscores";
    }
    @GetMapping("/highscores")
    public String viewHighscores(Model model) {
        // Hier können Sie Daten hinzufügen, die Sie auf der Seite anzeigen möchten, z.B.:
        List<Highscore> highscores = highscoreRepository.findAll();
        model.addAttribute("highscores", highscores);
        return "highscores";
    }

}