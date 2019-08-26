package com.patrycja.pound.models;

import javax.persistence.*;

/*@Entity
@Table(name = "employees")*/
public class Employee {

/*    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)*/
    protected int id;
    protected String name;
    protected String surname;

    public Employee() {
    }

    public Employee(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
