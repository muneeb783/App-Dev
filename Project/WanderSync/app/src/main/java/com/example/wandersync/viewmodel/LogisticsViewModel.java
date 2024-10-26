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
    private final MutableLiveData<ArrayList<String>> invitedUsers = new MutableLiveData<>(new ArrayList<>());
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
    public LiveData<Integer> getPlannedDays() {
        return plannedDays;
    }
    public LiveData<Long> getAllottedTime() {
        return allottedTime;
    }
    public LiveData<ArrayList<String>> getInvitedUsers() {
        return invitedUsers;
    }

    public LiveData<HashMap<String, ArrayList<String>>> getUserNotesMap() {
        return userNotesMap;
    }

    public LiveData<String> getInviteStatus() {
        return inviteStatus;
    }

    public LiveData<String> getNoteStatus() {
        return noteStatus;
    }
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
    public void inviteUser(String username, String currentUsername) {
        if (username.isEmpty()) {
            inviteStatus.setValue("Please enter a username");
            return;
        }

        if (username.equals(currentUsername)) {
            inviteStatus.setValue("You cannot invite yourself.");
            return;
        }

        if (invitedUsers.getValue().contains(username)) {
            inviteStatus.setValue(username + " is already invited.");
            return;
        }

        String safeUsername = username;
        databaseReference.child(safeUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<String> updatedUsers = new ArrayList<>(invitedUsers.getValue());
                    updatedUsers.add(username);
                    invitedUsers.setValue(updatedUsers);

                    ArrayList<String> userNotes = new ArrayList<>();
                    for (DataSnapshot noteSnapshot : snapshot.child("notes").getChildren()) {
                        String note = noteSnapshot.getValue(String.class);
                        if (note != null) {
                            userNotes.add(note);
                        }
                    }

                    HashMap<String, ArrayList<String>> updatedNotesMap = new HashMap<>(userNotesMap.getValue());
                    updatedNotesMap.put(username, new ArrayList<>());
                    userNotesMap.setValue(updatedNotesMap);

                    inviteStatus.setValue(username + " invited!");
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

    public void addNoteForCurrentUser(String note) {
        if (note.isEmpty()) {
            noteStatus.setValue("Note cannot be empty");
            return;
        }

        ArrayList<String> notes = userNotesMap.getValue().getOrDefault("current_user", new ArrayList<>());
        notes.add(note);

        HashMap<String, ArrayList<String>> updatedNotesMap = new HashMap<>(userNotesMap.getValue());
        updatedNotesMap.put("current_user", notes);
        userNotesMap.setValue(updatedNotesMap);
        databaseReference.child(username).child("notes").setValue(notes)
                .addOnSuccessListener(aVoid -> noteStatus.setValue("Note added."))
                .addOnFailureListener(e -> noteStatus.setValue("Failed to add note: " + e.getMessage()));
        noteStatus.setValue("Note added.");
    }

}
