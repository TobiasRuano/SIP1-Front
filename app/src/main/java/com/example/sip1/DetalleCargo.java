package com.example.sip1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

public class DetalleCargo extends AppCompatActivity {

    TextView detalleTitulo;
    TextView detalleCategoria;
    TextView detalleMonto;
    TextView detalleFechaProximoPago;
    Button detalleDarBaja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cargo);
        uiconfiguration();
    }

    private void uiconfiguration() {
        detalleTitulo = (TextView) findViewById(R.id.txt_detalle_titulo_cargo);
        detalleCategoria = (TextView) findViewById(R.id.txt_detalle_categoria_cargo);
        detalleMonto = (TextView) findViewById(R.id.txt_detalle_monto_cargo);
        detalleFechaProximoPago = (TextView) findViewById(R.id.txt_detalle_fecha_proximo_pago);
        detalleDarBaja = (Button) findViewById(R.id.btn_detalle_darBaja);

        detalleTitulo.setText("Netflix");
        detalleCategoria.setText("Entretenimineto");
        detalleMonto.setText("$" + " " + "1450");
        detalleFechaProximoPago.setText("23/10/2022");
    }

    private void dardebaja() {

    }
}