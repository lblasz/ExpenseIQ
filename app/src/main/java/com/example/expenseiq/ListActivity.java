package com.example.expenseiq;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListActivity extends AppCompatActivity {
    private ExpenseDao expenseDao;
    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        expenseDao = new ExpenseDao(this);
        recyclerView = findViewById(R.id.recyclerViewExpenses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadExpenses();
    }

    private void loadExpenses() {
        List<Gastos> expenses = expenseDao.getGastos();
        adapter = new ExpenseAdapter(expenses, expenseDao, this);
        recyclerView.setAdapter(adapter);
    }
}
