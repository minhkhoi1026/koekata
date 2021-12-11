package com.example.koekata.ui.gallery;

import static com.example.koekata.Constants.DONE_BUTTON;
import static com.example.koekata.Constants.DONE_STATUS;
import static com.example.koekata.Constants.RELAX_BUTON;
import static com.example.koekata.Constants.STATIC_BUTTON;
import static com.example.koekata.Constants.STATIC_STATUS;
import static com.example.koekata.Constants.STUDY_BUTTON;
import static com.example.koekata.Constants.STUDY_STATUS;

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
import com.example.koekata.databinding.FragmentGalleryBinding;

import org.w3c.dom.Text;

import java.util.Locale;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;

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
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress_bar);
        textStatus = view.findViewById(R.id.text_status);
        textTime = view.findViewById(R.id.text_time);
        btnPomodoro = view.findViewById(R.id.button_pomodoro);
        setOnUpdateStatus(view);

        btnPomodoro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryViewModel.clickUpdateStatus();
            }
        });
    }

    private void setOnUpdateStatus(View view) {
        LiveData<String> liveStatus = galleryViewModel.getStatus();
        liveStatus.removeObservers(getViewLifecycleOwner());
        liveStatus.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textStatus.setText(STATIC_STATUS);
                if(s == STATIC_STATUS){
                    progressBar.setProgress(0);
                    img.setImageResource(R.drawable.ic_pomodoro_start);
                    btnPomodoro.setText(STATIC_BUTTON);
                }
                else if(s == STUDY_STATUS){
                    progressBar.setProgress(0);
                    Glide.with(view).load(R.drawable.pomodoro_study).into(img);
                    btnPomodoro.setText(STUDY_BUTTON);
                }
                else if(s == DONE_STATUS){
                    progressBar.setProgress(100);
                    img.setImageResource(R.drawable.ic_pomodoro_complete);
                    btnPomodoro.setText(DONE_BUTTON);
                }
                else{
                    progressBar.setProgress(0);
                    Glide.with(view).load(R.drawable.pomodoro_relax).into(img);
                    btnPomodoro.setText(RELAX_BUTON);
                }
            }
        });
    }

    private void setOnNewCountDown(){
        LiveData<Long> liveCountDownTimeInMillis = galleryViewModel.getCountDownTimeInMillis();
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
        LiveData<Long> liveTimeLeftInMillis = galleryViewModel.getTimeLeftInMillions();
        liveTimeLeftInMillis.removeObservers(getViewLifecycleOwner());

        liveTimeLeftInMillis.observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long timeLeft) {
                progressBar.setProgress((int) (timeLeft/countDownTime));
                int minutes = (int)(timeLeft /1000)/60;
                int seconds = (int)(timeLeft /1000)%60;


                String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                textTime.setText(timeFormatted);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        galleryViewModel.cancelCountDown();
        binding = null;
    }
}