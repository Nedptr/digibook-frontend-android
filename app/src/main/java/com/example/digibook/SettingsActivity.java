package com.example.digibook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.digibook.models.User;
import com.example.digibook.utilities.CurrentSession;

public class SettingsActivity extends AppCompatActivity {

    Button update;
    EditText name,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        name = findViewById(R.id.settingsName);
        email = findViewById(R.id.settingsEmail);
        password = findViewById(R.id.settingsPassword);
        update= findViewById(R.id.settingsUpdateButton);

        name.setText(CurrentSession.CurrentUser.getName());
        email.setText(CurrentSession.CurrentUser.getEmail());
        password.setText(CurrentSession.CurrentUser.getPassword());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User updatedUser = new User();

                updatedUser.setPassword(CurrentSession.CurrentUser.getPassword());
                updatedUser.setEmail(CurrentSession.CurrentUser.getEmail());
                updatedUser.setName(CurrentSession.CurrentUser.getName());
                updatedUser.setDate(CurrentSession.CurrentUser.getDate());
                updatedUser.setId(CurrentSession.CurrentUser.getId());
                updatedUser.setPicurl(CurrentSession.CurrentUser.getPicurl());
                updatedUser.setV(CurrentSession.CurrentUser.getV());

                updatedUser.setName(name.getText().toString());
                updatedUser.setEmail(email.getText().toString());
                updatedUser.setPassword(password.getText().toString());
                CurrentSession.updateUser(updatedUser, getApplicationContext(), CurrentSession.CurrentUser.getEmail(), CurrentSession.CurrentUser.getPassword().toString());
            }
        });

    }
}