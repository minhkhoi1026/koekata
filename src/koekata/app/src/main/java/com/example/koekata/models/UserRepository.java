package com.example.koekata.models;

import static com.example.koekata.utils.Constants.*;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class UserRepository {
    private static final String TAG = "UserRepository";

    public MediatorLiveData<Long> getHomeStatusLiveData() {
        return homeStatusLiveData;
    }

    public MediatorLiveData<HashMap<String, Long>> getPomodorosLiveData() {
        return pomodorosLiveData;
    }

    public MediatorLiveData<HashMap<String, Long>> getPomodoroSettingsLiveData() {
        return pomodoroSettingsLiveData;
    }

    public MediatorLiveData<HashMap<String, UserTask>> getTasksLiveData() {
        return tasksLiveData;
    }

    public MediatorLiveData<HashMap<String, Long>> getTaskHistoryLiveData() {
        return taskHistoryLiveData;
    }

    public MediatorLiveData<HashMap<String, UserEvent>> getEventsLiveData() {
        return eventsLiveData;
    }

    private final MediatorLiveData<Long> homeStatusLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<HashMap<String, Long>> pomodorosLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<HashMap<String, Long>> pomodoroSettingsLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<HashMap<String, UserTask>> tasksLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<HashMap<String, Long>> taskHistoryLiveData = new MediatorLiveData<>();
    private final MediatorLiveData<HashMap<String, UserEvent>> eventsLiveData = new MediatorLiveData<>();

    private final DatabaseReference homeStatusRef;
    private final DatabaseReference pomodorosRef;
    private final DatabaseReference pomodoroSettingsRef;
    private final DatabaseReference tasksRef;
    private final DatabaseReference taskHistoryRef;
    private final DatabaseReference eventsRef;

    public void addPomodoro(Long pomodoro) {
        pomodorosRef.push().setValue(pomodoro);
    }

    public void setPomodoroTime(Long study, Long shortRelax, Long longRelax) {
        HashMap<String, Long> settings = new HashMap<>();
        settings.put(STUDY_TIME, study);
        settings.put(SHORT_RELAX_TIME, shortRelax);
        settings.put(LONG_RELAX_TIME, longRelax);
        pomodoroSettingsRef.setValue(settings);
    }

    public void updateHomeStatus(Long homeStatus) {
        homeStatusRef.setValue(homeStatus);
    }

    public void addTask(UserTask userTask) {
        tasksRef.push().setValue(userTask);
    }

    public void deleteTask(String id) {
        tasksRef.child(id).removeValue();
    }

    public void updateTask(String id, UserTask newUserTask) {
        tasksRef.child(id).setValue(newUserTask);
    }

    public void addEvent(UserEvent userEvent) {
        eventsRef.push().setValue(userEvent);
    }

    public void deleteEvent(String id) {
        eventsRef.child(id).removeValue();
    }

    public void updateEvent(String id, UserEvent newUserEvent) {
        eventsRef.child(id).setValue(newUserEvent);
    }

    public void updateTaskHistory(String id, Long newHistory) {
        taskHistoryRef.child(id).setValue(newHistory);
    }

    public UserRepository(DatabaseReference ref) {
        Log.d(TAG, "Repository is starting ...");

        homeStatusRef = ref.child("homeStatus");
        GenericTypeIndicator<Long> homeStatusType = new GenericTypeIndicator<Long>() {};
        bindRefToLiveData(homeStatusLiveData, homeStatusRef, homeStatusType);

        pomodorosRef = ref.child("pomodoros").child("success");
        GenericTypeIndicator<HashMap<String, Long>> pomodorosType = new GenericTypeIndicator<HashMap<String, Long>>() {};
        bindRefToLiveData(pomodorosLiveData, pomodorosRef, pomodorosType);

        pomodoroSettingsRef = ref.child("pomodoros").child("setting");
        GenericTypeIndicator<HashMap<String, Long>> pomodoroSettingsType = new GenericTypeIndicator<HashMap<String, Long>>() {};
        bindRefToLiveData(pomodoroSettingsLiveData, pomodoroSettingsRef, pomodoroSettingsType);
        validatePomodoroSettings();

        tasksRef = ref.child("tasks");
        GenericTypeIndicator<HashMap<String, UserTask>> tasksType = new GenericTypeIndicator<HashMap<String, UserTask>>() {};
        bindRefToLiveData(tasksLiveData, tasksRef, tasksType);

        eventsRef = ref.child("events");
        GenericTypeIndicator<HashMap<String, UserEvent>> eventsType = new GenericTypeIndicator<HashMap<String, UserEvent>>() {};
        bindRefToLiveData(eventsLiveData, eventsRef, eventsType);

        taskHistoryRef = ref.child("taskHistory");
        GenericTypeIndicator<HashMap<String, Long>> taskHistoryType = new GenericTypeIndicator<HashMap<String, Long>>() {};
        bindRefToLiveData(taskHistoryLiveData, taskHistoryRef, taskHistoryType);
    }

    private <T> void bindRefToLiveData(MediatorLiveData<T> mediatorLiveData, DatabaseReference ref,
                                       GenericTypeIndicator<T> typeIndicator) {
        FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(ref);
        mediatorLiveData.addSource(liveData, res -> {
            if (res != null) {
                new Thread(() ->
                        mediatorLiveData.postValue(res.getValue(typeIndicator))).start();;
            }
        });
    }

    private void validatePomodoroSettings() {
        pomodoroSettingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    setPomodoroTime(DEFAULT_STUDY_TIME, DEFAULT_SHORT_RELAX_TIME, DEFAULT_LONG_RELAX_TIME);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}