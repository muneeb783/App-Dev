package com.example.wandersync.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wandersync.Model.Destination;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DestinationViewModel extends AndroidViewModel {
    private final DatabaseManager databaseManager;
    private final MutableLiveData<List<Destination>> destinationsLiveData;
    private final MutableLiveData<String> errorLiveData;
    private final String username;


    public DestinationViewModel(@NonNull Application application) {
        super(application);
        databaseManager = DatabaseManager.getInstance();
        destinationsLiveData = new MutableLiveData<>(new ArrayList<>());
        errorLiveData = new MutableLiveData<>();
        SharedPreferences sharedPreferences = application.getSharedPreferences("WanderSyncPrefs", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);
        DatabaseReference usersRef = databaseManager.getUsersReference();
        DatabaseReference destinationRef = databaseManager.getDestinationsReference();

        loadDestinations();
    }

    public LiveData<List<Destination>> getDestinations() {
        return destinationsLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void addDestination(Destination destination) {
        if (username != null) {
            databaseManager.addDestination(username, destination,
                    aVoid -> loadDestinations(),
                    e -> errorLiveData.setValue(e.getMessage())
            );
        } else {
            errorLiveData.setValue("Username is not available");
        }
    }

    private void loadDestinations() {
        databaseManager.loadDestinations(username, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Destination> destinationList = new ArrayList<>();
                for (DataSnapshot destinationSnapshot : snapshot.getChildren()) {
                    Destination destination = destinationSnapshot.getValue(Destination.class);
                    if (destination != null && destination.getUsername() != null && destination.getUsername().equals(username)) {
                        destinationList.add(destination);
                    }
                }
                destinationsLiveData.setValue(destinationList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorLiveData.setValue(error.getMessage());
            }
        });
    }

    public void saveVacationTime(long duration) {
        if (username != null) {
            databaseManager.saveVacationTime(username, duration,
                    aVoid -> { /* Success callback */ },
                    e -> errorLiveData.setValue("Failed to save vacation time: " + e.getMessage())
            );
        } else {
            errorLiveData.setValue("Username is not available");
        }
    }
}
