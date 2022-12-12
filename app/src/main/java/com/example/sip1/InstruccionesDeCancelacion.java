package com.example.sip1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sip1.models.Expense;

public class InstruccionesDeCancelacion extends AppCompatActivity {

    TextView textViewTitulo;
    TextView textViewInstrucciones;
    Button irALaWebButton;
    Expense expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones_de_cancelacion);
        uiconfiguration();
        irALaWebButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(expense.getUrl()));
                startActivity(browserIntent);
            }
        });
    }

    private void uiconfiguration() {
        textViewTitulo = findViewById(R.id.textViewTitulo);
        textViewInstrucciones = findViewById(R.id.textViewInstrucciones);
        irALaWebButton = findViewById(R.id.IrALaWeb_button);

        Intent intent = getIntent();
        expense = (Expense) intent.getSerializableExtra("cargo_elemento");

        textViewInstrucciones.setText(expense.getPasosDesuscripcion());
    }

}
