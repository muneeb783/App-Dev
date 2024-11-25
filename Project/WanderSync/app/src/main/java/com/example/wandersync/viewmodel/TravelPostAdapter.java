package com.example.wandersync.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wandersync.R;
import com.example.wandersync.Model.TravelPost;
import com.example.wandersync.view.TravelPostDetailsDialog;

import java.util.List;

public class TravelPostAdapter extends
        RecyclerView.Adapter<TravelPostAdapter.TravelPostViewHolder> {

    private final List<TravelPost> travelPosts;

    public TravelPostAdapter(List<TravelPost> travelPosts) {
        this.travelPosts = travelPosts;
    }

    @NonNull
    @Override
    public TravelPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_travel_post, parent, false);
        return new TravelPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelPostViewHolder holder, int position) {
        TravelPost travelPost = travelPosts.get(position);

        holder.setUserIdTextView("User ID: " + travelPost.getUserId());
        holder.setStartDateTextView("Start Date: " + travelPost.getStartDate());
        holder.setEndDateTextView("End Date: " + travelPost.getEndDate());
        holder.setEnteredDestinationTextView("Destination: " + travelPost.getEnteredDestination());
        holder.setEnteredAccommodationTextView("Accommodation: "
                + travelPost.getEnteredAccommodation());
        holder.setEnteredDiningTextView("Dining: " + travelPost.getEnteredDining());
        holder.setRatingTextView("Rating: " + travelPost.getRating());

        holder.itemView.setOnClickListener(v -> {
            TravelPostDetailsDialog dialog = TravelPostDetailsDialog.newInstance(travelPost);
            dialog.show(((FragmentActivity) v.getContext()).getSupportFragmentManager(),
                    "travel_post_details");
        });
    }

    @Override
    public int getItemCount() {
        return travelPosts.size();
    }

    public static class TravelPostViewHolder extends RecyclerView.ViewHolder {

        private final TextView startDateTextView;
        private final TextView endDateTextView;
        private final TextView userIdTextView;
        private final TextView enteredDestinationTextView;
        private final TextView enteredAccommodationTextView;
        private final TextView enteredDiningTextView;
        private final TextView ratingTextView;

        public TravelPostViewHolder(@NonNull View itemView) {
            super(itemView);

            this.startDateTextView = itemView.findViewById(R.id.start_date_text);
            this.endDateTextView = itemView.findViewById(R.id.end_date_text);
            this.userIdTextView = itemView.findViewById(R.id.user_id_text);
            this.enteredDestinationTextView = itemView.findViewById(R.id.entered_destination_text);
            this.enteredAccommodationTextView = itemView.
                    findViewById(R.id.entered_accommodation_text);
            this.enteredDiningTextView = itemView.findViewById(R.id.entered_dining_text);
            this.ratingTextView = itemView.findViewById(R.id.rating);
        }

        public void setStartDateTextView(String text) {
            this.startDateTextView.setText(text);
        }

        public void setEndDateTextView(String text) {
            this.endDateTextView.setText(text);
        }

        public void setUserIdTextView(String text) {
            this.userIdTextView.setText(text);
        }

        public void setEnteredDestinationTextView(String text) {
            this.enteredDestinationTextView.setText(text);
        }

        public void setEnteredAccommodationTextView(String text) {
            this.enteredAccommodationTextView.setText(text);
        }

        public void setEnteredDiningTextView(String text) {
            this.enteredDiningTextView.setText(text);
        }
        public void setRatingTextView(String text) {
            this.ratingTextView.setText(text);
        }
    }
}
