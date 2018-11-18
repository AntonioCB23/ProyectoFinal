package com.example.ancobra.proyectofinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class AddNota extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{
    EditText AUTOR, TEXTO; //EDITTEXT DEL ACTIVITY
    CheckBox URGE; //CHECKBOX DEL ACTIVITY
    private static final int SALIR = Menu.FIRST; //OPCION DE MENU
    BDadap DB; //ADAPTADOR DE LA BD
    ProgressDialog progreso; //VENTANA DE CARGA
    SharedPreferences prefs; //PREFERENCIAS
    SharedPreferences.Editor editor; //EDITOR DE PREFENCIASS
    String database, ip; //STRINGS AUXILIARES
    RequestQueue request; //REQUEST UTILIZADO PARA REALIZAR LA CONSULTA A LA BD
    JsonObjectRequest jsonObjectRequest; //JSON UTILIZADO EN LA CONSULTA

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnota);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setTitle("WeUnite > Añadir nota");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5641cb")));

        //INICIALIZACION DE VARIABLES Y OBTENCION DE DATOS VARIOS
        prefs = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();
        if(prefs.getBoolean("off",true)){
            database = "notasPersonales";
        }else{
            database = "Notas";
        }
        ip = prefs.getString("ip","");
        Button add = findViewById(R.id.button_Add);
        AUTOR = findViewById(R.id.editText_Autor);
         TEXTO = findViewById(R.id.editText_Texto);
         URGE = findViewById(R.id.checkBox);
        request = Volley.newRequestQueue(this);

        //CLICK DEL BOTON
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SI LA BASE DE DATOS SOBRE LA QUE ACTUAMOS HARA UNA COSA U OTRA, VA RELACIONADO AL MODO ONLINE Y OFFLINE
                if(database.equals("Notas")){
                    enviaDatos();
                    addNota();
                }else{
                    addNota();
                }

            }
        });
    }

    /**
     * Metodo que gestiona el añadido de la nota a la bd externa
     */
    private void enviaDatos() {
        progreso = new ProgressDialog(this);
        progreso.setMessage("Enviando datos.... Si tarda demasiado debería revisar si la ip introducida es correcta");
        progreso.show();
        String url="";

        //COMPRUEBA SI EL CHECK DE URGENTE ESTA ACTIVO, ASI REALIZA UNA CONSULTA U OTRA
        if (URGE.isChecked()){
             url = "http://"+ip+"/WebService/enviarDatos.php?autor="+AUTOR.getText().toString()+
                    "&texto="+TEXTO.getText().toString()+"&urgente=S";
        }else{
            url = "http://"+ip+"/WebService/enviarDatos.php?autor="+AUTOR.getText().toString()+
                    "&texto="+TEXTO.getText().toString()+"&urgente=N";
        }
        url = url.replace(" ","%20");//REMPLAZA LOS ESPACIOS PORQUE SINO DARIA ERROR
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    /**
     * Añade las opciones de menu en la parte superior
     * @param menu menu a gestionar
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        super.onCreateOptionsMenu(menu);
        menu.add(1,SALIR,0,R.string.menu_salir);
        return  true;
    }

    /**
     * Gestiona la pulsacion del menu
     * @param item opcion seleciona
     * @return la opcion correcta
     */
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
        DB = new BDadap(this,database);
        String autor, texto;
        Boolean urge;
        autor = AUTOR.getText().toString().trim();
        texto = TEXTO.getText().toString().trim();
        urge = URGE.isChecked();

        if (!autor.equals("") ||!texto.equals("")){
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
            Toast.makeText(this,"Introduzca un autor y un texto", Toast.LENGTH_SHORT).show();
        }
    }

    public void actividad (String autor, String texto, String urge){
        Intent intent = new Intent(AddNota.this,VerNota.class);
        intent.putExtra("autor",autor);
        intent.putExtra("texto",texto);
        intent.putExtra("urge",urge);
        startActivity(intent);
    }

    /**
     * Si la conexion es erronea entra aqui
     * @param error error asociado
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
    }

    /**
     * Si la conexion es correcta entra aqui
     * @param response la consulta a la bd
     */
    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
        limpia();
        finish();
    }

    /**
     * limpia los datos
     */
    public void limpia(){
        AUTOR.setText("");
        TEXTO.setText("");
        URGE.setChecked(false);
    }

}
