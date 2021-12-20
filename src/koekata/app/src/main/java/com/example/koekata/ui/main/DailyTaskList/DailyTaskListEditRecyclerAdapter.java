package com.example.koekata.ui.main.DailyTaskList;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koekata.R;
import com.example.koekata.models.UserTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DailyTaskListEditRecyclerAdapter
        extends RecyclerView.Adapter<DailyTaskListEditRecyclerAdapter.TaskListEditViewHolder> {

    private static final String TAG = "DailyTaskListRecyclerAdapter";
    private final List<Map.Entry<String, UserTask>> tasks = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public TaskListEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dailytasklist_task_edit_item, parent, false);
        return new TaskListEditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListEditViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(Map<String, UserTask> taskMaps){
        tasks.clear();
        tasks.addAll(taskMaps.entrySet());
        Log.d(TAG, tasks.size() + "");
        notifyDataSetChanged();
    }

    public class TaskListEditViewHolder extends RecyclerView.ViewHolder{
        Button btn;
        TextView tv;

        public TaskListEditViewHolder(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.btn_task_edit);
            tv = itemView.findViewById(R.id.tv_task_title);

            btn.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(tasks.get(position));
                }
            });
        }

        public void bind(Map.Entry<String, UserTask> task) {
            tv.setText(task.getValue().title);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Map.Entry<String, UserTask> task);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}