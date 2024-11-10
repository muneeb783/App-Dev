package com.example.wandersync.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
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
import com.example.wandersync.view.ReservationsAdapter;
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
    private EditText locationEditText, websiteEditText, dateEditText, timeEditText, reviewEditText;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        diningViewModel.getReservationsLiveData().observe(getViewLifecycleOwner(), reservations -> {
            if (reservations != null) {
                reservationsAdapter.setReservations(reservations);
            }
        });

        diningViewModel.getReservationAddedStatus().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(getContext(), "Reservation added successfully", Toast.LENGTH_SHORT).show();
                clearDialogFields();
                dialogLayout.setVisibility(View.GONE);
            } else {
                Toast.makeText(getContext(), "Failed to add reservation", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners(View view) {
        addDiningButton.setOnClickListener(v -> dialogLayout.setVisibility(View.VISIBLE));

        dateEditText.setOnClickListener(v -> showDatePickerDialog());
        timeEditText.setOnClickListener(v -> showTimePickerDialog());
        view.findViewById(R.id.button_add_reservation).setOnClickListener(v -> addReservation());
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    dateEditText.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    timeEditText.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime()));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void addReservation() {
        String location = locationEditText.getText().toString();
        String website = websiteEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String time = timeEditText.getText().toString();
        String review = reviewEditText.getText().toString();

        diningViewModel.addReservation(location, website, date, time, review);
    }

    private void clearDialogFields() {
        locationEditText.setText("");
        websiteEditText.setText("");
        dateEditText.setText("");
        timeEditText.setText("");
        reviewEditText.setText("");
    }
}
//
//import android.app.DatePickerDialog;
//import android.app.TimePickerDialog;
//import android.os.Bundle;
//import android.util.Patterns;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.wandersync.R;
//import com.example.wandersync.model.DiningReservation;
//import com.example.wandersync.viewmodel.DiningViewModel;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Calendar;
//import java.util.Locale;
//
//public class DiningEstablishmentFragment extends Fragment {
//
//    private DiningViewModel diningViewModel;
//    private RecyclerView reservationsRecyclerView;
//    private DiningReservationsAdapter reservationsAdapter;
//    private List<DiningReservation> reservationList;
//    private FloatingActionButton addDiningButton;
//    private LinearLayout dialogLayout;
//    private EditText locationEditText, websiteEditText, dateEditText, timeEditText, reviewEditText;
//    private Button addReservationButton;
//    private Calendar calendar;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_dining_establishment, container, false);
//
//        diningViewModel = new ViewModelProvider(this).get(DiningViewModel.class);
//        calendar = Calendar.getInstance();
//
//        reservationsRecyclerView = view.findViewById(R.id.reservationsRecyclerView);
//        addDiningButton = view.findViewById(R.id.addDiningButton);
//        dialogLayout = view.findViewById(R.id.dialog_add_dining);
//        locationEditText = view.findViewById(R.id.edit_text_location);
//        websiteEditText = view.findViewById(R.id.edit_text_website);
//        dateEditText = view.findViewById(R.id.edit_text_date);
//        timeEditText = view.findViewById(R.id.edit_text_time);
//        reviewEditText = view.findViewById(R.id.edit_text_review);
//        addReservationButton = view.findViewById(R.id.button_add_reservation);
//
//        reservationList = new ArrayList<>();
//        reservationsAdapter = new DiningReservationsAdapter(reservationList);
//        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        reservationsRecyclerView.setAdapter(reservationsAdapter);
//
//        diningViewModel.getReservationsLiveData().observe(getViewLifecycleOwner(), new Observer<List<DiningReservation>>() {
//            @Override
//            public void onChanged(List<DiningReservation> reservations) {
//                reservationsAdapter.updateReservations(reservations);
//            }
//        });
//
//        addDiningButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogLayout.setVisibility(View.VISIBLE);
//            }
//        });
//
//        addReservationButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addReservation();
//            }
//        });
//
//        timeEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showTimePickerDialog();
//            }
//        });
//
//        dateEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog();
//            }
//        });
//
//        return view;
//    }
//
//    private void showDatePickerDialog() {
//        DatePickerDialog datePickerDialog = new DatePickerDialog(
//                getContext(),
//                (view, year, month, dayOfMonth) -> {
//                    calendar.set(year, month, dayOfMonth);
//                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//                    dateEditText.setText(dateFormat.format(calendar.getTime()));
//                },
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//        );
//
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
//
//        datePickerDialog.show();
//    }
//
//
//    private void showTimePickerDialog() {
//        Calendar currentCalendar = Calendar.getInstance();
//        boolean isToday = calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
//                calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
//                calendar.get(Calendar.DAY_OF_MONTH) == currentCalendar.get(Calendar.DAY_OF_MONTH);
//
//        TimePickerDialog timePickerDialog = new TimePickerDialog(
//                getContext(),
//                (view, hourOfDay, minute) -> {
//                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                    calendar.set(Calendar.MINUTE, minute);
//                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
//                    timeEditText.setText(timeFormat.format(calendar.getTime()));
//                },
//                calendar.get(Calendar.HOUR_OF_DAY),
//                calendar.get(Calendar.MINUTE),
//                true
//        );
//
//        if (isToday) {
//            timePickerDialog.updateTime(
//                    currentCalendar.get(Calendar.HOUR_OF_DAY),
//                    currentCalendar.get(Calendar.MINUTE)
//            );
//        }
//
//        timePickerDialog.show();
//    }
//
//    private void addReservation() {
//        String location = locationEditText.getText().toString();
//        String website = websiteEditText.getText().toString();
//        String date = dateEditText.getText().toString();
//        String time = timeEditText.getText().toString();
//        String review = reviewEditText.getText().toString();
//
//        if (location.isEmpty() || website.isEmpty() || date.isEmpty() || time.isEmpty() || review.isEmpty()) {
//            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (!isValidWebsite(website)) {
//            Toast.makeText(getContext(), "Please enter a valid website URL", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
//            String dateTime = date + " " + time;
//            long reservationTime = dateFormat.parse(dateTime).getTime();
//
//            DiningReservation reservation = new DiningReservation(location, website, reservationTime, review);
//
//            diningViewModel.addDiningReservation(reservation, new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    Toast.makeText(getContext(), "Reservation added successfully", Toast.LENGTH_SHORT).show();
//
//                    locationEditText.setText("");
//                    websiteEditText.setText("");
//                    dateEditText.setText("");
//                    timeEditText.setText("");
//                    reviewEditText.setText("");
//                    dialogLayout.setVisibility(View.GONE);
//                }
//            }, new OnFailureListener() {
//                @Override
//                public void onFailure(Exception e) {
//                    Toast.makeText(getContext(), "Failed to add reservation", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } catch (Exception e) {
//            Toast.makeText(getContext(), "Invalid date or time format", Toast.LENGTH_SHORT).show();
//        }
//    }

