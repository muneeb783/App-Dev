package com.example.wandersync.viewmodel;

import androidx.annotation.NonNull;

import com.example.wandersync.model.Destination;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.GenericTypeIndicator;


import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static DatabaseManager instance;
    private final DatabaseReference usersReference;
    private final DatabaseReference destinationsReference;

    private DatabaseManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersReference = database.getReference("users");
        destinationsReference = database.getReference("destinations");
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
    public DatabaseReference getDestinationsReference() {
        return destinationsReference;
    }

    public void addDestination(String username, Destination destination,
                               OnSuccessListener<Void> onSuccessListener,
                               OnFailureListener onFailureListener) {
        destination.setUsername(username);
        String destinationId = destinationsReference.push().getKey();
        if (destinationId != null) {
            destinationsReference.child(destinationId).setValue(destination)
                    .addOnSuccessListener(onSuccessListener)
                    .addOnFailureListener(onFailureListener);
        } else {
            onFailureListener.onFailure(new Exception("Error generating destination ID"));
        }
    }

    public void loadDestinations(String username, ValueEventListener valueEventListener) {
        destinationsReference.addValueEventListener(valueEventListener);
    }

    public void saveVacationTime(String username, long duration,
                                 OnSuccessListener<Void> onSuccessListener,
                                 OnFailureListener onFailureListener) {
        DatabaseReference userVacationRef = usersReference.child(username).child("allotedTime");
        userVacationRef.setValue(duration)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }
    public void addCollaborator(String username, String mainUserUsername, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        DatabaseReference collaboratorRef = usersReference.child(mainUserUsername).child("contributors");

        collaboratorRef.get().addOnSuccessListener(snapshot -> {
            List<String> contributors = snapshot.exists() ? snapshot.getValue(new GenericTypeIndicator<List<String>>() {}) : new ArrayList<>();

            if (!contributors.contains(username)) {
                contributors.add(username);
                collaboratorRef.setValue(contributors)
                        .addOnSuccessListener(aVoid -> {
                            DatabaseReference userRef = usersReference.child(username);
                            userRef.child("isCollaborator").setValue(true)
                                    .addOnSuccessListener(aVoid1 -> {
                                        userRef.child("mainUserId").setValue(mainUserUsername)
                                                .addOnSuccessListener(onSuccessListener)
                                                .addOnFailureListener(onFailureListener);
                                    })
                                    .addOnFailureListener(onFailureListener);
                        })
                        .addOnFailureListener(onFailureListener);
            } else {
                onSuccessListener.onSuccess(null); // Collaborator already exists
            }
        }).addOnFailureListener(onFailureListener);
    }
}
