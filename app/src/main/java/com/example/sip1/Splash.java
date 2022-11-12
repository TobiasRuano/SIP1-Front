package com.example.sip1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkOnboardingShowed();
    }

    private void checkOnboardingShowed() {
        Boolean onboardinghasBeenShown = SaveManager.Shared().readOnboardingStatus(this);

        if ((onboardinghasBeenShown != null) && onboardinghasBeenShown) {
            Intent intent = new Intent(this, MainActivity.class);

            this.startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivityOnboarding.class);
            this.startActivity(intent);
        }
    }
}