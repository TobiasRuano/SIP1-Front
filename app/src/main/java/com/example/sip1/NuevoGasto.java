package com.example.sip1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileReader;

import com.example.sip1.models.Expense;
import com.example.sip1.models.Usage;
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

    Spinner spinnerCategoria = findViewById(R.id.idSpinnerCategorias);
    private String datos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_gasto);
        configureUI();
        setData();
        agregarButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                String nombre = textViewNombre.getText().toString();
                String monto = textViewMonto.getText().toString();
                String fecha = textViewFechaProximoPago.getText().toString();
                String categoria = textViewCategoria.getText().toString();

                if (nombre.matches("")) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese el nombre de un cargo", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (monto.matches("")) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese un monto", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (categoria.matches("")) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese una categoria", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (fecha.matches("")) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese una fecha valida", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Formateo las fechas para poder pasarlas y crear Expense
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        expense = new Expense(textViewNombre.getText().toString(),
                                Double.parseDouble(textViewMonto.getText().toString()),
                                formatter.parse(textViewFechaProximoPago.getText().toString()),
                                textViewCategoria.getText().toString(), Usage.UNKOWN);

                        //Salto a Home y paso el objeto expense
                        goToHome(expense);
                    } catch (ParseException e) {
                        Toast.makeText(getApplicationContext(), "Por favor verifique la fecha ingresada. Debe tener el siguiente formato: dd/MM/AAAA", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
    }

    private void goToHome(Expense expense) {
        Intent intent = new Intent(NuevoGasto.this, MainActivity.class);
        intent.putExtra("newExpense", expense);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void setData() {
        ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(this, R.array.categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinnerCategoria.setAdapter(adapter);
    }

    private void configureUI() {
        textViewNombre = (TextView) findViewById(R.id.idTextNombre);
        textViewMonto = (TextView) findViewById(R.id.idTextMonto);
        /*textViewCategoria = (TextView) findViewById(R.id.idTextCategoria);*/
        spinnerCategoria = (Spinner) findViewById(R.id.idSpinnerCategorias);
        textViewTipoDeCargo = (TextView) findViewById(R.id.idTextTipoDeCargo);
        textViewDetalle = (TextView) findViewById(R.id.idTextDetalle);
        textViewFechaProximoPago = (TextView) findViewById(R.id.idDateFechaProximoPago);
        textViewLinkDeCancelacion = (TextView) findViewById(R.id.idTextLinkDeCancelacion);
        agregarButton = (Button) findViewById(R.id.Agregar_NuevoCargo_button);
    }
}