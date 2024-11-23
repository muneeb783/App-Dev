//package com.example.wandersync.viewmodel;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.ListAdapter;
//import androidx.recyclerview.widget.DiffUtil;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.wandersync.R;
//import com.example.wandersync.Model.TravelPost;
//
//public class TravelPostAdapter extends ListAdapter<TravelPost, TravelPostAdapter.TravelPostViewHolder> {
//
//    public TravelPostAdapter() {
//        super(DIFF_CALLBACK);
//    }
//
//    private static final DiffUtil.ItemCallback<TravelPost> DIFF_CALLBACK = new DiffUtil.ItemCallback<TravelPost>() {
//        @Override
//        public boolean areItemsTheSame(@NonNull TravelPost oldItem, @NonNull TravelPost newItem) {
//            return oldItem.getId() == newItem.getId();
//        }
//
//        @Override
//        public boolean areContentsTheSame(@NonNull TravelPost oldItem, @NonNull TravelPost newItem) {
//            return oldItem.equals(newItem);
//        }
//    };
//
//    @NonNull
//    @Override
//    public TravelPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_travel_post, parent, false);
//        return new TravelPostViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TravelPostViewHolder holder, int position) {
//        TravelPost currentPost = getItem(position);
//        holder.bind(currentPost);
//    }
//
//    // ViewHolder class
//    static class TravelPostViewHolder extends RecyclerView.ViewHolder {
//
//        private final TextView destinationTextView;
//        private final TextView datesTextView;
//        private final TextView accommodationsTextView;
//        private final TextView bookingTextView;
//        private final TextView notesTextView;
//
//        public TravelPostViewHolder(@NonNull View itemView) {
//            super(itemView);
//            destinationTextView = itemView.findViewById(R.id.destination_text);
//            datesTextView = itemView.findViewById(R.id.dates_text);
//            accommodationsTextView = itemView.findViewById(R.id.accommodations_text);
//            bookingTextView = itemView.findViewById(R.id.booking_text);
//            notesTextView = itemView.findViewById(R.id.notes_text);
//        }
//
//        public void bind(TravelPost travelPost) {
//            destinationTextView.setText(travelPost.getDestination());
//            datesTextView.setText(travelPost.getStartDate() + " - " + travelPost.getEndDate());
//            accommodationsTextView.setText(travelPost.getAccommodations());
//            bookingTextView.setText(travelPost.getBookingReservation());
//            notesTextView.setText(travelPost.getNotes());
//        }
//    }
//}
package com.example.wandersync.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wandersync.R;
import com.example.wandersync.Model.TravelPost;

public class TravelPostAdapter extends ListAdapter<TravelPost, TravelPostAdapter.TravelPostViewHolder> {

    public TravelPostAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<TravelPost> DIFF_CALLBACK = new DiffUtil.ItemCallback<TravelPost>() {
        @Override
        public boolean areItemsTheSame(@NonNull TravelPost oldItem, @NonNull TravelPost newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TravelPost oldItem, @NonNull TravelPost newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public TravelPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_travel_post, parent, false);
        return new TravelPostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelPostViewHolder holder, int position) {
        TravelPost currentPost = getItem(position);
        holder.bind(currentPost);
    }

    static class TravelPostViewHolder extends RecyclerView.ViewHolder {

        private final TextView destinationTextView;
        private final TextView datesTextView;
        private final TextView accommodationsTextView;
        private final TextView bookingTextView;
        private final TextView notesTextView;

        public TravelPostViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationTextView = itemView.findViewById(R.id.destination_text);
            datesTextView = itemView.findViewById(R.id.dates_text);
            accommodationsTextView = itemView.findViewById(R.id.accommodations_text);
            bookingTextView = itemView.findViewById(R.id.booking_text);
            notesTextView = itemView.findViewById(R.id.notes_text);
        }

        public void bind(TravelPost travelPost) {
            destinationTextView.setText(travelPost.getDestination());
            datesTextView.setText(travelPost.getStartDate() + " - " + travelPost.getEndDate());
            accommodationsTextView.setText(travelPost.getAccommodations());
            bookingTextView.setText(travelPost.getBookingReservation());
            notesTextView.setText(travelPost.getNotes());
        }
    }
}