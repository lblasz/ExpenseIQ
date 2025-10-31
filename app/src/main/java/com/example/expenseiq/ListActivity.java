package com.example.expenseiq;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        expenseDao = new ExpenseDao(this);
        recyclerView = findViewById(R.id.recyclerViewExpenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // loadExpenses(); // Llama a esto desde onResume
    }

    // 3. Mueve la carga de datos a onResume()
    // Esto asegura que la lista se actualice si añades un gasto y vuelves
    @Override
    protected void onResume() {
        super.onResume();
        loadExpenses();
    }

    private void loadExpenses() {
        // 4. Ejecuta la lectura en el hilo secundario
        executor.execute(() -> {
            List<Gastos> expenses = expenseDao.getGastos();

            // 5. Envía el resultado al hilo principal para actualizar la UI
            mainHandler.post(() -> {
                adapter = new ExpenseAdapter(expenses, expenseDao, ListActivity.this);
                recyclerView.setAdapter(adapter);
            });
        });
    }
}