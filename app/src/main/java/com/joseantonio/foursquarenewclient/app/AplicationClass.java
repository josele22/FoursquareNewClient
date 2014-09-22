package com.joseantonio.foursquarenewclient.app;

import android.app.Application;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

//Bus de eventos Otto depositaremos los eventos correspondientes, y los distintos fragments se suscribirán y se
//tendrán que registrar al bus de eventos(para poder utilizar dicho evento para sus acciones)al igual que darse de baja o desubscribe:

/**
 * Created by josetorres on 13/05/14.
 */
public class AplicationClass extends Application { //Esta clase Application es la primera que se ejecutará por detrás en la app,
                                                   //donde el manifest.xml primero irá a ejecutarlo:

    public static Bus bus;
    public static Bus bus2;

    //Crearemos el bus en dicha clase,que se ejecutará siempre al abrir la APP(por debajo):
    @Override
    public void onCreate() {
        super.onCreate();

        bus=new Bus();//Instanciamos el bus al arrancar al app
        bus2=new Bus();
    }
}
