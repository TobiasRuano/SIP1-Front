package com.example.sip1;

import static com.example.sip1.R.layout.dropdown_item;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
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
import com.example.sip1.models.RangoVencimiento;
import com.example.sip1.models.Usage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class NuevoGasto extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView textViewMonto;
    TextView checkEsGastoFijo;
    TextView textViewDetalle;
    TextView textViewFechaProximoPago;
    TextView textViewLinkDeCancelacion;
    TextView textViewRangoVencimiento;
    Spinner spinnerRangoVencimiento;
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
    String rangoVencimiento = "";
    Boolean esMapeado = false;

    private final static String CHANNEL_ID = "NOTIFICACION";
    private static int NOTIFICACION_ID = 0;

    private static final String[] paths = {"Dia/s", "Mes", "Año/s"};

    Spinner spinnerCategoria;


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

                String rangoValue = textViewRangoVencimiento.getText().toString();
                nombre = actv.getText().toString();
                monto = actvMonto.getText().toString();
                fecha = textViewFechaProximoPago.getText().toString();
                categoria = spinnerCategoria.getSelectedItem().toString();
                url = textViewLinkDeCancelacion.getText().toString();
                String descripcion = textViewDetalle.getText().toString();

                Price expensePrice = new Price();

                if (esMapeado) {
                    for (Cargo cargo : cargosList) {
                        if (nombre.equals(cargo.nombre)) {
                            categoria = cargo.categoria;
                            for (Price price : cargo.precios) {
                                if (Integer.parseInt(monto) == price.getAmount()){
                                    expensePrice = price;
                                } else {
                                    categoria = "Entretenimiento";
                                    String montoString = actvMonto.getText().toString().replace(" ", "");
                                    expensePrice.setAmount(Integer.parseInt(montoString));
                                    expensePrice.setId(0);
                                }
                            }
                        } else {
                            categoria = "Entretenimiento";
                            String montoString = actvMonto.getText().toString().replace(" ", "");
                            expensePrice.setAmount(Integer.parseInt(montoString));
                            expensePrice.setId(0);
                        }
                    }
                } else {
                    String montoString = actvMonto.getText().toString().replace(" ", "");
                    expensePrice.setAmount(Integer.parseInt(montoString));
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

                if (rangoValue.matches("")) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese el tiempo entre cobros del servicio", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Formateo las fechas para poder pasarlas y crear Expense
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        RangoVencimiento rango = new RangoVencimiento();
                        rango.setRangoVencimiento(rangoVencimiento);
                        rango.setValue(Integer.parseInt(rangoValue));

                        expense = new Expense(nombre,
                                expensePrice,
                                formatter.parse(textViewFechaProximoPago.getText().toString()),
                                spinnerCategoria.getSelectedItem().toString(),
                                Usage.UNKOWN,
                                url,
                                desubscripcion,
                                esGastoFijo,
                                rango,
                                descripcion);

                        //Acá se crea la notificacion a mostrar
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            CompletableFuture.delayedExecutor(2, TimeUnit.MINUTES).execute(() -> {
                                createNotification(expense);
                            });
                        }


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

    private void createNotification(Expense expense){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_baseline_notification_important_24);
        builder.setContentTitle("Un gasto esta próximo a vencer.");
        builder.setContentText("Su gasto: " + expense.getName() + " vencerá el próximo " + expense.getNextChargeDate() + ".");
        builder.setColor(Color.rgb(100,92,170));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NOTIFICACION_ID = (int) (Math.random() * 2147483647);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
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
        spinnerCategoria = findViewById(R.id.idSpinnerCategorias);
        checkEsGastoFijo = findViewById(R.id.idCheckEsGastoFijo);
        textViewDetalle = findViewById(R.id.idTextDetalle);
        textViewFechaProximoPago = findViewById(R.id.idDateFechaProximoPago);
        textViewLinkDeCancelacion = findViewById(R.id.idTextLinkDeCancelacion);
        textViewRangoVencimiento = findViewById(R.id.idtiempoEntreVencimientos);
        spinnerRangoVencimiento = findViewById(R.id.id_RangoTemporalVencimiento);
        agregarButton = findViewById(R.id.Agregar_NuevoCargo_button);
        actvMonto = findViewById(R.id.idTextMonto);


        ArrayAdapter<String>adapterVencimiento = new ArrayAdapter<String>(NuevoGasto.this,
                android.R.layout.simple_spinner_item,paths);

        adapterVencimiento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRangoVencimiento.setAdapter(adapterVencimiento);
        spinnerRangoVencimiento.setOnItemSelectedListener(this);

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
                    actvMonto.setEnabled(true);
                    actvMonto.setText(" ");
                    PRICES.clear();
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
        actvMonto.setEnabled(false);
        ArrayAdapter<String> adapterMonto = new ArrayAdapter<String>(this, dropdown_item, PRICES);
        actvMonto.setAdapter(adapterMonto);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        rangoVencimiento = paths[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}