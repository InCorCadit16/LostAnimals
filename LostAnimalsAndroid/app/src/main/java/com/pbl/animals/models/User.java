package com.pbl.animals.models;

public class User {
    public String email;
    public String firstName;
    public String lastName;

    public String getFullName() {
        return lastName == null? firstName : firstName + " " + lastName;
    }
}
