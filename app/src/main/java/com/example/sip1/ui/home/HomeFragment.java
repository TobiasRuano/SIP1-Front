package com.example.sip1.ui.home;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sip1.FlitrosDeGastos;
import com.example.sip1.NuevoGasto;
import com.example.sip1.R;
import com.example.sip1.SaveManager;
import com.example.sip1.databinding.FragmentHomeBinding;
import com.example.sip1.models.Expense;
import com.example.sip1.models.Usage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Button createNewExpenseButton;
    Button filtrarButton;
    Button eliminarFiltrosButton;
    RecyclerView recyclerView;
    TextView montoMensualTextView;
    CargoHomeAdapter adapter;

    List<Expense> expenses = new ArrayList<>();

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Expense expense = (Expense) result.getData().getSerializableExtra("newExpense");
                addNewExpense(expense);
            }
        }
    });

    ActivityResultLauncher<Intent> mGetContentFiltro = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Date fechaDesde = (Date) result.getData().getSerializableExtra("fechaDesde");
                Date fechaHasta = (Date) result.getData().getSerializableExtra("fechaHasta");
                String categoria = (String) result.getData().getSerializableExtra("categoria");
                Double montoMaximo = (Double) result.getData().getSerializableExtra("montoMaximo");
                filterExpenses(fechaDesde, fechaHasta, categoria, montoMaximo);
            }
        }
    });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<Expense> returnedExpenses = SaveManager.Shared().readExpenses(getActivity());
        if (returnedExpenses != null) {
            expenses.addAll(returnedExpenses);
        }

        configureUI();

        this.checkAndShowServicePopup();

        return root;
    }

    private void configureUI() {
        createNewExpenseButton = (Button) binding.getRoot().findViewById(R.id.nuevo_cargo_button);
        filtrarButton = (Button) binding.getRoot().findViewById(R.id.filtrar_button);
        eliminarFiltrosButton = (Button) binding.getRoot().findViewById(R.id.eliminar_filtros_button);
        recyclerView = (RecyclerView) binding.getRoot().findViewById(R.id.home_cargos_list);
        montoMensualTextView = (TextView) binding.getRoot().findViewById(R.id.valor_monto_mensual);

        adapter = new CargoHomeAdapter(getContext(), expenses);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        Double totalAmount = calculateMonthlyAmount(expenses);
        String totalAmountString = String.format("$ %.2f", totalAmount);
        montoMensualTextView.setText(totalAmountString);

        createNewExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NuevoGasto.class);

                mGetContent.launch(intent);
            }
        });

        filtrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FlitrosDeGastos.class);

                mGetContentFiltro.launch(intent);
            }
        });

        eliminarFiltrosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.setItems(expenses);
            }
        });
    }

    public void addNewExpense(Expense expense) {
        expenses.add(expense);
        SaveManager.Shared().saveExpenses(expenses, getActivity());
        adapter.setItems(expenses);
        calculateMonthlyAmount(expenses);
    }

    public void filterExpenses(Date fechaDesde, Date fechaHasta, String categoria, Double montoMaximo){

        List<Expense> expensesFiltered = new ArrayList<>();
        for(Expense e: expenses){
            if(e.getNextChargeDate().after(fechaDesde) &&
                    e.getNextChargeDate().before(fechaHasta) &&
                    e.getCategory() == categoria &&
                    e.getAmount() < montoMaximo){
                expensesFiltered.add(e);
            }
        }
        adapter.setItems(expensesFiltered);
    }

    private Double calculateMonthlyAmount(List<Expense> expenses) {
        Double total = 0.0;

        for (int i = 0; i < expenses.size(); i ++) {
            total += expenses.get(i).getAmount();
        }

        return total;
    }

    private void checkAndShowServicePopup() {
        // TODO: este popup no se debe mostrar siempre!!

        if (expenses.size() == 0) {
            return;
        }

        Expense expensePopup = null;
        Integer position = null;
        for (int i = 0; i < expenses.size(); i ++) {
            if (expenses.get(i).getUseAmount() == Usage.UNKOWN) {
                expensePopup = expenses.get(i);
                position = i;
            }
        }

        if (expensePopup == null) {
            return;
        }

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.service_popup);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button okayButton = dialog.findViewById(R.id.popup_cargo_boton);
        SeekBar seekBar = dialog.findViewById(R.id.popup_cargo_seekbar);
        TextView popUpTitle = dialog.findViewById(R.id.popup_cargo_text);

        popUpTitle.setText(expensePopup.getName());

        Expense finalExpensePopup = expensePopup;
        Integer finalPosition = position;
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Integer percentageUseage = seekBar.getProgress();
                String string = "El valor de la barra es: " + percentageUseage + "%";
                Toast.makeText(getContext(), string, Toast.LENGTH_LONG).show();

                if (percentageUseage < 40) {
                    finalExpensePopup.setUseAmount(Usage.LOW);
                } else if (percentageUseage < 60) {
                    finalExpensePopup.setUseAmount(Usage.MEDIUM);
                } else {
                    finalExpensePopup.setUseAmount(Usage.HIGH);
                }

                expenses.remove(finalExpensePopup);
                expenses.add(finalExpensePopup);
                SaveManager.Shared().saveExpenses(expenses, getActivity());
            }
        });

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}