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

import com.example.sip1.CargoHomeAdapter;
import com.example.sip1.R;
import com.example.sip1.databinding.FragmentHomeBinding;
import com.example.sip1.models.Expense;

import java.util.ArrayList;
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

        adapter = new CargoHomeAdapter(getContext(), expenses);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setItems(expenses);

        Double totalAmount = calculateMonthlyAmount(expenses);
        String totalAmountString = String.format("$ %f", totalAmount);
        montoMensualTextView.setText(totalAmountString);
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