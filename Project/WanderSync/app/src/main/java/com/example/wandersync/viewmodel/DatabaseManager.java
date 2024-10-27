package com.example.wandersync.viewmodel;

import com.example.wandersync.Model.Destination;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

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
}
