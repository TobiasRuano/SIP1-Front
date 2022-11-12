package com.example.sip1.ui.home;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
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
import com.example.sip1.databinding.FragmentHomeBinding;
import com.example.sip1.models.Expense;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<Expense> returnedExpenses = readExpenses();
        if (returnedExpenses != null) {
            expenses.addAll(returnedExpenses);
        }

        configureUI();

        this.checkAndShowServicePopup();

        return root;
    }

    private void configureUI() {
        createNewExpenseButton = (Button) binding.getRoot().findViewById(R.id.nuevo_cargo_button);
        recyclerView = (RecyclerView) binding.getRoot().findViewById(R.id.home_cargos_list);
        montoMensualTextView = (TextView) binding.getRoot().findViewById(R.id.valor_monto_mensual);

        //createMockExpenses();

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
    }

    public void addNewExpense(Expense expense) {
        expenses.add(expense);
        saveExpenses(expenses);
        adapter.setItems(expenses);
    }

    private void createMockExpenses() {
        Expense expense1 = new Expense("Netflix", 1550.25, new Date(), "Entretenimiento");
        expenses.add(expense1);

        Date expense2Date = new GregorianCalendar(2022, Calendar.NOVEMBER, 10).getTime();
        Expense expense2 = new Expense("Spotify", 325.0, expense2Date, "Entretenimiento");
        expenses.add(expense2);

        Date expense3Date = new GregorianCalendar(2022, Calendar.NOVEMBER, 30).getTime();
        Expense expense3 = new Expense("Disney+", 356.0, expense3Date, "Entretenimiento");
        expenses.add(expense3);

        Date expense4Date = new GregorianCalendar(2022, Calendar.OCTOBER, 31).getTime();
        Expense expense4 = new Expense("Paramount+", 750.0, expense4Date, "Entretenimiento");
        expenses.add(expense4);
    }

    private Double calculateMonthlyAmount(List<Expense> expenses) {
        Double total = 0.0;

        for (int i = 0; i < expenses.size(); i ++) {
            total += expenses.get(i).getAmount();
        }

        return total;
    }

    public Boolean saveExpenses(List<Expense> expenses) {//saves expenses into fileName (data.bin)
        ObjectOutputStream oos = null;
        try {
            File file = new File(getActivity().getFilesDir().toString(), "data.bin");
            file.createNewFile();
            FileOutputStream fos = getActivity().openFileOutput("data.bin", getActivity().MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(expenses);
            oos.close();
            fos.close();
            return true;
        } catch (FileNotFoundException err) {
            Toast.makeText(getActivity(), "Something went wrong while saving", Toast.LENGTH_SHORT).show();
            return false;
        } catch (Exception abcd) {
            Toast.makeText(getActivity(), "Something went wrong while saving 2.0", Toast.LENGTH_SHORT).show();
            return false;
        }
        finally {//makes sure to close the ObjectOutputStream
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Expense> readExpenses() {//reads expenses[] object from(data.bin) and returns it.
        ObjectInputStream ois = null;
        try {
            File file = new File(getActivity().getFilesDir().toString()+"/"+"data.bin");
            if (file.exists()) {
                ois = new ObjectInputStream(new FileInputStream(file));
                List<Expense> temp = (List<Expense>)ois.readObject();
                ois.close();
                return temp;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkAndShowServicePopup() {
        // TODO: este popup no se debe mostrar siempre!!

        if (expenses.size() != 0) {
            return;
        }

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.service_popup);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button okayButton = dialog.findViewById(R.id.popup_cargo_boton);
        SeekBar seekBar = dialog.findViewById(R.id.popup_cargo_seekbar);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String string = "El valor de la barra es: " + seekBar.getProgress() + "%";
                Toast.makeText(getContext(), string, Toast.LENGTH_LONG).show();
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