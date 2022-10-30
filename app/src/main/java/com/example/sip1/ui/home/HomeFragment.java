package com.example.sip1.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sip1.R;
import com.example.sip1.databinding.FragmentHomeBinding;
import com.example.sip1.models.Expense;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Button createNewExpenseButton;
    RecyclerView recyclerView;
    TextView montoMensualTextView;
    CargoHomeAdapter adapter;

    List<Expense> expenses = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        configureUI();

        return root;
    }

    private void configureUI() {
        createNewExpenseButton = (Button) binding.getRoot().findViewById(R.id.nuevo_cargo_button);
        recyclerView = (RecyclerView) binding.getRoot().findViewById(R.id.home_cargos_list);
        montoMensualTextView = (TextView) binding.getRoot().findViewById(R.id.valor_monto_mensual);

        createMockExpenses();

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
                // TODO: Aca presentamos la pantalla de nuevo cargo
            }
        });
    }

    private void createMockExpenses() {
        Expense expense1 = new Expense(1, "Netflix", 1550.25, new Date(), null, "Entretenimiento");
        expenses.add(expense1);

        Date expense2Date = new GregorianCalendar(2022, Calendar.NOVEMBER, 10).getTime();
        Expense expense2 = new Expense(1, "Spotify", 325.0, expense2Date, null, "Entretenimiento");
        expenses.add(expense2);

        Date expense3Date = new GregorianCalendar(2022, Calendar.NOVEMBER, 30).getTime();
        Expense expense3 = new Expense(1, "Disney+", 356.0, expense3Date, null, "Entretenimiento");
        expenses.add(expense3);

        Date expense4Date = new GregorianCalendar(2022, Calendar.OCTOBER, 31).getTime();
        Expense expense4 = new Expense(1, "Paramount+", 750.0, expense4Date, null, "Entretenimiento");
        expenses.add(expense4);
    }

    private Double calculateMonthlyAmount(List<Expense> expenses) {
        Double total = 0.0;

        for (int i = 0; i < expenses.size(); i ++) {
            total += expenses.get(i).getAmount();
        }

        return total;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}