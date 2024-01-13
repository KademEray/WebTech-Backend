package de.webtech.backend.model;

/**
 * Data Transfer Object (DTO) für Benutzerdaten.
 * Diese Klasse wird verwendet, um Benutzerdaten zwischen verschiedenen Schichten der Anwendung zu übertragen,
 * insbesondere bei Interaktionen mit der API. Sie enthält grundlegende Benutzerinformationen wie ID, Benutzername und Passwort.
 */
public class UserDTO {
    private Long id;
    private String username;
    private String password;

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

    // Weitere Getter und Setter für zusätzliche Felder
}
