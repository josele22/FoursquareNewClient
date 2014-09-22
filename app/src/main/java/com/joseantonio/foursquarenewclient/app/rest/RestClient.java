package com.joseantonio.foursquarenewclient.app.rest;

import com.joseantonio.foursquarenewclient.app.rest.Profiles.Profiles;
import com.joseantonio.foursquarenewclient.app.rest.dominio.Places;
import com.joseantonio.foursquarenewclient.app.rest.tipsdominio.Comments;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RestAdapter;

/**
 * Created by josetorres on 13/03/14.
 */

public class RestClient {

    private Callback<String> callback;
    private Retrofit retrofit;


    public RestClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.foursquare.com/v2")
                .build();

        retrofit = restAdapter.create(Retrofit.class);
    }

    //Este método sacará los lugares cuya categoría sea gastroPub:
    public void explore_place(String lat,String lon,String photo,String type,String client_id,String client_secret,String v, Callback<Places>callback) throws IOException {
        retrofit.search_pub(lat + "," + lon, photo, type, client_id, client_secret, v, callback);
    }

    //Este método sacará los lugares cuya categoría filtre por la búsqueda
    public void explore_categories(String lat,String lon,String photo,String query,String client_id,String client_secret,String v, Callback<Places>callback) throws IOException {
        retrofit.search_categories(lat + "," + lon, photo, query, client_id, client_secret, v, callback);
    }

    //Este método sacará los tips(consejos del lugar donde pinchemos):
    public void explore_tips(String id,String sort,String client_id,String client_secret,String v, Callback<Comments> callback) throws IOException {
        retrofit.search_tips(id, sort, client_id, client_secret, v, callback);
    }

    //Este método sacará el perfil de cada usuario en el caso de que exista y esté registrado:
    public void explore_users(String name,String email,String token,String date, Callback<Profiles> callback) throws IOException {
        retrofit.search_user(name,email,token,date,callback);
    }

}
