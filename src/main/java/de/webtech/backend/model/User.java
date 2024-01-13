package de.webtech.backend.model;

import jakarta.persistence.*;
import javax.validation.constraints.NotBlank;

import java.util.List;

/**
 * Entitätsklasse, die einen Benutzer repräsentiert.
 * Die Klasse wird in einer Datenbank mit JPA gespeichert und beinhaltet Informationen wie
 * Benutzername, Passwort und Punkte. Benutzername und Passwort dürfen nicht leer sein.
 */
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Username cannot be blank")
    private String username;
    @NotBlank(message = "Password cannot be blank")
    private String password;
    private int points;

    /**
     * Gibt die ID des Benutzers zurück.
     *
     * @return Die ID des Benutzers.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die ID des Benutzers.
     *
     * @param id Die zu setzende ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gibt den Benutzernamen zurück.
     *
     * @return Der Benutzername.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setzt den Benutzernamen.
     *
     * @param username Der zu setzende Benutzername.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gibt das Passwort des Benutzers zurück.
     *
     * @return Das Passwort des Benutzers.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setzt das Passwort des Benutzers.
     *
     * @param password Das zu setzende Passwort.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gibt die Punkte des Benutzers zurück.
     *
     * @return Die Punkte des Benutzers.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Setzt die Punkte des Benutzers.
     *
     * @param points Die zu setzenden Punkte.
     */
    public void setPoints(int points) {
        this.points = points;
    }

}
