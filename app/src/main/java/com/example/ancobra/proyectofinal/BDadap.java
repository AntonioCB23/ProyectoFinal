package com.example.ancobra.proyectofinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDadap extends SQLiteOpenHelper {
    public static final String TABLE_ID ="idNota";
    public static final String AUTOR ="autor";
    public static final String TEXTO ="texto";
    public static final String URGE ="urgente";

    private  static final String DATABASE ="Notas";
    private  static final String TABLE ="Notas";
    public BDadap(Context context, String database) {

        super(context, database, null, 1);
    }

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

    public void addNote(String autor, String texto , String urgente){
        ContentValues valores = new ContentValues();
        valores.put(AUTOR,autor);
        valores.put(TEXTO,texto);
        valores.put(URGE,urgente);
        this.getWritableDatabase().insert(TABLE,null,valores);
    }

    public Cursor getNota(String condition){
        String col[] ={TABLE_ID,AUTOR,TEXTO,URGE};
        String[] args = new String[] {condition};
        Cursor c = this.getReadableDatabase().query(TABLE,col, TEXTO+"=?",args,null,null, TABLE_ID+" DESC");
        return c;
    }

    public Cursor getTodasNotas(){
        String col[] ={TABLE_ID,AUTOR,TEXTO,URGE};
        Cursor c = this.getReadableDatabase().query(TABLE,col,null,null,null,null,TABLE_ID+" DESC");
        return c;
    }

    public void deleteNote(String condition){
        String[]args = {condition};
        this.getWritableDatabase().delete(TABLE,AUTOR+"=?",args);
    }
}
