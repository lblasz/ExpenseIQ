package com.example.expenseiq;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import android.widget.TextView;
import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class ListActivity extends AppCompatActivity {
    private ExpenseDao expenseDao;
    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private TextView tvTotalGastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        expenseDao = new ExpenseDao(this);
        tvTotalGastos = findViewById(R.id.tvTotalGastos);
        recyclerView = findViewById(R.id.recyclerViewExpenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // 3. Mueve la carga de datos a onResume()
    // Esto asegura que la lista se actualice si añades un gasto y vuelves
    @Override
    protected void onResume() {
        super.onResume();
        loadExpenses();
    }

    public void loadExpenses() {
        // 4. Ejecuta la lectura en el hilo secundario
        executor.execute(() -> {
            List<Gastos> expenses = expenseDao.getGastos();
            double total = expenseDao.getSumaTotalGastos(); // <-- Llamada al nuevo método


            // Envía ambos resultados al hilo principal
            mainHandler.post(() -> {
                // Actualiza el texto del total
                tvTotalGastos.setText(String.format("Total: $%.2f", total));

                // Actualiza el RecyclerView
                adapter = new ExpenseAdapter(expenses, expenseDao, ListActivity.this);
                recyclerView.setAdapter(adapter);
            });
        });
    }
}