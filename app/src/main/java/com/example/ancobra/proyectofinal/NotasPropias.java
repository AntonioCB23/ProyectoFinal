package com.example.ancobra.proyectofinal;

import android.app.Activity;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;

public class NotasPropias extends AppCompatActivity {
        //final ArrayNotas global=(ArrayNotas) getApplicationContext();
        Publicacion publi;
        String[] arrayNotas;
        ArrayList<Publicacion> notasPropias;

        @Override
        protected void onCreate (Bundle savedInstanceState){
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00a6a8")));

        super.onCreate(savedInstanceState);
        //Intent intent = getIntent();
        setContentView(R.layout.notaspropias);
            arrayNotas = fileList();
            notasPropias = rellenaParaPruebas();
            for (int i = 0; i < arrayNotas.length; i++) {
                try {
                    InputStreamReader archivo = new InputStreamReader(
                            openFileInput(arrayNotas[i]));
                    File f =  new File(arrayNotas[i]);
                    BufferedReader br = new BufferedReader(archivo);
                    String linea = br.readLine();
                    String todo = "";
                    while (linea != null) {
                        todo = todo + linea + "\n";
                        linea = br.readLine();
                    }
                    br.close();
                    archivo.close();
                    notasPropias.add(new Publicacion(todo.trim(),"Un tipo interesante".trim(), false));
                }catch (IOException e){}
            }
        ListView ls = findViewById(R.id.lbPropias);
        final Adaptador adap = new Adaptador(this, notasPropias);
        ls.setAdapter(adap);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("PRUEBA---", "Clickado");
                Intent muestraNota = new Intent(getApplicationContext(), MostrarNota.class);
                muestraNota.putExtra("texto", notasPropias.get(i).contenido);
                muestraNota.putExtra("autor", notasPropias.get(i).autor);
                muestraNota.putExtra("urge", notasPropias.get(i).urgente);
                startActivity(muestraNota);
            }
        });
        ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Publicacion nota= notas.get(i);
                Toast toast = Toast.makeText(getApplicationContext(), "Funciona!!! ", Toast.LENGTH_LONG);
                    File f =  new File(arrayNotas[i]);
                if (f.delete())
                    Log.i("PRUEBA---", "Borrado!!!");
                else
                    Log.i("PRUEBA---", "Aquí no pasa nada");

                notasPropias.remove(i);
                adap.notifyDataSetChanged();
                return true;
            }
        });
    }


        private ArrayList<Publicacion> rellenaParaPruebas () {
        ArrayList<Publicacion> hola = new ArrayList<Publicacion>();
        ;
        Publicacion pb1 = new Publicacion("Esto es una prueba", "Antonio", false);
        hola.add(pb1);
        Publicacion pb2 = new Publicacion("Esto es una prueba, esta vez algo más larga", "Un individuo amable", true);
        hola.add(pb2);
        Publicacion pb3 = new Publicacion("Bueno esta nota se pasa de larga, pero es para tratar de comprobar la funcionalidad al completo del ListView que pretendemos incorporar", "Geralt de Rivia", true);
        hola.add(pb3);
        return hola;
    }
    }
