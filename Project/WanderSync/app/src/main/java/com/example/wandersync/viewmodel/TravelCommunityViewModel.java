package com.example.wandersync.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.wandersync.Model.TravelPost;
import com.example.wandersync.viewmodel.sorting.SortStrategy;
import com.example.wandersync.viewmodel.sorting.SortByCheckInDate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TravelCommunityViewModel extends AndroidViewModel {

    private final DatabaseManager databaseManager;
    private final MutableLiveData<List<TravelPost>> travelPostsLiveData =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private String username;  // Store the logged-in user's username
    private SortStrategy sortStrategy;

    public TravelCommunityViewModel(Application application) {
        super(application);
        databaseManager = DatabaseManager.getInstance();

        // Initialize SharedPreferences to retrieve the username
        SharedPreferences sharedPreferences =
                application.getSharedPreferences("WanderSyncPrefs", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", null); // Retrieve the username

        // Set default sorting strategy (e.g., sort by check-in date)
        sortStrategy = new SortByCheckInDate();

        // Check if a username exists, and load the travel posts
        if (username != null) {
            loadTravelPosts();
        } else {
            errorLiveData.setValue("No username found.");
        }
    }

    // Public method to get travel posts as LiveData
    public LiveData<List<TravelPost>> getTravelPosts() {
        return travelPostsLiveData;
    }

    // Public method to get any error messages
    public LiveData<String> getError() {
        return errorLiveData;
    }

    // Method to load travel posts from Firebase and update LiveData
    private void loadTravelPosts() {
        if (username == null) {
            errorLiveData.setValue("Username is not available.");
            return;
        }

        databaseManager.loadTravelPosts(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<TravelPost> loadedPosts = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    TravelPost travelPost = postSnapshot.getValue(TravelPost.class);
                    if (travelPost != null) {
                        loadedPosts.add(travelPost);
                    }
                }
                travelPostsLiveData.setValue(loadedPosts); // Update LiveData with the loaded posts
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                errorLiveData.setValue("Failed to load travel posts: " + databaseError.getMessage());
            }
        });
    }

    // MUNEEB LOOK AT THIS Method to add a new travel post to the list and Firebase
    public void addTravelPost(int id, String startDate, String endDate, String destination,
                              String accommodations, String bookingReservation, String notes) {
        if (username == null) {
            errorLiveData.setValue("User is not logged in.");
            return;
        }

        TravelPost newPost = new TravelPost(id, startDate, endDate, destination, accommodations, bookingReservation, notes);

        // Add the post to Firebase
        databaseManager.addTravelPost(newPost,
                aVoid -> loadTravelPosts(),
                e -> errorLiveData.setValue("Failed to add travel post: " + e.getMessage())
        );
    }

    // Method to set the logged-in user's username and store it in SharedPreferences
    public void setUsername(String username) {
        this.username = username;

        // Save the username to SharedPreferences
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("WanderSyncPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    // Method to clear the username from SharedPreferences (e.g., when logging out)
    public void clearUsername() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("WanderSyncPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.apply();
        username = null;
    }

    // Method to handle sorting of the posts
    public void setSortStrategy(SortStrategy strategy) {
        this.sortStrategy = strategy;
        sortAndSetTravelPosts();
    }

    // Method to apply sorting to travel posts
    private void sortAndSetTravelPosts() {
        List<TravelPost> travelPosts = travelPostsLiveData.getValue();
        if (travelPosts != null) {
            travelPostsLiveData.setValue(sortStrategy.sort(new ArrayList<>(travelPosts)));
        }
    }

    public SortStrategy getSortStrategy() {
        return sortStrategy;
    }
}
