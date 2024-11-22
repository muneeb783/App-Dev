package com.example.wandersync.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wandersync.model.Accommodation;
import com.example.wandersync.viewmodel.sorting.SortStrategy;
import com.example.wandersync.viewmodel.sorting.SortByCheckInDate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AccommodationViewModel extends AndroidViewModel {

    private final DatabaseManager databaseManager;
    private final MutableLiveData<List<Accommodation>> accommodationsLiveData =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private String userId;
    private SortStrategy sortStrategy;

    public AccommodationViewModel(@NonNull Application application) {
        super(application);
        databaseManager = DatabaseManager.getInstance();

        SharedPreferences sharedPreferences =
                application.getSharedPreferences("WanderSyncPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("username", null);

        // Set default sorting strategy (e.g., sort by check-in date)
        sortStrategy = new SortByCheckInDate();

        checkCollaboratorStatusAndLoadAccommodations();
    }

    public LiveData<List<Accommodation>> getAccommodations() {
        return accommodationsLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void addAccommodation(String hotelName, String location, String checkInDate,
                                 String checkOutDate, String numRooms, String roomType) {
        if (userId == null) {
            errorLiveData.setValue("User ID is not available.");
            return;
        }

        Accommodation newAccommodation = new Accommodation(userId, location,
                hotelName, checkInDate, checkOutDate, numRooms, roomType);
        databaseManager.addAccommodation(newAccommodation,
                aVoid -> loadAccommodations(),
                e -> errorLiveData.setValue("Failed to add accommodation: " + e.getMessage()));
    }

    public void setSortStrategy(SortStrategy strategy) {
        this.sortStrategy = strategy;
        sortAndSetAccommodations(); // Apply sorting immediately when strategy changes
    }

    private void checkCollaboratorStatusAndLoadAccommodations() {
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
                                            loadAccommodations();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            errorLiveData.setValue("Failed to load main user ID: "
                                                    + error.getMessage());
                                        }
                                    });
                        } else {
                            loadAccommodations();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        errorLiveData.setValue("Failed to check collaborator status: "
                                + error.getMessage());
                    }
                });
    }

    private void loadAccommodations() {
        databaseManager.loadAccommodations(userId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Accommodation> accommodationList = new ArrayList<>();
                for (DataSnapshot accommodationSnapshot : snapshot.getChildren()) {
                    Accommodation accommodation =
                            accommodationSnapshot.getValue(Accommodation.class);
                    if (accommodation != null) {
                        accommodationList.add(accommodation);
                    }
                }
                accommodationsLiveData.setValue(sortStrategy.sort(accommodationList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorLiveData.setValue("Failed to load accommodations: " + error.getMessage());
            }
        });
    }

    private void sortAndSetAccommodations() {
        List<Accommodation> accommodations = accommodationsLiveData.getValue();
        if (accommodations != null) {
            accommodationsLiveData.setValue(sortStrategy.sort(new ArrayList<>(accommodations)));
        }
    }

    public SortStrategy getSortStrategy() {
        return sortStrategy;
    }
}