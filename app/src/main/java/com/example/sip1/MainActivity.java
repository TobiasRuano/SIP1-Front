package com.example.sip1;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sip1.models.Expense;
import com.example.sip1.models.Usage;
import com.example.sip1.ui.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sip1.databinding.ActivityMainBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private int daysToNotify;

    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "notificacion";
    private static int NOTIFICACION_ID = 0;

    List<Expense> expenses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.appBarMain.toolbar;
        setSupportActionBar(toolbar);
        //toolbar.setLogo(R.drawable.app_icon_rounded);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.group_10));

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        getDaysToNotify();
        getLessUsage();
    }

    private void getDaysToNotify() {
        Date lastNotificationDate = SaveManager.Shared().getFecha(this);
        if (lastNotificationDate != null) {
            Date currentDate = new Date(System.currentTimeMillis());
            daysToNotify = (int) ((lastNotificationDate.getTime() - currentDate.getTime()));
        }
    }

    private void getLessUsage() {
        if( daysToNotify == 15) { //SOLO CUANDO HAN PASADO 15 DÍAS SE MOSTRARA OTRA NOTIFICACION EN CASO QUE EXISTA UN GASTO CON POCO USO;
            for(int i = 0; i < expenses.size(); i++) {
                Expense expense = expenses.get(i);
                Usage useAmount = expense.getUseAmount();
                if(useAmount.ordinal() == 1) { //SOLO CUANDO SU USO ES BAJO(1 = LOW) SALTA LA NOTIFICACION
                    notifyLowUsage(expense);
                }
            }
        }
    }

    private void notifyLowUsage(Expense expense){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_baseline_notification_important_24);
        builder.setContentTitle("Tenemos una recomendación para vos.");
        builder.setContentText("Notamos que usás poco: " + expense.getName() + ", lo podés cancelar para tener un ahorro en tus gastos del mes" + ".");
        builder.setColor(Color.rgb(100,92,170));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setDefaults(Notification.DEFAULT_SOUND);

        SaveManager.Shared().setFecha(new Date(System.currentTimeMillis()), this); //aqui seteo la fecha actual en saveManager para comparar con la próxima noticación

        NOTIFICACION_ID = (int) (Math.random() * 2147483647);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 0) {
            if (data != null) {
                Expense expense = (Expense) data.getSerializableExtra("newExpense");
            }
        }
    }
}