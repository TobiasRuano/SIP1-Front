package com.example.sip1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sip1.models.Expense;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CargoHomeAdapter extends RecyclerView.Adapter<CargoHomeAdapter.CardViewHolder> {

    List<Expense> expenses;
    Context context;

    public CargoHomeAdapter(Context ct, List<Expense> expenses) {
        this.context = ct;
        this.expenses = expenses;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_element_cargo, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Expense currentExpense = expenses.get(position);

        holder.title.setText(currentExpense.getName());
        holder.category.setText(currentExpense.getCategory());
        //holder.amount.setText(currentExpense.getAmount());

        selectCardColor(holder, calculateNextChargDate(currentExpense.getFirstChargeDate()));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showActionAlert("Eliminar", "Estas seguro que deseas eliminar este servicio?");
                return false;
            }

            private void showActionAlert(String titulo, String mensaje) {
                new AlertDialog.Builder(holder.card.getContext())
                        .setTitle(titulo)
                        .setMessage(mensaje)
                        .setPositiveButton("Cancelar", null)
                        .setNegativeButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteService(expenses.get(position));
                            }
                        })
                        .show();
            }

            private void deleteService(Expense expense) {
                // Eliminar el servicio
                System.out.println("Se elimino un elemento de la tabla");
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Se apreto un elemento de la tabla");
            }
        });
    }

    public void setItems(List<Expense> items){
        expenses = items;
        notifyDataSetChanged();
    }

    private Date calculateNextChargDate(Date first) {
        Date today = new Date();

        int dateComparison = today.compareTo(first);
        if (dateComparison == 0) {
            return today;
        } else if (dateComparison > 0) {
            return first;
        } else {
            Date returnDate = first;

            while (dateComparison < 0) {
                GregorianCalendar calendar =new GregorianCalendar();
                calendar.setTime(first);
                calendar.add(Calendar.MONTH , 1);

                returnDate = calendar.getTime();

                dateComparison = today.compareTo(returnDate);
            }

            return returnDate;
        }
    }

    // Aca es donde se cambia el color dependiendo de la cercania de la fecha del cargo
    private void selectCardColor(CardViewHolder holder, Date nextChargeDate) {
        Date today = new Date();
        long days = getDateDiff(nextChargeDate, today, TimeUnit.DAYS);

        if (days <= 5) {
            holder.colorLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.layout_border_red));
        } else if (days < 15) {
            holder.colorLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.layout_border_yellow));
        } else {
            holder.colorLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.layout_border_green));
        }
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{
        CardView card;
        LinearLayout colorLayout;
        TextView title;
        TextView category;
        TextView nextChargeDate;
        TextView amount;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.cv_element_list);
            colorLayout = itemView.findViewById(R.id.list_element_color_layout);
            title = itemView.findViewById(R.id.title_element_list_cargo);
            category = itemView.findViewById(R.id.category_list_element_cargo);
            nextChargeDate = itemView.findViewById(R.id.nextChargeDate_element_list);
            amount = itemView.findViewById(R.id.precio_list_element_cargo);
        }
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
}
