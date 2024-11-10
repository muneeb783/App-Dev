//package com.example.wandersync.view;
//
//import android.content.Context;
//import android.graphics.drawable.GradientDrawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.SectionIndexer;
//import android.widget.TextView;
//
//import com.example.wandersync.R;
//import com.example.wandersync.model.DiningReservation;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import android.graphics.Color;
//
//import androidx.annotation.NonNull;
//import androidx.core.content.ContextCompat;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Locale;
//import java.util.Map;

//public class ReservationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private List<DiningReservation> reservations = new ArrayList<>();
//    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
//    private Map<String, List<DiningReservation>> sectionedReservations = new LinkedHashMap<>();
//
//    private static final int TYPE_HEADER = 0;
//    private static final int TYPE_ITEM = 1;
//
//    public  ReservationsAdapter(List<DiningReservation> reservations) {
//        setReservations(reservations);
//    }
//
//    public void setReservations(List<DiningReservation> reservations) {
//        this.reservations = reservations;
//        sortAndSectionReservations();
//        notifyDataSetChanged();
//    }
//
//    private void sortAndSectionReservations() {
//        // Sort reservations by date and time
//        Collections.sort(reservations, (r1, r2) -> Long.compare(r1.getReservationTime(), r2.getReservationTime()));
//
//        // Group reservations by date
//        sectionedReservations.clear();
//        SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//
//        for (DiningReservation reservation : reservations) {
//            String dateKey = dayFormat.format(reservation.getReservationTime());
//
//            if (!sectionedReservations.containsKey(dateKey)) {
//                sectionedReservations.put(dateKey, new ArrayList<>());
//            }
//            sectionedReservations.get(dateKey).add(reservation);
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        String key = getSectionKeyAtPosition(position);
//        return key != null ? TYPE_HEADER : TYPE_ITEM;
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == TYPE_HEADER) {
//            View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_header_item, parent, false);
//            return new DateHeaderViewHolder(headerView);
//        } else {
//            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.form_reservation, parent, false);
//            return new DiningReservationViewHolder(itemView);
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof DateHeaderViewHolder) {
//            String dateKey = getSectionKeyAtPosition(position);
//            ((DateHeaderViewHolder) holder).bind(dateKey);
//        } else if (holder instanceof DiningReservationViewHolder) {
//            DiningReservation reservation = getItemAtPosition(position);
//            ((DiningReservationViewHolder) holder).bind(reservation);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        int count = sectionedReservations.size(); // One for each header
//        for (String date : sectionedReservations.keySet()) {
//            count += sectionedReservations.get(date).size();
//        }
//        return count;
//    }
//
//    private DiningReservation getItemAtPosition(int position) {
//        int pos = position;
//        for (String date : sectionedReservations.keySet()) {
//            pos--;
//            if (pos < 0) break;
//            List<DiningReservation> reservations = sectionedReservations.get(date);
//            if (pos < reservations.size()) return reservations.get(pos);
//            pos -= reservations.size();
//        }
//        return null;
//    }
//
//    private String getSectionKeyAtPosition(int position) {
//        int pos = position;
//        for (String date : sectionedReservations.keySet()) {
//            if (pos == 0) return date;
//            pos--;
//            List<DiningReservation> reservations = sectionedReservations.get(date);
//            pos -= reservations.size();
//        }
//        return null;
//    }
//
//    class DateHeaderViewHolder extends RecyclerView.ViewHolder {
//        private final TextView dateText;
//
//        public DateHeaderViewHolder(@NonNull View itemView) {
//            super(itemView);
//            dateText = itemView.findViewById(R.id.dateText);
//        }
//
//        public void bind(String date) {
//            dateText.setText(date);
//        }
//    }
//
//    class DiningReservationViewHolder extends RecyclerView.ViewHolder {
//        private final TextView locationText, websiteText, dateText, reviewText;
//
//        public DiningReservationViewHolder(@NonNull View itemView) {
//            super(itemView);
//            locationText = itemView.findViewById(R.id.locationText);
//            websiteText = itemView.findViewById(R.id.websiteText);
//            dateText = itemView.findViewById(R.id.dateText);
//            reviewText = itemView.findViewById(R.id.reviewText);
//        }
//
//        public void bind(DiningReservation reservation) {
//            locationText.setText(reservation.getLocation());
//            websiteText.setText(reservation.getWebsite());
//            dateText.setText(dateFormat.format(reservation.getReservationTime()));
//            reviewText.setText(reservation.getReview());
//
//            // Visual indication for past dates
//            long currentTime = Calendar.getInstance().getTimeInMillis();
//            if (reservation.getReservationTime() < currentTime) {
//                itemView.setBackgroundColor(Color.parseColor("#FFE0E0")); // light red background for past dates
//            } else {
//                itemView.setBackgroundColor(Color.WHITE); // default color for future dates
//            }
//        }
//    }
//}

//public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder> {
//
//    private List<DiningReservation> reservations;
//    private final Context context;
//    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
//
//    public ReservationsAdapter(Context context, List<DiningReservation> reservations) {
//        this.context = context;
//        this.reservations = reservations != null ? reservations : new ArrayList<>();
//    }
//
//    public void setReservations(List<DiningReservation> reservations) {
//        this.reservations = reservations;
//        sortReservationsByDate();
//        notifyDataSetChanged();
//    }
//
//    private void sortReservationsByDate() {
//        Collections.sort(reservations, (r1, r2) -> Long.compare(r1.getReservationTime(), r2.getReservationTime()));
//    }
//
//    @NonNull
//    @Override
//    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.form_reservation, parent, false);
//        return new ReservationViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
//        DiningReservation reservation = reservations.get(position);
//        holder.bind(reservation, position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return reservations.size();
//    }
//
//    public class ReservationViewHolder extends RecyclerView.ViewHolder {
//        private final TextView locationTextView;
//        private final TextView websiteTextView;
//        private final TextView timeTextView;
//        private final TextView reviewTextView;
//        private final TextView dateTextView;
//
//        public ReservationViewHolder(View itemView) {
//            super(itemView);
//            locationTextView = itemView.findViewById(R.id.text_view_location);
//            websiteTextView = itemView.findViewById(R.id.text_view_website);
//            timeTextView = itemView.findViewById(R.id.text_view_time);
//            reviewTextView = itemView.findViewById(R.id.text_view_review);
//            dateTextView = itemView.findViewById(R.id.text_view_date);
//        }
//
//        public void bind(DiningReservation reservation, int position) {
//            locationTextView.setText(reservation.getLocation());
//            websiteTextView.setText(reservation.getWebsite());
//            timeTextView.setText(timeFormat.format(reservation.getReservationTime()));
//            reviewTextView.setText(reservation.getReview());
//
//            String formattedDate = dateFormat.format(reservation.getReservationTime());
//            dateTextView.setVisibility(View.GONE);
//
//            // Show date header if it's the first item or date differs from the previous item
//            if (position == 0 || !formattedDate.equals(dateFormat.format(reservations.get(position - 1).getReservationTime()))) {
//                dateTextView.setText(formattedDate);
//                dateTextView.setVisibility(View.VISIBLE);
//            }
//
//            if (reservation.isExpired()) {
//                GradientDrawable backgroundDrawable = new GradientDrawable();
//                backgroundDrawable.setColor(Color.RED); // Light gray for expired reservations
//                backgroundDrawable.setCornerRadius(40);
//                itemView.setBackground(backgroundDrawable); // Use itemView here
//            }
//        }
//
//    }
//}