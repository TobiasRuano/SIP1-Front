package com.example.sip1;

import static com.example.sip1.R.layout.dropdown_item;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.sip1.models.Cargo;
import com.example.sip1.models.Expense;
import com.example.sip1.models.Price;
import com.example.sip1.models.Usage;

import java.text.ParseException;
import java.util.ArrayList;

public class NuevoGasto extends AppCompatActivity {
    TextView textViewNombre;
    TextView checkEsGastoFijo;
    TextView textViewDetalle;
    TextView textViewFechaProximoPago;
    TextView textViewLinkDeCancelacion;
    Button agregarButton;
    Expense expense;
    AutoCompleteTextView actv;
    AutoCompleteTextView actvMonto;
    String desubscripcion;
    String url;
    String nombre;
    String monto;
    String fecha;
    String categoria;
    Boolean esGastoFijo = false;
    Boolean esMapeado = false;
    Price expensePrice;

    Spinner spinnerCategoria;
    private String datos;


    ArrayList<Cargo> cargosList = new ArrayList<>();
    ArrayList SERVICIOS = new ArrayList();
    ArrayList PRICES = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_gasto);

        Intent intent = getIntent();
        cargosList = (ArrayList<Cargo>) intent.getSerializableExtra("cargos_mapeados");
        SERVICIOS = (ArrayList) intent.getSerializableExtra("cargos_String");

        setContentView(R.layout.activity_nuevo_gasto);
        configureUI();
        setData();
        agregarButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                nombre = actv.getText().toString();
                monto = actvMonto.getText().toString();
                fecha = textViewFechaProximoPago.getText().toString();
                categoria = spinnerCategoria.getSelectedItem().toString();
                url = textViewLinkDeCancelacion.getText().toString();

                if (esMapeado) {
                    for (Cargo cargo : cargosList) {
                        if (nombre.equals(cargo.nombre)) {
                            categoria = cargo.categoria;
                            for (Price price : cargo.precios) {
                                if (Integer.parseInt(monto) == price.getAmount()){
                                    expensePrice = price;
                                }
                            }

                        }
                    }
                } else {
                    expensePrice = new Price();
                    expensePrice.setAmount(Integer.parseInt(actvMonto.getText().toString()));
                    expensePrice.setId(0);
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
                        expense = new Expense(nombre,
                                expensePrice,
                                formatter.parse(textViewFechaProximoPago.getText().toString()),
                                spinnerCategoria.getSelectedItem().toString(),
                                Usage.UNKOWN,
                                url,
                                desubscripcion,
                                esGastoFijo);

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
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item, SERVICIOS);
        actv = findViewById(R.id.idTextNombre);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        //textViewNombre = (TextView) findViewById(R.id.idTextNombre);
        spinnerCategoria = findViewById(R.id.idSpinnerCategorias);
        checkEsGastoFijo = findViewById(R.id.idCheckEsGastoFijo);
        textViewDetalle = findViewById(R.id.idTextDetalle);
        textViewFechaProximoPago = findViewById(R.id.idDateFechaProximoPago);
        textViewLinkDeCancelacion = findViewById(R.id.idTextLinkDeCancelacion);
        agregarButton = findViewById(R.id.Agregar_NuevoCargo_button);
        actvMonto = findViewById(R.id.idTextMonto);


        checkEsGastoFijo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                esGastoFijo = !esGastoFijo;

                if (esGastoFijo) {
                    textViewLinkDeCancelacion.setVisibility(View.GONE);
                } else {
                    textViewLinkDeCancelacion.setVisibility(View.VISIBLE);
                }
            }
        });

        actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for (Cargo cargo : cargosList) {
                    if (charSequence.toString().equals(cargo.nombre)){
                        textViewLinkDeCancelacion.setText(cargo.url);
                        url = cargo.url;
                        categoria = cargo.categoria;
                        desubscripcion = cargo.pasosDesubscripcion;
                        PRICES.clear();
                        for (Price price: cargo.precios){
                            PRICES.add(price.getAmount());
                        }
                        cargarAdapterMonto();
                        esMapeado = true;
                        break;
                    } else {
                        esMapeado = false;
                    }
                }
                if (esMapeado) {
                    checkEsGastoFijo.setVisibility(View.GONE);
                } else {
                    checkEsGastoFijo.setVisibility(View.VISIBLE);
                    textViewLinkDeCancelacion.setText("");
                    url = "";
                    categoria = "";
                    desubscripcion = "";
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                cargarCargosList();
            }
        });
    }

    private void cargarCargosList() {
        for (Cargo cargo : cargosList) {
            if (actv.getText().toString().equals(cargo.nombre)){
                textViewLinkDeCancelacion.setText(cargo.url);
                url = cargo.url;
                categoria = cargo.categoria;
                desubscripcion = cargo.pasosDesubscripcion;
                PRICES.clear();
                for (Price price: cargo.precios){
                    PRICES.add(price.getAmount());
                }
                cargarAdapterMonto();


            }
        }
    }

    private void cargarAdapterMonto() {
        actvMonto.setInputType(InputType.TYPE_NULL);
        ArrayAdapter<String> adapterMonto = new ArrayAdapter<String>(this, dropdown_item, PRICES);
        actvMonto.setAdapter(adapterMonto);
    }
}