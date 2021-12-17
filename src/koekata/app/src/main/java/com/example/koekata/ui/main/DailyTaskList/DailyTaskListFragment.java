package com.example.koekata.ui.main.DailyTaskList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koekata.R;
import com.example.koekata.viewmodelprovider.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class DailyTaskListFragment extends DaggerFragment {

    private DailyTaskListViewModel viewModel;
    private DailyTaskListRecyclerAdapter adapter;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dailytasklist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
        subscribeObservers();
    }

    private void subscribeObservers() {
        viewModel = new ViewModelProvider(this, providerFactory)
                .get(DailyTaskListViewModel.class);
        viewModel.getDailyTasks().removeObservers(getViewLifecycleOwner());
        viewModel.getDailyTasks().observe(getViewLifecycleOwner(), tasks -> {
            if (tasks != null) {
                adapter.setTasks(tasks);
            }
        });
    }

    private void initRecyclerView(View view){
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new DailyTaskListRecyclerAdapter();
        adapter.setOnItemClickListener(dailyTask ->
                viewModel.finishTask(dailyTask.getKey(), dailyTask.getValue().task));
        recyclerView.setAdapter(adapter);
    }
}
