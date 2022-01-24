package com.example.koekata.ui.main.Home;

import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.koekata.BaseApplication;
import com.example.koekata.R;
import com.example.koekata.ui.auth.AuthActivity;
import com.example.koekata.viewmodelprovider.ViewModelProviderFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class HomeFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory providerFactory;
    
    private HomeViewModel homeViewModel;

    private List<HomeStatus> displayStatuses = new ArrayList<>();

    private LinearLayout homeDefault;
    private LinearLayout statusShow;
    private Dialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> openAddMiniScreenDialog());

        homeDefault = view.findViewById(R.id.llHomeDefault);
        statusShow = view.findViewById(R.id.ll_status_show);
        dialog = createNewDialog();

        subscribeObservers(view);
    }

    private Dialog createNewDialog() {
        Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.dialog_addminiscreen);

        Window window = dialog.getWindow();
        if (window == null) {
            return null;
        }

        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        return dialog;
    }

    private void subscribeObservers(View view) {
        homeViewModel = new ViewModelProvider(this, providerFactory).get(HomeViewModel.class);
        homeViewModel.getHomeStatusLiveData().removeObservers(getViewLifecycleOwner());
        homeViewModel.getHomeStatusLiveData().observe(getViewLifecycleOwner(), value -> {
            displayStatuses = HomeStatus.unpackHomeStatus(value);
            validateDefaultStatus();

            hideAllStatuses();
            showSelectedStatuses(view);

            uncheckAllBoxes();
            checkSelectedBoxes();
        });
    }

    private void validateDefaultStatus() {
        if (displayStatuses.isEmpty()) {
            homeDefault.setVisibility(View.VISIBLE);
        } else {
            homeDefault.setVisibility(View.GONE);
        }
    }

    private void hideAllStatuses() {
        for(int index = 0; index < statusShow.getChildCount(); index++) {
            View child = statusShow.getChildAt(index);
            child.setVisibility(View.GONE);
        }
    }

    private void showSelectedStatuses(View view) {
        for (HomeStatus status: displayStatuses) {
            LinearLayout ll = statusShow.findViewById(status.getLayoutId());
            ll.setVisibility(View.VISIBLE);
        }
    }

    private void uncheckAllBoxes() {
        LinearLayout ll = dialog.findViewById(R.id.ll_status_select);
        for(int index = 0; index < ll.getChildCount(); index++) {
            View child = ll.getChildAt(index);

            if (child instanceof CheckBox) {
                ((CheckBox) child).setChecked(false);
            }
        }
    }

    private void checkSelectedBoxes() {
        for (HomeStatus status: displayStatuses) {
            CheckBox cb = dialog.findViewById(status.getCheckboxId());
            cb.setChecked(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void openAddMiniScreenDialog() {
        if (dialog == null) {
            dialog = createNewDialog();
            return;
        }

        dialog.show();

        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(view -> {
            long selectedStatus = getSelectedStatuses();
            homeViewModel.updateHomeStatus(selectedStatus);
            dialog.cancel();
        });
    }

    private long getSelectedStatuses() {
        LinearLayout ll = dialog.findViewById(R.id.ll_status_select);
        long value = 0L;

        for(int index = 0; index < ll.getChildCount(); index++) {
            View child = ll.getChildAt(index);

            if (child instanceof CheckBox && ((CheckBox) child).isChecked()) {
                int id = child.getId();
                HomeStatus status = HomeStatus.findHomeStatusByCheckboxId(id);

                if (status != null)
                    value |= status.getValue();
            }
        }
        return value;
    }
}