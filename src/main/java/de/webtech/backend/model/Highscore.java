package de.webtech.backend.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import de.webtech.backend.model.Person;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Highscore {

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
    private int score;
    @Id
    private Long id;

    public Highscore() {
    }

    // Getter und Setter für Person
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}