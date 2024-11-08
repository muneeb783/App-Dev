package com.example.wandersync.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wandersync.model.DiningReservation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DiningViewModel extends ViewModel {
    private static DiningViewModel instance;
    private final DatabaseReference diningReservationsReference;
    private final MutableLiveData<List<DiningReservation>> reservationsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>(); // To handle error messages

    private DiningViewModel() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        diningReservationsReference = database.getReference("diningReservations");

        loadDiningReservations();
    }

    public static synchronized DiningViewModel getInstance() {
        if (instance == null) {
            instance = new DiningViewModel();
        }
        return instance;
    }

    public LiveData<List<DiningReservation>> getReservationsLiveData() {
        return reservationsLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void addDiningReservation(DiningReservation reservation,
                                     OnSuccessListener<Void> onSuccessListener,
                                     OnFailureListener onFailureListener) {
        String reservationId = diningReservationsReference.push().getKey();
        if (reservationId != null) {
            diningReservationsReference.child(reservationId).setValue(reservation)
                    .addOnSuccessListener(onSuccessListener)
                    .addOnFailureListener(onFailureListener);
        } else {
            onFailureListener.onFailure(new Exception("Error generating reservation ID"));
        }
    }

    private void loadDiningReservations() {
        diningReservationsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<DiningReservation> reservations = new ArrayList<>();
                for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                    DiningReservation reservation = reservationSnapshot.getValue(DiningReservation.class);
                    if (reservation != null) {
                        reservations.add(reservation);
                    }
                }
                sortReservationsByDateTime(reservations);
                reservationsLiveData.setValue(reservations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorLiveData.setValue(error.getMessage());
            }
        });
    }

    private void sortReservationsByDateTime(List<DiningReservation> reservations) {
        Collections.sort(reservations, new Comparator<DiningReservation>() {
            @Override
            public int compare(DiningReservation r1, DiningReservation r2) {
                return Long.compare(r1.getReservationTime(), r2.getReservationTime());
            }
        });
    }

    public void updateDiningReservation(String reservationId, DiningReservation updatedReservation,
                                        OnSuccessListener<Void> onSuccessListener,
                                        OnFailureListener onFailureListener) {
        diningReservationsReference.child(reservationId).setValue(updatedReservation)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void deleteDiningReservation(String reservationId,
                                        OnSuccessListener<Void> onSuccessListener,
                                        OnFailureListener onFailureListener) {
        diningReservationsReference.child(reservationId).removeValue()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public boolean isPastReservation(long reservationTime) {
        return reservationTime < System.currentTimeMillis();
    }
}
