package com.example.ancobra.proyectofinal;

import android.content.Intent;
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
    private static final int ADD = Menu.FIRST;
    private static final int DELETE = Menu.FIRST +1;
    private static final int EXIST = Menu.FIRST +2;
    ListView lvNotas;
    TextView textLista;
    BDadap db;
    List<String> item = null;
    String getTitle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notaspropias);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00a6a8")));

        textLista = findViewById(R.id.textView_lista);
        lvNotas = findViewById(R.id.listView_Lista);

        lvNotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getTitle = (String) lvNotas.getItemAtPosition(i);
                actividad("ver");
            }
        });
        mostrarNotas();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        menu.add(1,ADD,0,R.string.menu_crear);
        menu.add(2,DELETE,0,R.string.menu_borrar);
        menu.add(3,EXIST,0,R.string.menu_salir);
        super.onCreateOptionsMenu(menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case ADD:
                actividad("add");
                return true ;
            case DELETE:
                return true ;
            case EXIST:
                finish();
                return true ;
                default:
                    return onOptionsItemSelected(item);
        }
    }
    public void actividad (String act){
        String type = "";
        String content = "";
        if(act.equals("add")){
            type = "add";
            Intent intent= new Intent(getApplicationContext(),AddNota.class);
            intent.putExtra("type",type);
            startActivity(intent);
        }else if (act.equals("ver")){
            Intent intent = new Intent(getApplicationContext(),VerNota.class);
            intent.putExtra("autor",getNota());
            intent.putExtra("texto",getTitle);
            intent.putExtra("urge","N");
            startActivity(intent);
        }
    }

    private void mostrarNotas(){
        db = new BDadap(this);
        Cursor c = db.getTodasNotas();
        item = new ArrayList<String>();
        String autor = "";

        if (c.moveToFirst()== false ){
            textLista.setText("No hay notas para mostrar");
        }else{
            do{
                autor = c.getString(2);
                item.add(autor);
            }while (c.moveToNext());
        }
        ArrayAdapter<String> adaptador= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,item);
        lvNotas.setAdapter(adaptador);
    }
    public String getNota(){
        String type = "",texto ="";

        db = new BDadap(this);
        Cursor c = db.getNota(getTitle);
        if (c.moveToFirst()) {
            do {
                texto = c.getString(1);
            } while (c.moveToNext());
        }
        return texto;
    }

    public String getUrgencia(){
        String type = "",texto ="";

        db = new BDadap(this);
        Cursor c = db.getNota(getTitle);
                if(c.moveToFirst()){
                do{
            texto = c.getString(3);

         }while(c.moveToNext());

}
        return texto;
    }
}
