package com.example.koekata.ui.home;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.koekata.R;
import com.example.koekata.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private FloatingActionButton fab;

    private LinearLayout llHomeDefault;
    private LinearLayout llPomodoro;
    private LinearLayout llPomodoroStatistic;
    private LinearLayout llDailyTaskList;
    private LinearLayout llDailyTaskListStatistic;
    private LinearLayout llSchedule;
    private LinearLayout llScheduleStatistic;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        llHomeDefault = root.findViewById(R.id.llHomeDefault);
        llPomodoro = root.findViewById(R.id.llPomodoro);
        llPomodoroStatistic = root.findViewById(R.id.llPomodoroStatistic);
        llDailyTaskList = root.findViewById(R.id.llDailyTaskList);
        llDailyTaskListStatistic = root.findViewById(R.id.llDailyTaskListStatistic);
        llSchedule = root.findViewById(R.id.llSchedule);
        llScheduleStatistic = root.findViewById(R.id.llScheduleStatistic);

        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddMiniScreenDialog();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void openAddMiniScreenDialog() {
        final Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.dialog_addminiscreen);

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

        CheckBox cbPomodoro = dialog.findViewById(R.id.cbPomodoro);
        CheckBox cbPomodoroStatistic = dialog.findViewById(R.id.cbPomodoroStatistic);
        CheckBox cbDailyTaskList = dialog.findViewById(R.id.cbDailyTaskList);
        CheckBox cbDailyTaskListStatistic = dialog.findViewById(R.id.cbDailyTaskListStatistic);
        CheckBox cbSchedule = dialog.findViewById(R.id.cbSchedule);
        CheckBox cbScheduleStatistic = dialog.findViewById(R.id.cbScheduleStatistic);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);

        if (llPomodoro.getVisibility() == View.VISIBLE)
            cbPomodoro.setChecked(true);
        if (llPomodoroStatistic.getVisibility() == View.VISIBLE)
            cbPomodoroStatistic.setChecked(true);
        if (llDailyTaskList.getVisibility() == View.VISIBLE)
            cbDailyTaskList.setChecked(true);
        if (llDailyTaskListStatistic.getVisibility() == View.VISIBLE)
            cbDailyTaskListStatistic.setChecked(true);
        if (llSchedule.getVisibility() == View.VISIBLE)
            cbSchedule.setChecked(true);
        if (llScheduleStatistic.getVisibility() == View.VISIBLE)
            cbScheduleStatistic.setChecked(true);


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 0;
                if (cbPomodoro.isChecked()) {
                    count+=1;
                    llPomodoro.setVisibility(View.VISIBLE);
                } else {
                    llPomodoro.setVisibility(View.GONE);
                }
                if (cbPomodoroStatistic.isChecked()) {
                    count+=1;
                    llPomodoroStatistic.setVisibility(View.VISIBLE);
                } else {
                    llPomodoroStatistic.setVisibility(View.GONE);
                }
                if (cbDailyTaskList.isChecked()) {
                    count+=1;
                    llDailyTaskList.setVisibility(View.VISIBLE);
                } else {
                    llDailyTaskList.setVisibility(View.GONE);
                }
                if (cbDailyTaskListStatistic.isChecked()) {
                    count+=1;
                    llDailyTaskListStatistic.setVisibility(View.VISIBLE);
                } else {
                    llDailyTaskListStatistic.setVisibility(View.GONE);
                }
                if (cbSchedule.isChecked()) {
                    count+=1;
                    llSchedule.setVisibility(View.VISIBLE);
                } else {
                    llSchedule.setVisibility(View.GONE);
                }
                if (cbScheduleStatistic.isChecked()) {
                    count+=1;
                    llScheduleStatistic.setVisibility(View.VISIBLE);
                } else {
                    llScheduleStatistic.setVisibility(View.GONE);
                }
                if (count==0) {
                    llHomeDefault.setVisibility(View.VISIBLE);
                } else {
                    llHomeDefault.setVisibility(View.GONE);
                }
                dialog.cancel();
            }
        });


    }
}