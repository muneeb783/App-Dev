package com.example.wandersync.viewmodel;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wandersync.model.Accommodation;
import com.example.wandersync.R;

import java.util.ArrayList;
import java.util.List;

public class AccommodationAdapter extends
        RecyclerView.Adapter<AccommodationAdapter.AccommodationViewHolder> {

    private List<Accommodation> accommodations;

    public AccommodationAdapter(List<Accommodation> accommodations) {
        this.accommodations = accommodations != null ? accommodations : new ArrayList<>();
    }

    public void setAccommodations(List<Accommodation> accommodations) {
        this.accommodations = accommodations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AccommodationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.accommodation_item, parent, false);
        return new AccommodationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AccommodationViewHolder holder, int position) {
        Accommodation accommodation = accommodations.get(position);
        holder.getHotelNameText().setText(accommodation.getHotelName());
        holder.getLocationText().setText(accommodation.getLocation());
        holder.getCheckInOutText().setText(accommodation.getCheckInOut());
        holder.getNumRoomsText().setText("Number of Rooms: " + accommodation.getNumRooms());
        holder.getRoomTypeText().setText(accommodation.getRoomType());

        if (accommodation.isExpired()) {
            GradientDrawable backgroundDrawable = new GradientDrawable();
            backgroundDrawable.setColor(Color.RED); // Light gray for expired reservations
            backgroundDrawable.setCornerRadius(40);
            holder.itemView.setBackground(backgroundDrawable);
        }
    }

    @Override
    public int getItemCount() {
        return accommodations.size();
    }

    public static class AccommodationViewHolder extends RecyclerView.ViewHolder {
        private TextView hotelNameText;
        private TextView locationText;
        private TextView checkInOutText;
        private TextView numRoomsText;
        private TextView roomTypeText;

        public AccommodationViewHolder(View itemView) {
            super(itemView);
            hotelNameText = itemView.findViewById(R.id.hotelNameText);
            locationText = itemView.findViewById(R.id.locationText);
            checkInOutText = itemView.findViewById(R.id.checkInOutText);
            numRoomsText = itemView.findViewById(R.id.numRoomsText);
            roomTypeText = itemView.findViewById(R.id.roomTypeText);
        }

        // Getter and Setter for hotelNameText
        public TextView getHotelNameText() {
            return hotelNameText;
        }

        public void setHotelNameText(TextView hotelNameText) {
            this.hotelNameText = hotelNameText;
        }

        // Getter and Setter for locationText
        public TextView getLocationText() {
            return locationText;
        }

        public void setLocationText(TextView locationText) {
            this.locationText = locationText;
        }

        // Getter and Setter for checkInOutText
        public TextView getCheckInOutText() {
            return checkInOutText;
        }

        public void setCheckInOutText(TextView checkInOutText) {
            this.checkInOutText = checkInOutText;
        }

        // Getter and Setter for numRoomsText
        public TextView getNumRoomsText() {
            return numRoomsText;
        }

        public void setNumRoomsText(TextView numRoomsText) {
            this.numRoomsText = numRoomsText;
        }

        // Getter and Setter for roomTypeText
        public TextView getRoomTypeText() {
            return roomTypeText;
        }

        public void setRoomTypeText(TextView roomTypeText) {
            this.roomTypeText = roomTypeText;
        }
    }
}
