package com.example.koekata.ui.main.Schedule;

import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koekata.models.UserEvent;
import com.example.koekata.models.UserRepository;
import com.firebase.ui.auth.data.model.User;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class ScheduleViewModel extends ViewModel {
    UserRepository userRepository;

    private final MediatorLiveData<HashMap<String, UserEvent>> eventsLiveData;
    private MediatorLiveData<Date> focusedDateLiveData;
    private final MediatorLiveData<ArrayList<UserEvent>> showingEventsLiveData;
    private final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

    @Inject
    public ScheduleViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.eventsLiveData = userRepository.getEventsLiveData();
        this.focusedDateLiveData = new MediatorLiveData<>();
        this.showingEventsLiveData = new MediatorLiveData<>();
        showingEventsLiveData.addSource(eventsLiveData, events -> {
            updateShowingEventsLiveData(events, focusedDateLiveData.getValue());
        });

        showingEventsLiveData.addSource(focusedDateLiveData, focusedDate -> {
            updateShowingEventsLiveData(eventsLiveData.getValue(), focusedDate);
        });
    }

    private void updateShowingEventsLiveData(HashMap<String, UserEvent> events, Date focusedDate) {
        ArrayList<UserEvent> arrayList = new ArrayList<>();

        if (events != null && focusedDate != null) {
            Log.d("updateShowingEventsLiveData", "Focus Date " +fmt.format(focusedDate));
            String formatedFocusedDate = fmt.format(focusedDate);
            for (UserEvent event: events.values()) {
                Date date = new Date(event.date);
                Log.d("updateShowingEventsLiveData", "Date: " + fmt.format(date));
                if (fmt.format(date).equals(formatedFocusedDate)) {
                    arrayList.add(event);
                    Log.d("updateShowingEventsLiveData", "Added " + fmt.format(date));
                }
            }
        }

        showingEventsLiveData.postValue(arrayList);
    }

    public void setFocusedDateLiveData(int year, int month, int dayOfMonth) {
        LocalDate localDate = LocalDate.of(year, month + 1, dayOfMonth);
        Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        focusedDateLiveData.setValue(date);
    }

    public void setFocusedDateLiveData(long timeMillis) {
        Date date = new Date(timeMillis);
        focusedDateLiveData.setValue(date);
    }

    public MediatorLiveData<ArrayList<UserEvent>> getShowingEventsLiveData() {
        return showingEventsLiveData;
    }

    public MediatorLiveData<HashMap<String, UserEvent>> getEventsLiveData() {
        return eventsLiveData;
    }

    public void addEvent(UserEvent userEvent) {
        userRepository.addEvent(userEvent);
    }

    public void deleteEvent(UserEvent event) {
        HashMap<String, UserEvent> events = eventsLiveData.getValue();
        for (Map.Entry<String, UserEvent> entry: events.entrySet()) {
            if (entry.getValue().equals(event)) {
                userRepository.deleteEvent(entry.getKey());
            }
        }
    }

    public void updateEvent(String id, UserEvent newUserEvent) {
        userRepository.updateEvent(id, newUserEvent);
    }

    public void addEvent(String title, String desc, Long dateTimeMillis) {
        UserEvent newUserEvent = new UserEvent(title, desc, dateTimeMillis);
        userRepository.addEvent(newUserEvent);
    }
}
