package de.webtech.backend.controller;

import de.webtech.backend.model.Highscore;
import de.webtech.backend.model.Person;
import de.webtech.backend.repository.HighscoreRepository;
import de.webtech.backend.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        if (personRepository.findByUsername(username).isPresent()) {
            model.addAttribute("message", "Benutzername bereits vergeben");
            return "registration";
        }
        Person person = new Person();
        person.setUsername(username);
        person.setPassword(passwordEncoder.encode(password));
        personRepository.save(person);
        return "redirect:/highscores";
    }

    @PostMapping("/addHighscore")
    public String addHighscore(@RequestParam String username, @RequestParam int highscore) {
        Person person = personRepository.findByUsername(username).orElse(null);
        if (person != null) {
            Highscore hs = new Highscore();
            hs.setPerson(person);
            hs.setScore(highscore);
            highscoreRepository.save(hs);
        }
        return "redirect:/highscores";
    }

    @GetMapping("/highscores")
    public String viewAllHighscores(Model model) {
        model.addAttribute("highscores", highscoreRepository.findAll());
        model.addAttribute("persons", personRepository.findAll()); // Fügen Sie diese Zeile hinzu
        return "highscores";
    }

}

