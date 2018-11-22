package com.example.ancobra.proyectofinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDadap extends SQLiteOpenHelper {
    //COLUMNAS DE LA BASE DE DATOS
    public static final String TABLE_ID ="idNota";
    public static final String AUTOR ="autor";
    public static final String TEXTO ="texto";
    public static final String URGE ="urgente";

    private  static final String DATABASE ="Notas";
    private  static final String TABLE ="Notas";

    /**
     * Constructor de la clase
     * @param context context pasado
     * @param database base de datos a tratar
     */
    public BDadap(Context context, String database) {

        super(context, database, null, 1);
    }

    /**
     * Creacion de la tabla en la base datos
     * @param db base de datos sobre la que se actua
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE+" ("+
                    TABLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    AUTOR + " TEXT, "+TEXTO + " TEXT, "+URGE +" TEXT)");
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
    public void addNote(String autor, String texto , String urgente){
        ContentValues valores = new ContentValues();
        valores.put(AUTOR,autor);
        valores.put(TEXTO,texto);
        valores.put(URGE,urgente);
        this.getWritableDatabase().insert(TABLE,null,valores);
    }
    /**
     * Obtiene los datos de la bd interna
     * @param condition condicion a realizar (un where)
     * @return el cursor sobre el que obtiene los datos
     */
    public Cursor getNota(String condition){
        String col[] ={TABLE_ID,AUTOR,TEXTO,URGE};
        String[] args = new String[] {condition};
        Cursor c = this.getReadableDatabase().query(TABLE,col, TEXTO+"=?",args,null,null, TABLE_ID+" DESC");
        return c;
    }

    /**
     * Obtiene todos los datos de la tabla
     * @return el cursor sobre el que obtiene los datos
     */
    public Cursor getTodasNotas(String condition){
        String col[] ={TABLE_ID,AUTOR,TEXTO,URGE};
        String[] args = new String[] {condition};
        Cursor c = this.getReadableDatabase().query(TABLE,col,AUTOR+"=?",args,null,null,TABLE_ID+" DESC",null);
        return c;
    }

    /**
     * Obtiene todos los datos de la tabla "offline"
     * @return el cursor sobre el que obtiene los datos
     */
    public Cursor getTodasNotasOff(){
        String col[] ={TABLE_ID,AUTOR,TEXTO,URGE};
        Cursor c = this.getReadableDatabase().query(TABLE,col,null,null,null,null,TABLE_ID+" DESC");
        return c;
    }

    /**
     * Borra la nota de la tabla que cumpla la condicion
     * @param condition condicion para borrar la nota seleciconada
     */
    public void deleteNote(String condition){
        String[]args = {condition};
        this.getWritableDatabase().delete(TABLE,AUTOR+"=?",args);
    }
}
