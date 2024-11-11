package com.example.wandersync.viewmodel;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wandersync.R;
import com.example.wandersync.model.DiningReservation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ReservationsAdapter extends
        RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder> {

    private List<DiningReservation> reservations;
    private final Context context;
    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat timeFormat =
            new SimpleDateFormat("HH:mm", Locale.getDefault());

    public ReservationsAdapter(Context context, List<DiningReservation> reservations) {
        this.context = context;
        this.reservations = reservations != null ? reservations : new ArrayList<>();
    }

    public void setReservations(List<DiningReservation> reservations) {
        // No need to check for duplicates, just set and sort the reservations
        this.reservations = reservations != null ? reservations : new ArrayList<>();
        sortReservationsByDate(); // Sort the reservations by date and time
        notifyDataSetChanged(); // Update the RecyclerView
    }

    private void sortReservationsByDate() {
        Collections.sort(reservations,
                (r1, r2) -> Long.compare(r1.getReservationTime(), r2.getReservationTime()));
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.form_reservation, parent, false);
        return new ReservationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        DiningReservation reservation = reservations.get(position);
        holder.bind(reservation, position);
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public class ReservationViewHolder extends RecyclerView.ViewHolder {
        private final TextView locationTextView;
        private final TextView websiteTextView;
        private final TextView timeTextView;
        private final TextView reviewTextView;
        private final TextView dateTextView;
        private GradientDrawable defaultBackgroundDrawable;

        public ReservationViewHolder(View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.text_view_location);
            websiteTextView = itemView.findViewById(R.id.text_view_website);
            timeTextView = itemView.findViewById(R.id.text_view_time);
            reviewTextView = itemView.findViewById(R.id.text_view_review);
            dateTextView = itemView.findViewById(R.id.text_view_date);
            defaultBackgroundDrawable = (GradientDrawable) itemView.getBackground();
        }

        public void bind(DiningReservation reservation, int position) {
            locationTextView.setText(reservation.getLocation());
            websiteTextView.setText(reservation.getWebsite());
            timeTextView.setText(timeFormat.format(reservation.getReservationTime()));
            reviewTextView.setText(reservation.getReview());

            String formattedDate = dateFormat.format(reservation.getReservationTime());
            dateTextView.setVisibility(View.GONE);

            // Show date header if it's the first item or date differs from the previous item
            if (position == 0 || !formattedDate.equals(dateFormat.
                    format(reservations.get(position - 1).getReservationTime()))) {
                dateTextView.setText(formattedDate);
                dateTextView.setVisibility(View.VISIBLE);
            }

            // Get current time and compare the full date-time with the reservation
            long currentTime = System.currentTimeMillis();  // Current time (date + time)

            // Extract date and time from the reservation
            Calendar reservationCalendar = Calendar.getInstance();
            reservationCalendar.setTimeInMillis(reservation.getReservationTime());

            Calendar currentCalendar = Calendar.getInstance();

            // Compare the reservation's date and time with the current date and time
            if (reservationCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis()) {
                // Reservation has expired, mark it as expired (red background)
                setExpiredBackground();
            } else {
                // Reservation has not expired, revert to the original background
                resetBackground();
            }
        }

        // Method to set the expired background
        private void setExpiredBackground() {
            GradientDrawable backgroundDrawable = new GradientDrawable();
            backgroundDrawable.setColor(Color.RED); // Light red background for expired reservations
            backgroundDrawable.setCornerRadius(40);
            itemView.setBackground(backgroundDrawable); // Apply red background to the item view
        }

        // Method to reset the background to the original one
        private void resetBackground() {
            if (defaultBackgroundDrawable != null) {
                itemView.setBackground(defaultBackgroundDrawable);
            }
        }

    }
}
