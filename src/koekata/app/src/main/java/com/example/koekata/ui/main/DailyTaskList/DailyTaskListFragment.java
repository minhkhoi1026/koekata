package com.example.koekata.ui.main.DailyTaskList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koekata.R;
import com.example.koekata.databinding.FragmentDailytasklistBinding;
import com.example.koekata.utils.NotificationUtils;
import com.example.koekata.viewmodelprovider.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class DailyTaskListFragment extends DaggerFragment {

    private static final int HOUR_NOTIFICATION = 8;

    private FragmentDailytasklistBinding binding;
    private DailyTaskListViewModel viewModel;
    private DailyTaskListRecyclerAdapter adapter;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDailytasklistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
        subscribeObservers();
        reminderNotification();

        assert getActivity() != null;
        Button btn = view.findViewById(R.id.btnEdit);
        btn.setOnClickListener(v -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_nav_dailytasklist_to_nav_dailytasklist_edit));
    }

    private void subscribeObservers() {
        viewModel = new ViewModelProvider(this, providerFactory)
                .get(DailyTaskListViewModel.class);
        viewModel.getDailyTasksLiveData().removeObservers(getViewLifecycleOwner());
        viewModel.getDailyTasksLiveData().observe(getViewLifecycleOwner(), tasks -> {
            if (tasks != null) {
                adapter.setTasks(tasks);
            }
        });
    }

    private void initRecyclerView(View view){
        RecyclerView recyclerView = view.findViewById(R.id.daily_task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new DailyTaskListRecyclerAdapter();
        adapter.setOnItemClickListener(dailyTask ->
                viewModel.finishTask(dailyTask.getKey(), dailyTask.getValue().task));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void reminderNotification() {
        NotificationUtils notificationUtils = new NotificationUtils(getActivity());
        notificationUtils.setReminder(HOUR_NOTIFICATION);
    }
}
