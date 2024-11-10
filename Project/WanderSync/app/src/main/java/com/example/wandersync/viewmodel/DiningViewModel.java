package com.example.wandersync.viewmodel;

import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wandersync.model.DiningReservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiningViewModel extends ViewModel {

    private final MutableLiveData<List<DiningReservation>> reservationsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> reservationAddedStatus = new MutableLiveData<>();

    public LiveData<List<DiningReservation>> getReservationsLiveData() {
        return reservationsLiveData;
    }

    public LiveData<Boolean> getReservationAddedStatus() {
        return reservationAddedStatus;
    }

    public void addReservation(String location, String website, String date, String time, String review) {
        if (location.isEmpty() || website.isEmpty() || date.isEmpty() || time.isEmpty() || review.isEmpty()) {
            reservationAddedStatus.setValue(false);
            return;
        }

        if (!Patterns.WEB_URL.matcher(website).matches()) {
            reservationAddedStatus.setValue(false);
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            long reservationTime = dateFormat.parse(date + " " + time).getTime();

            DiningReservation reservation = new DiningReservation(location, website, reservationTime, review);
            List<DiningReservation> currentList = reservationsLiveData.getValue();
            currentList.add(reservation);
            reservationsLiveData.setValue(currentList);
            reservationAddedStatus.setValue(true);

        } catch (Exception e) {
            reservationAddedStatus.setValue(false);
        }
    }

    public boolean isExpired(@NonNull DiningReservation reservation) {
        long currentTime = System.currentTimeMillis();
        return reservation.getReservationTime() < currentTime;
    }
}