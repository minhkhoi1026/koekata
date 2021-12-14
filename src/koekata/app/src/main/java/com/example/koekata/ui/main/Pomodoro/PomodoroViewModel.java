package com.example.koekata.ui.main.Pomodoro;

import static com.example.koekata.utils.Constants.*;

import android.os.CountDownTimer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

public class PomodoroViewModel extends ViewModel {
    private MediatorLiveData<String> mStatus;
    private MediatorLiveData<Long> mCountDownTimeInMillis;
    private MediatorLiveData<Long> mTimeLeftInMillis;
    private MediatorLiveData<Long> mStudyTime;
    private MediatorLiveData<Long> mShortRelaxTime;
    private MediatorLiveData<Long> mLongRelaxTime;
    private MediatorLiveData<Integer> mAlarmSound;

    private int mCountPomodoro;
    private CountDownTimer mCountDownTimer;

    public PomodoroViewModel() {
        mStatus = new MediatorLiveData<>();
        mCountDownTimeInMillis = new MediatorLiveData<>();
        mTimeLeftInMillis = new MediatorLiveData<>();
        mStudyTime = new MediatorLiveData<>();
        mShortRelaxTime = new MediatorLiveData<>();
        mLongRelaxTime = new MediatorLiveData<>();
        mAlarmSound = new MediatorLiveData<>();
        mStatus.setValue(STATIC_STATUS);
        mCountPomodoro = 0;
        // set initial value
        mCountDownTimeInMillis.setValue(DEFAULT_STUDY_TIME);
        mStudyTime.setValue(DEFAULT_STUDY_TIME);
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

    void clickUpdateStatus(){
        cancelCountDown();
        if(mStatus.getValue().toString().equals(STATIC_STATUS)){
            mStatus.postValue(STUDY_STATUS);
            mCountDownTimeInMillis.postValue(mStudyTime.getValue());
        }
        else if(mStatus.getValue().toString().equals(STUDY_STATUS)){
            mStatus.postValue(STATIC_STATUS);
            mCountDownTimeInMillis.postValue(mStudyTime.getValue());
        }
        else if(mStatus.getValue().toString().equals(DONE_STATUS)){
            mStatus.postValue(RELAX_STATUS);
            if(mCountPomodoro != 4){
                mCountDownTimeInMillis.postValue(mShortRelaxTime.getValue());
            }
            else{
                mCountDownTimeInMillis.postValue(mLongRelaxTime.getValue());
            }

        }
        else{
            mStatus.postValue(STATIC_STATUS);
            mCountDownTimeInMillis.postValue(mStudyTime.getValue());
        }
        mTimeLeftInMillis.postValue(mCountDownTimeInMillis.getValue());
        startCountDown();
    }

    void autoUpdateStatus(){
        cancelCountDown();
        if(mStatus.getValue().toString().equals(STUDY_STATUS)){
            mStatus.postValue(DONE_STATUS);
            mCountDownTimeInMillis.postValue(new Long(0));
        }
        else if(mStatus.getValue().toString().equals(RELAX_STATUS)){
            mStatus.postValue(STATIC_STATUS);
            mCountDownTimeInMillis.postValue(mStudyTime.getValue());
            mCountPomodoro = mCountPomodoro % 4 + 1;
        }
        else{
            return;
        }

        mTimeLeftInMillis.postValue(mCountDownTimeInMillis.getValue());
        startCountDown();

    }

    private void startCountDown() {
        if(mStatus.getValue().toString().equals(DONE_STATUS)
            || mStatus.getValue().toString().equals(STATIC_STATUS))
        {
            return;
        }

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

}
