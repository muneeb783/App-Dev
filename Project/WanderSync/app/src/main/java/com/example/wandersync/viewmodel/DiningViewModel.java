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

            String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "anonymous";  // Get userId

            DiningReservation reservation = new DiningReservation(location, website, reservationTime, review, userId);

            // Add reservation to Firebase Database
            reservationsDatabase.push().setValue(reservation)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Update the LiveData with new reservations
                            fetchReservations();
                            reservationAddedStatus.setValue(true);
                        } else {
                            reservationAddedStatus.setValue(false);
                        }
                    });

        } catch (Exception e) {
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
