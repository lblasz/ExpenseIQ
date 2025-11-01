package com.example.expenseiq;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ExpenseIQDB";
    private static final int DB_VERSION = 1;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE Gastos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "descripcion TEXT," +
                "cantidad REAL," +
                "categoria TEXT," +
                "fecha TEXT)";
        db.execSQL(createTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Gastos");
        onCreate(db);
    }
}