package com.example.ancobra.proyectofinal;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by Ancobra on 05/07/2018.
 */

public class Nota extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nota);
        final ArrayNotas global=(ArrayNotas) getApplicationContext();

        Button btnEnviar = (Button) findViewById(R.id.btnEnviar);
        EditText enviar = (EditText)findViewById(R.id.txtNota);
        enviar.requestFocus();
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast toast = Toast.makeText(getApplicationContext(), "La nota se ha subido satisfactoriamente", Toast.LENGTH_LONG);
                //toast.show();
                File file = new File("/data/", "Prueba.txt");
                String filename = "myfile";
                Log.i("----",file.getAbsolutePath());
                String string = "Hello world!";
                FileOutputStream outputStream;
                global.publicaciones.add(new Publicacion("Hola que tal, soy el chico de las poesías", "Buena esa",true));
               /* try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(string.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error de procesamiento", Toast.LENGTH_LONG);
                    toast.show();
                    e.printStackTrace();
                }*/
            }
        });
    }
}