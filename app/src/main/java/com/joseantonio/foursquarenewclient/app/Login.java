package com.joseantonio.foursquarenewclient.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.joseantonio.foursquarenewclient.app.rest.Profiles.Profiles;
import com.joseantonio.foursquarenewclient.app.rest.RestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Login extends FragmentActivity {

    private RestClient client;
    private EditText textname;
    private EditText email;
    private dateUsers register;
    private Callback<Profiles>callback;
    private ProgressDialog ringProgressDialog;
    public static final String PREF_SHARED = "datos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inicializamos el callback:
        initPlacesCallback();

        textname=(EditText)findViewById(R.id.txtnombre);
        email=(EditText)findViewById(R.id.txtmail);

        //Instanciamos el cliente:
        client=new RestClient();

        //Link textView:
        TextView register=(TextView)findViewById(R.id.txtLink);
        register.setMovementMethod(LinkMovementMethod.getInstance());
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Uri address= Uri.parse("https://es.foursquare.com/signup/");
                Intent browser= new Intent(Intent.ACTION_VIEW, address);
                startActivity(browser);
            }
        });

        //Button Login de la pantalla de inicio:
        Button login=(Button)findViewById(R.id.btnlogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    testNetWork();
            } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }//fin del oncreate

    public boolean testNetWork() {
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            new BackgroundTask().execute();//Lanzamos un asyntask
        }else{
            Toast.makeText(getApplicationContext(), "El servicio no responde.Compruebe su conexión", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    //Se podría hacer también con asystask el tema del progress dialog,pero simplemente vamos a mostrar un progress dialog
    //ya que el mismo callback es el que devuelve:
    private class BackgroundTask extends AsyncTask<Void, Void, Void> {

        final String nombre= String.valueOf(textname.getText());
        final String correo= String.valueOf(email.getText());

        @Override
        protected void onPreExecute() {
            ringProgressDialog = ProgressDialog.show(Login.this, "Cargando ...", "Por favor espere ...", true);
            ringProgressDialog.setCancelable(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //Petición a la API de foursquare:
                Thread.sleep(1000);
                client.explore_users(nombre,correo,"LQVS4B2VQ0QKP0KBP0R4SMG3EJ5CW0ARESTEE4Y4X1Z25F21","20140530",callback);
            } catch (Exception e) {}

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (ringProgressDialog.isShowing()) {
                try {
                    Thread.sleep(500);
                    ringProgressDialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Inicializamos el callback:
    public void initPlacesCallback() {
        callback=new Callback<Profiles>() {
            @Override
            public void success(final Profiles profiles, Response response) {
                ringProgressDialog.hide();
                 try{
                      Log.d("Login", "Entra en el success");
                      Log.d("Login",profiles.getResponse().getResults().get(0).getLastName());
                      Log.d("Login",profiles.getResponse().getResults().get(0).getFirstName());
                      Log.d("Login","Result "+String.valueOf(profiles.getResponse().getResults().get(0).getContact().getEmail()));


                     if(profiles.getResponse().getResults().get(0).getContact().getEmail()==null && profiles.getResponse().getResults().get(0).getFirstName().equals(textname.getText().toString()))
                     {
                         intent(profiles);
                     }

                     if(profiles.getResponse().getResults().get(0).getFirstName().equals(textname.getText().toString()) && profiles.getResponse().getResults().get(0).getContact().getEmail().equals(email.getText().toString()))
                     {
                         intent(profiles);
                     }else{
                        error();
                     }


                 }catch(Exception e){
                     ringProgressDialog.hide();
                 }


                //Si el tamaño de los resultados es 0:
                if(profiles.getResponse().getResults().size()==0)
                {
                    Log.d("Login","El tamaño devuelto es 0,no existe ninguno con ese nombre o email");
                    ringProgressDialog.hide();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    DialogoError dialogo = new DialogoError();
                    dialogo.show(fragmentManager, "tagAlerta");
                }

            }

            @Override
            public void failure(RetrofitError error) {
                ringProgressDialog.hide();
                if(error != null)
                {
                    error();

                    Log.d("Login","Pasa por el failure del catch");

                    Log.d("retrofit","Fallo en el callback");
                    Log.d("retrofit",""+error.getLocalizedMessage());
                    Log.d("retrofit",""+error.getResponse().getStatus());
                    Log.d("retrofit",""+error.getResponse().getReason());
                    Log.d("retrofit",""+error.getResponse().getBody());
                    Log.d("retrofit",""+error.getResponse().getUrl());
                }

                if(error.getResponse().getStatus()==403)
                {
                    Log.d("Login","Se ha excedido de peticiones");

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    DialogoRate dialogo = new DialogoRate();
                    dialogo.show(fragmentManager, "tagAlerta");

                    //A falta de cambiar lo de cuota de peticiones al sitio correcto,que sería arriba lo ponemos aquí
                    //para testear la app y terminarla:
                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);
                }

                if(error==null)
                {
                    Toast.makeText(getApplicationContext(), "Datos no correctos", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    //Intent que pasa los datos y salta de actividad:
    public void intent(Profiles profiles)
    {
        Log.d("retrofit","Nombre devuelto "+profiles.getResponse().getResults().get(0).getFirstName());
        Log.d("retrofit","Tamaño de la lista de Result: "+profiles.getResponse().getResults().size());

        //Creamos el archivo de configuración:
        SharedPreferences preferences = getSharedPreferences(PREF_SHARED, Context.MODE_PRIVATE);

        String name=profiles.getResponse().getResults().get(0).getFirstName();
        String lastname=profiles.getResponse().getResults().get(0).getLastName();
        String gender=profiles.getResponse().getResults().get(0).getGender();
        String city=profiles.getResponse().getResults().get(0).getHomeCity();
        String mail=email.getText().toString();
        String prefijo=profiles.getResponse().getResults().get(0).getPhoto().getPrefix();
        String sufijo=profiles.getResponse().getResults().get(0).getPhoto().getSuffix();
        String photo="si";
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("firstName",name);
        editor.putString("LastName",lastname);
        editor.putString("Gender",gender);
        editor.putString("city",city);
        editor.putString("email",mail);
        editor.putString("prefijo",prefijo);
        editor.putString("sufijo",sufijo);
        editor.putString("photo",photo);
        editor.commit();

        Intent i = new Intent(Login.this, MainActivity.class);//Lanzamos a la MainActivity
        i.putExtra("foto",profiles);
        startActivity(i);
        finish();

        //Moficamos los datos de registro:
        register=new dateUsers(getApplicationContext());
        Users user=new Users();
        register.modificarCONTACTOno("si");//Establecemos que estamos logueados
        user=register.recuperarCONTACTO();
        Log.d("Login","Login en activity "+user.getLogin());
    }

    //Mensaje de error:
    public void error()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogoError dialogo = new DialogoError();
        dialogo.show(fragmentManager, "tagAlerta");
    }
}
