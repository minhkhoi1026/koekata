package com.example.koekata.ui.main.Schedule;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koekata.models.UserEvent;
import com.example.koekata.models.UserRepository;

import java.util.HashMap;

import javax.inject.Inject;

public class ScheduleViewModel extends ViewModel {
    UserRepository userRepository;

    private final MediatorLiveData<HashMap<String, UserEvent>> eventsLiveData;

    @Inject
    public ScheduleViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.eventsLiveData = userRepository.getEventsLiveData();
    }

    public MediatorLiveData<HashMap<String, UserEvent>> getEventsLiveData() {
        return eventsLiveData;
    }

    public void addEvent(UserEvent userEvent) {
        userRepository.addEvent(userEvent);
    }

    public void deleteEvent(String id) {
        userRepository.deleteEvent(id);
    }

    public void updateEvent(String id, UserEvent newUserEvent) {
        userRepository.updateEvent(id, newUserEvent);
    }
}
