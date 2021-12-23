package com.example.koekata.ui.main.Pomodoro;

import static com.example.koekata.utils.Constants.*;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.koekata.R;
import com.example.koekata.databinding.FragmentPomodoroBinding;
import com.example.koekata.viewmodelprovider.ViewModelProviderFactory;

import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class PomodoroFragment extends DaggerFragment {

    private PomodoroViewModel pomodoroViewModel;
    private FragmentPomodoroBinding binding;

    private ProgressBar progressBar;
    private ImageView img;
    private TextView textStatus;
    private TextView textTime;
    private Button btnPomodoro;
    private ImageButton btnEdit;

    private long countDownTime;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    Spinner studySpinner, shortRelaxSpinner, longRelaxSpinner, soundAlarmSpinner;

    private long studyTime;
    private long shortRelaxTime;
    private long longRelaxTime;

    @Inject
    ViewModelProviderFactory providerFactory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pomodoroViewModel =
                new ViewModelProvider(this, providerFactory).get(PomodoroViewModel.class);

        binding = FragmentPomodoroBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View pomodoroView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(pomodoroView, savedInstanceState);
        progressBar = pomodoroView.findViewById(R.id.progress_bar);
        textStatus = pomodoroView.findViewById(R.id.text_status);
        textTime = pomodoroView.findViewById(R.id.text_time);
        img = pomodoroView.findViewById(R.id.image_view_progress);
        btnPomodoro = pomodoroView.findViewById(R.id.button_pomodoro);
        btnEdit = pomodoroView.findViewById(R.id.button_edit);

        setOnUpdateStatus(pomodoroView);
        setOnNewCountDown();
        setOnUpdateCountDown();
        setOnUpdateSetting();

        btnPomodoro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pomodoroViewModel.clickUpdateStatus();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog editDialog = new Dialog(pomodoroView.getContext());
                editDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                editDialog.setContentView(R.layout.dialog_pomodoro_setting);

                Window window = editDialog.getWindow();
                if(window == null){
                    return;
                }
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                editDialog.setCancelable(false);

                studySpinner = editDialog.findViewById(R.id.spinner_study);
                shortRelaxSpinner = editDialog.findViewById(R.id.spinner_short_relax);
                longRelaxSpinner = editDialog.findViewById(R.id.spinner_long_relax);
                //soundAlarmSpinner = editDialog.findViewById(R.id.spinner_alarm_sound);

                ArrayAdapter studyAdapter = setStudySpinnerSelect(editDialog);
                ArrayAdapter shortRelaxAdapter = setShortRelaxSelect(editDialog);
                ArrayAdapter longRelaxAdapter = setLongRelaxSelect(editDialog);

                updateSpinner(studyAdapter, shortRelaxAdapter, longRelaxAdapter);

                editDialog.findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        studyTime = Long.parseLong(studySpinner.getSelectedItem().toString()) * 1000;
                        shortRelaxTime = Long.parseLong(shortRelaxSpinner.getSelectedItem().toString()) * 1000;
                        longRelaxTime = Long.parseLong(longRelaxSpinner.getSelectedItem().toString()) * 1000;
                        pomodoroViewModel.changeSettingTime(studyTime, shortRelaxTime, longRelaxTime);
                        editDialog.dismiss();
                    }
                });

                editDialog.show();
            }
        });
    }

    private void updateSpinner(ArrayAdapter studyAdapter, ArrayAdapter shortRelaxAdapter,
                               ArrayAdapter longRelaxAdapter) {
        int pos = studyAdapter.getPosition(String.valueOf(studyTime/1000));
        studySpinner.setSelection(pos);
        pos = shortRelaxAdapter.getPosition(String.valueOf(shortRelaxTime/1000));
        shortRelaxSpinner.setSelection(pos);
        pos = longRelaxAdapter.getPosition(String.valueOf(longRelaxTime/1000));
        longRelaxSpinner.setSelection(pos);
    }


    private ArrayAdapter setStudySpinnerSelect(@NonNull Dialog diaglog) {
        ArrayAdapter studyAdapter=ArrayAdapter.createFromResource(
                diaglog.getContext(),
                R.array.study_time,
                R.layout.custom_spinner
                );
        studyAdapter.setDropDownViewResource(R.layout.custom_dropdown_spiner);
        studySpinner.setAdapter(studyAdapter);
        return studyAdapter;

    }

    private ArrayAdapter setShortRelaxSelect(@NonNull Dialog dialog){
        ArrayAdapter shortRelaxAdapter=ArrayAdapter.createFromResource(
                dialog.getContext(),
                R.array.short_relax_time,
                R.layout.custom_spinner
        );
        shortRelaxAdapter.setDropDownViewResource(R.layout.custom_dropdown_spiner);
        shortRelaxSpinner.setAdapter(shortRelaxAdapter);
        return shortRelaxAdapter;
    }

    private ArrayAdapter setLongRelaxSelect(@NonNull Dialog dialog){
        ArrayAdapter longRelaxAdapter=ArrayAdapter.createFromResource(
                dialog.getContext(),
                R.array.short_relax_time,
                R.layout.custom_spinner
        );
        longRelaxAdapter.setDropDownViewResource(R.layout.custom_dropdown_spiner);
        longRelaxSpinner.setAdapter(longRelaxAdapter);
        return longRelaxAdapter;
    }

    private void setOnUpdateStatus(View view) {
        LiveData<String> liveStatus = pomodoroViewModel.getStatus();
        liveStatus.removeObservers(getViewLifecycleOwner());
        liveStatus.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s == STATIC_STATUS){
                    textStatus.setText(STATIC_STATUS);
                    progressBar.setProgress(100);
                    img.setImageResource(R.drawable.ic_pomodoro_start);
                    btnPomodoro.setText(STATIC_BUTTON);
                }
                else if(s == STUDY_STATUS){
                    textStatus.setText(STUDY_STATUS);
                    progressBar.setProgress(0);
                    Glide.with(view).load(R.drawable.pomodoro_study).into(img);
                    btnPomodoro.setText(STUDY_BUTTON);
                }
                else if(s == DONE_STATUS){
                    textStatus.setText(DONE_STATUS);
                    progressBar.setProgress(100);
                    img.setImageResource(R.drawable.ic_pomodoro_complete);
                    btnPomodoro.setText(DONE_BUTTON);
                }
                else{
                    textStatus.setText(RELAX_STATUS);
                    progressBar.setProgress(0);
                    Glide.with(view).load(R.drawable.pomodoro_relax).into(img);
                    btnPomodoro.setText(RELAX_BUTTON);
                }
            }
        });
    }

    private void setOnNewCountDown(){
        LiveData<Long> liveCountDownTimeInMillis = pomodoroViewModel.getCountDownTimeInMillis();
        liveCountDownTimeInMillis.removeObservers(getViewLifecycleOwner());
        liveCountDownTimeInMillis.observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long totalTime) {
                    countDownTime = totalTime;
            }
        });
    }

    private void setOnUpdateCountDown(){
        LiveData<Long> liveTimeLeftInMillis = pomodoroViewModel.getTimeLeftInMillions();
        liveTimeLeftInMillis.removeObservers(getViewLifecycleOwner());

        liveTimeLeftInMillis.observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long timeLeft) {
                int minutes = (int)(timeLeft /1000)/60;
                int seconds = (int)(timeLeft /1000)%60;

                String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                progressBar.setProgress(100 - (int)Math.round(timeLeft*1.0/countDownTime*100));
                textTime.setText(timeFormatted);
                if(seconds==0){
                    progressBar.setProgress(100);
                }
            }
        });
    }

    private void setOnUpdateSetting(){
        LiveData<HashMap<String, Long>> liveSettingTime = pomodoroViewModel.getSettingTime();
        liveSettingTime.removeObservers(getViewLifecycleOwner());

        liveSettingTime.observe(getViewLifecycleOwner(), new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> settingTimeHashMap) {
                if (settingTimeHashMap != null) {
                    studyTime = settingTimeHashMap.get(STUDY_TIME);
                    shortRelaxTime = settingTimeHashMap.get(SHORT_RELAX_TIME);
                    longRelaxTime = settingTimeHashMap.get(LONG_RELAX_TIME);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

        // Comment neu muon pomodoro chay ngam khi mo sang tab khac
        pomodoroViewModel.cancelCountDown();
        textStatus.setText(STATIC_STATUS);
        progressBar.setProgress(100);
        img.setImageResource(R.drawable.ic_pomodoro_start);
        btnPomodoro.setText(STATIC_BUTTON);
    }
}