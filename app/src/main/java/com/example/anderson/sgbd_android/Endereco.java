package com.example.anderson.sgbd_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Endereco extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_endereco);
    }



    public void Salvar(View V) {

        BdController crud = new BdController(getBaseContext());
        EditText endereco = (EditText)findViewById(R.id.endereco);
        String enderecoString = endereco.getText().toString();
        boolean resultado;

        resultado = crud.insereDado(enderecoString, "" ,"");

        if (resultado){
            Toast.makeText(getApplicationContext(), "Dados Gravados!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "ERRO AO SALVAR", Toast.LENGTH_LONG).show();
        }
    }


    public void Cancelar(View V) {
        finish();
    }
}
