package com.example.koekata.ui.main.DailyTaskList;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koekata.R;
import com.example.koekata.ui.main.DailyTaskList.DailyTaskListViewModel.UserDailyTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DailyTaskListRecyclerAdapter
        extends RecyclerView.Adapter<DailyTaskListRecyclerAdapter.TaskListViewHolder> {

    private static final String TAG = "DailyTaskListRecyclerAdapter";
    private final List<Map.Entry<String, UserDailyTask>> tasks = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dailytasklist_task_item, parent, false);
        return new TaskListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(Map<String, UserDailyTask> taskMaps){
        tasks.clear();
        tasks.addAll(taskMaps.entrySet());
        Log.d(TAG, tasks.size() + "");
        notifyDataSetChanged();
    }

    public class TaskListViewHolder extends RecyclerView.ViewHolder{
        CheckBox cb;

        public TaskListViewHolder(@NonNull View itemView) {
            super(itemView);
            cb = itemView.findViewById(R.id.cb_daily_task);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(tasks.get(position));
                }
            });
        }

        public void bind(Map.Entry<String, UserDailyTask> task) {
            cb.setText(task.getValue().task.title);
            cb.setChecked(task.getValue().isFinished);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Map.Entry<String, UserDailyTask> task);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
