package com.example.wandersync.Model;

import java.util.ArrayList;

public class User {
    private String username;
    private String email;
    private String password;
    private ArrayList<String> notes;
    private ArrayList<String> contributors; // List of contributor usernames

    // Default constructor
    public User() {
        notes = new ArrayList<>();
        contributors = new ArrayList<>();
    }

    // Constructor with parameters
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.notes = new ArrayList<>();
        this.contributors = new ArrayList<>();
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public ArrayList<String> getContributors() {
        return contributors;
    }



    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }

    public void setContributors(ArrayList<String> contributors) {
        this.contributors = contributors;
    }


    // Additional methods


    public void addContributor(String contributor) {
        if (!contributors.contains(contributor)) {
            contributors.add(contributor);
        }
    }

}
