package com.example.anderson.sgbd_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by anderson on 13/11/16.
 */

public class BdController {

    private SQLiteDatabase db;
    private CriaBanco banco;

    public BdController(Context context) {
        banco = new CriaBanco(context);
    }

    public Boolean insereDado(String endereco, String longitude, String latitude) {
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(CriaBanco.ENDERECO, endereco);
        valores.put(CriaBanco.LONGITUDE, longitude);
        valores.put("latitude", latitude);

        resultado = db.insert("local", null, valores);

        db.close();

        if (resultado == -1)
            return Boolean.FALSE;
        else
            return Boolean.TRUE;

    }

    public Cursor carregaDados() {
        Cursor cursor;
        String[] campos = {"id", "endereco"};
        db = banco.getReadableDatabase();
        cursor = db.query("local", campos, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }


}

