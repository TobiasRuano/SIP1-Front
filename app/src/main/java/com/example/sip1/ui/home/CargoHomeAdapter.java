package com.example.sip1.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sip1.DetalleCargo;
import com.example.sip1.R;
import com.example.sip1.models.Expense;

import java.text.SimpleDateFormat;
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
        String totalAmountString = String.format("$%d", currentExpense.getAmount().getAmount());
        holder.amount.setText(totalAmountString);

        Date fechaVencimiento = calculateNextChargDate(currentExpense.getNextChargeDate());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(fechaVencimiento);
        holder.nextChargeDate.setText(strDate);

        selectCardColor(holder, calculateNextChargDate(currentExpense.getNextChargeDate()));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showActionAlert();
                return false;
            }

            private void showActionAlert() {
                new AlertDialog.Builder(holder.card.getContext())
                        .setTitle("Eliminar")
                        .setMessage("Estas seguro que deseas eliminar este servicio?")
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
                expenses.remove(position);
                notifyDataSetChanged();
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,expenses.size());
                System.out.println("Se elimino un elemento de la tabla");
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), DetalleCargo.class);
                intent.putExtra("cargo_elemento", expenses.get(position));
                intent.putExtra("amount_elemento", holder.amount.getText());
                intent.putExtra("fechaCargo_elemento", holder.nextChargeDate.getText());
                intent.putExtra("backgroundColor", holder.type);

                context.startActivity(intent);
            }
        });
    }

    public void setItems(List<Expense> items){
        expenses = items;
        notifyDataSetChanged();
    }

    private Date calculateNextChargDate(Date first) {
        Date today = new Date();

        int dateComparison = first.compareTo(today);
        if (dateComparison == 0) {
            return today;
        } else if (dateComparison > 0) {
            return first;
        } else {
            Date returnDate = first;

            while (dateComparison < 0) {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(returnDate);
                calendar.add(Calendar.MONTH , 1);

                returnDate = calendar.getTime();

                dateComparison = returnDate.compareTo(today);
            }

            return returnDate;
        }
    }

    // Aca es donde se cambia el color dependiendo de la cercania de la fecha del cargo
    private void selectCardColor(CardViewHolder holder, Date nextChargeDate) {
        Date today = new Date();
        long days = getDateDiff(today, nextChargeDate, TimeUnit.DAYS);

        if (days <= 5) {
            holder.colorLayout.setBackgroundResource(R.drawable.layout_border_red);
            holder.type = 0;
        } else if (days < 15) {
            holder.colorLayout.setBackgroundResource(R.drawable.layout_border_yellow);
            holder.type = 1;
        } else {
            holder.colorLayout.setBackgroundResource(R.drawable.layout_border_green);
            holder.type = 2;
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
        int type;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.list_onboarding_entretenimiento);
            colorLayout = itemView.findViewById(R.id.list_element_color_layout);
            title = itemView.findViewById(R.id.title_element_list_cargo);
            category = itemView.findViewById(R.id.category_list_element_cargo);
            nextChargeDate = itemView.findViewById(R.id.nextChargeDate_element_list);
            amount = itemView.findViewById(R.id.precio_list_element_cargo);

            type = 0;
        }
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
}
