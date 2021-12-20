package com.example.koekata.ui.main.Schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koekata.R;
import com.example.koekata.models.UserEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class UserEventAdapter extends RecyclerView.Adapter<UserEventAdapter.EventViewHolder> {
    public interface OnDeleteClickedListener {
        void OnDeleteClickedListener(UserEvent event);
    }

    private final LayoutInflater layoutInflater;
    private final ArrayList<UserEvent> items;
    private OnDeleteClickedListener deleteClickedListener;

    public UserEventAdapter(Context context, ArrayList<UserEvent> items, OnDeleteClickedListener deleteClickedListener) {
        layoutInflater = LayoutInflater.from(context);
        this.items = items;
        this.deleteClickedListener = deleteClickedListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.user_event_recycler_item, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        UserEvent item = items.get(position);
        holder.setTextViewTitle("Title: " + item.title);

        Date date = new Date(item.date);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm", Locale.getDefault());
        holder.setTextViewDate("Date: " + dateFormat.format(date));

        holder.setTextViewDesc("Description: " + item.description);

        holder.getDeleteButton().setOnClickListener(view -> {
            deleteClickedListener.OnDeleteClickedListener(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewTitle;
        private final TextView textViewDate;
        private final TextView textViewDesc;
        private final Button deleteButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title_event);
            textViewDate = itemView.findViewById(R.id.text_view_date_event);
            textViewDesc = itemView.findViewById(R.id.text_view_desc_event);
            deleteButton = itemView.findViewById(R.id.button_delete_event);
        }

        public void setTextViewTitle(String title) {
            this.textViewTitle.setText(title);
        }

        public void setTextViewDate(String date) {
            this.textViewDate.setText(date);
        }

        public void setTextViewDesc(String desc) {
            this.textViewDesc.setText(desc);
        }

        public Button getDeleteButton() {
            return deleteButton;
        }
    }
}
