package com.example.expenseiq;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditExpenseActivity extends AppCompatActivity {

    private EditText etDescription, etAmount, etCategory, etDate;
    private Button btnSaveChanges;

    private ExpenseDao expenseDao;
    private int gastoId;
    private Gastos gastoActual;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        expenseDao = new ExpenseDao(this);
        // Obtener el ID del gasto que enviamos desde el Adapter
        gastoId = getIntent().getIntExtra("GASTO_ID", -1);
        if (gastoId == -1) {
            Toast.makeText(this, "Error: Gasto no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        etDescription = findViewById(R.id.etEditDescripcion);
        etAmount = findViewById(R.id.etEditCantidad);
        etCategory = findViewById(R.id.etEditCategoria);
        etDate = findViewById(R.id.etEditFecha);
        btnSaveChanges = findViewById(R.id.btnGuardarCambios);
        // Cargar los datos del gasto en segundo plano
        loadGastoData();

        btnSaveChanges.setOnClickListener(v -> {
            saveGastoChanges();
        });
    }

    private void loadGastoData() {
        executor.execute(() -> {
            // Buscamos el gasto en la BD
            gastoActual = expenseDao.getGastoById(gastoId);

            // Volvemos al hilo principal para rellenar el formulario
            mainHandler.post(() -> {
                if (gastoActual != null) {
                    etDescription.setText(gastoActual.getDescripcion());
                    etAmount.setText(String.valueOf(gastoActual.getCantidad()));
                    etCategory.setText(gastoActual.getCategoria());
                    etDate.setText(gastoActual.getFecha());
                }
            });
        });
    }

    private void saveGastoChanges() {
        // Obtenemos los nuevos valores del formulario
        String desc = etDescription.getText().toString();
        String amountStr = etAmount.getText().toString();
        String category = etCategory.getText().toString();
        String date = etDate.getText().toString();

        if (desc.isEmpty() || amountStr.isEmpty() || category.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Por favor rellene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);

            // Actualiza el objeto gastoActual
            gastoActual.setDescripcion(desc);
            gastoActual.setCantidad(amount);
            gastoActual.setCategoria(category);
            gastoActual.setFecha(date);

            // Guarda en la BD en segundo plano
            executor.execute(() -> {
                expenseDao.updateGastos(gastoActual);
                // Vuelve al hilo principal para notificar y cerrar
                mainHandler.post(() -> {
                    Toast.makeText(EditExpenseActivity.this, "Gasto actualizado", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor, ingrese un monto válido", Toast.LENGTH_SHORT).show();
        }
    }
}