package com.example.koekata.ui.main.WakeupTime;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.koekata.R;
import com.example.koekata.databinding.FragmentWakeuptimeBinding;
import com.example.koekata.viewmodelprovider.ViewModelProviderFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class WakeupTimeFragment extends DaggerFragment {

    private WakeupTimeViewModel wakeupTimeViewModel;
    private FragmentWakeuptimeBinding binding;
    private EditText editTextHour;
    private EditText editTextMinute;
    private TextView textViewSleepTime1;
    private TextView textViewSleepTime2;
    private TextView textViewSleepTime3;
    private TextView textViewSleepTime4;
    private Button buttonSubmit;
    private ConstraintLayout layoutSleepTime;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        wakeupTimeViewModel =
                new ViewModelProvider(this, viewModelProviderFactory).get(WakeupTimeViewModel.class);

        binding = FragmentWakeuptimeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextHour = view.findViewById(R.id.et_hour);
        editTextMinute = view.findViewById(R.id.et_minute);

        layoutSleepTime = view.findViewById(R.id.cl_sleep_time);
        textViewSleepTime1 = layoutSleepTime.findViewById(R.id.tv_sleep_time_1);
        textViewSleepTime2 = layoutSleepTime.findViewById(R.id.tv_sleep_time_2);
        textViewSleepTime3 = layoutSleepTime.findViewById(R.id.tv_sleep_time_3);
        textViewSleepTime4 = layoutSleepTime.findViewById(R.id.tv_sleep_time_4);

        MediatorLiveData<ArrayList<String>> sleepTimesLiveData = wakeupTimeViewModel.getSleepTimesLiveData();
        sleepTimesLiveData.removeObservers(getViewLifecycleOwner());
        sleepTimesLiveData.observe(getViewLifecycleOwner(), sleepTimes -> {
            textViewSleepTime1.setText(sleepTimes.get(0));
            textViewSleepTime2.setText(sleepTimes.get(1));
            textViewSleepTime3.setText(sleepTimes.get(2));
            textViewSleepTime4.setText(sleepTimes.get(3));
        });

        buttonSubmit = view.findViewById(R.id.btn_calculate);
        buttonSubmit.setOnClickListener(v -> {
            int hour = Integer.parseInt(editTextHour.getText().toString());
            int min = Integer.parseInt(editTextMinute.getText().toString());
            if (layoutSleepTime.getVisibility() == View.INVISIBLE)
                layoutSleepTime.setVisibility(View.VISIBLE);
            wakeupTimeViewModel.setSleepTimesFromWakeUpTime(hour, min);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}