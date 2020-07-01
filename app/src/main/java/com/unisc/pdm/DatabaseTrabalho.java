package com.unisc.pdm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseTrabalho extends SQLiteOpenHelper {
    public DatabaseTrabalho(@Nullable Context context) {
        super(context, "database.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE amostra (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "red TEXT NOT NULL," +
                "green TEXT NOT NULL, " +
                "blue TEXT NOT NULL, " +
                "nm_amostra TEXT NOT NULL); ";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
