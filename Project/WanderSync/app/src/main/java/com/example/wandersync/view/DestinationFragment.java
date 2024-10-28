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
import android.app.DatePickerDialog;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DestinationFragment extends Fragment {

    private RecyclerView recyclerView;
    private DestinationAdapter adapter;
    private List<Destination> destinationList;
    private DestinationViewModel viewModel;

    private LinearLayout formLayout;
    private LinearLayout formLayout1;
    private LinearLayout resultLayout;

    private EditText travelLocationEditText;
    private EditText estimatedStartEditText;
    private EditText estimatedEndEditText;
    private EditText duration;
    private EditText estimatedStartEditText1;
    private EditText estimatedEndEditText1;

    private Button logTravelButton;
    private Button cancelButton;
    private Button submitButton;
    private Button cancelButton1;
    private Button submitButton1;
    private Button calculateButton;

    private TextView resultAmountTextView;
    private TextView resetResultButton;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate startDate1;
    private LocalDate endDate1;
    private String lastErrorMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_destinations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        destinationList = new ArrayList<>();
        LocalDate now = LocalDate.now();
        destinationList.add(new Destination("Destination", 0, now, now));
        destinationList.add(new Destination("Destination", 0, now, now));

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

        viewModel = new ViewModelProvider(this).get(DestinationViewModel.class);

        viewModel.getDestinations().observe(getViewLifecycleOwner(), destinations -> {
            if (destinations.isEmpty()) {
                destinationList.clear();
                destinationList.add(new Destination("Paris", 0, now, now));
                destinationList.add(new Destination("London", 0, now, now));
            } else {
                destinationList.clear();
                destinationList.addAll(destinations);
            }
            adapter.notifyDataSetChanged();
        });

        viewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        logTravelButton.setOnClickListener(v -> formLayout.setVisibility(View.VISIBLE));
        cancelButton.setOnClickListener(v -> resetDestinationForm());
        submitButton.setOnClickListener(v -> addDestination());
        calculateButton.setOnClickListener(v -> {
            formLayout1.setVisibility(View.VISIBLE);
            resultLayout.setVisibility(View.GONE);
        });
        cancelButton1.setOnClickListener(v -> resetVacationForm());
        submitButton1.setOnClickListener(v -> calculateVacationTime());
        resetResultButton.setOnClickListener(v -> resultAmountTextView.setText("0 days"));

        estimatedStartEditText.setOnClickListener(v -> showDatePickerDialog(true));
        estimatedEndEditText.setOnClickListener(v -> showDatePickerDialog(false));
        estimatedStartEditText1.setOnClickListener(v -> showDatePickerDialog1(true));
        estimatedEndEditText1.setOnClickListener(v -> showDatePickerDialog1(false));

        return view;
    }

    public void addDestination() {
        String location = travelLocationEditText.getText().toString();
        LocalDate start = startDate;
        LocalDate end = endDate;

        if (start == null && end == null && TextUtils.isEmpty(location)) {
            Toast.makeText(requireContext(), "Please enter all the fields.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (start == null) {
            Toast.makeText(requireContext(), "Please enter a start date.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (end == null) {
            Toast.makeText(requireContext(), "Please enter an end date.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(location)) {
            lastErrorMessage = "Location must be a valid name";
            Toast.makeText(requireContext(), "Location must be a valid name",
                            Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (end.isBefore(start)) {
            Toast.makeText(requireContext(),
                    "End date cannot be before start date.", Toast.LENGTH_SHORT).show();
            return;
        }

        long daysPlanned = ChronoUnit.DAYS.between(startDate, endDate);
        Destination newDestination = new Destination(location, daysPlanned, start, end);
        viewModel.addDestination(newDestination);

        resetDestinationForm();
    }

    private void showDatePickerDialog(boolean isStartDate) {
        LocalDate initialDate = isStartDate ? (startDate != null ? startDate : LocalDate.now())
                : (endDate != null ? endDate : LocalDate.now());

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    LocalDate selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                    if (isStartDate) {
                        startDate = selectedDate;
                        estimatedStartEditText.setText(
                                selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
                    } else {
                        endDate = selectedDate;
                        estimatedEndEditText.setText(
                                selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
                    }
                }, initialDate.getYear(), initialDate.getMonthValue() - 1,
                initialDate.getDayOfMonth());

        if (isStartDate) {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        } else if (startDate != null) {
            datePickerDialog.getDatePicker().setMinDate(
                    startDate.atStartOfDay(ZoneId.systemDefault())
                            .toInstant().toEpochMilli());
        } else {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        }
        datePickerDialog.show();
    }

    private void showDatePickerDialog1(boolean isStartDate) {
        LocalDate initialDate = isStartDate ? (startDate1 != null ? startDate1 : LocalDate.now())
                : (endDate1 != null ? endDate1 : LocalDate.now());

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    LocalDate selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
                    if (isStartDate) {
                        startDate1 = selectedDate;
                        estimatedStartEditText1.setText(
                                selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
                    } else {
                        endDate1 = selectedDate;
                        estimatedEndEditText1.setText(
                                selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
                    }
                }, initialDate.getYear(), initialDate.getMonthValue() - 1,
                initialDate.getDayOfMonth());

        if (isStartDate) {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        } else if (startDate1 != null) {
            datePickerDialog.getDatePicker().setMinDate(
                    startDate1.atStartOfDay(ZoneId.systemDefault())
                            .toInstant().toEpochMilli());
        } else {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        }
        datePickerDialog.show();
    }

    private void calculateVacationTime() {
        int blanks = 0;
        String durationStr = duration.getText().toString();
        if (TextUtils.isEmpty(durationStr)) {
            blanks++;
        }
        LocalDate start = startDate1;
        if (start == null) {
            blanks++;
        }
        LocalDate end = endDate1;
        if (end == null) {
            blanks++;
        }

        long durationLong;
        long daysPlanned;

        if (blanks >= 2) {
            Toast.makeText(requireContext(),
                    "Must fill at least 2 fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (TextUtils.isEmpty(durationStr)) {
                daysPlanned = calculateDaysPlanned(start, end);
                resultAmountTextView.setText(daysPlanned + " days");
                resultLayout.setVisibility(View.VISIBLE);
                viewModel.saveVacationTime(daysPlanned);
                resetVacationForm();
            } else if (end == null) {
                durationLong = Long.parseLong(durationStr);
                LocalDate startDate = startDate1;
                LocalDate endDate = datePlusDays(startDate, durationLong);
                resultAmountTextView.setText(durationLong + " days");
                resultLayout.setVisibility(View.VISIBLE);
                viewModel.saveVacationTime(durationLong);
                resetVacationForm();
            } else if (start == null) {
                durationLong = Long.parseLong(durationStr);
                LocalDate endDate = endDate1;
                LocalDate startDate = dateMinusDays(endDate, durationLong);
                validateStartDate(startDate);
                resultAmountTextView.setText(durationLong + " days");
                resultLayout.setVisibility(View.VISIBLE);
                viewModel.saveVacationTime(durationLong);
                resetVacationForm();
            } else {
                durationLong = Long.parseLong(durationStr);
                daysPlanned = ChronoUnit.DAYS.between(start, end);
                validateDuration(daysPlanned, durationLong);
                resultAmountTextView.setText(durationLong + " days");
                resultLayout.setVisibility(View.VISIBLE);
                viewModel.saveVacationTime(durationLong);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(),
                    "Duration must be a valid number", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(requireContext(),
                    "An error occurred while calculating dates.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private long calculateDaysPlanned(LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            Toast.makeText(requireContext(),
                    "End date cannot be before start date.",
                    Toast.LENGTH_SHORT).show();
            return 0;
        }
        return ChronoUnit.DAYS.between(start, end);
    }

    private void validateStartDate(LocalDate startDate) {
        if (startDate.isBefore(LocalDate.now())) {
            Toast.makeText(requireContext(),
                    "Start date cannot be in the past.",
                    Toast.LENGTH_SHORT).show();
            throw new IllegalArgumentException("Invalid start date");
        }
    }

    private void validateDuration(long daysPlanned, long durationLong) {
        if (daysPlanned != durationLong) {
            Toast.makeText(requireContext(),
                    "Duration does not match the days planned",
                    Toast.LENGTH_SHORT).show();
            throw new IllegalArgumentException("Invalid duration");
        }
    }

    private void resetDestinationForm() {
        travelLocationEditText.setText("");
        estimatedStartEditText.setText("");
        estimatedEndEditText.setText("");
        startDate = null;
        endDate = null;
        formLayout.setVisibility(View.GONE);
    }

    private void resetVacationForm() {
        duration.setText("");
        estimatedStartEditText1.setText("");
        estimatedEndEditText1.setText("");
        startDate1 = null;
        endDate1 = null;
        formLayout1.setVisibility(View.GONE);
    }

    public boolean isValidLocation(String location) {
        return location != null && !location.trim().isEmpty();
    }

    public boolean isValidDuration(String durationStr) {
        if (durationStr == null || durationStr.trim().isEmpty()) {
            return false;
        }
        try {
            int duration = Integer.parseInt(durationStr.trim());
            return duration > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int countWords(String str) {
        if (str == null || str.trim().isEmpty()) {
            return 0;
        }
        String[] words = str.trim().split("\\s+");
        return words.length;
    }

    public long calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public LocalDate datePlusDays(LocalDate firstDay, long timePeriod) {
        return firstDay.plusDays(timePeriod);
    }

    public LocalDate dateMinusDays(LocalDate firstDay, long timePeriod) {
        return firstDay.minusDays(timePeriod);
    }

    public boolean isEndDateAfterStartDate(LocalDate startDate, LocalDate endDate) {
        return endDate != null && startDate != null && endDate.isAfter(startDate);
    }

    public void setLastErrorMessage(String message) {
        lastErrorMessage = message;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }
}
