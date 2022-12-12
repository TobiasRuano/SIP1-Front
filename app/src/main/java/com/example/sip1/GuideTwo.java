package com.example.sip1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GuideTwo extends AppCompatActivity {
    Button continueButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_two);
        continueButton = findViewById(R.id.bt_guide_two);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideTwo.this, GuideThree.class); //change
                startActivity(intent);
            }
        });
    }
}