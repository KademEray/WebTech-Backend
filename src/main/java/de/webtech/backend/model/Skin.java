package de.webtech.backend.model;

import jakarta.persistence.*;

/**
 * Entitätsklasse, die einen Skin repräsentiert.
 * Ein Skin ist definiert durch seine Farbe, Form und den Benutzernamen des Besitzers.
 * Diese Klasse wird in einer Datenbank gespeichert mit der Verwendung von JPA.
 */
@Entity
public class Skin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String color; // Farbe als String
    private String shape; // Form als String
    private String username;

    /**
     * Gibt die ID des Skins zurück.
     *
     * @return Die ID des Skins.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die ID des Skins.
     *
     * @param id Die zu setzende ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gibt die Farbe des Skins zurück.
     *
     * @return Die Farbe des Skins.
     */
    public String getColor() {
        return color;
    }

    /**
     * Setzt die Farbe des Skins.
     *
     * @param color Die zu setzende Farbe des Skins.
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Gibt die Form des Skins zurück.
     *
     * @return Die Form des Skins.
     */
    public String getShape() {
        return shape;
    }

    /**
     * Setzt die Form des Skins.
     *
     * @param shape Die zu setzende Form des Skins.
     */
    public void setShape(String shape) {
        this.shape = shape;
    }

    /**
     * Gibt den Benutzernamen des Besitzers des Skins zurück.
     *
     * @return Der Benutzername des Besitzers.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setzt den Benutzernamen des Besitzers des Skins.
     *
     * @param username Der zu setzende Benutzername des Besitzers.
     */
    public void setUsername(String username) {
        this.username = username;
    }
}