package com.example.sip1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class InstruccionesDeCancelacion extends AppCompatActivity {

    TextView textViewTitulo;
    TextView textViewInstrucciones;
    Button irALaWebButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones_de_cancelacion);
        uiconfiguration();
        irALaWebButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(InstruccionesDeCancelacion.this, MainActivity.class);
                finish();
            }

        });
    }

    private void uiconfiguration() {
        textViewTitulo = (TextView) findViewById(R.id.textViewTitulo);
        textViewInstrucciones = (TextView) findViewById(R.id.textViewInstrucciones);
        irALaWebButton = (Button) findViewById(R.id.IrALaWeb_button);
    }

}
