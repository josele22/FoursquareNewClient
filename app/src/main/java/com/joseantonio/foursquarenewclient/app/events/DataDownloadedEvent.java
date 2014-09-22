package com.joseantonio.foursquarenewclient.app.events;

import com.joseantonio.foursquarenewclient.app.rest.dominio.Item;
import java.util.List;

/**
 * Created by josetorres on 13/05/14.
 */
public class DataDownloadedEvent {

    //Declaramos una lista de items que será lo que tenga el evento,que será lo que queremos mostrar:
    private List<Item> downloadedVenues;

    public DataDownloadedEvent(List<Item> downloadedVenues) {
        this.downloadedVenues = downloadedVenues;
    }

    public List<Item> getDownloadedVenues() {
        return downloadedVenues;
    }
}
