package com.example.ancobra.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Ancobra on 05/07/2018.
 */

public class Menu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
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
                Intent nuevaNota= new Intent(getApplicationContext(), MostrarNota.class);
                startActivity(nuevaNota);
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
