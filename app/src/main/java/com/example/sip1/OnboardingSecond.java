package com.example.sip1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OnboardingSecond extends AppCompatActivity {
    TextView textView;
    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_second);
        configureUI();
        continueButton = findViewById(R.id.IrALaWeb_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OnboardingSecond.this, OnboardingThird.class); //change
                startActivity(intent);
            }
        });
    }

    private void configureUI() {
        textView = findViewById(R.id.textView1);
        textView.setText("Configura tus montos por categoría");
        textView = findViewById(R.id.categoria_onboarding_0);
        textView.setText("Entretenimiento");
        textView = findViewById(R.id.categoria_onboarding_2);
        textView.setText("Hogar");
        textView = findViewById(R.id.categoria_onboarding_3);
        textView.setText("Salud");
        textView = findViewById(R.id.categoria_onboarding_4);
        textView.setText("Vehiculo");

    }
}