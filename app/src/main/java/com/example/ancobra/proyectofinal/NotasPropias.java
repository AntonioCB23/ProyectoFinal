package com.example.ancobra.proyectofinal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase NotasPropias que muestra las notas que uno mismo ha creado en el servidor
 */
public class NotasPropias extends AppCompatActivity {
    //OPCIONES DE MENU
    private static final int ADD = Menu.FIRST;
    private static final int CAMBIAR = Menu.FIRST +1 ;
    private static final int EXIST = Menu.FIRST +2;

    ListView lvNotas;
    TextView textLista;
    BDadap db;
    String getContenido;
    SharedPreferences prefs; //PREFERENCIAS
    SharedPreferences.Editor editor; //EDITOR DE PREFENCIAS
    String database;
    ArrayList<Nota> arrayNotas;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notaspropias);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setTitle("WeUnite > "+getString(R.string.notasPropias));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5641cb")));

        textLista = findViewById(R.id.textView_modo);
        lvNotas = findViewById(R.id.listView_Lista);
        arrayNotas = new ArrayList<>();

        prefs = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();
        if(prefs.getBoolean("off",true)){
            database = "notasPersonales";
            textLista.setText(""+getString(R.string.notas_offline));
        }else{
            database = "Notas";
            textLista.setText(""+getString(R.string.notas_person));
        }
        lvNotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getContenido = arrayNotas.get(i).getTexto();
                actividad("ver");
            }
        });
        mostrarNotas();
    }
    /**
     * Añade las opciones de menu en la parte superior
     * @param menu menu a gestionar
     * @return true
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        menu.add(1,ADD,0,R.string.menu_crear);
        if(!prefs.getBoolean("off",true)){ //Si no estamos en modo offline nos permite visualizar las notas locales y las subidas al server
            menu.add(2,CAMBIAR,0,R.string.menu_cambia);
        }
        menu.add(3,EXIST,0,R.string.menu_salir);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    /**
     * Gestiona la pulsacion del menu
     * @param item opcion seleciona
     * @return la opcion correcta
     */
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case ADD:
                actividad("add");
                return true;
            case CAMBIAR:
                    if(database.equals("Notas")){
                        database = "notasPersonales";

                        textLista.setText(""+getString(R.string.notas_offline));
            }else{

                database = "Notas";
                        textLista.setText(""+getString(R.string.notas_person));
            }
            arrayNotas.clear();
            mostrarNotas();
                return true;
            case EXIST:
                finish();
                return true ;
                default:
                    return onOptionsItemSelected(item);
        }
    }

    /**
     * Gestiona si la accion que queremos hacer es añadir o visualizar una nota
     * @param act accion
     */
    public void actividad (String act){
        String type = "";
        if(act.equals("add")){
            if(database.equals("notasPersonales")){
                muestraError(""+getString(R.string.adv_notas));
            }else{
                Intent intent= new Intent(getApplicationContext(),AddNota.class);
                startActivity(intent);
            }

        }else if (act.equals("ver")){
            Intent intent = new Intent(getApplicationContext(),VerNota.class);
            intent.putExtra("autor",getAutor());
            intent.putExtra("texto",getContenido);
            intent.putExtra("urge","N");
            if(database.equals("Notas")){
                intent.putExtra("offline","Normal");
            }else{
                intent.putExtra("offline","Off");
            }
            startActivity(intent);
        }
    }

    /**
     * Muestra todas las notas de la base de datos interna
     */
    private void mostrarNotas(){
            db = new BDadap(this,database);
        Log.i("Response",""+database.toString());
        Cursor c;
        if(database.equals("Notas")){
            c = db.getTodasNotas(prefs.getString("autor","").trim());
            Log.i("Response","1111 "+prefs.getString("autor",""));
        }else{
            c = db.getTodasNotasOff();
        }

        String texto = "";

        if (c.moveToFirst()== false ){
            textLista.setText(""+getString(R.string.noNotas));
        }else{
            do{
                texto = c.getString(2);
                getContenido=texto;
                Nota n = new Nota(getAutor(),texto,getUrgencia());
                arrayNotas.add(n);
            }while (c.moveToNext());
        }

        //INICIALIZACION DEL ADAPTADOR Y AÑADIDO AL LISTVIEW
        Adaptador adap = new Adaptador(NotasPropias.this, arrayNotas);
        lvNotas.setAdapter(adap);
    }

    /**
     * Obtiene el texto
     * @return el autor de la nota seleccionada
     */
    public String getAutor(){
        String texto ="";

        db = new BDadap(this,database);
        Cursor c = db.getNota(getContenido);
        if (c.moveToFirst()) {
            do {
                texto = c.getString(1);
            } while (c.moveToNext());
        }
        return texto;
    }

    /**
     * Obtiene la urgencia de la nota
     * @return la urgencia de la nota seleccionada
     */
    public String getUrgencia(){
        String texto ="";

        db = new BDadap(this,database);
        Cursor c = db.getNota(getContenido);
                if(c.moveToFirst()){
                do{
            texto = c.getString(3);

         }while(c.moveToNext());
}
        return texto;
    }

    /**
     * Funcion que unicamente saca un AlertDialog de 1 boton que tendra el texto que queramos
     * @param textoError texto a mostrar en el cuadro de dialogo
     */
    private void muestraError(String textoError){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(NotasPropias.this);
        builder.setTitle("Advertencia");
        builder.setMessage(textoError);
        builder.setCancelable(false);
        builder.setPositiveButton(""+getString(R.string.alert_entendido), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent= new Intent(getApplicationContext(),AddNota.class);
                startActivity(intent);}
        });
        builder.show();
    }
}
