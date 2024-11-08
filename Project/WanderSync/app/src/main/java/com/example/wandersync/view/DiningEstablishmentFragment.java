package com.example.wandersync.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.wandersync.R;
import com.example.wandersync.model.DiningReservation;
import com.example.wandersync.viewmodel.DiningViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiningEstablishmentFragment extends Fragment {
    private DiningViewModel diningViewModel;
    private EditText locationEditText;
    private EditText websiteEditText;
    private EditText dateEditText;
    private EditText timeEditText;
    private EditText reviewEditText;
    private Button addReservationButton;
    private ListView reservationsListView;
    private ReservationsAdapter reservationsAdapter;

    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dining_establishment, container, false);

        diningViewModel = new ViewModelProvider(this).get(DiningViewModel.class);

        locationEditText = rootView.findViewById(R.id.edit_text_location);
        websiteEditText = rootView.findViewById(R.id.edit_text_website);
        dateEditText = rootView.findViewById(R.id.edit_text_date);
        timeEditText = rootView.findViewById(R.id.edit_text_time);
        reviewEditText = rootView.findViewById(R.id.edit_text_review);
        addReservationButton = rootView.findViewById(R.id.button_add_reservation);
        reservationsListView = rootView.findViewById(R.id.recycler_view_reservations);

        reservationsAdapter = new ReservationsAdapter(getContext());
        reservationsListView.setAdapter(reservationsAdapter);

        diningViewModel.getReservationsLiveData().observe(getViewLifecycleOwner(), new Observer<List<DiningReservation>>() {
            @Override
            public void onChanged(List<DiningReservation> reservations) {
                reservationsAdapter.updateReservations(reservations);
            }
        });

        addReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReservation();
            }
        });

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        return rootView;
    }

    private void addReservation() {
        String location = locationEditText.getText().toString();
        String website = websiteEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String time = timeEditText.getText().toString();
        String review = reviewEditText.getText().toString();

        if (TextUtils.isEmpty(location) || TextUtils.isEmpty(website) || TextUtils.isEmpty(date) || TextUtils.isEmpty(time) || TextUtils.isEmpty(review)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String dateTime = date + " " + time;
            Log.d("Reservation", "Date and Time string: " + dateTime);

            Date parsedDate = dateFormat.parse(dateTime);

            if (parsedDate == null) {
                Toast.makeText(getContext(), "Invalid date or time format", Toast.LENGTH_SHORT).show();
                return;
            }

            long reservationTime = parsedDate.getTime();
            Log.d("Reservation", "Parsed reservation time in milliseconds: " + reservationTime);

            DiningReservation reservation = new DiningReservation(location, website, reservationTime, review);

            if (checkForConflict(reservationTime, location)) {
                Toast.makeText(getContext(), "This reservation already exists (conflict detected)!", Toast.LENGTH_SHORT).show();
            } else {
                diningViewModel.addDiningReservation(reservation, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Reservation added successfully", Toast.LENGTH_SHORT).show();

                        // Reset input fields after successful addition
                        locationEditText.setText("");
                        websiteEditText.setText("");
                        dateEditText.setText("");
                        timeEditText.setText("");
                        reviewEditText.setText("");
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("Reservation", "Error adding reservation to database: ", e);
                        Toast.makeText(getContext(), "Failed to add reservation", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            Log.e("Reservation", "Error during reservation creation: ", e);
            Toast.makeText(getContext(), "Error adding reservation", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkForConflict(long reservationTime, String location) {
        List<DiningReservation> existingReservations = getExistingReservations(location);

        for (DiningReservation existingReservation : existingReservations) {
            if (existingReservation.getReservationTime() == reservationTime) {
                return true;
            }
        }
        return false;
    }

    private List<DiningReservation> getExistingReservations(String location) {
        List<DiningReservation> reservations = new ArrayList<>();
        reservations.add(new DiningReservation("Restaurant A", "www.example.com", 1678363200000L, "Great place"));
        reservations.add(new DiningReservation("Restaurant A", "www.example.com", 1678366800000L, "Nice ambiance"));

        return reservations;
    }





    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            selectedYear = year;
            selectedMonth = month;
            selectedDay = dayOfMonth;

            dateEditText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
        }, selectedYear, selectedMonth, selectedDay);

        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            selectedHour = hourOfDay;
            selectedMinute = minute;

            timeEditText.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
        }, selectedHour, selectedMinute, true);

        timePickerDialog.show();
    }
}
