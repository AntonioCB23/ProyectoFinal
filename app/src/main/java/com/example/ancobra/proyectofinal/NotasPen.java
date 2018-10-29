package com.example.ancobra.proyectofinal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Ancobra on 05/07/2018.
 */

public class NotasPen extends AppCompatActivity {
    //final ArrayNotas global=(ArrayNotas) getApplicationContext();
    Publicacion publi;
    ArrayList<Publicacion> notas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00a6a8")));

        super.onCreate(savedInstanceState);
        //Intent intent = getIntent();
        setContentView(R.layout.notaspendientes);
        notas = rellenaParaPruebas();
        ListView ls = findViewById(R.id.lvNotas);
        Adaptador adap = new Adaptador(this, notas);
        ls.setAdapter(adap);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Publicacion nota= notas.get(i);
                //Toast toast = Toast.makeText(getApplicationContext(), "Funciona!!! ", Toast.LENGTH_LONG);
                Log.i("PRUEBA---","Clickado");
                Intent muestraNota = new Intent(getApplicationContext(), MostrarNota.class);
                muestraNota.putExtra("texto",notas.get(i).contenido);
                muestraNota.putExtra("autor",notas.get(i).autor);
                muestraNota.putExtra("urge",notas.get(i).urgente);
                startActivity(muestraNota);
            }
        });
        ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Publicacion nota= notas.get(i);
                Toast toast = Toast.makeText(getApplicationContext(), "Funciona!!! ", Toast.LENGTH_LONG);
                return true;
            }
        });
    }


    private ArrayList<Publicacion> rellenaParaPruebas() {
        ArrayList<Publicacion> hola = new ArrayList<Publicacion>();;
        Publicacion pb1 = new Publicacion("Esto es una prueba","Antonio",false);
        hola.add(pb1);
        Publicacion pb2 = new Publicacion("Esto es una prueba, esta vez algo más larga","Un individuo amable",true);
        hola.add(pb2);
        Publicacion pb3 = new Publicacion("Bueno esta nota se pasa de larga, pero es para tratar de comprobar la funcionalidad al completo del ListView que pretendemos incorporar","Geralt de Rivia",true);
        hola.add(pb3);
        return hola;
    }
}
