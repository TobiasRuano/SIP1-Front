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

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FlitrosDeGastos extends AppCompatActivity {

    TextView detalleFechaDesde;
    TextView detalleFechaHasta;
    TextView detalleCategoria;
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

                if (detalleCategoria.toString().matches("")) {
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
                intent.putExtra("categoria", detalleCategoria.toString());
                intent.putExtra("montoMaximo", Double.parseDouble(detalleMontoMaximo.getText().toString()));

                setResult(Activity.RESULT_OK, intent);
                finish();
            }

        });
    }

    private void uiconfiguration() {
        detalleFechaDesde = (TextView) findViewById(R.id.idDateFechaDesde);
        detalleFechaHasta = (TextView) findViewById(R.id.idDateFechaHasta);
        detalleCategoria = (TextView) findViewById(R.id.idTextCategoria);
        detalleMontoMaximo = (TextView) findViewById(R.id.idTextMontoMaximo);
        detalleFiltrar = (Button) findViewById(R.id.Filtrar_button);
    }

}
