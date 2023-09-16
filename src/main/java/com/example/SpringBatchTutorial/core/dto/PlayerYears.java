package com.example.SpringBatchTutorial.core.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PlayerYears implements Serializable {

    private String ID;
    private String lastName;
    private String firstName;
    private String position;
    private int birthYear;
    private int debutYear;

    private int yearsExperience;

    public PlayerYears(Player item) {
        this.ID = item.getID();
        this.lastName = item.getLastName();
        this.firstName = item.getFirstName();
        this.position = item.getPosition();
        this.birthYear = item.getBirthYear();
        this.debutYear = item.getDebutYear();
        this.yearsExperience = LocalDateTime.now().getYear() - item.getDebutYear();
    }


    public String toString() {
        return "PLAYER:ID=" + ID + ",Last Name=" + lastName +
                ",First Name=" + firstName + ",Position=" + position +
                ",Birth Year=" + birthYear + ",DebutYear=" +
                debutYear;
    }



}