package com.example.expenseiq;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ExpenseDao expenseDao;
    private EditText etDescription, etAmount, etCategory, etDate;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expenseDao = new ExpenseDao(this);

        etDescription = findViewById(R.id.etDescripcion);
        etAmount = findViewById(R.id.etCantidad);
        etCategory = findViewById(R.id.etCategoria);
        etDate = findViewById(R.id.etFecha);

        Button btnAdd = findViewById(R.id.btnAgregar);
        Button btnList = findViewById(R.id.btnList);

        btnAdd.setOnClickListener(v -> {
            String desc = etDescription.getText().toString();
            String amountStr = etAmount.getText().toString();
            String category = etCategory.getText().toString();
            String date = etDate.getText().toString();

            if (desc.isEmpty() || amountStr.isEmpty() || category.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double amount = Double.parseDouble(amountStr);
                Gastos gastos = new Gastos(desc, amount, category, date);
                executor.execute(() -> {
                    long result = expenseDao.insertGastos(gastos);

                    // 3. Muestra el Toast de vuelta en el hilo principal
                    runOnUiThread(() -> {
                        if (result > 0) {
                            Toast.makeText(MainActivity.this, "Gasto agregado", Toast.LENGTH_SHORT).show();
                            // Limpiar campos
                            etDescription.setText("");
                            etAmount.setText("");
                            etCategory.setText("");
                            etDate.setText("");
                        } else {
                            Toast.makeText(MainActivity.this, "Error al agregar el gasto", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Por favor ingresa un monto válido", Toast.LENGTH_SHORT).show();
            }
        });

        btnList.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
        });


    }
}