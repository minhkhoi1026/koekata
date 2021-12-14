package com.example.koekata.ui.auth;

import static com.example.koekata.utils.Constants.BASE_URL;
import static com.example.koekata.utils.Constants.USER_ROOT;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import com.example.koekata.R;
import com.example.koekata.ui.main.MainActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class AuthActivity extends DaggerAppCompatActivity {
    @Inject
    FirebaseAuth firebaseAuth;

    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth.AuthStateListener authStateListener;
    private boolean isUpgrade; // check if user wants to upgrade from anonymous account
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        isUpgrade = getIntent().getBooleanExtra("UPGRADE", false);

        providers = new ArrayList<>();
        providers.add(new AuthUI.IdpConfig.GoogleBuilder().build());
        if (!isUpgrade)
            providers.add(new AuthUI.IdpConfig.AnonymousBuilder().build());

        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null && !isUpgrade) {
                startMainActivity();

            } else {
                launchSignInProviders();
            }
        };
    }

    private void startMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void launchSignInProviders() {
        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .enableAnonymousUsersAutoUpgrade()
                .build();

        signInLauncher.launch(intent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            startMainActivity();

        } else {
            assert response != null;
            assert response.getError() != null;
            if (response.getError().getErrorCode() == ErrorCodes.ANONYMOUS_UPGRADE_MERGE_CONFLICT) {
                showToast("This account has already existed.");
                cleanUpAnonymousUser(response);

            } else {
                showToast("Login error");
            }
        }
    }

    private void cleanUpAnonymousUser(IdpResponse response) {
        FirebaseDatabase
                .getInstance(BASE_URL)
                .getReference()
                .child(USER_ROOT)
                .child(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())
                .removeValue();

        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(v -> signInWithCredential(response));
    }

    private void signInWithCredential(IdpResponse response) {
        AuthCredential nonAnonymousCredential = response.getCredentialForLinking();
        assert nonAnonymousCredential != null;
        firebaseAuth.signInWithCredential(nonAnonymousCredential)
                .addOnSuccessListener(result -> startMainActivity())
                .addOnFailureListener(result -> showToast("Login error"));
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}