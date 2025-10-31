package com.example.expenseiq;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class ExpenseDao {
    private DataBaseHelper dbHelper;

    public ExpenseDao(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    public long insertGastos(Gastos gastos) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("descripcion", gastos.getDescripcion());
        values.put("cantidad", gastos.getCantidad());
        values.put("categoria", gastos.getCategoria());
        values.put("fecha", gastos.getFecha());
        return db.insert("Gastos", null, values);
    }

    public List<Gastos> getGastos() {
        List<Gastos> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Gastos ORDER BY fecha DESC", null);
        while (cursor.moveToNext()) {
            list.add(new Gastos(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getString(4)
            ));
        }
        cursor.close();
        return list;
    }

    public int updateGastos(Gastos gastos) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("descripcion", gastos.getDescripcion());
        values.put("cantidad", gastos.getCantidad());
        values.put("categoria", gastos.getCategoria());
        values.put("fecha", gastos.getFecha());
        return db.update("Gastos", values, "id=?", new String[]{String.valueOf(gastos.getId())});
    }

    public int deleteGastos(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("Gastos", "id=?", new String[]{String.valueOf(id)});
    }

    public double getSumaTotalGastos() {
        double total = 0.0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // usa SUM para obtener todos los gastos
        Cursor cursor = db.rawQuery("SELECT SUM(cantidad) FROM Gastos", null);

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0); // El resultado está en la primera columna
        }
        cursor.close();
        return total;
    }
    public Gastos getGastoById(int id) {
        Gastos gasto = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consulta SQL para buscar por ID
        Cursor cursor = db.rawQuery("SELECT * FROM Gastos WHERE id = ?", new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            gasto = new Gastos(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );
        }
        cursor.close();
        return gasto;
    }}
