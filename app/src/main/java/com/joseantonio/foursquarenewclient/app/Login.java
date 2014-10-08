package com.joseantonio.foursquarenewclient.app;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


public class Login extends FragmentActivity implements LocationListener  {

    private LocationManager locationManager;
    private String lat;
    private String lon;
    private ProgressBar progress;
    private String provider;
    private TextView cargando;
    private TextView gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Ponemos invisible el progress bar al iniciar:
        progress=(ProgressBar)findViewById(R.id.progressBarFirst);

        //Texto de cargando:
        cargando=(TextView)findViewById(R.id.txtcargando);
        //Texto de Por favor active el GPS:
        gps=(TextView)findViewById(R.id.txtgps);

        //Clase que permite sacar la latitud y longitud
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //Pretender darte la mejor localización:
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        locationManager.requestLocationUpdates(provider, 25000, 0, this);
        Location location = locationManager.getLastKnownLocation(provider);

        //Si la localización no es nula,le pasamos los parámetros de la localización al método
        //para sacar la latitud y longitud:
        if (location != null) {
            onLocationChanged(location);
        }


        //Comprobación de la activación del GPS:
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            progress.setVisibility(View.INVISIBLE);
            gps.setText("Por favor,active el GPS");
            cargando.setText("");
        }else {
            new nextactivity1().execute();
        }


    }//fin del oncreate


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onLocationChanged(Location location) {
        lat= String.valueOf(location.getLatitude());
        lon= String.valueOf(location.getLongitude());

        Log.d("ConsultOK","Localización en LOGIN: "+lat+","+lon);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        new nextactivity1().execute();
    }

    @Override
    public void onProviderDisabled(String s) {
    }



    //Ejecución en segundo plano:
    private class nextactivity1 extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
            cargando.setText("Cargando");
            gps.setText("");
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(5500);
            } catch(InterruptedException e) {}
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            locationManager.removeUpdates(Login.this);//Elimino la actualización

            // Start the next activity
            Intent mainIntent = new Intent().setClass(Login.this, MainActivity.class);
            startActivity(mainIntent);

            // Close the activity so the user won't able to go back this
            // activity pressing Back button
            finish();




            //continua de las últimas posiciones GPS
        }
    }


}
