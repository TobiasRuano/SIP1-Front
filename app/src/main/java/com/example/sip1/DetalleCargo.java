package com.example.sip1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sip1.models.Expense;

public class DetalleCargo extends AppCompatActivity {

    ConstraintLayout colorLayout;
    TextView detalleTitulo;
    TextView detalleCategoria;
    TextView detalleMonto;
    TextView detalleFechaProximoPago;
    Button detalleDarBaja;
    Expense expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cargo);
        uiconfiguration();
        detalleDarBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetalleCargo.this, InstruccionesDeCancelacion.class);
                intent.putExtra("cargo_elemento", expense);
                startActivity(intent);
            }

        });
    }

    private void uiconfiguration() {
        colorLayout = findViewById(R.id.detalle_Background);
        detalleTitulo = findViewById(R.id.txt_detalle_titulo_cargo);
        detalleCategoria = findViewById(R.id.txt_detalle_categoria_cargo);
        detalleMonto = findViewById(R.id.txt_detalle_monto_cargo);
        detalleFechaProximoPago = findViewById(R.id.txt_detalle_fecha_proximo_pago);
        detalleDarBaja = findViewById(R.id.btn_detalle_darBaja);

        Intent intent = getIntent();
        expense = (Expense) intent.getSerializableExtra("cargo_elemento");
        String date = intent.getStringExtra("fechaCargo_elemento");
        String amount = intent.getStringExtra("amount_elemento");
        int backgroundType = intent.getIntExtra("backgroundColor", 0);

        detalleTitulo.setText(expense.getName());
        detalleCategoria.setText(expense.getCategory());
        detalleMonto.setText(amount);
        detalleFechaProximoPago.setText(date);

        if (backgroundType == 0) {
            colorLayout.setBackgroundResource(R.drawable.layout_border_red);
        } else if (backgroundType == 1) {
            colorLayout.setBackgroundResource(R.drawable.layout_border_yellow);
        } else {
            colorLayout.setBackgroundResource(R.drawable.layout_border_green);
        }
    }

    private void dardebaja() {

    }
}