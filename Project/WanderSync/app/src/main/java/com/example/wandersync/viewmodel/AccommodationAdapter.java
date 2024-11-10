package com.example.wandersync.viewmodel;

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

public class AccommodationAdapter extends RecyclerView.Adapter<AccommodationAdapter.AccommodationViewHolder> {

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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.accommodation_item, parent, false);
        return new AccommodationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AccommodationViewHolder holder, int position) {
        Accommodation accommodation = accommodations.get(position);
        holder.hotelNameText.setText(accommodation.getHotelName());
        holder.locationText.setText(accommodation.getLocation());
        holder.checkInOutText.setText(accommodation.getCheckInOut());
        holder.numRoomsText.setText("Number of Rooms: " + accommodation.getNumRooms());
        holder.roomTypeText.setText(accommodation.getRoomType());
    }

    @Override
    public int getItemCount() {
        return accommodations.size();
    }

    public static class AccommodationViewHolder extends RecyclerView.ViewHolder {
        TextView hotelNameText;
        TextView locationText;
        TextView checkInOutText;
        TextView numRoomsText;
        TextView roomTypeText;

        public AccommodationViewHolder(View itemView) {
            super(itemView);
            hotelNameText = itemView.findViewById(R.id.hotelNameText);
            locationText = itemView.findViewById(R.id.locationText);
            checkInOutText = itemView.findViewById(R.id.checkInOutText);
            numRoomsText = itemView.findViewById(R.id.numRoomsText);
            roomTypeText = itemView.findViewById(R.id.roomTypeText);
        }
    }
}
