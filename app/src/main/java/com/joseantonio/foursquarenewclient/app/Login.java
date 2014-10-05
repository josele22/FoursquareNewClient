package com.joseantonio.foursquarenewclient.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import com.joseantonio.foursquarenewclient.app.rest.Profiles.Profiles;
import com.joseantonio.foursquarenewclient.app.rest.RestClient;
import java.util.Timer;
import java.util.TimerTask;
import retrofit.Callback;

public class Login extends FragmentActivity implements LocationListener  {

    private RestClient client;
    private EditText textname;
    private EditText email;
    private dateUsers register;
    private Callback<Profiles>callback;
    private ProgressDialog ringProgressDialog;
    public static final String PREF_SHARED = "datos";
    private LocationManager locationManager;
    private String lat;
    private String lon;
    //Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 3000;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Clase que permite sacar la latitud y longitud
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        textname=(EditText)findViewById(R.id.txtnombre);
        email=(EditText)findViewById(R.id.txtmail);

        //Instanciamos el cliente:
        client=new RestClient();

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getApplicationContext(),"Activado",Toast.LENGTH_LONG).show();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {

                    // Start the next activity
                    Intent mainIntent = new Intent().setClass(Login.this, MainActivity.class);
                    startActivity(mainIntent);
                    // Close the activity so the user won't able to go back this
                    // activity pressing Back button
                    finish();
                }
            };

            // Simulate a long loading process on application startup.
            Timer timer = new Timer();
            timer.schedule(task, SPLASH_SCREEN_DELAY);

        }else{
            Toast.makeText(getApplicationContext(),"Desactivado",Toast.LENGTH_LONG).show();
        }


        Log.d("ConsultOK","Mostrado latitude y longitude: "+lat+lon);

    }//fin del oncreate


    @Override
    public void onResume() {
        super.onResume();

    }


    public boolean testNetWork() {
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            //new BackgroundTask().execute();//Lanzamos un asyntask
        }else{
            Toast.makeText(getApplicationContext(), "El servicio no responde.Compruebe su conexión", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    //Mensaje de error:
    public void error()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogoError dialogo = new DialogoError();
        dialogo.show(fragmentManager, "tagAlerta");
    }


    @Override
    public void onLocationChanged(Location location) {
        lat= String.valueOf(location.getLatitude());
        lon= String.valueOf(location.getLongitude());

        Log.d("ConsultOK","Localización: "+lat+","+lon);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(getApplicationContext(), "Activado", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
