package com.example.sip1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.sip1.models.Expense;
import com.example.sip1.models.Usage;

import java.text.ParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

public class NuevoGasto extends AppCompatActivity {
    TextView textViewNombre;
    TextView textViewMonto;
    TextView textViewTipoDeCargo;
    TextView textViewDetalle;
    TextView textViewFechaProximoPago;
    TextView textViewLinkDeCancelacion;
    Button agregarButton;
    Expense expense;
    AutoCompleteTextView actv;
    String desubscripcion;
    String url;

    Spinner spinnerCategoria;
    private String datos;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<Cargos> cargosList = new ArrayList<>();
    ArrayList SERVICIOS = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_gasto);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Cargos");
        // calling method
        // for getting data.
        getdata();

        setContentView(R.layout.activity_nuevo_gasto);
        configureUI();
        setData();
        agregarButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                String nombre = actv.getText().toString();
                String monto = textViewMonto.getText().toString();
                String fecha = textViewFechaProximoPago.getText().toString();
                String categoria = spinnerCategoria.getSelectedItem().toString();

                for (Cargos cargo : cargosList) {
                    if (nombre.equals(cargo.getNombre())){
                        textViewLinkDeCancelacion.setText(cargo.getURL());
                        url = cargo.getURL();
                        categoria = cargo.getCategoria();
                        desubscripcion = cargo.getPasosDesubscripcion();


                    }
                }


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
                        expense = new Expense(actv.getText().toString(),
                                Double.parseDouble(textViewMonto.getText().toString()),
                                formatter.parse(textViewFechaProximoPago.getText().toString()),
                                spinnerCategoria.getSelectedItem().toString(), Usage.UNKOWN, url, desubscripcion);

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

    private void getdata() {

        // calling add value event listener method
        // for getting the values from database.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                cargosList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Cargos cargos = postSnapshot.getValue(Cargos.class);
                    cargosList.add(cargos);
                    SERVICIOS.add(cargos.getNombre());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(NuevoGasto.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @IgnoreExtraProperties
    private static class Cargos {
        private String Categoria;
        private String Nombre;
        private String PasosDesubscripcion;
        private String URL;

        public Cargos() {  }

        public Cargos(String Categoria, String Nombre, String PasosDesubscripcion, String URL) {
            this.Categoria = Categoria;
            this.Nombre = Nombre;
            this.PasosDesubscripcion = PasosDesubscripcion;
            this.URL = URL;
        }
        public String getCategoria(){
            return Categoria;
        }

        public String getNombre(){
            return Nombre;
        }

        public String getPasosDesubscripcion() {
            return PasosDesubscripcion;
        }

        public String getURL() {
            return URL;
        }
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item, SERVICIOS);
        actv = (AutoCompleteTextView) findViewById(R.id.idTextNombre);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        //textViewNombre = (TextView) findViewById(R.id.idTextNombre);
        textViewMonto = (TextView) findViewById(R.id.idTextMonto);
        spinnerCategoria = (Spinner) findViewById(R.id.idSpinnerCategorias);
        textViewTipoDeCargo = (TextView) findViewById(R.id.idTextTipoDeCargo);
        textViewDetalle = (TextView) findViewById(R.id.idTextDetalle);
        textViewFechaProximoPago = (TextView) findViewById(R.id.idDateFechaProximoPago);
        textViewLinkDeCancelacion = (TextView) findViewById(R.id.idTextLinkDeCancelacion);
        agregarButton = (Button) findViewById(R.id.Agregar_NuevoCargo_button);
    }
}