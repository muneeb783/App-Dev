package com.example.wandersync.viewmodel.sorting;

import java.util.List;

public interface SortStrategy<T> {
    List<T> sort(List<T> items);
}
