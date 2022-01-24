package com.example.koekata.ui.main;

import static com.example.koekata.utils.Constants.BASE_URL;
import static com.example.koekata.utils.Constants.CONNECTION_ROOT;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.koekata.BaseActivity;
import com.example.koekata.R;
import com.example.koekata.databinding.ActivityMainBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @Inject
    FirebaseUser firebaseUser;

    @Inject
    DatabaseReference userDatabaseReference;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavigationView navigationView;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
//        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openAddMiniScreenDialog();
//            }
//
//
//        });
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
        navigationView.setItemIconTintList(null);

        setupNavigationHeader();

        // navigationView.setItemTextColor();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, 
                R.id.nav_pomodoro, 
                R.id.nav_wakeup_time, 
                R.id.nav_schedule,
                R.id.nav_dailytasklist)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        handleDisconnection();
    }

    public void setupNavigationHeader() {
        TextView textViewLogOut = navigationView.getHeaderView(0).findViewById(R.id.text_view_logout);
        textViewLogOut.setOnClickListener(view -> onLogOutSelected());
        TextView textViewLinkAccount = navigationView.getHeaderView(0).findViewById(R.id.text_view_link_account);
        textViewLinkAccount.setOnClickListener(view -> onLinkAccountSelected());
        setupNavigationUserInfo();
    }

    private void setupNavigationUserInfo() {
        headerView = navigationView.getHeaderView(0);
        setupNavigationPhoto();
        setupNavigationEmail();
    }

    private void setupNavigationPhoto() {
        ImageView photoView = headerView.findViewById(R.id.image_view_user_avatar);
        if (!firebaseUser.isAnonymous()) {
            Glide.with(this).load(firebaseUser.getPhotoUrl())
                    .fitCenter().into(photoView);
        } else {
            photoView.setImageResource(R.drawable.nav_bar_user);
        }
    }

    private void setupNavigationEmail() {
        TextView emailView = headerView.findViewById(R.id.text_view_username);
        String email;
        if (!firebaseUser.isAnonymous()) {
            email = firebaseUser.getEmail();
        } else {
            email = "Anonymous User";
        }
        emailView.setText(email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void onLinkAccountSelected() {//
        if (firebaseUser.isAnonymous()) {
            navigateLoginScreen(true);

        } else {
            Toast.makeText(
                    this.getBaseContext(),
                    "This account has been linked.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void onLogOutSelected() { //
        if (firebaseUser.isAnonymous()) {
            deleteUser();
        } else {
            signOutUser();
        }
    }

    private void signOutUser() {
        AuthUI.getInstance().signOut(this);
    }//

    private void deleteUser() {//
        userDatabaseReference.removeValue();
        AuthUI.getInstance().delete(this);
    }

    private void handleDisconnection() {
        FirebaseDatabase
                .getInstance(BASE_URL)
                .getReference()
                .child(CONNECTION_ROOT)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean connected = snapshot.getValue(Boolean.class);
                        if (connected == null || !connected) {
                            showDisconnectedDialog();
                        } else {
                            hideDisconnectedDialog();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    private void showDisconnectedDialog() {
        // TODO
    }

    private void hideDisconnectedDialog() {
        // TODO
    }
}
