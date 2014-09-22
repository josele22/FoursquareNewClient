package com.joseantonio.foursquarenewclient.app.adapters;

//Adaptador para mostrar la lista de lugares en función del ítem del
//navigation drawer que se pulse:

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.joseantonio.foursquarenewclient.app.R;
import com.joseantonio.foursquarenewclient.app.rest.dominio.Item;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by josetorres on 13/05/14.
 */

public class AdapterList extends ArrayAdapter<Item>{

    private Activity context;
    private int layoutResourceId;
    private List<Item>items;
    private Typeface title;
    private Typeface text;
    private Typeface name;

    public AdapterList(Activity context, int layoutResourceId, List<Item> items) {
        super(context, layoutResourceId, items);

            this.context=context;
            this.layoutResourceId=layoutResourceId;
            this.items=items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(layoutResourceId, null);

        title = Typeface.createFromAsset(context.getAssets(), "font/Roboto/Roboto-Light.ttf");//Título
        text = Typeface.createFromAsset(context.getAssets(), "font/Roboto/Roboto-Thin.ttf");//Descripción
        name = Typeface.createFromAsset(context.getAssets(), "font/Roboto/Roboto-Regular.ttf");//Descripción

        Log.d("RetrofitPosition", "Ha entrado en el getView...");

        //Imagen de fondo de cada lugar(la cargamos con Picasso):
        ImageView imageBackground = (ImageView) item.findViewById(R.id.imageView);

        if(items.get(position).getVenue().getPhotos().getCount()==0) {
            imageBackground.setImageResource(R.drawable.background_listview);
        }

        try{
             Picasso.with(context)
                    .load(items.get(position).getVenue().getPhotos().getGroups().get(0).getItems().get(0).getPrefix()+"original"+items.get(position).getVenue().getPhotos().getGroups().get(0).getItems().get(0).getSuffix())
                    .into(imageBackground);
        }catch(Exception e){}

        TextView lblDescription3 = (TextView) item.findViewById(R.id.editText3list);
        if(items.get(position).getVenue().getLocation().getAddress()==null)
        {
             lblDescription3.setText("Not available");
             lblDescription3.setTypeface(text);
        }else{
            lblDescription3.setText(items.get(position).getVenue().getLocation().getAddress());
            lblDescription3.setTypeface(text);
        }

        //Nombre:
        TextView textView1 = (TextView) item.findViewById(R.id.txtnombrelist);
        textView1.setText("Name:");
        textView1.setTypeface(title);

        TextView lblDescription = (TextView) item.findViewById(R.id.editText1list);
        lblDescription.setText(items.get(position).getVenue().getName());
        lblDescription.setTypeface(name);

        //Categoría:
        TextView textView2 = (TextView) item.findViewById(R.id.txtcategorylist);
        textView2.setText("Category:");
        textView2.setTypeface(title);


        TextView lblDescription2 = (TextView) item.findViewById(R.id.editText2list);
        if(items.get(position).getVenue().getCategories().size()==0)
        {
            lblDescription2.setText("Not available");
            lblDescription2.setTypeface(text);
        }else{
            lblDescription2.setText(items.get(position).getVenue().getCategories().get(0).getName());
            lblDescription2.setTypeface(text);
        }

        //Dirección:
        TextView textView3 = (TextView) item.findViewById(R.id.txtdirectionlist);
        textView3.setText("Direction:");
        textView3.setTypeface(title);

        return (item);
    }



}
