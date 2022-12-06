package com.example.sip1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OnboardingThird extends AppCompatActivity {
    TextView textView;
    Button continueButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_third);
        configureUI();
        continueButton = findViewById(R.id.start_onboarding_to_app);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveManager.Shared().saveOnboardingStatus(true, OnboardingThird.this);
                Intent intent = new Intent(OnboardingThird.this, MainActivity.class); //change
                startActivity(intent);
            }
        });
    }
    private void configureUI() {
        textView = findViewById(R.id.onboarding_third_text);
        textView.setText("Todo listo!\n\nAhora ya podés empezar a agregar tus gatos recurrentes.");
    }
}