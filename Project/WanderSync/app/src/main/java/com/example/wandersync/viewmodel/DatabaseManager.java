package com.example.wandersync.viewmodel;

import com.example.wandersync.model.Accommodation;
import com.example.wandersync.model.Destination;
import com.example.wandersync.model.DiningReservation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static DatabaseManager instance;
    private final DatabaseReference usersReference;
    private final DatabaseReference destinationsReference;
    private final DatabaseReference accommodationsReference;
    private final DatabaseReference diningReservationsReference;

    private DatabaseManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersReference = database.getReference("users");
        destinationsReference = database.getReference("destinations");
        accommodationsReference = database.getReference("accommodations");
        diningReservationsReference = database.getReference("diningReservations");
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

    public DatabaseReference getAccommodationsReference() {
        return accommodationsReference;
    }

    public DatabaseReference getDiningReservationsReference() {
        return diningReservationsReference;
    }

    // Method to add a new accommodation
    public void addAccommodation(Accommodation accommodation,
                                 OnSuccessListener<Void> onSuccessListener,
                                 OnFailureListener onFailureListener) {
        String accommodationId = accommodationsReference.push().getKey();
        if (accommodationId != null) {
            accommodationsReference.child(accommodationId).setValue(accommodation)
                    .addOnSuccessListener(onSuccessListener)
                    .addOnFailureListener(onFailureListener);
        } else {
            onFailureListener.onFailure(new Exception("Error generating accommodation ID"));
        }
    }

    // Method to load accommodations for a specific userID
    public void loadAccommodations(String userID, ValueEventListener valueEventListener) {
        Query userAccommodationsQuery = accommodationsReference.
                orderByChild("userID").equalTo(userID);
        userAccommodationsQuery.addListenerForSingleValueEvent(valueEventListener);
    }

    // Method to add a new dining reservation
    public void addDiningReservation(DiningReservation reservation,
                                     OnSuccessListener<Void> onSuccessListener,
                                     OnFailureListener onFailureListener) {
        String reservationId = diningReservationsReference.push().getKey();
        if (reservationId != null) {
            diningReservationsReference.child(reservationId).setValue(reservation)
                    .addOnSuccessListener(onSuccessListener)
                    .addOnFailureListener(onFailureListener);
        } else {
            onFailureListener.onFailure(new Exception("Error generating reservation ID"));
        }
    }

    // Method to load dining reservations for a specific userID
    public void loadDiningReservations(String userID, ValueEventListener valueEventListener) {
        Query userReservationsQuery = diningReservationsReference.
                orderByChild("userId").equalTo(userID);
        userReservationsQuery.addListenerForSingleValueEvent(valueEventListener);
    }

    // Method to add a new destination
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

    // Method to load destinations for a specific user
    public void loadDestinations(String userID, ValueEventListener valueEventListener) {
        Query userDestinationsQuery = destinationsReference.
                orderByChild("username").equalTo(userID);
        userDestinationsQuery.addListenerForSingleValueEvent(valueEventListener);
    }

    public void saveVacationTime(String username, long duration,
                                 OnSuccessListener<Void> onSuccessListener,
                                 OnFailureListener onFailureListener) {
        DatabaseReference userVacationRef = usersReference.child(username).child("allotedTime");
        userVacationRef.setValue(duration)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void addCollaborator(String username, String mainUserUsername,
                                OnSuccessListener<Void> onSuccessListener,
                                OnFailureListener onFailureListener) {
        DatabaseReference collaboratorRef = usersReference.
                child(mainUserUsername).child("contributors");

        collaboratorRef.get().addOnSuccessListener(snapshot -> {
            List<String> contributors = snapshot.exists() ? snapshot.
                    getValue(new GenericTypeIndicator<List<String>>() { }) : new ArrayList<>();

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
                onSuccessListener.onSuccess(null);
            }
        }).addOnFailureListener(onFailureListener);
    }
}
