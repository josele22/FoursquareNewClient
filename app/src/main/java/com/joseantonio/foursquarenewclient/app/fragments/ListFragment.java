package com.joseantonio.foursquarenewclient.app.fragments;

/**
 * Created by josetorres on 13/05/14.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.joseantonio.foursquarenewclient.app.ActivityDetail;
import com.joseantonio.foursquarenewclient.app.R;
import com.joseantonio.foursquarenewclient.app.adapters.AdapterList;
import com.joseantonio.foursquarenewclient.app.events.DataDownloadedEvent;
import com.joseantonio.foursquarenewclient.app.AplicationClass;
import com.joseantonio.foursquarenewclient.app.rest.dominio.Item;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;

//Cada fragment puede estar suscrito al bus de eventos para poder hacer uso de la información que se vaya depositando en el bus

/**
 * A placeholder fragment containing a simple view.
 */
public class ListFragment extends Fragment {

    private AdapterList adapterlist;
    private ArrayList<Item>datos;
    private ListView listViewItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View ListVenueView = inflater.inflate(R.layout.list_fragment, container, false);

        //Instanciamos los datos:
        datos=new ArrayList<Item>();

        //Instanciamos y creamos el ListView:
        listViewItems = (ListView) ListVenueView.findViewById(R.id.listViewListado);

        //Adaptador de la lista(a la que le pasaremos el listViewItems y los datos para mostrarlos):
        adapterlist = new AdapterList(getActivity(), R.id.listViewListado, datos);//Aquí meteremos los datos actualizados.
        listViewItems.setAdapter(adapterlist);//Pasamos el listview al adapter

        //Establezco el escuchador en el listview para captar la posición según pulse un ítem u otro:
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //Lanzamos un intent,para lanzar una segunda actividad:
                Log.d("ConsultOK","Position "+adapterlist.getItems().get(position));

                Intent listActivity = new Intent(getActivity(), ActivityDetail.class);
                listActivity.putExtra("com.joseantonio.foursquarenewclient.app.rest.dominio.Item", adapterlist.getItem(position));
                startActivity(listActivity);
            }
        });

        return ListVenueView;
    }//Fin del oncreate


    //Aquí ejecutaremos el pintado correspondiente de la lista de Venues,es decir,
    //lo que queremos que haga cuando le llegue el evento correspondiente al que está suscrito(En este caso ListFragment):
    @Subscribe
    public void onDataDownloadedEvent(final DataDownloadedEvent event) {
        Log.d("retrofit", "Salta el evento en el Fragment 1");

        adapterlist = new AdapterList(getActivity(), R.layout.listadapter, event.getDownloadedVenues());
        listViewItems.setAdapter(adapterlist);
        adapterlist.notifyDataSetChanged();

        for(int i =0;i<event.getDownloadedVenues().size();i++)
        {
            Log.d("retrofit", "Tamaño de lo contenido en el evento: " + ""+ event.getDownloadedVenues().size());
            Log.d("retrofit", "Nombres contenidos: " + event.getDownloadedVenues().get(i).getVenue().getName());
        }

    }

    //Siempre en el onResume nos encargaremos de registrar el fragment en el bus:
    @Override
    public void onResume() {
        super.onResume();
        Log.d("retrofit", "ListFragment se registra en el evento");
        AplicationClass.bus.register(this);
    }


    //Siempre en el onDestroy nos encargaremos de dar de baja al fragment en el bus:
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("retrofit", "ListFragment se desregistra del evento");
        AplicationClass.bus.unregister(this);
    }

}
