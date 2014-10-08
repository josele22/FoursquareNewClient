package com.joseantonio.foursquarenewclient.app.rest;

import com.joseantonio.foursquarenewclient.app.rest.Profiles.Profiles;
import com.joseantonio.foursquarenewclient.app.rest.dominio.Places;
import com.joseantonio.foursquarenewclient.app.rest.tipsdominio.Comments;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by josetorres on 8/05/14.
 */
public interface Retrofit {
   //Petición de búsqueda de lugares según mi posición(cuando hay ? hay que utilizar @Query,para realizar
   //la consulta,estableciendo por orden cada uno de los campos y sustituyendolo en la URL por lo que corresponda(ll,client_id,etc..):
    //https://api.foursquare.com/v2/venues/explore?ll=40.7,-74&venuePhotos=1&client_id=A2JLH23PAQX0WRSKCAAJENRWNRB13GZ5MA5DJYRJNTSBTA0F&client_secret=XJRLPT114VH4T0IE1LGF0R44WRRNXKSOIUDIU0UBKHPEFN3S&v=20140512
    //Petición sacar todos los lugares próximos,sean de la categoría que sean:

    //Lista general:
    //Consulta para sacar todos los sitios,pasándole en sección según sea comida,bebidas...etc:
    @GET("/venues/explore")
    public void search_general(@Query("ll") String latLng,
                           @Query("venuePhotos") String photo,
                           @Query("client_id") String clientId,
                           @Query("client_secret") String clientSecret,
                           @Query("v") String date,
                           Callback<Places> callback); //En este caso devolveremos objetos de tipo Places



    //Consulta para sacar todos los sitios,pasándole en sección según sea comida,bebidas...etc:
    @GET("/venues/explore")
         public void search_pub(@Query("ll") String latLng,
                                @Query("venuePhotos") String photo,
                                @Query("section") String food,
                                @Query("client_id") String clientId,
                                @Query("client_secret") String clientSecret,
                                @Query("v") String date,
                                Callback<Places> callback); //En este caso devolveremos objetos de tipo Places

    //Consulta para sacar los lugares según la categoría que quiera buscar en el WidgetSearch:
    @GET("/venues/explore")
    public void search_categories(@Query("ll") String latLng,
                                  @Query("venuePhotos") String photo,
                                  @Query("query") String category,
                                  @Query("client_id") String clientId,
                                  @Query("client_secret") String clientSecret,
                                  @Query("v") String date,
                                  Callback<Places> callback); //En este caso devolveremos objetos de tipo Places

    //Consulta para sacar todos los tips(consejos)de un lugar en concreto:
    //https://api.foursquare.com/v2/venues/40a55d80f964a52020f31ee3/tips?sort=recent&oauth_token=LQVS4B2VQ0QKP0KBP0R4SMG3EJ5CW0ARESTEE4Y4X1Z25F21&v=20140522
    @GET("/venues/{id}/tips")
    public void search_tips(@Path("id") String id,
                           @Query("sort") String sort,
                           @Query("client_id") String clientId,
                           @Query("client_secret") String clientSecret,
                           @Query("v") String date,
                           Callback<Comments> callback); //En este caso devolveremos objetos de tipo Comments


    //Consulta para sacar la información del usuario en sí que se loguea:
    @GET("/users/search")
    public void search_user(@Query("firstName") String name,
                            @Query("email") String email,
                            @Query("oauth_token")String token,
                            @Query("v") String date,
                            Callback<Profiles> callback); //En este caso devolveremos objetos de tipo Profiles

}
