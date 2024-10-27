package com.example.wandersync.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LogisticsViewModel extends AndroidViewModel {

    private final DatabaseReference databaseReference;
    private final MutableLiveData<ArrayList<String>> contributors = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<HashMap<String, ArrayList<String>>> userNotesMap = new MutableLiveData<>(new HashMap<>());
    private final MutableLiveData<String> inviteStatus = new MutableLiveData<>();
    private final MutableLiveData<String> noteStatus = new MutableLiveData<>();
    private final MutableLiveData<Long> allottedTime = new MutableLiveData<>();
    private final MutableLiveData<Integer> plannedDays = new MutableLiveData<>(0);

    private String username;

    public LogisticsViewModel(@NonNull Application application) {
        super(application);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        SharedPreferences sharedPreferences = application.getSharedPreferences("WanderSyncPrefs", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);
        loadAllottedTime();
        calculatePlannedDays(username);
    }

    // Getter methods for LiveData
    public LiveData<Integer> getPlannedDays() { return plannedDays; }
    public LiveData<Long> getAllottedTime() { return allottedTime; }
    public LiveData<ArrayList<String>> getContributors() { return contributors; }
    public LiveData<HashMap<String, ArrayList<String>>> getUserNotesMap() { return userNotesMap; }
    public LiveData<String> getInviteStatus() { return inviteStatus; }
    public LiveData<String> getNoteStatus() { return noteStatus; }
    public String getUsername() { return username; }

    // Methods for fetching data from Firebase
    public void calculatePlannedDays(String currentUsername) {
        DatabaseReference destinationRef = FirebaseDatabase.getInstance().getReference("destinations");
        destinationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalPlannedDays = 0;
                for (DataSnapshot destination : snapshot.getChildren()) {
                    String username = destination.child("username").getValue(String.class);
                    if (currentUsername.equals(username)) {
                        Integer duration = destination.child("daysPlanned").getValue(Integer.class);
                        if (duration != null) {
                            totalPlannedDays += duration;
                        }
                    }
                }
                plannedDays.setValue(totalPlannedDays);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any database errors here
            }
        });
    }

    private void loadAllottedTime() {
        databaseReference.child(username).child("allotedTime").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long time = snapshot.getValue(Long.class);
                allottedTime.setValue(time != null ? time : 0L);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                allottedTime.setValue(0L); // Set a default if loading fails
            }
        });
    }

    // Modified inviteUser to handle contributor addition and note fetching
    public void inviteUser(String username, String currentUsername) {
        if (username.isEmpty()) {
            inviteStatus.setValue("Please enter a username");
            return;
        }

        if (username.equals(currentUsername)) {
            inviteStatus.setValue("You cannot invite yourself.");
            return;
        }

        if (contributors.getValue().contains(username)) {
            inviteStatus.setValue(username + " is already invited.");
            return;
        }

        String safeUsername = username;
        databaseReference.child(safeUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<String> updatedUsers = new ArrayList<>(contributors.getValue());
                    updatedUsers.add(username);
                    contributors.setValue(updatedUsers);

                    DatabaseReference currentUserContributorsRef = databaseReference.child(currentUsername).child("contributors");
                    currentUserContributorsRef.setValue(updatedUsers)
                            .addOnSuccessListener(aVoid -> inviteStatus.setValue(username + " added as a contributor."))
                            .addOnFailureListener(e -> inviteStatus.setValue("Failed to add contributor: " + e.getMessage()));

                    fetchContributorNotes(username); // Fetch notes for the invited contributor
                } else {
                    inviteStatus.setValue("User " + username + " does not exist.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                inviteStatus.setValue("Database error: " + error.getMessage());
            }
        });
    }

    // Method to fetch notes for a single contributor
    private void fetchContributorNotes(String contributorUsername) {
        databaseReference.child(contributorUsername).child("notes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot notesSnapshot) {
                ArrayList<String> contributorNotes = new ArrayList<>();
                for (DataSnapshot noteSnapshot : notesSnapshot.getChildren()) {
                    String note = noteSnapshot.getValue(String.class);
                    if (note != null) {
                        contributorNotes.add(note);
                    }
                }

                HashMap<String, ArrayList<String>> updatedNotesMap = new HashMap<>(userNotesMap.getValue());
                updatedNotesMap.put(contributorUsername, contributorNotes);
                userNotesMap.setValue(updatedNotesMap); // Update notes map with contributor's notes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                noteStatus.setValue("Failed to load notes for contributor " + contributorUsername + ": " + error.getMessage());
            }
        });
    }

    public void fetchContributors(String currentUsername) {
        databaseReference.child(currentUsername).child("contributors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> loadedContributors = new ArrayList<>();
                HashMap<String, ArrayList<String>> allNotesMap = new HashMap<>();

                for (DataSnapshot contributorSnapshot : snapshot.getChildren()) {
                    String contributor = contributorSnapshot.getValue(String.class);
                    if (contributor != null) {
                        loadedContributors.add(contributor);
                        fetchContributorNotes(contributor); // Fetch each contributor's notes
                    }
                }
                contributors.setValue(loadedContributors);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                inviteStatus.setValue("Failed to load contributors: " + error.getMessage());
            }
        });
    }

    public void fetchCurrentUserNotes() {
        if (username == null) {
            noteStatus.setValue("Username not available.");
            return;
        }

        databaseReference.child(username).child("notes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> currentUserNotes = new ArrayList<>();
                for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                    String note = noteSnapshot.getValue(String.class);
                    if (note != null) {
                        currentUserNotes.add(note);
                    }
                }
                HashMap<String, ArrayList<String>> updatedNotesMap = new HashMap<>(userNotesMap.getValue());
                updatedNotesMap.put(username, currentUserNotes);
                userNotesMap.setValue(updatedNotesMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                noteStatus.setValue("Failed to load notes: " + error.getMessage());
            }
        });
    }
    public void addNoteForCurrentUser(String note) {
        if (note.isEmpty()) {
            noteStatus.setValue("Note cannot be empty");
            return;
        }
        ArrayList<String> notes = userNotesMap.getValue().getOrDefault(username, new ArrayList<>());
        notes.add(note);

        databaseReference.child(username).child("notes").setValue(notes)
                .addOnSuccessListener(aVoid -> {
                    HashMap<String, ArrayList<String>> updatedNotesMap = new HashMap<>(userNotesMap.getValue());
                    updatedNotesMap.put(username, notes);
                    userNotesMap.setValue(updatedNotesMap);
                    noteStatus.setValue("Note added.");
                })
                .addOnFailureListener(e -> noteStatus.setValue("Failed to add note: " + e.getMessage()));
    }
}
