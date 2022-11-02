package com.example.sip1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sip1.models.Expense;
import com.example.sip1.ui.home.HomeFragment;

import java.text.ParseException;

public class NuevoGasto extends AppCompatActivity {
    TextView textViewNombre;
    TextView textViewMonto;
    TextView textViewCategoria;
    TextView textViewTipoDeCargo;
    TextView textViewDetalle;
    TextView textViewFechaProximoPago;
    TextView textViewLinkDeCancelacion;
    Button agregarButton;
    Expense expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_gasto);
        configureUI();
        agregarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Valido los datos para crear el Expense


                //Formateo las fechas para poder pasarlas y crear Expense
                SimpleDateFormat formatter = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    formatter = new SimpleDateFormat("dd.MM.yyyy");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        expense = new Expense(textViewNombre.getText().toString(), Double.parseDouble(textViewMonto.getText().toString()),
                                formatter.parse(textViewFechaProximoPago.getText().toString()),
                                    textViewCategoria.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                //Salto a Home y paso el objeto expense
                Intent intent = new Intent(NuevoGasto.this, MainActivity.class);
                intent.putExtra("newExpense", expense);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void configureUI() {
        textViewNombre = (TextView) findViewById(R.id.idTextNombre);
        textViewMonto = (TextView) findViewById(R.id.idTextMonto);
        textViewCategoria = (TextView) findViewById(R.id.idTextCategoria);
        textViewTipoDeCargo = (TextView) findViewById(R.id.idTextTipoDeCargo);
        textViewDetalle = (TextView) findViewById(R.id.idTextDetalle);
        textViewFechaProximoPago = (TextView) findViewById(R.id.idDateFechaProximoPago);
        textViewLinkDeCancelacion = (TextView) findViewById(R.id.idTextLinkDeCancelacion);
        agregarButton = (Button) findViewById(R.id.Agregar_NuevoCargo_button);
    }
}