package com.joseantonio.foursquarenewclient.app;

/**
 * Created by josetorres on 23/05/14.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class ActivityMapPlace extends FragmentActivity implements OnMapClickListener,OnInfoWindowClickListener {

    private GoogleMap mapa;
    private double latitude;
    private double longitude;
    private String name;
    private String category;
    private Target target;
    private String url;
    private String prefix;
    private String suffix;
    private String address;
    private DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_place);

        //Sacar la densidad de la pantalla:
        metrics = getResources().getDisplayMetrics();

        //Título del actionbar:
        getActionBar().setTitle("Map Place");

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        //Creamos un bundle para pasar la información y recogerla:
        Bundle b = getIntent().getExtras();
        name=b.getString("name");
        prefix=b.getString("prefix");
        suffix=b.getString("suffix");
        latitude = b.getDouble("latitude");//Variable local
        longitude = b.getDouble("longitude");//Variable local
        address=b.getString("address");
        category=b.getString("category");

        Log.d("ConsultOK", "Latitude recibido en el mapa: " + latitude);
        Log.d("ConsultOK","Latitude recibido en el mapa: "+ longitude);
        Log.d("ConsultOK","Prefijo de URL "+ prefix);
        Log.d("ConsultOK","Sufijo de URL "+ suffix);

        //Construimos la URL que nos viene y le damos un tamaño de 64:
        url=prefix+"88"+suffix;

        Log.d("ConsultOK","URL: "+ url);

        //Añadimos el mapa:
        mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.place_map)).getMap();

        //Establecemos un click sobre cada marcador para que nos mande al mapa oficial de google maps:
        mapa.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //Con este "intent" nos saldrá un diálogo que nos permita elegir las distintas apps que puede
                //abrir lo que pido,que en este caso es la localización:

                //Buscaremos el sitio por sus coordenadas y lo etiquetaremos con su nombre:
                showMap(Uri.parse("geo:0,0?q="+latitude+","+longitude+"("+name+")"));

            }
        });

        //Creamos un target que utilizaremos para cargar las imagenes que nos vayan llegando para crear el marcador:
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                //Establecemos un mapa del tipo fragment y le añadimos el marcador correspondiente:
                mapa.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.Draw(bitmap)))
                                .snippet("Category: " + category)
                                .title(name));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d("Errores ", "Error al cargar el bitmap");

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };//Fin del target

        //Cargamos la imagen en Picasso para posteriormente pasarsela al Target:
        loadBitmap(url);

    }//FIN DEL Oncreate

    //Método para mostrar el marcador del lugar en sí en el google maps(oficial):
    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    //Aqui cargamos en función de la URL que le pasemos para cargar uno u otro icono:
    private void loadBitmap(String url) {

        //En función de las densidades de pantalla haremos más grande o más pequeño
        //el bitmap con el icono:

        if(metrics.density==1.0)//LDPI
        {
            Picasso.with(getBaseContext()).load(url).resize(30,30).into(target);
        }

        if(metrics.density==1.5)//MDPI
        {
            Picasso.with(getBaseContext()).load(url).resize(40,40).into(target);
        }

        if(metrics.density==2.0)//XDPI
        {
            Picasso.with(getBaseContext()).load(url).resize(60,60).into(target);
        }

        if(metrics.density==3.0)//XXDPI
        {
            Picasso.with(getBaseContext()).load(url).resize(90,90).into(target);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Estableces la latitude y longitud y el zoom que le queremos dar a la posición en concreto:
        mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16));
    }


    //Acciones que ocurren cuando pulsamos algún ítem del action bar:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //En el caso de pulsar el botón de home,volvemos al padre(atrás):
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                onBackPressed();//Volvemos a la anterior pantalla
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Acciones cuando pulsamos en el mapa:
    @Override
    public void onMapClick(LatLng point) {

    }

}
