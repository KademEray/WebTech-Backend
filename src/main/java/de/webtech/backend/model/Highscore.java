package de.webtech.backend.model;


import jakarta.persistence.*;
import de.webtech.backend.model.Person;

@Entity
public class Highscore {

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
    private int score;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
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