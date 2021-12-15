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
    private TextView textViewResult1;
    private TextView textViewResult2;
    private TextView textViewResult3;
    private TextView textViewResult4;
    private Button buttonSubmit;

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
        editTextHour = view.findViewById(R.id.edit_text_wakeup_hour);
        editTextMinute = view.findViewById(R.id.edit_text_wakeup_minute);

        textViewResult1 = view.findViewById(R.id.text_view_result_1_wakeup_time);
        textViewResult2 = view.findViewById(R.id.text_view_result_2_wakeup_time);
        textViewResult3 = view.findViewById(R.id.text_view_result_3_wakeup_time);
        textViewResult4 = view.findViewById(R.id.text_view_result_4_wakeup_time);

        MediatorLiveData<ArrayList<String>> sleepTimesLiveData = wakeupTimeViewModel.getSleepTimesLiveData();
        sleepTimesLiveData.removeObservers(getViewLifecycleOwner());
        sleepTimesLiveData.observe(getViewLifecycleOwner(), sleepTimes -> {
            textViewResult1.setText(sleepTimes.get(0));
            textViewResult2.setText(sleepTimes.get(1));
            textViewResult3.setText(sleepTimes.get(2));
            textViewResult4.setText(sleepTimes.get(3));
        });

        buttonSubmit = view.findViewById(R.id.button_submit_wakeup_time);
        buttonSubmit.setOnClickListener(v -> {
            int hour = Integer.parseInt(editTextHour.getText().toString());
            int min = Integer.parseInt(editTextMinute.getText().toString());
            wakeupTimeViewModel.setSleepTimesFromWakeUpTime(hour, min);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}