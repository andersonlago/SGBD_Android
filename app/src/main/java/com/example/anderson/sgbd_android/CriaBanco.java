package com.example.anderson.sgbd_android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by anderson on 13/11/16.
 */

public class CriaBanco extends SQLiteOpenHelper {
    public static final String TABELA = "local";
    public static final String ID = "id";
    public static final String ENDERECO = "endereco";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    private static final String NOME_BANCO = "sgbd.db";
    private static final int VERSAO = 1;


    public CriaBanco(Context context) {
       super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABELA+" ("
                + ID + " integer primary key autoincrement,"
                + ENDERECO + " text,"
                + LONGITUDE + " text,"
                + LATITUDE + " text"
                +")";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
