package com.example.koekata.ui.main.Pomodoro;

import static com.example.koekata.utils.Constants.*;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.koekata.R;
import com.example.koekata.databinding.FragmentPomodoroBinding;

import java.util.Locale;

public class PomodoroFragment extends Fragment {

    private PomodoroViewModel pomodoroViewModel;
    private FragmentPomodoroBinding binding;

    private ProgressBar progressBar;
    private ImageView img;
    private TextView textStatus;
    private TextView textTime;
    private Button btnPomodoro;

    private long countDownTime;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pomodoroViewModel =
                new ViewModelProvider(this).get(PomodoroViewModel.class);

        binding = FragmentPomodoroBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress_bar);
        textStatus = view.findViewById(R.id.text_status);
        textTime = view.findViewById(R.id.text_time);
        img = view.findViewById(R.id.image_view_progress);
        btnPomodoro = view.findViewById(R.id.button_pomodoro);
        setOnUpdateStatus(view);
        setOnNewCountDown();
        setOnUpdateCountDown();

        btnPomodoro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pomodoroViewModel.clickUpdateStatus();
            }
        });
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
                    btnPomodoro.setText(RELAX_BUTON);
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
                    timeLeftInMillis = totalTime;
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
                progressBar.setProgress((int)(100 - (timeLeft*1.0/countDownTime)*100));
                textTime.setText(timeFormatted);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        pomodoroViewModel.cancelCountDown();
        binding = null;
    }
}