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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Intent;

class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private List<Gastos> gastos;
    private ExpenseDao expenseDao;
    private Context context;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

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


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gastos gastoParaEditar = gastos.get(holder.getAdapterPosition());

                Intent intent = new Intent(context, EditExpenseActivity.class);

                // Ponemos el ID del gasto en el Intent
                intent.putExtra("GASTO_ID", gastoParaEditar.getId());

                context.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Obtiene el gasto actual
                Gastos gastoParaEliminar = gastos.get(holder.getAdapterPosition());

                // diálogo de confirmación
                new AlertDialog.Builder(context)
                        .setTitle("Confirmar eliminación")
                        .setMessage("¿Estás seguro de que quieres eliminar este gasto?")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                executor.execute(() -> {
                                    expenseDao.deleteGastos(gastoParaEliminar.getId());
                                    //  Volver al hilo principal para actualizar la UI
                                    new Handler(Looper.getMainLooper()).post(() -> {
                                        // Recargar la lista llamando al método público
                                        if (context instanceof ListActivity) {
                                            ((ListActivity) context).loadExpenses();
                                        }
                                        Toast.makeText(context, "Gasto eliminado", Toast.LENGTH_SHORT).show();
                                    });
                                });
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }
}