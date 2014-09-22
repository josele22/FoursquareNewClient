package com.joseantonio.foursquarenewclient.app;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.joseantonio.foursquarenewclient.app.fragments.FragmentProfile;
import com.joseantonio.foursquarenewclient.app.rest.Profiles.Profiles;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.joseantonio.foursquarenewclient.app.adapters.NavDrawerListAdapter;
import com.joseantonio.foursquarenewclient.app.adapters.SectionsPagerAdapter;
import com.joseantonio.foursquarenewclient.app.events.DataDownloadedEvent;
import com.joseantonio.foursquarenewclient.app.fragments.ListFragment;
import com.joseantonio.foursquarenewclient.app.fragments.MapPlaces;
import com.joseantonio.foursquarenewclient.app.rest.RestClient;
import com.joseantonio.foursquarenewclient.app.rest.dominio.Places;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity implements LocationListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private PageIndicator mIndicator;
    private LocationManager locationManager;
    private String provider;
    private Callback<Places> callback;
    private RestClient restClient;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private String lat;
    private String lon;
    private int selection;
    private ProgressBar progress;
    private SearchView searchView;
    private TextView logout;
    private dateUsers register;
    private Users user;

    //Used to store app title
    private CharSequence mTitle;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter NavAdapter;//Esto pintará los correspondiente al nav drawer
    private TextView name;
    private TextView email;
    private Profiles profilesSelected;
    public static Login log;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        register = new dateUsers(getApplicationContext());
        user = new Users();

        try {
            Log.d("Login", "Pasa por el try del MainActivity");
            user = register.recuperarCONTACTO();
            if (user.getLogin() == null || user.getLogin().equals("no")) {
                register.insertarCONTACTO("no");
                Log.d("Login", "Login al entrar " + user.getLogin());
                this.finish();
                login();
            }
        }catch (Exception e) {
            Log.d("Login", "Pasa por el catch del MainActivity");

            if (user.getLogin() == null || user.getLogin().equals("no")) {
                register.insertarCONTACTO("no");
                Log.d("Login", "Login al entrar " + user.getLogin());
                this.finish();
                login();
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Llamamos al método para inicializar las llamadas:
        initPlacesCallback();

        //Instanciamos un objeto de la clase RestCLient:
        restClient = new RestClient();

        //Enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //Instanciamos y creamos los fragments correspondientes:
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new ListFragment());
        fragments.add(new MapPlaces());
        fragments.add(new FragmentProfile());

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);

        mTitle = getTitle();

        //Inicializo la selection a -1,como inicio de la app,para que muestre si pulso el refresh todas las categorias:
        selection = -1;

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.Places);
        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        //Creamos el drawer layout y el listview que saldrá de la parte izquierda:
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        //Añadimos un LinearLayout en el footer del listview del navigation drawer,con el botón de desloguearse:
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.footer_listview, drawerList, false);
        drawerList.addFooterView(footer, null, false);

        logout = (TextView) findViewById(R.id.txtlogout);
        //Listener sobre el botón de logout:
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register.modificarCONTACTOsi("no");
                user = register.recuperarCONTACTO();
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
                finish();
                Log.d("Login", "Deslogueo a " + user.getLogin());
            }
        });


        //Create ArrayList items:
        navDrawerItems = new ArrayList<NavDrawerItem>();
        // adding nav drawer items to array
        // GastroPub:
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Restaurant:
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Tapas restaurant:
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Café:
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        // Historic site:
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // Mall:
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        //Stadium soccer:
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));

        // Recycle the typed array
        navMenuIcons.recycle();

        // setting the nav drawer list adapter(pasamos el nav drawer al adaptador):
        NavAdapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        drawerList.setAdapter(NavAdapter);

        //Esto permitirá que cuando le demos al actionbar enel icono de la esquina se abra o se cierre:
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("Select location");
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };


        //Listener de cada uno de los items del Listview del navigation,cuando pulsamos en cada uno de ellos:
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Establecemos la posición de cada uno de los ítems del listview:
                drawerList.getItemAtPosition(position).toString();

                //Buscamos sitios de comida:
                if (position == 0) {
                    drawerLayout.closeDrawers();//Cerramos el navigation drawer con su listview y cargamos el fragment correspondiente
                    getActionBar().setTitle("Food");

                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        try {
                            progress.setVisibility(View.VISIBLE);
                            //Le pasamos los parámetros correspondientes,en este caso GastroPub:
                            restClient.explore_place(lat, lon, "1", "food", "A2JLH23PAQX0WRSKCAAJENRWNRB13GZ5MA5DJYRJNTSBTA0F",
                                    "XJRLPT114VH4T0IE1LGF0R44WRRNXKSOIUDIU0UBKHPEFN3S", "20140512", callback);

                            Log.d("ConsultOK", "Localización coordenadas: " + lat + "" + lon);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Activa el GPS!", Toast.LENGTH_SHORT).show();
                    }
                }

                //Buscamos sitios de comida:
                if (position == 1) {
                    drawerLayout.closeDrawers();//Cerramos el navigation drawer con su listview y cargamos el fragment correspondiente
                    getActionBar().setTitle("Drinks:");

                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        try {
                            progress.setVisibility(View.VISIBLE);
                            //Le pasamos los parámetros correspondientes,en este caso GastroPub:
                            restClient.explore_place(lat, lon, "1", "drinks", "A2JLH23PAQX0WRSKCAAJENRWNRB13GZ5MA5DJYRJNTSBTA0F",
                                    "XJRLPT114VH4T0IE1LGF0R44WRRNXKSOIUDIU0UBKHPEFN3S", "20140512", callback);


                            Log.d("ConsultOK", "Localización coordenadas: " + lat + "" + lon);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Activa el GPS!", Toast.LENGTH_SHORT).show();
                    }
                }

                //Buscamos Restaurantes de tapas:
                if (position == 2) {
                    drawerLayout.closeDrawers();//Cerramos el navigation drawer con su listview y cargamos el fragment correspondiente
                    getActionBar().setTitle("Cofee");

                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        try {
                            progress.setVisibility(View.VISIBLE);
                            //Le pasamos los parámetros correspondientes,en este caso GastroPub:
                            restClient.explore_place(lat, lon, "1", "coffee", "A2JLH23PAQX0WRSKCAAJENRWNRB13GZ5MA5DJYRJNTSBTA0F",
                                    "XJRLPT114VH4T0IE1LGF0R44WRRNXKSOIUDIU0UBKHPEFN3S", "20140520", callback);


                            Log.d("ConsultOK", "Localización coordenadas: " + lat + "" + lon);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Activa el GPS!", Toast.LENGTH_SHORT).show();
                    }
                }

                if (position == 3) {
                    drawerLayout.closeDrawers();//Cerramos el navigation drawer con su listview y cargamos el fragment correspondiente
                    getActionBar().setTitle("Shops");

                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        try {
                            progress.setVisibility(View.VISIBLE);
                            //Le pasamos los parámetros correspondientes,en este caso GastroPub:
                            restClient.explore_place(lat, lon, "1", "shops", "A2JLH23PAQX0WRSKCAAJENRWNRB13GZ5MA5DJYRJNTSBTA0F",
                                    "XJRLPT114VH4T0IE1LGF0R44WRRNXKSOIUDIU0UBKHPEFN3S", "20140512", callback);


                            Log.d("ConsultOK", "Localización coordenadas: " + lat + "" + lon);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Activa el GPS!", Toast.LENGTH_SHORT).show();
                    }
                }

                if (position == 4) {
                    drawerLayout.closeDrawers();//Cerramos el navigation drawer con su listview y cargamos el fragment correspondiente
                    getActionBar().setTitle("Arts");

                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        try {
                            progress.setVisibility(View.VISIBLE);
                            //Le pasamos los parámetros correspondientes,en este caso GastroPub:
                            restClient.explore_place(lat, lon, "1", "arts", "A2JLH23PAQX0WRSKCAAJENRWNRB13GZ5MA5DJYRJNTSBTA0F",
                                    "XJRLPT114VH4T0IE1LGF0R44WRRNXKSOIUDIU0UBKHPEFN3S", "20140512", callback);


                            Log.d("ConsultOK", "Localización coordenadas: " + lat + "" + lon);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Activa el GPS!", Toast.LENGTH_SHORT).show();
                    }
                }

                if (position == 5) {
                    drawerLayout.closeDrawers();//Cerramos el navigation drawer con su listview y cargamos el fragment correspondiente
                    getActionBar().setTitle("Outdoors");

                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        try {
                            progress.setVisibility(View.VISIBLE);
                            //Le pasamos los parámetros correspondientes,en este caso GastroPub:
                            restClient.explore_place(lat, lon, "1", "outdoors", "A2JLH23PAQX0WRSKCAAJENRWNRB13GZ5MA5DJYRJNTSBTA0F",
                                    "XJRLPT114VH4T0IE1LGF0R44WRRNXKSOIUDIU0UBKHPEFN3S", "20140512", callback);


                            Log.d("ConsultOK", "Localización coordenadas: " + lat + "" + lon);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Activa el GPS!", Toast.LENGTH_SHORT).show();
                    }
                }

                if (position == 6) {
                    drawerLayout.closeDrawers();//Cerramos el navigation drawer con su listview y cargamos el fragment correspondiente
                    getActionBar().setTitle("Sights");

                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        try {
                            progress.setVisibility(View.VISIBLE);
                            //Le pasamos los parámetros correspondientes,en este caso GastroPub:
                            restClient.explore_place(lat, lon, "1", "sights", "A2JLH23PAQX0WRSKCAAJENRWNRB13GZ5MA5DJYRJNTSBTA0F",
                                    "XJRLPT114VH4T0IE1LGF0R44WRRNXKSOIUDIU0UBKHPEFN3S", "20140512", callback);


                            Log.d("ConsultOK", "Localización coordenadas: " + lat + "" + lon);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Activa el GPS!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        //Establecemos la escucha del evento(para pasarle)al layout del navigation(las vistas):
        drawerLayout.setDrawerListener(drawerToggle);

        //Instanciamos el Adaptador de páginas o fragments:
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);

        //Establecemos el viewPager(gesto de fragments) en el adapter:
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);//Con esto conseguimos que cuando vaya recorriendo los fragments se carguen
        //rápidamente las anteriores,en este caso si estamos en el 3º que cuando volvamos atrás queden en memoria guardadas.

        mIndicator = (TitlePageIndicator)findViewById(R.id.indicator);

        mIndicator.setViewPager(mViewPager);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

    }//FIN DEL ONCREATE


    public void login()
    {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        locationManager.requestLocationUpdates(provider, 30000, 0, this);
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            onLocationChanged(location);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    //Cargamos el layout correspondiente al menú del action bar(donde tenemos 2 ítems:lupa y refresh):
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.picture_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { //Establecemos el listener de la búsqueda
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    progress.setVisibility(View.VISIBLE);
                    restClient.explore_categories(lat, lon, "1",query, "A2JLH23PAQX0WRSKCAAJENRWNRB13GZ5MA5DJYRJNTSBTA0F",
                            "XJRLPT114VH4T0IE1LGF0R44WRRNXKSOIUDIU0UBKHPEFN3S", "20140512", callback);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    //Acciones cuando pulsamos en los diferentes ítems del menú del action bar:
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.refresh:
                if(selection==0)
                {
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        try {
                            //Le pasamos los parámetros correspondientes:
                            restClient.explore_place(lat, lon, "1", "food", "A2JLH23PAQX0WRSKCAAJENRWNRB13GZ5MA5DJYRJNTSBTA0F",
                                    "XJRLPT114VH4T0IE1LGF0R44WRRNXKSOIUDIU0UBKHPEFN3S", "20140512", callback);

                            Log.d("ConsultOK","Localización coordenadas: "+lat+""+lon);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(this, "Activa el GPS!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }

        //En el caso de que pulsemos en el icono del navigation drawer(3 rayas)para abrir el navigationdrawer:
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.refresh).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    //Inicializamos todos los lugares,sean o no de comida:
    public void initPlacesCallback() {
        callback = new Callback<Places>() {
            @Override
            public void success(Places place, Response response) {
                //Guardamos el evento recogido,en nuestro caso Lista de Items en el eventBus(publicamos el evento):
                //los items que a la vez le pasaremos al AdapterList para que los pinte.
                progress.setVisibility(View.INVISIBLE);
                //Publico el evento descargar lugares en el bus1:
                try{
                  if(place.getResponse().getGroups().get(0).getItems().size()==0)
                  {
                      Toast.makeText(getApplicationContext(), "No se ha encontrado el lugar", Toast.LENGTH_LONG).show();
                  }else{
                      AplicationClass.bus.post(new DataDownloadedEvent(place.getResponse().getGroups().get(0).getItems()));

                  }

                }catch(Exception e){

                }

                Log.d("ConsultOK","Section del lugar "+place.getResponse().getQuery());
                Log.d("retrofit", "Publico el evento en el bus!");
                Log.d("retrofit", "Tamaño de grupos "+place.getResponse().getGroups().size());
            }

            @Override
            public void failure(RetrofitError error) {
                try{
                    if(error != null)
                    {
                        Log.d("retrofit","Fallo en el callback");
                        Log.d("retrofit",""+error.getLocalizedMessage());
                        Log.d("retrofit",""+error.getResponse().getStatus());

                        if(error.getResponse().getStatus()==400)
                        {
                            progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "El servidor no responde.Compruebe su conexión", Toast.LENGTH_LONG).show();
                        }
                        Log.d("retrofit",""+error.getResponse().getReason());
                        Log.d("retrofit",""+error.getResponse().getBody());
                        Log.d("retrofit",""+error.getResponse().getUrl());
                    }
                }catch(Exception e)
                {
                    if(error.getCause() != null && error.getCause() instanceof UnknownHostException)
                    {
                        progress.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "El servidor no responde.Compruebe su conexión", Toast.LENGTH_LONG).show();
                    }
                }

            }
        };
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

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}
