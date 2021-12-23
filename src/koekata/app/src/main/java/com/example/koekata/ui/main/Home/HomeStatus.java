package com.example.koekata.ui.main.Home;

import com.example.koekata.R;

import java.util.ArrayList;
import java.util.List;

public enum HomeStatus {
    Pomodoro(1, R.id.llPomodoro, R.id.cbPomodoro),
    PomodoroStatistic(2, R.id.llPomodoroStatistic, R.id.cbPomodoroStatistic),
    DailyTaskList(4, R.id.llDailyTaskList, R.id.cbDailyTaskList),
    DailyTaskListStatistic(8, R.id.llDailyTaskListStatistic, R.id.cbDailyTaskListStatistic),
    Schedule(16, R.id.llSchedule, R.id.cbSchedule),
    ScheduleStatistic(32, R.id.llScheduleStatistic, R.id.cbScheduleStatistic);

    private final int value;
    private final int layoutId;
    private final int checkboxId;

    HomeStatus(int value, int layoutId, int checkboxId) {
        this.value = value;
        this.layoutId = layoutId;
        this.checkboxId = checkboxId;
    }

    public int getValue() {
        return value;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public int getCheckboxId() {
        return checkboxId;
    }

    static public List<HomeStatus> unpackHomeStatus(Long value) {
        List<HomeStatus> homeStatusList = new ArrayList<>();
        if (value != null) {
            for (HomeStatus homeStatus : values()) {
                if ((value & homeStatus.getValue()) != 0)
                    homeStatusList.add(homeStatus);
            }
        }
        return homeStatusList;
    }

    static public HomeStatus findHomeStatusByCheckboxId(int id) {
        for (HomeStatus homeStatus : values()) {
            if (homeStatus.checkboxId == id)
                return homeStatus;
        }
        return null;
    }
}
