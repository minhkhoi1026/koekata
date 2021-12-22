package com.example.koekata.ui.main.WakeupTime;

import android.annotation.SuppressLint;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koekata.models.UserRepository;

import java.util.ArrayList;

import javax.inject.Inject;

public class WakeupTimeViewModel extends ViewModel {
    private final UserRepository userRepository;

    public MediatorLiveData<ArrayList<String>> getSleepTimesLiveData() {
        return sleepTimesLiveData;
    }

    private MediatorLiveData<ArrayList<String>> sleepTimesLiveData;

    @Inject
    public WakeupTimeViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        sleepTimesLiveData = new MediatorLiveData<>();
    }

    @SuppressLint("DefaultLocale")
    public void setSleepTimesFromWakeUpTime(int hour, int min) {
        int dmin = 0;
        int dhour = 0;
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 3; i <= 6; ++i) {
            dmin = 30 * i;
            dhour = i + (dmin / 60);
            dmin = dmin % 60;
            int new_hour = (hour - dhour - ((min < dmin - 14) ? 1 : 0) + 24) % 24;
            int new_min = (min - dmin - 14 + 60) % 60;
            arrayList.add(String.format("%d%dh%d%dm", new_hour/10, new_hour%10, new_min/10, new_min%10));
        }
        sleepTimesLiveData.postValue(arrayList);
    }
}
