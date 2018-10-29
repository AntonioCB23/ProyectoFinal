package com.example.ancobra.proyectofinal;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Ancobra on 05/07/2018.
 */

public class MostrarNota extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_nota);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00a6a8")));

        TextView descr = (TextView)findViewById(R.id.txtTexto);
        TextView autor = (TextView)findViewById(R.id.txtAutor);
        CheckBox urgente = findViewById(R.id.chkUrgente);
        String[] archivos = fileList();
        if (existe(archivos, "notas.txt"))
            try {
                InputStreamReader archivo = new InputStreamReader(
                        openFileInput("notas.txt"));
                File  f =  new File("notas.txt");
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                String todo = "";
                while (linea != null) {
                    todo = todo + linea + "\n";
                    linea = br.readLine();
                }
                br.close();
                archivo.close();
                descr.setText(todo);
                autor.setText(f.getName());
            } catch (IOException e) {
            }
        /*Intent it = getIntent();
        descr.setText(it.getStringExtra("texto"));
        autor.setText(it.getStringExtra("autor"));
        urgente.setChecked(it.getBooleanExtra("urge",false));*/
    }
    boolean existe(String[] archivos, String archbusca) {
        for (int f = 0; f < archivos.length; f++)
            if (archbusca.equals(archivos[f]))
                return true;
        return false;
    }
}
