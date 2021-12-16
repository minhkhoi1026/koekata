package com.example.koekata.ui.main.Pomodoro;

import static com.example.koekata.utils.Constants.*;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.koekata.models.UserRepository;

import java.util.HashMap;

import javax.inject.Inject;

public class PomodoroViewModel extends ViewModel {
    private UserRepository model;

    private MediatorLiveData<String> mStatus;
    private MediatorLiveData<Long> mCountDownTimeInMillis;
    private MediatorLiveData<Long> mTimeLeftInMillis;


    private MediatorLiveData<HashMap<String, Long>> settingTime;

    private int mCountPomodoro;
    private CountDownTimer mCountDownTimer;

    @Inject
    public PomodoroViewModel(UserRepository repository) {
        model = repository;

        mStatus = new MediatorLiveData<>();
        mCountDownTimeInMillis = new MediatorLiveData<>();
        mTimeLeftInMillis = new MediatorLiveData<>();
        settingTime = new MediatorLiveData<>();
        mStatus.setValue(STATIC_STATUS);
        mCountPomodoro = 0;

        // set initial value
        setInitSetting();
    }

    public LiveData<String> getStatus() {
        return mStatus;
    }

    public MediatorLiveData<Long> getCountDownTimeInMillis() {
        return mCountDownTimeInMillis;
    }

    public LiveData<Long> getTimeLeftInMillions() {

        return mTimeLeftInMillis;
    }

    public MediatorLiveData<HashMap<String, Long>> getSettingTime() {
        return settingTime;
    }


    private void setInitSetting(){
        MediatorLiveData<HashMap<String, Long>> liveSettingTime = model.getPomodoroSettingsLiveData();
        settingTime.addSource(liveSettingTime, new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> settingTimeHashMap) {
                if (settingTimeHashMap != null) {
                    settingTime.setValue(settingTimeHashMap);

                    if (mStatus.getValue().equals(STATIC_STATUS)) {
                        mTimeLeftInMillis.setValue(settingTimeHashMap.get(STUDY_TIME));
                    }
                }
            }
        });
    }

    public void clickUpdateStatus(){
        cancelCountDown();
        long totalTime;
        if(mStatus.getValue().toString().equals(STATIC_STATUS)){
            mStatus.setValue(STUDY_STATUS);
            totalTime = settingTime.getValue().get(STUDY_TIME);
            mCountDownTimeInMillis.setValue(totalTime);
            mTimeLeftInMillis.setValue(totalTime);
            startCountDown();
        }
        else if(mStatus.getValue().toString().equals(STUDY_STATUS)){
            mStatus.setValue(STATIC_STATUS);
            totalTime = settingTime.getValue().get(STUDY_TIME);
            mCountDownTimeInMillis.setValue(totalTime);
            mTimeLeftInMillis.setValue(totalTime);
        }
        else if(mStatus.getValue().toString().equals(DONE_STATUS)){
            mStatus.setValue(RELAX_STATUS);
            if(mCountPomodoro != 4){
                totalTime = settingTime.getValue().get(SHORT_RELAX_TIME);
            }
            else{
                totalTime = settingTime.getValue().get(LONG_RELAX_TIME);
            }
            mCountDownTimeInMillis.setValue(totalTime);
            mTimeLeftInMillis.setValue(totalTime);
            startCountDown();
        }
        else{
            mStatus.setValue(STATIC_STATUS);
            totalTime = settingTime.getValue().get(STUDY_TIME);
            mCountDownTimeInMillis.setValue(totalTime);
            mTimeLeftInMillis.setValue(totalTime);
        }
    }

    public void autoUpdateStatus(){
        if(mStatus.getValue().toString().equals(STUDY_STATUS)){
            mStatus.setValue(DONE_STATUS);
            mCountPomodoro = mCountPomodoro % 4 + 1;
            model.addPomodoro(System.currentTimeMillis());
        }
        else if(mStatus.getValue().toString().equals(RELAX_STATUS)){
            mStatus.setValue(STATIC_STATUS);
        }
        else{
            return;
        }
    }

    private void startCountDown() {
        mCountDownTimer = new CountDownTimer(mCountDownTimeInMillis.getValue(), 1000) {
            @Override
            public void onTick(long milliUntileEnd) {
                mTimeLeftInMillis.postValue(milliUntileEnd);
            }
            @Override
            public void onFinish() {
                mTimeLeftInMillis.postValue(new Long(0));
                autoUpdateStatus();
            }
        }.start();
    }

    public void cancelCountDown(){
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    public void changeSettingTime(long studyTime, long shortRelaxTime, long longRelaxTime){
        model.setPomodoroTime(studyTime, shortRelaxTime, longRelaxTime);
    }


}
