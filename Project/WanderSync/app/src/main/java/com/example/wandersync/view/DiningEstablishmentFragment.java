package com.example.wandersync.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wandersync.R;
import com.example.wandersync.viewmodel.DiningViewModel;
import com.example.wandersync.viewmodel.ReservationsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DiningEstablishmentFragment extends Fragment {

    private DiningViewModel diningViewModel;
    private RecyclerView reservationsRecyclerView;
    private ReservationsAdapter reservationsAdapter;
    private FloatingActionButton addDiningButton;
    private LinearLayout dialogLayout;
    private EditText locationEditText;
    private EditText websiteEditText;
    private EditText dateEditText;
    private EditText timeEditText;
    private EditText reviewEditText;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dining_establishment, container, false);

        diningViewModel = new ViewModelProvider(this).get(DiningViewModel.class);
        calendar = Calendar.getInstance();

        initUI(view);
        setupRecyclerView();
        setupObservers();
        setupClickListeners(view);

        return view;
    }

    private void initUI(View view) {
        reservationsRecyclerView = view.findViewById(R.id.reservationsRecyclerView);
        addDiningButton = view.findViewById(R.id.addDiningButton);
        dialogLayout = view.findViewById(R.id.dialog_add_dining);
        locationEditText = view.findViewById(R.id.edit_text_location);
        websiteEditText = view.findViewById(R.id.edit_text_website);
        dateEditText = view.findViewById(R.id.edit_text_date);
        timeEditText = view.findViewById(R.id.edit_text_time);
        reviewEditText = view.findViewById(R.id.edit_text_review);
    }

    private void setupRecyclerView() {
        reservationsAdapter = new ReservationsAdapter(requireContext(), new ArrayList<>());
        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reservationsRecyclerView.setAdapter(reservationsAdapter);
    }

    private void setupObservers() {
        // Observe reservation data changes
        diningViewModel.getReservations().observe(getViewLifecycleOwner(), reservations -> {
            if (reservations != null && !reservations.isEmpty()) {
                reservationsAdapter.setReservations(reservations);
                reservationsRecyclerView.setVisibility(View.VISIBLE);
            } else {
                reservationsRecyclerView.setVisibility(View.GONE);
                Toast.makeText(getContext(),
                        "No reservations available", Toast.LENGTH_SHORT).show();
            }
        });

        // Observe the status of the reservation being added
        diningViewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners(View view) {
        addDiningButton.setOnClickListener(v -> dialogLayout.setVisibility(View.VISIBLE));

        dateEditText.setOnClickListener(v -> showDatePickerDialog());
        timeEditText.setOnClickListener(v -> showTimePickerDialog());
        view.findViewById(R.id.button_add_reservation).setOnClickListener(v -> saveReservation());
        view.findViewById(R.id.button_cancel_reservation).
                setOnClickListener(v -> dialogLayout.setVisibility(View.GONE));
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    dateEditText.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            .format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (view, hourOfDay, minute) -> timeEditText.
                        setText(String.format(Locale.getDefault(), "%02d:%02d",
                        hourOfDay, minute)),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void saveReservation() {
        String location = locationEditText.getText().toString().trim();
        String website = websiteEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String time = timeEditText.getText().toString().trim();
        String review = reviewEditText.getText().toString().trim();

        if (TextUtils.isEmpty(location) || TextUtils.isEmpty(website) || TextUtils.isEmpty(date)
                || TextUtils.isEmpty(time) || TextUtils.isEmpty(review)) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(website.endsWith(".com") || website.endsWith(".org") || website.endsWith(".gov"))) {
            Toast.makeText(getContext(),
                    "Website must end with .com, .org, or .gov", Toast.LENGTH_SHORT).show();
            return;
        }

        diningViewModel.addReservation(location, website, date, time, review);
        clearDialogFields();
        dialogLayout.setVisibility(View.GONE);
    }

    private void clearDialogFields() {
        locationEditText.setText("");
        websiteEditText.setText("");
        dateEditText.setText("");
        timeEditText.setText("");
        reviewEditText.setText("");
    }
}
