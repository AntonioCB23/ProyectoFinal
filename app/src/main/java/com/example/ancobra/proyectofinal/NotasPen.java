package com.example.ancobra.proyectofinal;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ancobra on 05/07/2018.
 */

public class NotasPen extends AppCompatActivity {
    //final ArrayNotas global=(ArrayNotas) getApplicationContext();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Publicacion> notas;
        setContentView(R.layout.notaspendientes);
        notas = rellenaParaPruebas();
        ListView ls = findViewById(R.id.lvNotas);
        Adaptador adap = new Adaptador(this, notas);
        ls.setAdapter(adap);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Publicacion nota= notas.get(i);
                Toast toast = Toast.makeText(getApplicationContext(), "Funciona!!! ", Toast.LENGTH_LONG);
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
        ArrayList<Publicacion> notas = new ArrayList<Publicacion>();
        Publicacion pb1 = new Publicacion("Esto es una prueba","Antonio",true);
        notas.add(pb1);
        Publicacion pb2 = new Publicacion("Esto es una prueba, esta vez algo más larga","Un individuo amable",true);
        notas.add(pb2);
        Publicacion pb3 = new Publicacion("Bueno esta nota se pasa de larga, pero es para tratar de comprobar la funcionalidad al completo del ListView que pretendemos incorporar".substring(0,50)+"...","Geralt de Rivia",true);
        notas.add(pb3);
        return notas;
    }
}
