package com.example.wandersync.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wandersync.Model.Accommodation;

import java.util.ArrayList;
import java.util.List;

public class AccommodationViewModel extends ViewModel {

    private final MutableLiveData<List<Accommodation>> accommodations;

    public AccommodationViewModel() {
        accommodations = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<Accommodation>> getAccommodations() {
        return accommodations;
    }

    public void addAccommodation(Accommodation accommodation) {
        List<Accommodation> currentList = accommodations.getValue();
        if (currentList != null) {
            currentList.add(accommodation);
            accommodations.setValue(currentList);
        }
    }
}
