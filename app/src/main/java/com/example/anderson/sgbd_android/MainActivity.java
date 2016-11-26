package com.example.anderson.sgbd_android;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class MainActivity extends ListActivity {

    EditText nome, editLocation, cpf;
    Button butao ;

    private LocationManager locationMangaer = null;
    private LocationListener locationListener = null;

    public static String getUrlContents(String theUrl) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(theUrl);

            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }

            bufferedReader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Intent intent;

         switch (position) {
            case 0:
                intent = new Intent(this, Endereco.class);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(this, ListaEndereco.class);
                startActivity(intent);
                break;
            default:
                finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String[] atividades = new String[]{"Cadastrar", "Listar", "Sair"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, atividades);
        setListAdapter(adapter);


        locationMangaer = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


            Class.forName("com.mysql.jdbc.Driver").newInstance();
            //carregarDados();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void teste(View V) {

        String i = "insert into pessoa(nome,cpf) values('" + nome.getText().toString() + "','" + cpf.getText().toString() + "')";
        try {
            Connection con;
            PreparedStatement st;
            con = DriverManager.getConnection("jdbc:mysql://192.168.0.11/batepapo", "batepapo", "batepapo");
            st = con.prepareStatement(i);
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void carregarDados() {
        String i = "select * from local";
        try {
            Connection con;
            PreparedStatement st;

            con = DriverManager.getConnection("jdbc:mysql://177.0.0.207/batepapo", "batepapo", "batepapo");
            st = con.prepareStatement(i);
            ResultSet rs = st.executeQuery();

            ArrayList<String> atividades = new ArrayList();

            while (rs.next()) {

                atividades.add(rs.getString("nomelocal")) ;

                //String s = getUrlContents("http://maps.googleapis.com/maps/api/distancematrix/json?origins="+ rs.getString("latitude") + ","+ rs.getString("longitude")  +"&destinations=Cidade%20Nova,%20Salvador%20-%20BA,%20Brasil&mode=train&language=pt-BR");
                String s = getUrlContents("http://maps.googleapis.com/maps/api/distancematrix/json?origins=rua%20viriato%20lobo,%20santo%20antonio%20de%20jesus-Bahia&destinations=Cidade%20Nova,%20Salvador%20-%20BA,%20Brasil&mode=train&language=pt-BR");
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

            JSONObject jsonRespRouteDistance = new JSONObject(s)
                    .getJSONArray("rows")
                    .getJSONObject(0)
                    .getJSONArray ("elements")
                    .getJSONObject(0)
                    .getJSONObject("duration");

            //Toast.makeText(getApplicationContext(), jsonRespRouteDistance.get("text").toString(), Toast.LENGTH_LONG).show();
            atividades.add(rs.getString("nomelocal") + " - " + jsonRespRouteDistance.get("text").toString());

            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, atividades);
            setListAdapter(adapter);

            //locationListener = new MyLocationListener();

            //if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    return;
            //}
            //locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
        e.printStackTrace();
    }

}




    ///-----

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            //editLocation.setText("");
            //pb.setVisibility(View.INVISIBLE);
            //Toast.makeText(getApplicationContext(),"ANDERSON",Toast.LENGTH_LONG).show();
            Toast.makeText(
                    getBaseContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v(TAG, latitude);

        /*------- To get city name from coordinates -------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    Toast.makeText(getBaseContext(), addresses.get(0).getAddressLine(0) , Toast.LENGTH_LONG).show();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                    + cityName;
           // editLocation.setText(s);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

            Toast.makeText(getApplicationContext(),"ANDERSON status changed",Toast.LENGTH_LONG).show();
        }
    }

}
