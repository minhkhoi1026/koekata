package com.example.koekata;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.koekata.ui.auth.AuthActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity {
    @Inject
    FirebaseAuth firebaseAuth;

    private static final String TAG = "BaseActivity";
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                Log.d(TAG, "Log out");
                navigateLoginScreen(false);
            }
        };
    }

    protected void navigateLoginScreen(boolean upgrade) {
        Intent intent = new Intent(this, AuthActivity.class);
        intent.putExtra("UPGRADE", upgrade);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Remove listener");
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Add listener");
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
