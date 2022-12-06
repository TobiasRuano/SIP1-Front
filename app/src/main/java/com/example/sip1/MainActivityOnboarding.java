package com.example.sip1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivityOnboarding extends AppCompatActivity {
    Button continueButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activy_onboarding);
        continueButton = findViewById(R.id.start_main_onboarding_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityOnboarding.this, OnboardingSecond.class); //change
                startActivity(intent);
            }
        });
    }
}