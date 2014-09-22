package com.joseantonio.foursquarenewclient.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by josetorres on 30/05/14.
 */
public class dateUsers extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "bdusers.db";

    private static final String TABLE_USERS = "users";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LOGIN = "login";

    public dateUsers(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " +
                TABLE_USERS + "("+ COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_LOGIN + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_USERS);
        onCreate(db);
    }

    public void insertarCONTACTO(String login) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            ContentValues valores = new ContentValues();
            valores.put(COLUMN_LOGIN, login);
            db.insert(TABLE_USERS, null, valores);
        }

        db.close();
    }

    //Borrar todo el contenido de la tabla:
    public void borrarTabla() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+ "users");
    }

    //Borrar contacto en concreto:
    public void removeContact(String login)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("users","login"+"='" + login + "'",null);
        db.close();
    }

    public Users recuperarCONTACTO() {
        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"login"};
        Cursor c = db.query("users", valores_recuperar,null, null, null, null, null, null);
        if(c != null) {
            c.moveToFirst();
        }
        Users contactos = new Users(c.getString(0));
        db.close();
        c.close();
        return contactos;
    }

    //En el caso de que no estemos logueados y queramos cambiar el estado:
    public void modificarCONTACTOno(String login) {
        SQLiteDatabase db = getWritableDatabase();
        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put("login",login);
        //Actualizamos el registro en la base de datos
        db.update("users", valores," login = '"+"no"+"'", null);//En el "si" tenemos que concretar el dato que queremos cambiar,que en este caso es el NO
        db.close();
    }

    //En el caso de que estemos logueados y queramos desloguearnos:
    public void modificarCONTACTOsi(String login) {
        SQLiteDatabase db = getWritableDatabase();
        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put("login",login);
        //Actualizamos el registro en la base de datos
        db.update("users", valores," login = '"+"si"+"'", null);//En el "si" tenemos que concretar el dato que queremos cambiar,que en este caso es el NO
        db.close();
    }


}
