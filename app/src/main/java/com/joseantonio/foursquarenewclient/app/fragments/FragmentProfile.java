package com.joseantonio.foursquarenewclient.app.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joseantonio.foursquarenewclient.app.R;
import com.joseantonio.foursquarenewclient.app.RoundedTransformation;
import com.joseantonio.foursquarenewclient.app.rest.Profiles.Profiles;
import com.squareup.picasso.Picasso;

import static android.view.View.*;


/**
 * Created by josetorres on 13/05/14.
 */
public class FragmentProfile extends Fragment {

    private TextView nombre;
    private TextView apellidos;
    private TextView genero;
    private TextView ciudad;
    private TextView correo;
    private Typeface title;
    private Typeface text;
    private Typeface name;
    private Profiles profile;
    private ImageView imageBackground;

    public static final String PREF_SHARED = "datos";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View ListVenueView = inflater.inflate(R.layout.fragment_profile, container, false);

        title = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto/Roboto-Light.ttf");//Título
        text = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto/Roboto-Thin.ttf");//Descripción
        name = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto/Roboto-Regular.ttf");//Descripción

        try{
            profile =(Profiles)getActivity().getIntent().getSerializableExtra("foto");
            Log.d("Login","Prefijo de foto "+profile.getResponse().getResults().get(0).getPhoto().getPrefix());
            Log.d("Login","Sufijo de foto "+profile.getResponse().getResults().get(0).getPhoto().getSuffix());
        }catch(Exception e){}


        //Imagen de fondo de cada lugar(la cargamos con Picasso):
        imageBackground = (ImageView) ListVenueView.findViewById(R.id.imageProfile);
        //Cuando pulsamos sobre la foto,podremos cambiarla:
        imageBackground.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Uri address= Uri.parse("https://es.foursquare.com/settings/");
                Intent browser= new Intent(Intent.ACTION_VIEW, address);
                startActivity(browser);

                return false;
            }
        });

        nombre=(TextView)ListVenueView.findViewById(R.id.txtFirstName);
        nombre.setTypeface(text);
        apellidos=(TextView)ListVenueView.findViewById(R.id.txtLastName);
        apellidos.setTypeface(text);
        genero=(TextView)ListVenueView.findViewById(R.id.txtGenderProfile);
        genero.setTypeface(text);
        ciudad=(TextView) ListVenueView.findViewById(R.id.txtcityProfile);
        ciudad.setTypeface(text);
        correo=(TextView)ListVenueView.findViewById(R.id.txtemail);
        correo.setTypeface(text);

        //Instanciamos un objeto de la clase SharedPreferences para recuperar los datos:
        SharedPreferences settings=this.getActivity().getSharedPreferences(PREF_SHARED,0);
        nombre.setText(settings.getString("firstName", ""));
        apellidos.setText(settings.getString("LastName", ""));
        genero.setText(settings.getString("Gender", ""));
        ciudad.setText(settings.getString("city", ""));
        correo.setText(settings.getString("email", ""));
        String prefijo=settings.getString("prefijo", "");
        String sufijo=settings.getString("sufijo", "");
        String photo=settings.getString("photo", "");

        if(!photo.equals("si"))
        {
            imageBackground.setImageResource(R.drawable.background_listview);
        }else{
            Picasso.with(getActivity())
                    .load(prefijo+"original"+sufijo)
                    .fit().centerCrop()
                    .transform(new RoundedTransformation(700,0))
                    .into(imageBackground);
        }

        return ListVenueView;
    }


}
