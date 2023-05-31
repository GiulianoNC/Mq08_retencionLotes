package com.quantum.mq08.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//Creacion de la base de datos
public class DbHelper extends SQLiteOpenHelper {

    private static final String NOMBRE= "Retencion.db";
    public static final String NOMBRE_TABLA="T_Retencion";
    private static final int NOMBRE_VERSION=1;

    //Constructor para la Base de Datos
    public DbHelper(@Nullable Context context) {
        super(context, NOMBRE, null, NOMBRE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NOMBRE_TABLA + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "deposito TEXT  NOT NULL," +
                "retencion TEXT NOT NULL," +
                "item TEXT  NOT NULL," +
                "lote TEXT NOT NULL," +
                "resultado TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE " + NOMBRE_TABLA);
        onCreate(db);
    }
}
