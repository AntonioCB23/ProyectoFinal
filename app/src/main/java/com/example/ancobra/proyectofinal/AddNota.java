package com.example.ancobra.proyectofinal;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class AddNota extends AppCompatActivity {
    String type;
    EditText AUTOR, TEXTO;
    CheckBox URGE;
    private static final int SALIR = Menu.FIRST;
    BDadap DB;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnota);

        Button add = findViewById(R.id.button_Add);
        AUTOR = findViewById(R.id.editText_Autor);
         TEXTO = findViewById(R.id.editText_Texto);
         URGE = findViewById(R.id.checkBox);

        Bundle bundle = this.getIntent().getExtras();
        type = bundle.getString("type");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNota();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        super.onCreateOptionsMenu(menu);
        menu.add(1,SALIR,0,R.string.menu_salir);
        return  true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case SALIR:

                CookieSyncManager.createInstance(this);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
                Intent intent = new Intent(AddNota.this,MenuApp.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void addNota(){
        DB = new BDadap(this);
        String autor, texto;
        Boolean urge;
        autor = AUTOR.getText().toString().trim();
        texto = TEXTO.getText().toString().trim();
        urge = URGE.isChecked();

        if (!autor.equals("")){
            Cursor c = DB.getNota(autor);
            String getTitle="";
            if(c.moveToFirst()){
                do{
                    getTitle = c.getString(1);
                }while (c.moveToNext());
            }
            if(urge){
                Toast.makeText(this,"Enviada", Toast.LENGTH_SHORT).show();
                DB.addNote(autor,texto,"S");
                actividad(autor,texto,"S");
            }else{
                Toast.makeText(this,"Enviada", Toast.LENGTH_SHORT).show();
                DB.addNote(autor,texto,"N");
                actividad(autor,texto,"N");
            }
        }else{
            Toast.makeText(this,"Introduzca un autor", Toast.LENGTH_SHORT).show();
        }
    }

    public void actividad (String autor, String texto, String urge){
        Intent intent = new Intent(AddNota.this,VerNota.class);
        intent.putExtra("autor",autor);
        intent.putExtra("texto",texto);
        intent.putExtra("urge",urge);
        startActivity(intent);
    }

    private boolean enviarDatos()  {
        try {
            URL obj = new URL("http_//192.168.0.32/Proyectos_PHP/WebService/insertarDatos.php");
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
