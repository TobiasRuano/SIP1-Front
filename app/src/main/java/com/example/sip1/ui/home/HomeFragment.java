package com.example.sip1.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sip1.R;
import com.example.sip1.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Button createNewExpenseButton;
    RecyclerView expenseList;
    View monthlyExpense;

    List<String> expenses = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        configureUI();

        return root;
    }

    private void configureUI() {
        createNewExpenseButton = (Button) binding.getRoot().findViewById(R.id.nuevo_cargo_button);
        expenseList = (RecyclerView) binding.getRoot().findViewById(R.id.home_cargos_list);
        monthlyExpense = (View) binding.getRoot().findViewById(R.id.monto_mensual_header_view);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}