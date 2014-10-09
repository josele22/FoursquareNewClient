package com.joseantonio.foursquarenewclient.app.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joseantonio.foursquarenewclient.app.ImageUtils;
import com.joseantonio.foursquarenewclient.app.R;
import com.joseantonio.foursquarenewclient.app.events.DataDownloadedEvent;
import com.joseantonio.foursquarenewclient.app.AplicationClass;
import com.joseantonio.foursquarenewclient.app.rest.dominio.Item;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by josetorres on 13/05/14.
 */
public class MapPlaces extends Fragment {

    private Switch movemap;
    private GoogleMap map;
    private UiSettings settings;
    private View backgroundMap;
    private Target target;
    private DisplayMetrics metrics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        //Sacar la densidad de la pantalla:
        metrics = getResources().getDisplayMetrics();

        Log.d("ConsultOK", "Entra al onCreateView...");

        View v = inflater.inflate(R.layout.map_places, container, false);

        //Creamos el switch:
        movemap=(Switch)v.findViewById(R.id.switchmove);

        //Creamos el View:
        backgroundMap= v.findViewById(R.id.viewBackground);

        Log.d("ConsultOK", "Infla la vista...");


        //Creamos el mapa,haciendo uso de la librería Support(y haciendo referencia en el layout map_places):
        map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.places_map)).getMap();
        settings=map.getUiSettings();//Guardamos en una variable las opciones que tendrá el mapa:
        settings.setScrollGesturesEnabled(false);
        settings.setZoomControlsEnabled(false);
        settings.setZoomGesturesEnabled(false);


        //Switch que activará o desactivará las opciones que le digamos:
        movemap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    Log.d("anim", "Activado!");
                    settings.setZoomControlsEnabled(true);
                    settings.setScrollGesturesEnabled(true);
                    settings.setZoomGesturesEnabled(true);

                    //Animación al view:
                    ValueAnimator colorAnim = ObjectAnimator.ofFloat(backgroundMap, "alpha", 0.4f, 0);//Queremos darle un color de más oscuro a más claro
                    colorAnim.setDuration(500);
                    colorAnim.start();

                }else{
                    Log.d("anim", "Desactivado");
                    settings.setZoomControlsEnabled(false);
                    settings.setScrollGesturesEnabled(false);
                    settings.setZoomGesturesEnabled(false);

                    //Animación al view:
                    ValueAnimator colorAnim = ObjectAnimator.ofFloat(backgroundMap, "alpha", 0, 0.4f);//Color de más claro a más oscuro
                    colorAnim.setDuration(500);
                    colorAnim.start();
                }
            }
        });


         return v;
     }//Fin del oncreateView


    //Aquí recogeremos lo captado por el evento al que está suscrito dicho fragment(En este caso MapPlaces),
    //que serán los ítems,desde los que recogeremos latitud y longitud para pintar los marcadores:
    @Subscribe
    public void onDataDownloadedEvent(final DataDownloadedEvent event) {
        Log.d("MapPlaces", "Salta el evento en el Fragment 2");

        //Limpiamos el mapa:
        map.clear();

        for(Item currentItem : event.getDownloadedVenues())
        {
            target=new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    for(int i=0;i<event.getDownloadedVenues().size();i++)
                    {
                        try {
                            Log.d("MapPlaces", "Categorías" + event.getDownloadedVenues().get(i).getVenue().getCategories().get(0).getName());

                                map.addMarker(new MarkerOptions()
                                        .position(new LatLng(event.getDownloadedVenues().get(i).getVenue().getLocation().getLat(),
                                                event.getDownloadedVenues().get(i).getVenue().getLocation().getLng()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.Draw(bitmap)))
                                        .snippet("Category: " + event.getDownloadedVenues().get(i).getVenue().getCategories().get(0).getName())
                                        .title(event.getDownloadedVenues().get(i).getVenue().getName()));


                        }catch(Exception e){}

                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            //Cargamos los URL de los iconos
            //En cuanto al prefijo y sufijo,hacemos referencia a la resolución del icono:
            String url= currentItem.getVenue().getCategories().get(0).getIcon().getPrefix()+"88"+currentItem.getVenue().getCategories().get(0).getIcon().getSuffix();
            loadBitmap(url);//Cargamos la URL y se la pasamos a Picasso


            Log.d("MapPlacesPicasso","URL recibidas: "+currentItem.getVenue().getCategories().get(0).getIcon().getPrefix()+"64"+currentItem.getVenue().getCategories().get(0).getIcon().getSuffix());

        }


    }//FIN DEL EVENTO


    //Método para mostrar el marcador del lugar en sí en el google maps(oficial):
    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.getExtras()!= null) {
            startActivity(intent);
        }
    }

    //Aqui cargamos en función de la URL que le pasemos para cargar uno u otro icono
    //en el mapa(segundo fragment)y su tamañO:
    private void loadBitmap(String url) {

        if(metrics.density==1.0)//LDPI
        {
            Picasso.with(getActivity()).load(url).resize(30,30).into(target);
        }

        if(metrics.density==1.5)//MDPI
        {
            Picasso.with(getActivity()).load(url).resize(40,40).into(target);
        }

        if(metrics.density==2.0)//XDPI
        {
            Picasso.with(getActivity()).load(url).resize(60,60).into(target);
        }

        if(metrics.density==3.0)//XXDPI
        {
            Picasso.with(getActivity()).load(url).resize(80,80).into(target);
        }


        //Picasso.with(getActivity()).load(url).resize(50,50).into(target);
        Log.d("MapPlacesPicasso","URL recibidas de Picasso "+url);
    }


    //Siempre en el onResume nos encargaremos de registrar el fragment en el bus:
    @Override
    public void onResume() {
        super.onResume();
        AplicationClass.bus.register(this);

    }


    //Siempre en el onDestroy nos encargaremos de dar de baja al fragment en el bus:
    @Override
    public void onDestroy() {
        super.onDestroy();
        AplicationClass.bus.unregister(this);
    }


}