//    private void showSortMenu() {
//        if (isSortedByDate && !isSortedByTime) {
//            isSortedByTime = true;
//        } else if (!isSortedByDate && isSortedByTime) {
//            isSortedByDate = true;
//        } else {
//            isSortedByDate = true;
//        }
//
//        sortReservations();
//    }

//    private void sortReservations() {
//        if (isSortedByDate && !isSortedByTime) {
//            Collections.sort(reservationList, new Comparator<DiningReservation>() {
//                @Override
//                public int compare(DiningReservation r1, DiningReservation r2) {
//                    return Long.compare(r1.getReservationTime(), r2.getReservationTime());
//                }
//            });
//        } else if (!isSortedByDate && isSortedByTime) {
//            Collections.sort(reservationList, new Comparator<DiningReservation>() {
//                @Override
//                public int compare(DiningReservation r1, DiningReservation r2) {
//                    return Long.compare(r1.getReservationTime(), r2.getReservationTime());
//                }
//            });
//        } else {
//            // Sorting by both Date and Time
//            Collections.sort(reservationList, new Comparator<DiningReservation>() {
//                @Override
//                public int compare(DiningReservation r1, DiningReservation r2) {
//                    int dateComparison = Long.compare(r1.getReservationTime(), r2.getReservationTime());
//                    return (dateComparison != 0) ? dateComparison : 0; // Sort by both if needed
//                }
//            });
//        }
//        reservationsAdapter.updateReservations(reservationList);
//    }
//
//    private boolean isValidWebsite(String website) {
//        return Patterns.WEB_URL.matcher(website).matches();
//    }
//
//    private class DiningReservationsAdapter extends RecyclerView.Adapter<DiningReservationsAdapter.DiningReservationViewHolder> {
//
//        private List<DiningReservation> reservations;
//
//        public DiningReservationsAdapter(List<DiningReservation> reservations) {
//            this.reservations = reservations;
//        }
//
//        @NonNull
//        @Override
//        public DiningReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.form_reservation, parent, false);
//            return new DiningReservationViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull DiningReservationViewHolder holder, int position) {
//            DiningReservation reservation = reservations.get(position);
//            holder.bind(reservation);
//        }
//
//        @Override
//        public int getItemCount() {
//            return reservations.size();
//        }
//
//        public void updateReservations(List<DiningReservation> newReservations) {
//            this.reservations = newReservations;
//            notifyDataSetChanged();
//        }
//
//        class DiningReservationViewHolder extends RecyclerView.ViewHolder {
//            private TextView locationText, websiteText, dateText, reviewText;
//
//            public DiningReservationViewHolder(@NonNull View itemView) {
//                super(itemView);
//                locationText = itemView.findViewById(R.id.locationText);
//                websiteText = itemView.findViewById(R.id.websiteText);
//                dateText = itemView.findViewById(R.id.dateText);
//                reviewText = itemView.findViewById(R.id.reviewText);
//            }
//
//            public void bind(DiningReservation reservation) {
//                locationText.setText(reservation.getLocation());
//                websiteText.setText(reservation.getWebsite());
//                dateText.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(reservation.getReservationTime()));
//                reviewText.setText(reservation.getReview());
//            }
//        }
//    }
//}