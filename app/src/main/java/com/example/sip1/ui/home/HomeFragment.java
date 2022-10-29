package com.example.sip1.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    View monthlyExpense;
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
        monthlyExpense = (View) binding.getRoot().findViewById(R.id.monto_mensual_header_view);
        adapter = new CargoHomeAdapter(getContext(), expenses);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setItems(expenses);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}