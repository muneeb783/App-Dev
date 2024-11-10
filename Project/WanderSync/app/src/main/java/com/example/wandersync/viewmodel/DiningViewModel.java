package com.example.wandersync.viewmodel;

import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wandersync.model.DiningReservation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiningViewModel extends ViewModel {

    private final MutableLiveData<List<DiningReservation>> reservationsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> reservationAddedStatus = new MutableLiveData<>();
    private final DatabaseReference reservationsDatabase = FirebaseDatabase.getInstance().getReference("reservations");
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public DiningViewModel() {
        // Fetch reservations immediately when the ViewModel is created
        fetchReservations();
    }

    public LiveData<List<DiningReservation>> getReservationsLiveData() {
        return reservationsLiveData;
    }

    public LiveData<Boolean> getReservationAddedStatus() {
        return reservationAddedStatus;
    }

    public void addReservation(String location, String website, String date, String time, String review) {
        if (location.isEmpty() || website.isEmpty() || date.isEmpty() || time.isEmpty() || review.isEmpty()) {
            reservationAddedStatus.setValue(false);
            return;
        }

        if (!Patterns.WEB_URL.matcher(website).matches()) {
            reservationAddedStatus.setValue(false);
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            long reservationTime = dateFormat.parse(date + " " + time).getTime();

            // Fetch existing reservations from Firebase
            reservationsDatabase.get().addOnSuccessListener(snapshot -> {
                boolean isDuplicate = false;

                // Check if the new reservation time matches any existing reservation time
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DiningReservation existingReservation = dataSnapshot.getValue(DiningReservation.class);
                    if (existingReservation != null && existingReservation.getReservationTime() == reservationTime) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (isDuplicate) {
                    // If a duplicate is found, show the failure message and return
                    reservationAddedStatus.setValue(false);
                } else {
                    // No duplicates found, proceed to add the new reservation
                    String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "anonymous";  // Get userId
                    DiningReservation reservation = new DiningReservation(location, website, reservationTime, review, userId);

                    // Add reservation to Firebase Database
                    reservationsDatabase.push().setValue(reservation)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Update the LiveData with new reservations by adding it directly to the list
                                    List<DiningReservation> currentReservations = reservationsLiveData.getValue();
                                    if (currentReservations != null) {
                                        currentReservations.add(reservation); // Add the new reservation
                                        // Sort the list based on reservation time (or any other criteria)
                                        currentReservations.sort((r1, r2) -> Long.compare(r1.getReservationTime(), r2.getReservationTime()));
                                        reservationsLiveData.setValue(currentReservations); // Update the LiveData
                                    }
                                    reservationAddedStatus.setValue(true);
                                } else {
                                    reservationAddedStatus.setValue(false);
                                }
                            });
                }
            });

        } catch (ParseException e) {
            reservationAddedStatus.setValue(false);
        }
    }


    // Fetch reservations from the Firebase database
    private void fetchReservations() {
        reservationsDatabase.get().addOnSuccessListener(snapshot -> {
            List<DiningReservation> reservations = new ArrayList<>();
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                DiningReservation reservation = dataSnapshot.getValue(DiningReservation.class);
                if (reservation != null) {
                    reservations.add(reservation);
                }
            }
            reservationsLiveData.setValue(reservations);
        });
    }
}
