package com.joseantonio.foursquarenewclient.app.adapters;

/**
 * Created by josetorres on 22/05/14.
 */
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
import com.joseantonio.foursquarenewclient.app.rest.tipsdominio.Item_;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by josetorres on 13/05/14.
 */
public class AdapterComments extends ArrayAdapter<Item_>{

    private Activity context;
    private int layoutResourceId;
    private List<Item_>items;
    private TextView nocomments;
    private Typeface title;
    private Typeface text;
    private Typeface name;


    public AdapterComments(Activity context, int resource, List<Item_>items) {
        super(context, resource, items);

        this.context=context;
        this.layoutResourceId=resource;
        this.items=items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View item = inflater.inflate(layoutResourceId, null);

        title = Typeface.createFromAsset(context.getAssets(), "font/Roboto/Roboto-Light.ttf");//Título
        text = Typeface.createFromAsset(context.getAssets(), "font/Roboto/Roboto-Thin.ttf");//Descripción
        name = Typeface.createFromAsset(context.getAssets(), "font/Roboto/Roboto-Regular.ttf");//Descripción

        Log.d("RetrofitPosition", "Ha entrado en el getView...");

        //Imagen de fondo de cada persona que hace comentarios(la cargamos con Picasso):
        ImageView imageBackground = (ImageView) item.findViewById(R.id.imageViewDetail);
        if(items.get(position).getUser().getPhoto().equals(null))
        {
            imageBackground.setImageResource(R.drawable.no_photo);
        }else{
            Picasso.with(context)
                    .load(items.get(position).getUser().getPhoto().getPrefix() + "original" + items.get(position).getUser().getPhoto().getSuffix())
                    .resize(180,180)
                    .into(imageBackground);

        }

        //Nombre y apellido del usuario que escribe comentarios:
        TextView lblDescription2 = (TextView) item.findViewById(R.id.txtname2);
        if(items.get(position).getUser().getFirstName()!=null ||items.get(position).getUser().getLastName()!=null)
        {
            lblDescription2.setText(items.get(position).getUser().getFirstName() + "," + items.get(position).getUser().getLastName());
            lblDescription2.setTypeface(text);
        }

        //El comentario de la persona:
        TextView textView3 = (TextView) item.findViewById(R.id.txtcomment);
        textView3.setText(items.get(position).getText());
        textView3.setTypeface(title);


        return (item);
    }

}
