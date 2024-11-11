package com.example.wandersync.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wandersync.model.DiningReservation;
import com.example.wandersync.viewmodel.sorting.SortStrategy;
import com.example.wandersync.viewmodel.sorting.SortByReservationTime;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiningViewModel extends AndroidViewModel {

    private final DatabaseManager databaseManager;
    private final MutableLiveData<List<DiningReservation>> reservationsLiveData =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private String userId;
    private SortStrategy sortStrategy;

    public DiningViewModel(@NonNull Application application) {
        super(application);
        databaseManager = DatabaseManager.getInstance();

        SharedPreferences sharedPreferences = application.getSharedPreferences(
                "WanderSyncPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("username", null);

        sortStrategy = new SortByReservationTime();

        checkCollaboratorStatusAndLoadReservations();
    }

    public LiveData<List<DiningReservation>> getReservations() {
        return reservationsLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void addReservation(String location,
                               String website, String date, String time, String review) {
        if (userId == null) {
            errorLiveData.setValue("User ID is not available.");
            return;
        }

        if (location.isEmpty() || website.isEmpty()
                || date.isEmpty() || time.isEmpty() || review.isEmpty()
                || !Patterns.WEB_URL.matcher(website).matches()) {
            errorLiveData.setValue("Invalid data provided for reservation.");
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "dd/MM/yyyy HH:mm", Locale.getDefault());
            long reservationTime = dateFormat.parse(date + " " + time).getTime();
            DiningReservation newReservation =
                    new DiningReservation(location, website, reservationTime, review, userId);

            databaseManager.addDiningReservation(newReservation,
                    aVoid -> loadReservations(),
                    e -> errorLiveData.setValue("Failed to add reservation: " + e.getMessage()));

        } catch (ParseException e) {
            errorLiveData.setValue("Invalid date/time format.");
        }
    }

    public void setSortStrategy(SortStrategy strategy) {
        this.sortStrategy = strategy;
        sortAndSetReservations(); // Apply sorting immediately when the strategy changes
    }

    private void checkCollaboratorStatusAndLoadReservations() {
        if (userId == null) {
            errorLiveData.setValue("User ID is not available.");
            return;
        }

        databaseManager.getUsersReference().child(userId).child("isCollaborator")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean isCollaborator = snapshot.getValue(Boolean.class);
                        if (Boolean.TRUE.equals(isCollaborator)) {
                            databaseManager.getUsersReference().child(userId).child("mainUserId")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String mainUserId = snapshot.getValue(String.class);
                                            if (mainUserId != null) {
                                                userId = mainUserId;
                                            }
                                            loadReservations();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            errorLiveData.setValue("Failed to load main user ID: "
                                                    + error.getMessage());
                                        }
                                    });
                        } else {
                            loadReservations();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        errorLiveData.setValue("Failed to check collaborator status: "
                                + error.getMessage());
                    }
                });
    }

    private void loadReservations() {
        databaseManager.loadDiningReservations(userId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<DiningReservation> reservationList = new ArrayList<>();
                for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                    DiningReservation reservation =
                            reservationSnapshot.getValue(DiningReservation.class);
                    if (reservation != null) {
                        reservationList.add(reservation);
                    }
                }
                reservationsLiveData.setValue(sortStrategy.sort(reservationList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorLiveData.setValue("Failed to load reservations: " + error.getMessage());
            }
        });
    }

    private void sortAndSetReservations() {
        List<DiningReservation> reservations = reservationsLiveData.getValue();
        if (reservations != null) {
            reservationsLiveData.setValue(sortStrategy.sort(new ArrayList<>(reservations)));
        }
    }
}
