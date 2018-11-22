package com.example.ancobra.proyectofinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdapBDLogin extends SQLiteOpenHelper {
    //COLUMNAS DE LA BASE DE DATOS
    public static final String USER ="username";
    public static final String PASS ="pass";

    private  static final String TABLE ="usuarios";

    /**
     * Constructor de la clase
     * @param context context pasado
     */
    public AdapBDLogin(Context context) {

        super(context, "users", null, 1);
    }

    /**
     * Creacion de la tabla en la base datos
     * @param db base de datos sobre la que se actua
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE+" ("+
                USER + " TEXT, "+PASS + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }

    /**
     * Añade los datos a la tabla con los param pasados
     * @param autor de la nota
     * @param texto asociado a la nota
     * @param urgente indica si la nota es urgente o no
     */
    public void addUser(String autor, String texto){
        ContentValues valores = new ContentValues();
        valores.put(USER,autor);
        valores.put(PASS,texto);
        this.getWritableDatabase().insert(TABLE,null,valores);
    }
    /**
     * Obtiene los datos de la bd interna
     * @param condition condicion a realizar (un where)
     * @return el cursor sobre el que obtiene los datos
     */
    public Cursor getUser(String condition){
        String col[] ={USER,PASS};
        String[] args = new String[] {condition};
        Cursor c = this.getReadableDatabase().query(TABLE,col, USER+"=?",args,null,null, null);
        return c;
    }

    /**
     * Obtiene todos los datos de la tabla
     * @return el cursor sobre el que obtiene los datos
     */
    public Cursor getTodosUsers(){
        String col[] ={USER,PASS};
        Cursor c = this.getReadableDatabase().query(TABLE,col,null,null,null,null,null);
        return c;
    }

}
