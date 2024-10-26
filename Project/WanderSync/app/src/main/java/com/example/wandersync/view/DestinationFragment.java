package com.example.wandersync.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wandersync.R;
import com.example.wandersync.Model.Destination;
import com.example.wandersync.viewmodel.DestinationAdapter;
import com.example.wandersync.viewmodel.DestinationViewModel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DestinationFragment extends Fragment {

    private RecyclerView recyclerView;
    private DestinationAdapter adapter;
    private List<Destination> destinationList;
    private DestinationViewModel viewModel;

    private LinearLayout formLayout, formLayout1, resultLayout;
    private EditText travelLocationEditText, estimatedStartEditText, estimatedEndEditText;
    private EditText duration, estimatedStartEditText1, estimatedEndEditText1;
    private Button logTravelButton, cancelButton, submitButton, cancelButton1, submitButton1, calculateButton;
    private TextView resultAmountTextView, resetResultButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_destinations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        destinationList = new ArrayList<>();
        adapter = new DestinationAdapter(destinationList);
        recyclerView.setAdapter(adapter);

        formLayout = view.findViewById(R.id.form_layout);
        formLayout1 = view.findViewById(R.id.form_layout1);
        resultLayout = view.findViewById(R.id.result_layout);
        travelLocationEditText = view.findViewById(R.id.travel_location);
        estimatedStartEditText = view.findViewById(R.id.estimated_start);
        estimatedEndEditText = view.findViewById(R.id.estimated_end);
        duration = view.findViewById(R.id.duration);
        estimatedStartEditText1 = view.findViewById(R.id.estimated_start1);
        estimatedEndEditText1 = view.findViewById(R.id.estimated_end1);
        logTravelButton = view.findViewById(R.id.log_travel_button);
        cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton1 = view.findViewById(R.id.cancel_button1);
        submitButton = view.findViewById(R.id.submit_button);
        submitButton1 = view.findViewById(R.id.submit_button1);
        calculateButton = view.findViewById(R.id.calc_button);
        resultAmountTextView = view.findViewById(R.id.result_amount);
        resetResultButton = view.findViewById(R.id.reset_result_button);

        formLayout.setVisibility(View.GONE);
        formLayout1.setVisibility(View.GONE);
        resultLayout.setVisibility(View.GONE);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(DestinationViewModel.class);

        // Observe changes to the destination list
        viewModel.getDestinations().observe(getViewLifecycleOwner(), destinations -> {
            destinationList.clear();
            destinationList.addAll(destinations);
            adapter.notifyDataSetChanged();
        });

        // Observe error messages
        viewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Show the form layout for adding destinations
        logTravelButton.setOnClickListener(v -> formLayout.setVisibility(View.VISIBLE));

        // Hide the form layout and reset inputs
        cancelButton.setOnClickListener(v -> resetDestinationForm());

        // Submit new destination to the list
        submitButton.setOnClickListener(v -> addDestination());

        // Show the form layout for calculating vacation time
        calculateButton.setOnClickListener(v -> {
            formLayout1.setVisibility(View.VISIBLE);
            resultLayout.setVisibility(View.GONE);
        });

        // Hide the vacation time form and reset inputs
        cancelButton1.setOnClickListener(v -> resetVacationForm());

        // Submit vacation time calculations
        submitButton1.setOnClickListener(v -> calculateVacationTime());

        // Reset result display
        resetResultButton.setOnClickListener(v -> resultAmountTextView.setText("0 days"));

        return view;
    }

    private void addDestination() {
        String location = travelLocationEditText.getText().toString();
        String start = estimatedStartEditText.getText().toString();
        String end = estimatedEndEditText.getText().toString();

        if (TextUtils.isEmpty(location)) {
            Toast.makeText(requireContext(), "Location must be a valid name", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);
            long daysPlanned = ChronoUnit.DAYS.between(startDate, endDate);

            if (daysPlanned <= 0) {
                Toast.makeText(requireContext(), "Start date must be before end date", Toast.LENGTH_SHORT).show();
                return;
            }

            Destination newDestination = new Destination(location, daysPlanned);
            viewModel.addDestination(newDestination);
            resetDestinationForm();

        } catch (Exception e) {
            Toast.makeText(requireContext(), "Please enter dates in YYYY-MM-DD format", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateVacationTime() {
        int blanks = 0;
        String durationStr = duration.getText().toString();
        if (TextUtils.isEmpty(durationStr)) {
            blanks++;
        }
        String start = estimatedStartEditText1.getText().toString();
        if (TextUtils.isEmpty(start)) {
            blanks++;
        }
        String end = estimatedEndEditText1.getText().toString();
        if (TextUtils.isEmpty(end)) {
            blanks++;
        }
        long durationLong;
        long daysPlanned;

        if (blanks >= 2) {
            Toast.makeText(requireContext(), "Must fill at least 2 fields", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (TextUtils.isEmpty(durationStr)) {
                try {
                    LocalDate startDate = LocalDate.parse(start);
                    LocalDate endDate = LocalDate.parse(end);
                    daysPlanned = ChronoUnit.DAYS.between(startDate, endDate);
                    resultAmountTextView.setText(daysPlanned + " days");
                    resultLayout.setVisibility(View.VISIBLE);
                    resetVacationForm();
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Please enter dates in YYYY-MM-DD format", Toast.LENGTH_SHORT).show();
                }
            } else if (TextUtils.isEmpty(end)) {
                try {
                    durationLong = Long.parseLong(durationStr);
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Duration must be a valid number", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    LocalDate startDate = LocalDate.parse(start);
                    LocalDate endDate = LocalDate.parse(startDate.plusDays(durationLong).toString());
                    resultAmountTextView.setText(durationLong + " days");
                    resultLayout.setVisibility(View.VISIBLE);
                    resetVacationForm();
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Please enter dates in YYYY-MM-DD format", Toast.LENGTH_SHORT).show();
                }
            } else if (TextUtils.isEmpty(start)) {
                try {
                    durationLong = Long.parseLong(durationStr);
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Duration must be a valid number", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    LocalDate endDate = LocalDate.parse(end);
                    LocalDate startDate = LocalDate.parse(endDate.minusDays(durationLong).toString());
                    System.out.println(startDate.toString());
                    resultAmountTextView.setText(durationLong + " days");
                    resultLayout.setVisibility(View.VISIBLE);
                    resetVacationForm();
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Please enter dates in YYYY-MM-DD format", Toast.LENGTH_SHORT).show();
                }
            } else {
                try {
                    durationLong = Long.parseLong(durationStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Duration must be a valid number", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    LocalDate startDate = LocalDate.parse(start);
                    LocalDate endDate = LocalDate.parse(end);
                    daysPlanned = ChronoUnit.DAYS.between(startDate, endDate);

                    if (daysPlanned != durationLong) {
                        Toast.makeText(requireContext(), "Duration does not match the days planned", Toast.LENGTH_SHORT).show();
                    } else {
                        resultAmountTextView.setText(durationLong + " days");
                        resultLayout.setVisibility(View.VISIBLE);
                    }
                    viewModel.saveVacationTime(durationLong);
                    resetVacationForm();
                } catch (Exception e) {
                    Toast.makeText(requireContext(), "Please enter dates in YYYY-MM-DD format", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void resetDestinationForm() {
        travelLocationEditText.setText("");
        estimatedStartEditText.setText("");
        estimatedEndEditText.setText("");
        formLayout.setVisibility(View.GONE);
    }

    private void resetVacationForm() {
        duration.setText("");
        estimatedStartEditText1.setText("");
        estimatedEndEditText1.setText("");
        formLayout1.setVisibility(View.GONE);
    }
}

