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

import com.example.sip1.NuevoGasto;
import com.example.sip1.R;
import com.example.sip1.SaveManager;
import com.example.sip1.databinding.FragmentHomeBinding;
import com.example.sip1.models.Cargo;
import com.example.sip1.models.Expense;
import com.example.sip1.models.Price;
import com.example.sip1.models.Usage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Button createNewExpenseButton;
    RecyclerView recyclerView;
    TextView montoMensualTextView;
    CargoHomeAdapter adapter;

    List<Expense> expenses = new ArrayList<>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<Cargo> cargosList = new ArrayList<Cargo>();
    ArrayList SERVICIOS = new ArrayList();

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Expense expense = (Expense) result.getData().getSerializableExtra("newExpense");
                addNewExpense(expense);
            }
        }
    });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Cargos");

        List<Expense> returnedExpenses = SaveManager.Shared().readExpenses(getActivity());
        if (returnedExpenses != null) {
            expenses.addAll(returnedExpenses);
        }

        getdata();
        configureUI();

        this.checkAndShowServicePopup();

        return root;
    }

    private void configureUI() {
        createNewExpenseButton = binding.getRoot().findViewById(R.id.nuevo_cargo_button);
        recyclerView = binding.getRoot().findViewById(R.id.home_cargos_list);
        montoMensualTextView = binding.getRoot().findViewById(R.id.valor_monto_mensual);

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
                intent.putExtra("cargos_mapeados", cargosList);
                intent.putExtra("cargos_String", SERVICIOS);

                mGetContent.launch(intent);
            }
        });
    }

    private void getdata() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                cargosList.clear();
                for (DataSnapshot caseSnapshot : snapshot.getChildren()) {
                    System.out.println("Firebase Reading Member2 from "+snapshot.getKey() +", value="+snapshot.getValue());
                    Cargo cases = caseSnapshot.getValue(Cargo.class);
                    System.out.println(cases);
                    cargosList.add(cases);
                    SERVICIOS.add(cases.nombre);
                }

                updateExpensesPrice();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get server data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateExpensesPrice() {
        for(int i = 0; i < expenses.size(); i++) {
            for(int j = 0; j < cargosList.size(); j++) {
                Expense expense = expenses.get(i);
                Cargo servicioMapeado = cargosList.get(j);

                if (Objects.equals(expense.getName(), servicioMapeado.nombre)) {
                    for(int k = 0; k < servicioMapeado.precios.size(); k ++) {
                        Price expensePrice = expense.getAmount();
                        Price servicioPrice = servicioMapeado.precios.get(k);

                        if (expensePrice.getId() == servicioPrice.getId()) {
                            Price newPrice = expensePrice;
                            newPrice.setAmount(servicioPrice.getAmount());
                            expense.setAmount(newPrice);
                            expenses.remove(expense);
                            expenses.add(expense);
                        }
                    }
                }
            }
        }
        SaveManager.Shared().saveExpenses(expenses, getActivity());

        adapter.setItems(expenses);
    }

    public void addNewExpense(Expense expense) {
        expenses.add(expense);
        SaveManager.Shared().saveExpenses(expenses, getActivity());
        adapter.setItems(expenses);
        calculateMonthlyAmount(expenses);
    }

    private Double calculateMonthlyAmount(List<Expense> expenses) {
        Double total = 0.0;

        for (int i = 0; i < expenses.size(); i ++) {
            Price expensePrice = expenses.get(i).getAmount();
            total += expensePrice.getAmount();
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

                if (percentageUseage < 10) {
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