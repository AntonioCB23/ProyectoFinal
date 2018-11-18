package com.example.ancobra.proyectofinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class MenuApp  extends AppCompatActivity {
    SharedPreferences prefs; //PREFERENCIAS
    SharedPreferences.Editor editor; //EDITOR DE PREFENCIAS
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.menu);

            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().setIcon(R.drawable.icon);
            getSupportActionBar().setTitle("WeUnite > Menú");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5641cb")));

            Button btnNuevo = (Button) findViewById(R.id.btnNuevo);
            Button btnPen = (Button) findViewById(R.id.btnPendiente);
            Button btnNotas = (Button) findViewById(R.id.btnMisNotas);
            Button btnSalir = (Button) findViewById(R.id.btnSalir);

            prefs = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            editor = prefs.edit();

            //SI EL MODO ES OFFLINE OCULTA EL BOTON DE NOTAS EN RED Y RESITUA LOS BOTONES
            if (prefs.getBoolean("off",true)){
                btnPen.setVisibility(View.GONE);
                btnNuevo.setY(200);
                btnNotas.setY(205);
                btnSalir.setY(210);

            }
            //BOTON DE NOTA NUEVA
            btnNuevo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Has seleccionado una nueva nota recordatoria", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent nuevaNota= new Intent(getApplicationContext(), AddNota.class);
                    nuevaNota.putExtra("Type","add");
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
                    Intent Main= new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(Main);
                }
            });
        }

}
