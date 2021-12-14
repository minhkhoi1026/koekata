package com.example.koekata.ui.main.WakeupTime;

import androidx.lifecycle.ViewModel;

import com.example.koekata.models.UserRepository;

import javax.inject.Inject;

public class WakeupTimeViewModel extends ViewModel {
    private final UserRepository userRepository;

    @Inject
    public WakeupTimeViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
