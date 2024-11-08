package com.example.wandersync.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.wandersync.R;
import com.example.wandersync.model.DiningReservation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReservationsAdapter extends BaseAdapter implements SectionIndexer {
    private final Context context;
    private List<DiningReservation> reservations;
    private List<String> sectionHeaders;
    private List<Integer> sectionPositions;

    public ReservationsAdapter(Context context) {
        this.context = context;
        this.reservations = new ArrayList<>();
        this.sectionHeaders = new ArrayList<>();
        this.sectionPositions = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return reservations.size();
    }

    @Override
    public Object getItem(int position) {
        return reservations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_reservation, parent, false);
        }

        DiningReservation reservation = reservations.get(position);

        TextView locationTextView = convertView.findViewById(R.id.text_view_location);
        TextView websiteTextView = convertView.findViewById(R.id.text_view_website);
        TextView timeTextView = convertView.findViewById(R.id.text_view_time);
        TextView reviewTextView = convertView.findViewById(R.id.text_view_review);
        TextView dateTextView = convertView.findViewById(R.id.text_view_date);

        locationTextView.setText(reservation.getLocation());
        websiteTextView.setText(reservation.getWebsite());
        timeTextView.setText(reservation.getFormattedReservationTime());
        reviewTextView.setText(reservation.getReview());

        String formattedDate = reservation.getFormattedReservationDate();

        if (position == 0 || !formattedDate.equals(reservations.get(position - 1).getFormattedReservationDate())) {
            dateTextView.setText(formattedDate);
            dateTextView.setVisibility(View.VISIBLE);
        } else {
            dateTextView.setVisibility(View.GONE);
        }

        if (isPastReservation(reservation.getReservationTime())) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.past_reservation));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.future_reservation));
        }

        return convertView;
    }

    public void updateReservations(List<DiningReservation> newReservations) {
        Collections.sort(newReservations, (r1, r2) -> Long.compare(r1.getReservationTime(), r2.getReservationTime()));

        reservations.clear();
        sectionHeaders.clear();
        sectionPositions.clear();

        String currentDate = "";
        for (DiningReservation reservation : newReservations) {
            String formattedDate = reservation.getFormattedReservationDate();

            if (!formattedDate.equals(currentDate)) {
                currentDate = formattedDate;
                sectionHeaders.add(currentDate);
                sectionPositions.add(reservations.size());
            }

            reservations.add(reservation);
        }

        notifyDataSetChanged();
    }

    @Override
    public Object[] getSections() {
        return sectionHeaders.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return sectionPositions.get(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < sectionPositions.size(); i++) {
            if (position < sectionPositions.get(i)) {
                return i - 1;
            }
        }
        return sectionPositions.size() - 1;
    }

    private boolean isPastReservation(long reservationTime) {
        return reservationTime < System.currentTimeMillis();
    }
}
