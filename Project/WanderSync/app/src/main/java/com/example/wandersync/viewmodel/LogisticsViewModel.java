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

    private final DatabaseManager databaseManager;
    private final MutableLiveData<ArrayList<String>> contributors =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<HashMap<String, ArrayList<String>>> userNotesMap =
            new MutableLiveData<>(new HashMap<>());
    private final MutableLiveData<String> inviteStatus = new MutableLiveData<>();
    private final MutableLiveData<String> noteStatus = new MutableLiveData<>();
    private final MutableLiveData<Long> allottedTime = new MutableLiveData<>();
    private final MutableLiveData<Integer> plannedDays = new MutableLiveData<>(0);

    private String username;

    public LogisticsViewModel(@NonNull Application application) {
        super(application);
        databaseManager = DatabaseManager.getInstance();
        SharedPreferences sharedPreferences =
                application.getSharedPreferences("WanderSyncPrefs", Context.MODE_PRIVATE);

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

    public LiveData<ArrayList<String>> getContributors() {
        return contributors;
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

    public String getUsername() {
        return username;
    }

    public void calculatePlannedDays(String currentUsername) {
        checkCollaboratorStatusAndFetch(currentUsername, this::fetchPlannedDaysForUser);
    }

    private void fetchPlannedDaysForUser(String userId) {
        databaseManager.getDestinationsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalPlannedDays = 0;
                for (DataSnapshot destination : snapshot.getChildren()) {
                    String username = destination.child("username").getValue(String.class);
                    if (userId.equals(username)) {
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
                plannedDays.setValue(0); // Set a default if loading fails
            }
        });
    }

    private void loadAllottedTime() {
        checkCollaboratorStatusAndFetch(username, this::fetchAllottedTimeForUser);
    }

    private void fetchAllottedTimeForUser(String userId) {
        databaseManager.getUsersReference().child(userId).child("allotedTime")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Long time = snapshot.getValue(Long.class);
                        allottedTime.setValue(time != null ? time : 0L);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        allottedTime.setValue(0L);
                    }
                });
    }

    private void checkCollaboratorStatusAndFetch(String currentUsername, java.util.function.Consumer<String> fetchDataCallback) {
        databaseManager.getUsersReference().child(currentUsername).child("isCollaborator")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean isCollaborator = snapshot.getValue(Boolean.class);
                        if (isCollaborator != null && isCollaborator) {
                            databaseManager.getUsersReference().child(currentUsername).child("mainUserId")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String mainUserId = snapshot.getValue(String.class);
                                            if (mainUserId != null) {
                                                fetchDataCallback.accept(mainUserId);
                                            } else {
                                                fetchDataCallback.accept(currentUsername);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            fetchDataCallback.accept(currentUsername);
                                        }
                                    });
                        } else {
                            fetchDataCallback.accept(currentUsername);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        fetchDataCallback.accept(currentUsername);
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

        if (contributors.getValue() != null && contributors.getValue().contains(username)) {
            inviteStatus.setValue(username + " is already invited.");
            return;
        }

        databaseManager.getUsersReference().child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Boolean isCollaborator = snapshot.child("isCollaborator").getValue(Boolean.class);
                            if (Boolean.TRUE.equals(isCollaborator)) {
                                inviteStatus.setValue("User " + username + " is already a collaborator and cannot be added again.");
                            } else {
                                databaseManager.addCollaborator(username, currentUsername,
                                        aVoid -> {
                                            ArrayList<String> updatedContributors = new ArrayList<>(contributors.getValue());
                                            updatedContributors.add(username);
                                            contributors.setValue(updatedContributors);

                                            inviteStatus.setValue(username + " added as a collaborator.");
                                            fetchContributorNotes(username);
                                        },
                                        e -> inviteStatus.setValue("Failed to add collaborator: " + e.getMessage())
                                );
                            }
                        } else {
                            inviteStatus.setValue("User " + username + " does not exist.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        inviteStatus.setValue("Error checking user: " + error.getMessage());
                    }
                });
    }


    public void fetchContributors(String currentUsername) {
        checkCollaboratorStatusAndFetchContributors(currentUsername);
    }

    private void checkCollaboratorStatusAndFetchContributors(String currentUsername) {
        databaseManager.getUsersReference().child(currentUsername).child("isCollaborator")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean isCollaborator = snapshot.getValue(Boolean.class);
                        if (Boolean.TRUE.equals(isCollaborator)) {
                            // User is a collaborator, so we fetch contributors and notes using mainUserId
                            databaseManager.getUsersReference().child(currentUsername).child("mainUserId")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot mainUserSnapshot) {
                                            String mainUserId = mainUserSnapshot.getValue(String.class);
                                            if (mainUserId != null) {
                                                // Treat mainUserId as a contributor by adding them first
                                                ArrayList<String> loadedContributors = new ArrayList<>();
                                                loadedContributors.add(mainUserId); // Add mainUserId to the contributors list
                                                contributors.setValue(loadedContributors); // Update with mainUserId as the first "contributor"

                                                // Fetch notes for mainUserId
                                                fetchContributorNotes(mainUserId);

                                                // Fetch other contributors
                                                fetchAdditionalContributors(mainUserId, currentUsername, loadedContributors);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            inviteStatus.setValue("Failed to fetch main user ID: " + error.getMessage());
                                        }
                                    });
                        } else {
                            // If not a collaborator, fetch contributors normally without mainUserId
                            fetchAdditionalContributors(currentUsername, null, new ArrayList<>());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        inviteStatus.setValue("Failed to check collaborator status: " + error.getMessage());
                    }
                });
    }

    private void fetchAdditionalContributors(String userId, String currentUsername, ArrayList<String> loadedContributors) {
        databaseManager.getUsersReference().child(userId).child("contributors")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot contributorSnapshot : snapshot.getChildren()) {
                            String contributor = contributorSnapshot.getValue(String.class);
                            if (contributor != null && !contributor.equals(currentUsername) && !loadedContributors.contains(contributor)) {
                                loadedContributors.add(contributor);
                                fetchContributorNotes(contributor);
                            }
                        }
                        contributors.setValue(loadedContributors); // Update contributors with all contributors, including mainUserId
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        inviteStatus.setValue("Failed to load contributors: " + error.getMessage());
                    }
                });
    }

    private void fetchContributorNotes(String contributorUsername) {
        databaseManager.getUsersReference().child(contributorUsername).child("notes")
                .addListenerForSingleValueEvent(new ValueEventListener() {
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
                        userNotesMap.setValue(updatedNotesMap); // Update LiveData with contributor notes
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        noteStatus.setValue("Failed to load notes for contributor " + contributorUsername + ": " + error.getMessage());
                    }
                });
    }


    public void fetchCurrentUserNotes() {
        if (username == null) {
            noteStatus.setValue("Username not available.");
            return;
        }

        databaseManager.getUsersReference().child(username).child("notes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> currentUserNotes = new ArrayList<>();
                        for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                            String note = noteSnapshot.getValue(String.class);
                            if (note != null) {
                                currentUserNotes.add(note);
                            }
                        }

                        // Update the user notes map for the main user
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

        databaseManager.getUsersReference().child(username).child("notes").setValue(notes)
                .addOnSuccessListener(aVoid -> {

                    HashMap<String, ArrayList<String>> updatedNotesMap =
                            new HashMap<>(userNotesMap.getValue());

                    updatedNotesMap.put(username, notes);
                    userNotesMap.setValue(updatedNotesMap);
                    noteStatus.setValue("Note added.");
                })
                .addOnFailureListener(e -> noteStatus.setValue("Failed to add note: " + e.getMessage()));
    }
}
