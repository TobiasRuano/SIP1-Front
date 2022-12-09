package com.example.sip1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.sip1.models.Expense;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlitrosDeGastos extends AppCompatActivity {

    TextView detalleFechaDesde;
    TextView detalleFechaHasta;
    Spinner spinnerCategoria;
    TextView detalleMontoMaximo;
    Button detalleFiltrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros_de_gastos);
        uiconfiguration();
        detalleFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FlitrosDeGastos.this, MainActivity.class);

                if (detalleMontoMaximo.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese un monto", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (spinnerCategoria.getSelectedItem().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese una categoria", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (detalleFechaDesde.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese una fecha valida", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (detalleFechaHasta.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese una fecha valida", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Formateo las fechas para poder pasarlas y crear filtro
                String fechaDesdeString = detalleFechaDesde.getText().toString() ;
                String fechaHastaString = detalleFechaHasta.getText().toString() ;
                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
                try {
                    intent.putExtra("fechaDesde", simpleDateFormat.parse(fechaDesdeString));
                    intent.putExtra("fechaHasta", simpleDateFormat.parse(fechaHastaString));
                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(), "Por favor verifique la fecha ingresada. Debe tener el siguiente formato: dd/MM/AAAA", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra("categoria", spinnerCategoria.getSelectedItem().toString());
                intent.putExtra("montoMaximo", Double.parseDouble(detalleMontoMaximo.getText().toString()));

                setResult(Activity.RESULT_OK, intent);
                finish();
            }

        });
    }

    private void uiconfiguration() {
        detalleFechaDesde = (TextView) findViewById(R.id.idDateFechaDesde);
        detalleFechaHasta = (TextView) findViewById(R.id.idDateFechaHasta);
        spinnerCategoria = (Spinner) findViewById(R.id.idSpinnerCategorias);
        detalleMontoMaximo = (TextView) findViewById(R.id.idTextMontoMaximo);
        detalleFiltrar = (Button) findViewById(R.id.Filtrar_button);
    }

}
