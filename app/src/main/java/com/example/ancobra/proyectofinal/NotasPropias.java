package com.example.ancobra.proyectofinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        getSupportActionBar().setTitle("WeUnite > Notas propias");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5641cb")));

        textLista = findViewById(R.id.textView_modo);
        lvNotas = findViewById(R.id.listView_Lista);
        arrayNotas = new ArrayList<>();

        prefs = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();
        if(prefs.getBoolean("off",true)){
            database = "notasPersonales";
            textLista.setText("Notas modo offline");
            textLista.setX(225);
        }else{
            database = "Notas";
            textLista.setText("Notas personales subidas al servidor");
            textLista.setX(175);
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
        menu.add(2,CAMBIAR,0,R.string.menu_cambia);
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
                        editor.putBoolean("offline",true);
                        database = "notasPersonales";
                        textLista.setText("Notas modo offline ");
                        textLista.setX(225);
            }else{
                        editor.putBoolean("offline",false);
                database = "Notas";
                        textLista.setText("Notas personales subidas al servidor");
                        textLista.setX(175);
            }
                editor.commit();
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
            type = "add";
            Intent intent= new Intent(getApplicationContext(),AddNota.class);
            intent.putExtra("type",type);
            startActivity(intent);
        }else if (act.equals("ver")){
            Intent intent = new Intent(getApplicationContext(),VerNota.class);
            intent.putExtra("autor",getAutor());
            intent.putExtra("texto",getContenido);
            intent.putExtra("urge","N");
            startActivity(intent);
        }
    }

    /**
     * Muestra todas las notas de la base de datos interna
     */
    private void mostrarNotas(){
            db = new BDadap(this,database);
        Cursor c;
        if(database.equals("Notas")){
            c = db.getTodasNotas(prefs.getString("autor",""));
        }else{
            c = db.getTodasNotasOff();
        }

        String texto = "";

        if (c.moveToFirst()== false ){
            textLista.setText("No hay notas para mostrar");
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
}
