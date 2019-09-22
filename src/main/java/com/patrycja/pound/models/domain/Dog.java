package com.patrycja.pound.models.domain;

import lombok.*;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Dog extends Animal {

    private int numberOfTooth;

    @Builder(builderMethodName = "dogBuilder")
    public Dog(String name, int age, Zookeeper zookeeper, int numberOfTooth) {
        super(name, age, zookeeper);
        this.numberOfTooth = numberOfTooth;
    }

    public Dog(String name, int age, int numberOfTooth) {
        super(name, age);
        this.numberOfTooth = numberOfTooth;
    }

}
