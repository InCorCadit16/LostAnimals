package com.pbl.animals.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class User {
    public long id;
    public String email;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public byte[] imageSource;

    @JsonIgnore
    public List<Post> posts;

    public String getFullName() {
        return lastName == null? firstName : firstName + " " + lastName;
    }
}
