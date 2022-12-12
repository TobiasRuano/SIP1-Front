package com.example.sip1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FlitrosDeGastos extends AppCompatActivity {

    TextView detalleFechaDesde;
    TextView detalleFechaHasta;
    Spinner spinnerCategoria;
    TextView detalleMontoMaximo;
    Button detalleFiltrar;

    private String datos;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<Cargos> cargosList = new ArrayList<>();
    ArrayList SERVICIOS = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros_de_gastos);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Cargos");
        getdata();

        setContentView(R.layout.activity_filtros_de_gastos);
        uiconfiguration();
        setData();
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
                Toast.makeText(FlitrosDeGastos.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinnerCategoria.setAdapter(adapter);
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

    private void uiconfiguration() {
        detalleFechaDesde = (TextView) findViewById(R.id.idDateFechaDesde);
        detalleFechaHasta = (TextView) findViewById(R.id.idDateFechaHasta);
        spinnerCategoria = (Spinner) findViewById(R.id.idSpinnerCategorias);
        detalleMontoMaximo = (TextView) findViewById(R.id.idTextMontoMaximo);
        detalleFiltrar = (Button) findViewById(R.id.Filtrar_button);
    }

}
