package com.example.wandersync.viewmodel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseManager {
    private static DatabaseManager instance;
    private final DatabaseReference usersReference;

    private DatabaseManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersReference = database.getReference("users");
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    public DatabaseReference getUsersReference() {
        return usersReference;
    }
}


