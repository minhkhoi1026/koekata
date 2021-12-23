package com.example.koekata.ui.main.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koekata.models.UserRepository;
import com.firebase.ui.auth.data.model.User;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {
    private final UserRepository userRepository;

    @Inject
    public HomeViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<Long> getHomeStatusLiveData() {
        return userRepository.getHomeStatusLiveData();
    }

    public void updateHomeStatus(long value) {
        userRepository.updateHomeStatus(value);
    }
}