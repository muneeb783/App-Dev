package com.example.wandersync.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wandersync.Model.TravelPost;

import java.util.ArrayList;
import java.util.List;

public class TravelCommunityViewModel extends ViewModel {
    // LiveData to hold the list of travel posts
    private final MutableLiveData<List<TravelPost>> travelPosts = new MutableLiveData<>(new ArrayList<>());

    // Public method to get travel posts as LiveData
    public LiveData<List<TravelPost>> getTravelPosts() {
        return travelPosts;
    }

    // Method to add a new travel post to the list
    public void addTravelPost(int id, String startDate, String endDate, String destination,
                              String accommodations, String bookingReservation, String notes) {
        List<TravelPost> currentPosts = travelPosts.getValue();
        if (currentPosts != null) {
            currentPosts.add(new TravelPost(id, startDate, endDate, destination, accommodations, bookingReservation, notes));
            travelPosts.setValue(currentPosts);
        }
    }

    // Method to reset the travel post list
    public void clearTravelPosts() {
        if (travelPosts.getValue() != null) {
            travelPosts.getValue().clear();
            travelPosts.setValue(travelPosts.getValue());
        }
    }
}
