package com.example.koekata.ui.main.DailyTaskList;

import static com.example.koekata.utils.Constants.MILLIS_PER_DAY;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koekata.models.UserRepository;
import com.example.koekata.models.UserTask;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class DailyTaskListViewModel extends ViewModel {
    private final UserRepository userRepository;

    public static class UserDailyTask {
        public UserTask task;
        public boolean isFinished;
    }

    @Inject
    public DailyTaskListViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<HashMap<String, UserTask>> getTasksLiveData() {
        return userRepository.getTasksLiveData();
    }

    public LiveData<HashMap<String, UserDailyTask>> getDailyTasksLiveData() {
        MediatorLiveData<HashMap<String, UserDailyTask>> dailyTasksLiveData = new MediatorLiveData<>();
        dailyTasksLiveData.addSource(userRepository.getTasksLiveData(), tasks -> {
            HashMap<String, UserDailyTask> dailyTasks = new HashMap<>();
            for (Map.Entry<String, UserTask> task: tasks.entrySet()) {
                if (isTodayTask(task.getValue())) {
                    UserDailyTask dailyTask = convertToDailyTask(task.getValue());
                    dailyTasks.put(task.getKey(), dailyTask);
                }
            }
            dailyTasksLiveData.postValue(dailyTasks);
        });
        return dailyTasksLiveData;
    }

    private boolean isTodayTask(UserTask task) {
        long currentDate = roundMillisToDate(System.currentTimeMillis());
        long startDate = roundMillisToDate(task.start);
        if (task.repeat != 0) {
            long deltaDate = currentDate - startDate;
            return (deltaDate % task.repeat == 0);
        }
        return (currentDate == startDate);
    }

    private long roundMillisToDate(long millis) {
        return millis - millis % MILLIS_PER_DAY;
    }

    private UserDailyTask convertToDailyTask(UserTask task) {
        UserDailyTask dailyTask = new UserDailyTask();
        dailyTask.task = task;
        dailyTask.isFinished = isFinished(task);
        return dailyTask;
    }

    private boolean isFinished(UserTask task) {
        long currentDate = roundMillisToDate(System.currentTimeMillis());
        long lastDoneDate = roundMillisToDate(task.lastDone);
        return (currentDate == lastDoneDate);
    }

    public void finishTask(String id, UserTask info) {
        if (!isFinished(info)) {
            updateLastDone(id, info);
            updateTaskHistory();
        }
    }

    private void updateLastDone(String id, UserTask info) {
        UserTask newInfo = new UserTask(info);
        newInfo.lastDone = System.currentTimeMillis();
        userRepository.updateTask(id, newInfo);
    }

    private void updateTaskHistory() {
        Long today = System.currentTimeMillis();
        String currentDate = Long.toString(roundMillisToDate(today));
        userRepository.upTaskHistory(currentDate);
    }

    public void addTask(UserTask userTask) {
        userRepository.addTask(userTask);
    }

    public void deleteTask(String id) {
        userRepository.deleteTask(id);
    }

    public void updateTask(String id, UserTask newUserTask) {
        userRepository.updateTask(id, newUserTask);
    }
}
