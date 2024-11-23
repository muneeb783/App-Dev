package com.example.wandersync.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wandersync.R;
import com.example.wandersync.model.TravelPost;

import java.util.List;

public class TravelPostAdapter extends RecyclerView.Adapter<TravelPostAdapter.TravelPostViewHolder> {

    private final List<TravelPost> travelPosts;

    public TravelPostAdapter(List<TravelPost> travelPosts) {
        this.travelPosts = travelPosts;
    }

    @NonNull
    @Override
    public TravelPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_travel_post, parent, false); // Ensure this layout has required TextViews
        return new TravelPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelPostViewHolder holder, int position) {
        TravelPost travelPost = travelPosts.get(position);

        // Bind summary data
        holder.userIdTextView.setText("User ID: " + travelPost.getUserId());
        holder.startDateTextView.setText("Start Date: " + travelPost.getStartDate());
        holder.endDateTextView.setText("End Date: " + travelPost.getEndDate());

        // Bind entered data
        holder.enteredDestinationTextView.setText("Destination: " + travelPost.getEnteredDestination());
        holder.enteredAccommodationTextView.setText("Accommodation: " + travelPost.getEnteredAccommodation());
        holder.enteredDiningTextView.setText("Dining: " + travelPost.getEnteredDining());
    }

    @Override
    public int getItemCount() {
        return travelPosts.size();
    }

    public static class TravelPostViewHolder extends RecyclerView.ViewHolder {
        TextView startDateTextView, endDateTextView, userIdTextView;
        TextView enteredDestinationTextView, enteredAccommodationTextView, enteredDiningTextView;

        public TravelPostViewHolder(@NonNull View itemView) {
            super(itemView);
            startDateTextView = itemView.findViewById(R.id.start_date_text);
            endDateTextView = itemView.findViewById(R.id.end_date_text);
            userIdTextView = itemView.findViewById(R.id.user_id_text);
            enteredDestinationTextView = itemView.findViewById(R.id.entered_destination_text);
            enteredAccommodationTextView = itemView.findViewById(R.id.entered_accommodation_text);
            enteredDiningTextView = itemView.findViewById(R.id.entered_dining_text);
        }
    }
}
