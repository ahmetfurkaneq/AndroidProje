package com.example.androidproje;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseUser currentUser; // used to store current user of account
    FirebaseAuth mAuth; // Used for firebase authentication
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        logout = findViewById(R.id.logout);

        // To logout from the application
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                // After logging out send user to Login Activity to login again
                sendToLoginActivity();
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        // If saved instance state is null, start with the HomeFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user has already signed in; if not, send user to Login Activity
        if (currentUser == null) {
            sendToLoginActivity();
        }
    }

    private void sendToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private BottomNavigationView.OnItemSelectedListener navListener =
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    int itemId = item.getItemId();

                    if (itemId == R.id.navigation_home) {
                        selectedFragment = new HomeFragment();
                    } else if (itemId == R.id.navigation_search) {
                        selectedFragment = new SearchFragment();
                    } else if (itemId == R.id.navigation_favorites) {
                        selectedFragment = new FavoritesFragment();
                    } else if (itemId == R.id.navigation_account) {
                        selectedFragment = new AccountFragment();
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                        return true;
                    }

                   return false;

                }
            };
}
