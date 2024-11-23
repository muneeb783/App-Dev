package com.example.wandersync.viewmodel;
//
//import android.app.Application;
//import android.content.Context;
//import android.content.SharedPreferences;
//
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//
//import com.example.wandersync.Model.TravelPost;
//import com.example.wandersync.viewmodel.sorting.SortStrategy;
//import com.example.wandersync.viewmodel.sorting.SortByCheckInDate;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TravelCommunityViewModel extends AndroidViewModel {
//
//    private final DatabaseManager databaseManager;
//    private final MutableLiveData<List<TravelPost>> travelPostsLiveData = new MutableLiveData<>(new ArrayList<>());
//    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
//    private String username;
//    private SortStrategy sortStrategy;
//
//    public TravelCommunityViewModel(Application application) {
//        super(application);
//        databaseManager = DatabaseManager.getInstance();
//
//        SharedPreferences sharedPreferences =
//                application.getSharedPreferences("WanderSyncPrefs", Context.MODE_PRIVATE);
//        username = sharedPreferences.getString("username", null);
//
//        sortStrategy = new SortByCheckInDate();
//
//        if (username != null) {
//            loadTravelPosts();
//        } else {
//            errorLiveData.setValue("No username found.");
//        }
//
//        List<TravelPost> defaultPostList = new ArrayList<>();
//        defaultPostList.add(new TravelPost(1, "01/01/2023", "01/07/2023", "Paris", "Hotel", "Booking.com", "Sightseeing tour"));
//        travelPostsLiveData.setValue(defaultPostList);
//    }
//
//    public LiveData<List<TravelPost>> getTravelPosts() {
//        return travelPostsLiveData;
//    }
//
//    public LiveData<String> getError() {
//        return errorLiveData;
//    }
//
//    private void loadTravelPosts() {
//        if (username == null) {
//            errorLiveData.setValue("Username is not available.");
//            return;
//        }
//
//        databaseManager.loadTravelPosts(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<TravelPost> loadedPosts = new ArrayList<>();
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    TravelPost travelPost = postSnapshot.getValue(TravelPost.class);
//                    if (travelPost != null) {
//                        loadedPosts.add(travelPost);
//                    }
//                }
//                travelPostsLiveData.setValue(loadedPosts); // Update LiveData with the loaded posts
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                errorLiveData.setValue("Failed to load travel posts: " + databaseError.getMessage());
//            }
//        });
//    }
//
//    // Method to add a new travel post to the list and Firebase
//    public void addTravelPost(int id, String startDate, String endDate, String destination,
//                              String accommodations, String bookingReservation, String notes) {
//        if (username == null) {
//            errorLiveData.setValue("User is not logged in.");
//            return;
//        }
//
//        TravelPost newPost = new TravelPost(id, startDate, endDate, destination, accommodations, bookingReservation, notes);
//
//        // Add the new post to Firebase
//        databaseManager.addTravelPost(newPost,
//                aVoid -> loadTravelPosts(), // Load travel posts after adding a new one
//                e -> errorLiveData.setValue("Failed to add travel post: " + e.getMessage())
//        );
//
//        // Add the new post to the LiveData list to update UI immediately
//        List<TravelPost> currentPosts = travelPostsLiveData.getValue();
//        if (currentPosts != null) {
//            currentPosts.add(newPost);
//            travelPostsLiveData.setValue(currentPosts); // Update LiveData
//        }
//    }
//
//    // Method to set the logged-in user's username and store it in SharedPreferences
//    public void setUsername(String username) {
//        this.username = username;
//
//        // Save the username to SharedPreferences
//        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("WanderSyncPrefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("username", username);
//        editor.apply();
//    }
//
//    // Method to clear the username from SharedPreferences (e.g., when logging out)
//    public void clearUsername() {
//        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("WanderSyncPrefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.remove("username");
//        editor.apply();
//        username = null;
//    }
//
//    // Method to handle sorting of the posts
//    public void setSortStrategy(SortStrategy strategy) {
//        this.sortStrategy = strategy;
//        sortAndSetTravelPosts();
//    }
//
//    // Method to apply sorting to travel posts
//    private void sortAndSetTravelPosts() {
//        List<TravelPost> travelPosts = travelPostsLiveData.getValue();
//        if (travelPosts != null) {
//            travelPostsLiveData.setValue(sortStrategy.sort(new ArrayList<>(travelPosts)));
//        }
//    }
//
//    public SortStrategy getSortStrategy() {
//        return sortStrategy;
//    }
//}

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wandersync.Model.TravelPost;

import java.util.ArrayList;
import java.util.List;

public class TravelCommunityViewModel extends ViewModel {

    private final MutableLiveData<List<TravelPost>> travelPostsLiveData = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<TravelPost>> getTravelPosts() {
        return travelPostsLiveData;
    }

    public void addTravelPost(int id, String startDate, String endDate, String destination,
                              String accommodations, String bookingReservation, String notes) {
        List<TravelPost> currentPosts = travelPostsLiveData.getValue();
        if (currentPosts != null) {
            // Create a new list to ensure LiveData triggers updates
            List<TravelPost> updatedPosts = new ArrayList<>(currentPosts);
            updatedPosts.add(new TravelPost(id, startDate, endDate, destination, accommodations, bookingReservation, notes));
            travelPostsLiveData.setValue(updatedPosts); // Notify observers
        }
    }

    public void initializeDefaultPost() {
        List<TravelPost> defaultPosts = new ArrayList<>();
        defaultPosts.add(new TravelPost(1, "01/01/2023", "01/07/2023", "Paris", "Hotel", "Booking.com", "Sightseeing tour"));
        travelPostsLiveData.setValue(defaultPosts);
    }
}
