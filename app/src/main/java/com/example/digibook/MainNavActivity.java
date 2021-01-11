package com.example.digibook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.digibook.LoginActivity.FILE_NAME;

public class MainNavActivity extends AppCompatActivity {

    Button Logout;
    Button Quit;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);

        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);



        // toolbar handle
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("                                               ");
        //getSupportActionBar().setIcon(R.drawable.ic_baseline_link_off_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Logout = findViewById(R.id.mainLogout);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("USERNAME"); // or .clear() to remove everything
                editor.remove("PASSWORD");
                editor.apply();
                Intent goMainNavAct = new Intent(MainNavActivity.this, LoginActivity.class);
                startActivity(goMainNavAct);
                finish();
            }
        });

        // find the Logout button
/*        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("USERNAME"); // or .clear() to remove everything
                editor.remove("PASSWORD");
                editor.apply();
                Intent goMainNavAct = new Intent(MainNavActivity.this, LoginActivity.class);
                startActivity(goMainNavAct);
                finish();

            }
        });

        Quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("USERNAME"); // or .clear() to remove everything
                editor.remove("PASSWORD");
                editor.apply();
                finishAffinity();
                // this should quit the app.
            }
        });*/

        BottomNavigationView mainBotNav = findViewById(R.id.mainBottomNav);
        NavController navController = Navigation.findNavController(this, R.id.mainHostFragment);
        NavigationUI.setupWithNavController(mainBotNav, navController);

    }
}