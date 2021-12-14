package com.example.koekata.di.main;

import static com.example.koekata.utils.Constants.BASE_URL;
import static com.example.koekata.utils.Constants.USER_ROOT;

import com.example.koekata.models.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    private final UserRepository userRepository;
    private final DatabaseReference databaseReference;
    private final FirebaseUser firebaseUser;

    public MainModule() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        databaseReference =  FirebaseDatabase
                .getInstance(BASE_URL)
                .getReference()
                .child(USER_ROOT)
                .child(firebaseUser.getUid());
        userRepository = new UserRepository(databaseReference);
    }

    @Provides
    FirebaseUser provideFirebaseUser() {
        return firebaseUser;
    }

    @Provides
    DatabaseReference provideDefaultDatabaseReference() {
        return databaseReference;
    }

    @Provides
    UserRepository provideUserRepository() {
        return userRepository;
    }
}
