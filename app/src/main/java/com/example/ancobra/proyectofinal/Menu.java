package com.example.ancobra.proyectofinal;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ancobra on 05/07/2018.
 */

public class Menu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00a6a8")));

        Button btnNuevo = (Button) findViewById(R.id.btnNuevo);
        Button btnPen = (Button) findViewById(R.id.btnPendiente);
        Button btnNotas = (Button) findViewById(R.id.btnMisNotas);
        Button btnSalir = (Button) findViewById(R.id.btnSalir);

        //BOTON DE NOTA NUEVA
        btnNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Has seleccionado una nueva nota recordatoria", Toast.LENGTH_SHORT);
                toast.show();
                Intent nuevaNota= new Intent(getApplicationContext(), Nota.class);
                startActivity(nuevaNota);
            }
        });
        //BOTON DE NOTAS PENDIENTES
        btnPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Has seleccionado ver las notas pendientes", Toast.LENGTH_SHORT);
                toast.show();
                Intent notasPen= new Intent(getApplicationContext(), NotasPen.class);
                startActivity(notasPen);
            }
        });
        //BOTON PARA COMPROBAR LAS NOTAS PROPIAMENTE HECHAS
        btnNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Has seleccionado ver tus notas publicadas", Toast.LENGTH_SHORT);
                toast.show();
                Intent NotasPropias= new Intent(getApplicationContext(), NotasPropias.class);
                startActivity(NotasPropias);
            }
        });
        //BOTON SALIR
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Salir del programa", Toast.LENGTH_SHORT);
                toast.show();
                System.exit(0);
            }
        });
    }
}
