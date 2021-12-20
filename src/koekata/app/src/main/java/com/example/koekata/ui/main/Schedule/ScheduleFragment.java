package com.example.koekata.ui.main.Schedule;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koekata.R;
import com.example.koekata.databinding.FragmentScheduleBinding;
import com.example.koekata.databinding.FragmentWakeuptimeBinding;
import com.example.koekata.models.UserEvent;
import com.example.koekata.viewmodelprovider.ViewModelProviderFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ScheduleFragment extends DaggerFragment implements UserEventAdapter.OnDeleteClickedListener {
    private ScheduleViewModel scheduleViewModel;
    private FragmentScheduleBinding binding;
    private CalendarView calendarView;
    private RecyclerView recyclerView;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scheduleViewModel =
                new ViewModelProvider(this, viewModelProviderFactory).get(ScheduleViewModel.class);

        binding = FragmentScheduleBinding.inflate(inflater, container, false );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.calendar_view_event);

        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            scheduleViewModel.setFocusedDateLiveData(year, month, dayOfMonth);
        });

        recyclerView = view.findViewById(R.id.recycler_view_event);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        MediatorLiveData<ArrayList<UserEvent>> showingEventsLiveData = scheduleViewModel.getShowingEventsLiveData();
        showingEventsLiveData.observe(getViewLifecycleOwner(), arrayList -> {
            UserEventAdapter userEventAdapter = new UserEventAdapter(getContext(), arrayList, this);
            recyclerView.setAdapter(userEventAdapter);
        });

        scheduleViewModel.setFocusedDateLiveData(calendarView.getDate());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void OnDeleteClickedListener(UserEvent event) {
        scheduleViewModel.deleteEvent(event);
    }
}
