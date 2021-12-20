package com.example.koekata.ui.main.DailyTaskList;

import static com.example.koekata.utils.Constants.MILLIS_PER_DAY;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koekata.R;
import com.example.koekata.models.UserTask;
import com.example.koekata.utils.VerticalSpaceItemDecoration;
import com.example.koekata.viewmodelprovider.ViewModelProviderFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.TimeZone;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class DailyTaskListEditFragment extends DaggerFragment {

    private DailyTaskListViewModel viewModel;
    private DailyTaskListEditRecyclerAdapter adapter;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dailytasklist_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView(view);
        subscribeObservers();
        subscribeListeners(view);
    }

    private void initRecyclerView(View view){
        RecyclerView recyclerView = view.findViewById(R.id.daily_task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpaceItemDecoration itemDecoration = new VerticalSpaceItemDecoration(15);
        recyclerView.addItemDecoration(itemDecoration);

        adapter = new DailyTaskListEditRecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void subscribeObservers() {
        viewModel = new ViewModelProvider(this, providerFactory)
                .get(DailyTaskListViewModel.class);
        viewModel.getTasksLiveData().removeObservers(getViewLifecycleOwner());
        viewModel.getTasksLiveData().observe(getViewLifecycleOwner(), tasks -> {
            if (tasks != null) {
                adapter.setTasks(tasks);
            }
        });
    }

    private void subscribeListeners(View view) {
        adapter.setOnItemClickListener(this::showEditDialog);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(v -> showAddDialog());
        }
    }

    private void showEditDialog(Map.Entry<String, UserTask> dailyTask) {
        Dialog dialog = createEditDialog(dailyTask);
        if (dialog == null)
            return;
        dialog.show();

        Button updateButton = dialog.findViewById(R.id.btn_update);
        updateButton.setOnClickListener(v -> {
            Pair<UserTask, String> task = getUserTaskFilled(dialog);
            if (task.first != null) {
                viewModel.updateTask(dailyTask.getKey(), task.first);
                dialog.dismiss();

            } else
                Toast.makeText(getContext(), task.second, Toast.LENGTH_SHORT).show();
        });

        Button deleteButton = dialog.findViewById(R.id.btn_delete);
        deleteButton.setOnClickListener(v -> {
            viewModel.deleteTask(dailyTask.getKey());
            dialog.dismiss();
        });
    }

    private void showAddDialog() {
        Dialog dialog = createAddDialog();
        if (dialog == null)
            return;
        dialog.show();

        Button addButton = dialog.findViewById(R.id.btn_add);
        addButton.setOnClickListener(v -> {
            Pair<UserTask, String> task = getUserTaskFilled(dialog);
            if (task.first != null) {
                viewModel.addTask(task.first);
                dialog.dismiss();

            } else
                Toast.makeText(getContext(), task.second, Toast.LENGTH_SHORT).show();
        });
    }

    private Dialog createEditDialog(Map.Entry<String, UserTask> dailyTask) {
        Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.dailytasklist_update_dialog);

        if (setupWindow(dialog)) {
            setupSpinner(dialog);
            fillTaskData(dialog, dailyTask);
            return dialog;
        }
        return null;
    }

    private Dialog createAddDialog() {
        Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.dailytasklist_add_dialog);

        if (setupWindow(dialog)) {
            setupSpinner(dialog);
            fillToday(dialog);
            return dialog;
        }
        return null;
    }

    private boolean setupWindow(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window == null)
            return false;

        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;
        window.setAttributes(windowAttributes);
        return true;
    }

    private void setupSpinner(Dialog dialog) {
        Spinner spinner = dialog.findViewById(R.id.sp_repeat);
        spinnerAdapter = ArrayAdapter.createFromResource(
                dialog.getContext(),
                R.array.repeat_time,
                R.layout.custom_spinner
        );
        spinnerAdapter.setDropDownViewResource(R.layout.custom_dropdown_spiner);
        spinner.setAdapter(spinnerAdapter);
    }

    private void fillTaskData(Dialog dialog, Map.Entry<String, UserTask> dailyTask) {
        EditText title = dialog.findViewById(R.id.et_title);
        title.setText(dailyTask.getValue().title);

        EditText from = dialog.findViewById(R.id.et_from);
        LocalDateTime triggerTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(dailyTask.getValue().start),
                        TimeZone.getDefault().toZoneId());
        from.setText(triggerTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        Spinner spinner = dialog.findViewById(R.id.sp_repeat);
        long repeat = dailyTask.getValue().repeat / MILLIS_PER_DAY;
        int pos = spinnerAdapter.getPosition(Long.toString(repeat));
        spinner.setSelection(pos);
    }

    private void fillToday(Dialog dialog) {
        EditText from = dialog.findViewById(R.id.et_from);
        LocalDate localDate = LocalDate.now();
        from.setText(localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private Pair<UserTask, String> getUserTaskFilled(Dialog dialog) {
        EditText title = dialog.findViewById(R.id.et_title);
        if (title.getText().toString().isEmpty())
            return new Pair<>(null, "Title field must not be empty.");

        EditText from = dialog.findViewById(R.id.et_from);
        Long startDate = parseDate(from.getText().toString());

        Spinner spinner = dialog.findViewById(R.id.sp_repeat);
        return new Pair<>(
            new UserTask(
                title.getText().toString(),
                startDate,
                Long.parseLong(spinner.getSelectedItem().toString()) * MILLIS_PER_DAY,
                0L
            ), "No error");
    }

    private Long parseDate(String s) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(s, formatter);
            return localDate.toEpochDay() * MILLIS_PER_DAY;
        }
        catch (DateTimeParseException e) {
            return null;
        }
    }
}