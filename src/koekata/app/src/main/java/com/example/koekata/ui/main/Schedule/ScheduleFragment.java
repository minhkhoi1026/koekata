package com.example.koekata.ui.main.Schedule;

import static com.example.koekata.utils.Constants.MILLIS_PER_DAY;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ScheduleFragment extends DaggerFragment implements UserEventAdapter.OnDeleteClickedListener {
    private ScheduleViewModel scheduleViewModel;
    private FragmentScheduleBinding binding;
    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

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

        // setting for calendar view
        calendarView = view.findViewById(R.id.calendar_view_event);

        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            scheduleViewModel.setFocusedDateLiveData(year, month, dayOfMonth);
        });

        // setting for recycler view
        recyclerView = view.findViewById(R.id.recycler_view_event);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        MediatorLiveData<ArrayList<UserEvent>> showingEventsLiveData = scheduleViewModel.getShowingEventsLiveData();
        showingEventsLiveData.observe(getViewLifecycleOwner(), arrayList -> {
            UserEventAdapter userEventAdapter = new UserEventAdapter(getContext(), arrayList, this);
            recyclerView.setAdapter(userEventAdapter);
        });

        scheduleViewModel.setFocusedDateLiveData(calendarView.getDate());

        // floating action button
        fab = view.findViewById(R.id.fab_add_event);

        fab.setOnClickListener(view1 -> {
            try {
                showAddEventDialog();
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.d("hehe",e.getStackTrace().toString());
            }
        });
    }

    private void showAddEventDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.schedule_setting_dialog);

        Window window = dialog.getWindow();

        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.show();

        EditText editTextTitle = dialog.findViewById(R.id.edit_text_title);
        EditText editTextDate = dialog.findViewById(R.id.edit_text_date);
        EditText editTextDesc = dialog.findViewById(R.id.edit_text_desc);
        Button buttonSubmit = dialog.findViewById(R.id.btn_set);
        buttonSubmit.setOnClickListener(view -> {
            String title = editTextTitle.getText().toString();

            String desc = editTextDesc.getText().toString();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(editTextDate.getText().toString(), formatter);
            Long dateTimeMillis = localDate.toEpochDay() * MILLIS_PER_DAY;

            scheduleViewModel.addEvent(title, desc, dateTimeMillis);
            dialog.dismiss();
        });
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
