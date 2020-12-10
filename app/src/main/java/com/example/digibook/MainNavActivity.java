package com.example.digibook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainNavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);

        BottomNavigationView mainBotNav = findViewById(R.id.mainBottomNav);
        NavController navController = Navigation.findNavController(this, R.id.mainHostFragment);
        NavigationUI.setupWithNavController(mainBotNav, navController);

    }
}