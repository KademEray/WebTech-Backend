package de.webtech.backend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Person {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password; // Sie sollten Passwörter niemals im Klartext speichern. Verwenden Sie stattdessen eine Bibliothek wie BCrypt.

    @OneToMany(mappedBy = "person")
    private List<Highscore> highscores;

    public Person() {

    }
    // Getter und Setter für Highscores
    public List<Highscore> getHighscores() {
        return highscores;
    }

    public void setHighscores(List<Highscore> highscores) {
        this.highscores = highscores;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
