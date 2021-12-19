package com.example.koekata.ui.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.koekata.BaseActivity;
import com.example.koekata.R;
import com.example.koekata.databinding.ActivityMainBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @Inject
    FirebaseUser firebaseUser;

    @Inject
    DatabaseReference userDatabaseReference;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

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
        NavigationView navigationView = binding.navView;
        navigationView.setItemIconTintList(null);
        TextView textViewLogOut = navigationView.getHeaderView(0).findViewById(R.id.text_view_logout);
        textViewLogOut.setOnClickListener(view -> onLogOutSelected());
        // navigationView.setItemTextColor();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_pomodoro, R.id.nav_wakeup_time, R.id.nav_schedule)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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

}
