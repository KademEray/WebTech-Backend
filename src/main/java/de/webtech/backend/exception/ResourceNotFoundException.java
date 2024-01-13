package de.webtech.backend.exception;

/**
 * Eine benutzerdefinierte Ausnahme, die ausgelöst wird, wenn eine angeforderte Ressource nicht gefunden wird.
 * Erbt von RuntimeException, was bedeutet, dass es sich um eine unchecked Exception handelt.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Konstruktor für ResourceNotFoundException.
     *
     * @param message Die Fehlermeldung, die die Ursache der Ausnahme beschreibt.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}


