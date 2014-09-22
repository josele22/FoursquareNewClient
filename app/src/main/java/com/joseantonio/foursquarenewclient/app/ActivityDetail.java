package com.joseantonio.foursquarenewclient.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.joseantonio.foursquarenewclient.app.adapters.AdapterComments;
import com.joseantonio.foursquarenewclient.app.rest.RestClient;
import com.joseantonio.foursquarenewclient.app.rest.dominio.Item;
import com.joseantonio.foursquarenewclient.app.rest.tipsdominio.Comments;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.UnknownHostException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ActivityDetail extends Activity {

    private ListView listDetail;
    private Typeface title;
    private Typeface text;
    private Typeface nameStyle;
    private ImageView imagen;
    private TextView tip;
    private AdapterComments adapterlist;
    private ProgressBar progress2;
    private TextView placecomment;


    //Lo usado para el callback:
    private Comments datos;
    private Callback<Comments>callback;
    private RestClient client;
    private Item itemSelected;//Hacemos la variable global para poder utilizarla en el resto de métodos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_detail);
        //Título del actionbar:
        getActionBar().setTitle("Detail Place");

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        //Style(Estilo de las fuentes):
        title = Typeface.createFromAsset(getBaseContext().getAssets(), "font/Roboto/Roboto-Light.ttf");//Título
        text = Typeface.createFromAsset(getBaseContext().getAssets(), "font/Roboto/Roboto-Thin.ttf");//Descripción
        nameStyle = Typeface.createFromAsset(getBaseContext().getAssets(), "font/Roboto/Roboto-Regular.ttf");//Descripción

        //Inicializamos el callback de Comments que al principio será de 0:
        initTipsCallback();

        //Inicializamos el client:
        client=new RestClient();

        //Inicializamos el progressBar:
        progress2=(ProgressBar)findViewById(R.id.progressBar2);
        progress2.setVisibility(View.VISIBLE);

        placecomment=(TextView)findViewById(R.id.txtnocomments);
        placecomment.setVisibility(View.INVISIBLE);
        placecomment.setTypeface(text);

        //Inflamos la vista:
        listDetail=(ListView)findViewById(R.id.listViewListDetail);
        View headerView =View.inflate(this, R.layout.detail_header, null);//Inflamos el contenido de la cabecera que contendrá el ListViewDetail(todo menos los tips que son los items de comentarios)
        listDetail.addHeaderView(headerView);//Le asignamos el contenido de la cabecera al listDetail

        //Hacemos un casteo y recogemos lo pasado desde la listFragment a esta actividad(y convertirlo en objecto Item):
        itemSelected = (Item)getIntent().getExtras().getSerializable("com.joseantonio.foursquarenewclient.app.rest.dominio.Item");
        Log.d("ConsultOK", "Item selected name: " + itemSelected.getVenue().getName());
        Log.d("ConsultOK", "Item  ID selected name: " + itemSelected.getVenue().getId());

        //Creamos ListView:
        listDetail = (ListView)findViewById(R.id.listViewListDetail);
        listDetail.setAdapter(adapterlist);

        //Método para devolver los callback de comments(haces la petición):
        try {
            client.explore_tips(itemSelected.getVenue().getId(),"recent","A2JLH23PAQX0WRSKCAAJENRWNRB13GZ5MA5DJYRJNTSBTA0F",
                    "XJRLPT114VH4T0IE1LGF0R44WRRNXKSOIUDIU0UBKHPEFN3S", "20140522", callback);
        } catch (IOException e) {
            e.printStackTrace();
            e.getMessage();
        }

        //Asignamos en función del ítem la información a cada campo(Imagen,nombres,etc...):
        //Imagen:
        imagen=(ImageView)findViewById(R.id.imageDetail);

        if (itemSelected.getVenue().getPhotos().getCount()==0 ||itemSelected.getVenue().getPhotos().getGroups().size()==0) {
            imagen.setImageResource(R.drawable.no_photo);
        } else {
            Picasso.with(getApplication())
                    .load(itemSelected.getVenue().getPhotos().getGroups().get(0).getItems().get(0).getPrefix() + "original" + itemSelected.getVenue().getPhotos().getGroups().get(0).getItems().get(0).getSuffix())
                    .into(imagen);

            Log.d("ConsultOK", "Prefijo del lugar " + itemSelected.getVenue().getPhotos().getGroups().get(0).getItems().get(0).getPrefix());
            Log.d("ConsultOK", "Sufijo del lugar " + itemSelected.getVenue().getPhotos().getGroups().get(0).getItems().get(0).getSuffix());
        }

        //Capturo está excepción,por encontrar de manera rara una foto que posee URL pero me la saca en blanco:
        try {
            if (itemSelected.getVenue().getPhotos().getGroups().get(0).getItems().get(0).getSuffix().equals("/79549104_oV-p-aGSbpQ0wI0gX9A0k3h0RL0nh2IVX10CKkyO-Fk.jpg")) {
                imagen.setImageResource(R.drawable.no_photo);
            }
        }catch(Exception e){}


        /**Name**/
        TextView textView1 = (TextView)findViewById(R.id.TextView1);
        textView1.setText("Name:");
        textView1.setTypeface(title);

        TextView textView2 = (TextView)findViewById(R.id.txtname);
        textView2.setText(itemSelected.getVenue().getName());
        textView2.setTypeface(nameStyle);

        TextView textview3 = (TextView)findViewById(R.id.TextView2);
        textview3.setText("Category:");
        textview3.setTypeface(title);

        TextView textview4 = (TextView)findViewById(R.id.txtcategory);
        if(itemSelected.getVenue().getCategories().size()==0)
        {
            textview4.setText("Not available");
            textview4.setTypeface(text);
        }else{
            textview4.setText(itemSelected.getVenue().getCategories().get(0).getName());
            textview4.setTypeface(text);
        }

        TextView textview5 = (TextView)findViewById(R.id.TextView3);
        textview5.setText("Direction:");
        textview5.setTypeface(title);

        TextView textview6 = (TextView)findViewById(R.id.txtdirection);
        if(itemSelected.getVenue().getLocation().getAddress()==null)
        {
            textview6.setText("Not available");
            textview6.setTypeface(text);
        }else{
            textview6.setText(itemSelected.getVenue().getLocation().getAddress());
            textview6.setTypeface(text);
        }

        TextView textview11 = (TextView)findViewById(R.id.TextView4);
        textview11.setText("Phone:");
        textview11.setTypeface(title);

        TextView phone=(TextView)findViewById(R.id.txtphone);
        if(itemSelected.getVenue().getContact().getPhone()==null)
        {
            phone.setText("Not available");
            phone.setTypeface(text);
        }else{
            phone.setText(itemSelected.getVenue().getContact().getPhone());
            phone.setTypeface(text);
        }

        TextView textview7 = (TextView)findViewById(R.id.TextView5);
        textview7.setText("Price:");
        textview7.setTypeface(title);

        TextView textLevel=(TextView)findViewById(R.id.txtlevel);
        TextView textnumber=(TextView)findViewById(R.id.txtnumber);
        TextView textview8 = (TextView)findViewById(R.id.txtprice);
        if(itemSelected.getVenue().getPrice()==null)
        {
            textview8.setText("");
            textLevel.setText("Not available");
            textLevel.setTypeface(text);
            textnumber.setText("");
        }else{
            textnumber.setText(itemSelected.getVenue().getPrice().getTier().toString());
            textview8.setText(itemSelected.getVenue().getPrice().getMessage());
            textview8.setTypeface(text);
            textLevel.setTypeface(text);
            textnumber.setTypeface(text);
        }

        TextView textview9 = (TextView)findViewById(R.id.TextView6);
        textview9.setText("Hours:");
        textview9.setTypeface(title);

        TextView textview10 = (TextView)findViewById(R.id.txthours);
        if(itemSelected.getVenue().getHours()==null ||itemSelected.getVenue().getHours().getStatus()==null)
        {
            textview10.setText("Not available");
            textview10.setTypeface(text);
        }else{
            textview10.setText(itemSelected.getVenue().getHours().getStatus());
            textview10.setTypeface(text);
        }

        tip=(TextView)findViewById(R.id.txtips);
        tip.setTypeface(title);


    }//FIN DEL ONCREATE

    //Cargamos el layout correspondiente al menú del action bar:
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_place, menu);
        return true;
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
            case R.id.action_map:
                //Creamos un intent para pasar de una actividad a otra y un bundle que pase la información:
                Intent yourIntent = new Intent(ActivityDetail.this, ActivityMapPlace.class);
                Bundle b = new Bundle();
                b.putString("name",itemSelected.getVenue().getName());
                b.putDouble("latitude", itemSelected.getVenue().getLocation().getLat());
                b.putDouble("longitude", itemSelected.getVenue().getLocation().getLng());

                //En el caso de que no haya categoría:
                if(itemSelected.getVenue().getCategories().size()==0)
                {
                    b.putString("category", "Not available");//No disponible
                    b.putString("prefix","https://ss1.4sqi.net/img/categories_v2/food/default_");
                    b.putString("suffix",".png");
                }else{
                    b.putString("category", itemSelected.getVenue().getCategories().get(0).getName());//Nombre de la categoría
                    b.putString("prefix",itemSelected.getVenue().getCategories().get(0).getIcon().getPrefix());
                    b.putString("suffix",itemSelected.getVenue().getCategories().get(0).getIcon().getSuffix());
                }

                b.putString("address",itemSelected.getVenue().getLocation().getAddress());
                yourIntent.putExtras(b);
                startActivity(yourIntent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //Inicializamos todos los lugares,sean o no de comida:
    public void initTipsCallback() {
        callback = new Callback<Comments>() {
            @Override
            public void success(Comments comments, Response response) {
                Log.d("retrofit", "Todo devuelto de comments ok");

                if(comments.getResponse().getTips().getCount()==0)
                {
                    progress2.setVisibility(View.INVISIBLE);
                    placecomment.setText("No comments");
                    placecomment.setVisibility(View.VISIBLE);

                }else{
                    adapterlist = new AdapterComments(ActivityDetail.this, R.layout.detail_view,comments.getResponse().getTips().getItems());
                    listDetail.setAdapter(adapterlist);
                    progress2.setVisibility(View.INVISIBLE);
                    adapterlist.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                try{
                    if(error != null)
                    {
                        Log.d("retrofit","Fallo en el callback");
                        Log.d("retrofit",""+error.getLocalizedMessage());
                        Log.d("retrofit",""+error.getResponse().getStatus());
                        Log.d("retrofit",""+error.getResponse().getReason());
                        Log.d("retrofit",""+error.getResponse().getBody());
                        Log.d("retrofit",""+error.getResponse().getUrl());
                    }
                }catch(Exception e)
                {
                    if(error.getCause() != null && error.getCause() instanceof UnknownHostException)
                    {
                        Toast.makeText(getApplicationContext(), "El servidor no responde.Compruebe su conexión", Toast.LENGTH_LONG).show();
                    }
                }

            }

        };
    }
}
