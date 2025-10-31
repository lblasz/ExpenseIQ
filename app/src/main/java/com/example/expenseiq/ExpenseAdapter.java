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
        // 1. Pones los datos en la vista (esto ya lo tenías)
        Gastos e = gastos.get(position);
        holder.tvDate.setText(e.getFecha());
        holder.tvCategory.setText(e.getCategoria());
        holder.tvAmount.setText("$" + e.getCantidad());
        holder.tvDescription.setText(e.getDescripcion());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gastos gastoParaEditar = gastos.get(holder.getAdapterPosition());

                // Creamos un Intent para abrir la nueva EditExpenseActivity
                Intent intent = new Intent(context, EditExpenseActivity.class);

                // Ponemos el ID del gasto en el Intent
                intent.putExtra("GASTO_ID", gastoParaEditar.getId());

                // Iniciamos la nueva activity
                context.startActivity(intent);
            }
        });
        // 2. PEGA EL CÓDIGO DEL LISTENER AQUÍ
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Obtenemos el gasto actual
                // Usamos holder.getAdapterPosition() para estar seguros
                Gastos gastoParaEliminar = gastos.get(holder.getAdapterPosition());

                // Creamos el diálogo de confirmación
                new AlertDialog.Builder(context)
                        .setTitle("Confirmar eliminación")
                        .setMessage("¿Estás seguro de que quieres eliminar este gasto?")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // 1. Ejecutar en hilo secundario
                                executor.execute(() -> {
                                    // 2. Eliminar de la base de datos
                                    expenseDao.deleteGastos(gastoParaEliminar.getId());

                                    // 3. Volver al hilo principal para actualizar la UI
                                    new Handler(Looper.getMainLooper()).post(() -> {
                                        // 4. Recargar la lista llamando al método público
                                        if (context instanceof ListActivity) {
                                            ((ListActivity) context).loadExpenses();
                                        }
                                        Toast.makeText(context, "Gasto eliminado", Toast.LENGTH_SHORT).show();
                                    });
                                });
                            }
                        })
                        .setNegativeButton("Cancelar", null) // No hace nada si se cancela
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                return true; // Importante: 'true' consume el evento de long click
            }
        });
    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }
}