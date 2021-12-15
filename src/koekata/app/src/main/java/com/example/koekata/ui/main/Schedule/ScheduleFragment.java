package com.example.koekata.ui.main.Schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.koekata.R;
import com.example.koekata.databinding.FragmentScheduleBinding;
import com.example.koekata.databinding.FragmentWakeuptimeBinding;
import com.example.koekata.viewmodelprovider.ViewModelProviderFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ScheduleFragment extends DaggerFragment {
    private ScheduleViewModel scheduleViewModel;
    private FragmentScheduleBinding binding;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        scheduleViewModel =
                new ViewModelProvider(this, viewModelProviderFactory).get(ScheduleViewModel.class);

        binding = FragmentScheduleBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
