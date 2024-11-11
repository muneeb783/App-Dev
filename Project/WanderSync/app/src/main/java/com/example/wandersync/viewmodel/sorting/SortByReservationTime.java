package com.example.wandersync.viewmodel.sorting;

import com.example.wandersync.model.DiningReservation;
import java.util.List;

public class SortByReservationTime implements SortStrategy<DiningReservation> {
    @Override
    public List<DiningReservation> sort(List<DiningReservation> reservations) {
        reservations.sort((r1, r2) -> Long.compare(r1.getReservationTime(),
                r2.getReservationTime()));
        return reservations;
    }
}
