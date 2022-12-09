package com.example.sip1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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

    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;

    int diasDiferencia;
    Spinner spinnerCategoria;
    private String datos;

    ArrayList<Cargo> cargosList = new ArrayList<>();
    ArrayList SERVICIOS = new ArrayList();

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

                String nombre = actv.getText().toString();
                String monto = textViewMonto.getText().toString();
                String fecha = textViewFechaProximoPago.getText().toString();
                String categoria = spinnerCategoria.getSelectedItem().toString();

                for (Cargo cargo : cargosList) {
                    if (nombre.equals(cargo.nombre)){
                        textViewLinkDeCancelacion.setText(cargo.url);
                        url = cargo.url;
                        categoria = cargo.categoria;
                        desubscripcion = cargo.pasosDesubscripcion;
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
                        Price expensePrice = new Price();
                        expensePrice.setAmount(Integer.parseInt(textViewMonto.getText().toString()));
                        expensePrice.setId(0); // TODO: Cambiar este ID
                        expense = new Expense(actv.getText().toString(),
                                expensePrice,
                                formatter.parse(textViewFechaProximoPago.getText().toString()),
                                spinnerCategoria.getSelectedItem().toString(), Usage.UNKOWN, url, desubscripcion);

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
        //textViewNombre = (TextView) findViewById(R.id.idTextNombre);
        textViewMonto = findViewById(R.id.idTextMonto);
        spinnerCategoria = findViewById(R.id.idSpinnerCategorias);
        textViewTipoDeCargo = findViewById(R.id.idTextTipoDeCargo);
        textViewDetalle = findViewById(R.id.idTextDetalle);
        textViewFechaProximoPago = findViewById(R.id.idDateFechaProximoPago);
        textViewLinkDeCancelacion = findViewById(R.id.idTextLinkDeCancelacion);
        agregarButton = findViewById(R.id.Agregar_NuevoCargo_button);
    }
}