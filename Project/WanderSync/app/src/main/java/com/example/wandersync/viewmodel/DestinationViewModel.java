package com.example.wandersync.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wandersync.model.Destination;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class DestinationViewModel extends AndroidViewModel {
    private final DatabaseManager databaseManager;
    private final MutableLiveData<List<Destination>> destinationsLiveData;
    private final MutableLiveData<String> errorLiveData;
    private final MutableLiveData<Boolean> isCollaboratorLiveData;
    private final String username;

    public DestinationViewModel(@NonNull Application application) {
        super(application);
        databaseManager = DatabaseManager.getInstance();
        destinationsLiveData = new MutableLiveData<>(new ArrayList<>());
        errorLiveData = new MutableLiveData<>();
        isCollaboratorLiveData = new MutableLiveData<>();

        SharedPreferences sharedPreferences = application.getSharedPreferences("WanderSyncPrefs", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);

        checkCollaboratorStatusAndLoadDestinations();
    }

    public LiveData<List<Destination>> getDestinations() {
        return destinationsLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public LiveData<Boolean> getIsCollaborator() {
        return isCollaboratorLiveData;
    }

    private void checkCollaboratorStatusAndLoadDestinations() {
        if (username != null) {
            // Check if the current user is a collaborator
            databaseManager.getUsersReference().child(username).child("isCollaborator")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Boolean isCollaborator = snapshot.getValue(Boolean.class);
                            isCollaboratorLiveData.setValue(isCollaborator != null && isCollaborator);

                            // Fetch destinations based on collaborator status
                            if (Boolean.TRUE.equals(isCollaboratorLiveData.getValue())) {
                                fetchDestinationsForMainUser();
                            } else {
                                fetchDestinationsForUser(username);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            errorLiveData.setValue("Failed to load collaborator status: " + error.getMessage());
                        }
                    });
        } else {
            errorLiveData.setValue("Username is not available");
        }
    }

    private void fetchDestinationsForMainUser() {
        databaseManager.getUsersReference().child(username).child("mainUserId")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String mainUserId = snapshot.getValue(String.class);
                        if (mainUserId != null) {
                            fetchDestinationsForUser(mainUserId); // Fetch main user's destinations
                        } else {
                            errorLiveData.setValue("Main user ID not found for collaborator.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        errorLiveData.setValue("Failed to load main user ID: " + error.getMessage());
                    }
                });
    }

    private void fetchDestinationsForUser(String userId) {
        databaseManager.loadDestinations(userId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Destination> destinationList = new ArrayList<>();
                for (DataSnapshot destinationSnapshot : snapshot.getChildren()) {
                    Destination destination = destinationSnapshot.getValue(Destination.class);
                    if (destination != null && userId.equals(destination.getUsername())) {
                        destinationList.add(destination);
                    }
                }
                destinationsLiveData.setValue(destinationList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorLiveData.setValue("Failed to load destinations: " + error.getMessage());
            }
        });
    }

    public void addDestination(Destination destination) {
        if (Boolean.TRUE.equals(isCollaboratorLiveData.getValue())) {
            errorLiveData.setValue("Collaborators cannot add destinations to the main user's trip.");
            return;
        }

        if (username != null) {
            databaseManager.addDestination(username, destination,
                    aVoid -> checkCollaboratorStatusAndLoadDestinations(),
                    e -> errorLiveData.setValue(e.getMessage())
            );
        } else {
            errorLiveData.setValue("Username is not available");
        }
    }

    public void saveVacationTime(long duration) {
        if (Boolean.TRUE.equals(isCollaboratorLiveData.getValue())) {
            errorLiveData.setValue("Collaborators cannot modify the main user's vacation time.");
            return;
        }

        if (username != null) {
            databaseManager.saveVacationTime(username, duration,
                    aVoid -> { },
                    e -> errorLiveData.setValue("Failed to save vacation time: " + e.getMessage())
            );
        } else {
            errorLiveData.setValue("Username is not available");
        }
    }
}
