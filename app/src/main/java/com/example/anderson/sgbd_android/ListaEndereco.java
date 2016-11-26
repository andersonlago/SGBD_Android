package com.example.anderson.sgbd_android;

import android.app.ListActivity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ListaEndereco extends ListActivity implements LocationListener {

    public String longitude="";
    public String latitude="";
    //private meulocal m = new meulocal();
    public String localAtual="";
    private LocationManager locationMangaer = null;
    private LocationListener locationListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationMangaer = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 10, this);
        atualizatela();
    }

    public void atualizatela()
    {
        ArrayList<String> atividades  = new ArrayList<String>();

        BdController crud = new BdController(getBaseContext());

        Cursor cursor = crud.carregaDados();


        do
        {
           atividades.add( cursor.getString(0) + " - " + cursor.getString(1) + " - Tempo : " +  CalculaDistancia(cursor.getString(1)) );
        } while  (cursor.moveToNext());


        atividades.add("Atualização: " + Calendar.getInstance().getTime().toString());
        atividades.add("Coordenadas: " + latitude + " - " + longitude);
        atividades.add("End atual: " + localAtual );
        atividades.add("Sair");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, atividades);
        setListAdapter(adapter);
    }

    public String CalculaDistancia(String endereco)
    {

        String s, r ="";
        JSONObject jsonRespRouteDistance = null;
        ArrayList<String> atividades = new ArrayList();
        s = MainActivity.getUrlContents("http://maps.googleapis.com/maps/api/distancematrix/json?origins="+ latitude + ","+ longitude +"&destinations=" + URLEncoder.encode(endereco) + "&mode=driving&language=pt-BR");

        try {
            jsonRespRouteDistance = new JSONObject(s)
                            .getJSONArray("rows")
                            .getJSONObject(0)
                            .getJSONArray ("elements")
                            .getJSONObject(0)
                            .getJSONObject("duration");
            r = jsonRespRouteDistance.get("text").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public void onLocationChanged(Location loc) {

        latitude = String.valueOf(loc.getLatitude());
        longitude =String.valueOf(loc.getLongitude());

        String cityName = null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            if (addresses.size() > 0) {

                localAtual="";

                for (int x = 0; x < addresses.get(0).getMaxAddressLineIndex();x++)
                     {
                         localAtual += " " + addresses.get(0).getAddressLine(x);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        atualizatela();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //Toast.makeText(getApplicationContext(), l.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            default:
                finish();
        }
    }


}
