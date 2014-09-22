package com.joseantonio.foursquarenewclient.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by josetorres on 27/05/14.
 */

//Clase donde se implementará el icono según categoría y un fondo:
public class ImageUtils extends Activity {

    //Método en el caso de que la densidad sea de XXDPI:
    public static Bitmap Draw(Bitmap bitmap1)
    {   //Creamos un bitmap del mismo tamaño que el icono(bitmap que recoja Picasso)que recibamos:
        Bitmap output = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);//Creamos un objeto canvas(herramienta de trabajo)con el bitmap creado

        Paint paint = new Paint();//Con lo que pintaremos
        paint.setAntiAlias(true);//Antialiasing para evitar bordes de sierra
        paint.setColor(Color.parseColor("#2298C7"));//Color

        //Espacio(rectangular)sobre el se colocará el bitmap:
        final Rect rect = new Rect(0,0, bitmap1.getWidth(), bitmap1.getHeight());

        //Pintaremos el circulo del tamaño del bitmap1 que recibamos:
        canvas.drawCircle((bitmap1.getWidth()/2), (bitmap1.getHeight()/2), (bitmap1.getHeight()/2), paint);
        canvas.drawBitmap(bitmap1, rect, rect, paint);//Pintamos finalmente el bitmap resultante

        return output;
    }

}
