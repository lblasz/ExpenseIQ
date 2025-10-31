package com.example.expenseiq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.expenseiq.Gastos;
import com.example.expenseiq.ExpenseDao;

 class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private List<Gastos> gastos;
    private ExpenseDao expenseDao;
    private Context context;

    public ExpenseAdapter(List<Gastos> gastos, ExpenseDao expenseDao, Context context) {
        this.gastos = gastos;
        this.expenseDao = expenseDao;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvCategory, tvAmount, tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvItemFecha);
            tvCategory = itemView.findViewById(R.id.tvItemCategoria);
            tvAmount = itemView.findViewById(R.id.tvItemCantidad);
            tvDescription = itemView.findViewById(R.id.tvItemDescripcion);
        }
    }

    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpenseAdapter.ViewHolder holder, int position) {
        Gastos e = gastos.get(position);
        holder.tvDate.setText(e.getFecha());
        holder.tvCategory.setText(e.getCategoria());
        holder.tvAmount.setText("$" + e.getCantidad());
        holder.tvDescription.setText(e.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }
}