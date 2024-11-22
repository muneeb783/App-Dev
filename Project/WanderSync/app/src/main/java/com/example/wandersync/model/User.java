package com.example.wandersync.model;

import java.util.ArrayList;

public class User {
    private String username;
    private String email;
    private String password;
    private long allotedTime;
    private ArrayList<String> notes;
    private ArrayList<String> contributors;
    private boolean isCollaborator;
    private String mainUserId;

    public User() {
        notes = new ArrayList<>();
        contributors = new ArrayList<>();
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.notes = new ArrayList<>();
        this.contributors = new ArrayList<>();
        this.allotedTime = 0;
        this.isCollaborator = false;
        this.mainUserId = "";
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public long getAllotedTime() {
        return allotedTime;
    }
    public ArrayList<String> getNotes() {
        return notes;
    }

    public ArrayList<String> getContributors() {
        return contributors;
    }



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


    public void addContributor(String contributor) {
        if (!contributors.contains(contributor)) {
            contributors.add(contributor);
        }
    }
    public void setCollaborator(boolean collab) {
        this.isCollaborator = collab;
    }
    public boolean getIsCollaborator() {
        return isCollaborator;
    }
}
