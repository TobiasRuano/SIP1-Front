package com.example.sip1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
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
    }

    private void uiconfiguration() {
        colorLayout = (ConstraintLayout) findViewById(R.id.detalle_Background);
        detalleTitulo = (TextView) findViewById(R.id.txt_detalle_titulo_cargo);
        detalleCategoria = (TextView) findViewById(R.id.txt_detalle_categoria_cargo);
        detalleMonto = (TextView) findViewById(R.id.txt_detalle_monto_cargo);
        detalleFechaProximoPago = (TextView) findViewById(R.id.txt_detalle_fecha_proximo_pago);
        detalleDarBaja = (Button) findViewById(R.id.btn_detalle_darBaja);

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