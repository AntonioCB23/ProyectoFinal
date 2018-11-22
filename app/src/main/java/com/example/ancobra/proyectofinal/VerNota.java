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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class VerNota extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{
    String autor, texto;
    Boolean urgente;
    TextView AUTOR, TEXTO;
    CheckBox URGE;
    BDadap db;
    boolean offline;
    ProgressDialog progreso;
    SharedPreferences prefs; //PREFERENCIAS
    SharedPreferences.Editor editor; //EDITOR DE PREFENCIAS
    String database, ip;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    private static final int DELETE = Menu.FIRST + 1;
    private static final int ACTUALIZAR = Menu.FIRST;
    private static final int SUBIR = Menu.FIRST+2;
    @Override
    protected void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
        setContentView(R.layout.visnota);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setTitle("WeUnite > Visualizar nota");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5641cb")));

        //INICIALIZACION DE VARIABLES Y OBTENCION DE DATOS DE PREFERENCIAS
        prefs = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();
        offline = prefs.getBoolean("off",true);
        if(prefs.getBoolean("off",true)){
            database = "notasPersonales";
        }else{
            database = "Notas";
        }
        ip = prefs.getString("ip","");
        Bundle bundle = this.getIntent().getExtras();
        autor = bundle.getString("autor");
        texto = bundle.getString("texto");
        AUTOR = findViewById(R.id.textView_autor);
        TEXTO = findViewById(R.id.textView_texto);
        URGE = findViewById(R.id.checkBox_Urge);
        request = Volley.newRequestQueue(this);
        AUTOR.setText(autor);
        TEXTO.setText(texto);

        //SEGUN LA URGENCIA DE LA NOTA CHECKEA EL CKECKBOX O NO
        if(bundle.getString("urge").equals("S")){
            URGE.setChecked(true);
        }else{
            URGE.setChecked(false);
        }
        URGE.setEnabled(false);

    }

    /**
     * Añade las opciones de menu en la parte superior
     * @param menu menu a gestionar
     * @return true
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        menu.add(1,ACTUALIZAR,0,R.string.menu_act);
        menu.add(2,DELETE,0,R.string.menu_borrar);
        if(offline){
            menu.add(3,SUBIR,0,R.string.menu_subir);
        }

        super.onCreateOptionsMenu(menu);
        return true;
    }

    /**
     * Gestiona la pulsacion del menu
     * @param item opcion seleciona
     * @return la opcion correcta
     */
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case ACTUALIZAR:
                return true;
            case DELETE:
                //MENSAJE DE CONFIRMACION
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Mensaje de confirmación");
                builder.setMessage("A continuación se borrará la nota, ¿estás seguro?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrar();
                        borrarDatos();
                        Toast.makeText(getApplicationContext(), "Has seleccionado borrar los datos", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Cancelada la operación", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
                return true ;
            case SUBIR:
                //MENSAJE DE CONFIRMACION
                AlertDialog.Builder builderSubir = new AlertDialog.Builder(this);
                builderSubir.setTitle("Mensaje de confirmación");
                builderSubir.setMessage("¿Subir la nota al servidor compartido?");
                builderSubir.setCancelable(false);
                builderSubir.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            enviaDatos();
                        Toast.makeText(getApplicationContext(), "Se subirán los datos", Toast.LENGTH_SHORT).show();
                    }
                });

                builderSubir.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Cancelada la operación", Toast.LENGTH_SHORT).show();
                    }
                });

                builderSubir.show();
                    return true;
            default:
                return onOptionsItemSelected(item);
        }
    }
    public void borrar (){
        db = new BDadap(this,database);
        db.deleteNote(autor);
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        Intent intent = new Intent(VerNota.this,MenuApp.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * Borra los datos solicitados de la bd externa
     */
    private void borrarDatos() {
        progreso = new ProgressDialog(this);
        progreso.setMessage("Borrando datos....");
        progreso.show();
        String url="";
        if(URGE.isChecked()){
            url = "http://"+ip+"/WebService/borrarDatos.php?autor="+AUTOR.getText().toString()+
                    "&texto="+TEXTO.getText().toString()+"&urgente=S";
        }else{
            url = "http://"+ip+"/WebService/borrarDatos.php?autor="+AUTOR.getText().toString()+
                    "&texto="+TEXTO.getText().toString()+"&urgente=N";
        }

        url = url.replace(" ","%20");
        Log.i("Response: ", ""+url);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

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
        url = url.replace("\n","%20");//REMPLAZA LOS ESPACIOS PORQUE SINO DARIA ERROR
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    private void addNota(){
        BDadap DB = new BDadap(this,database);
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
                DB.addNote(autor,texto,"S");
            }else{
                DB.addNote(autor,texto,"N");
            }
        }else{
            Toast.makeText(this,"Introduzca un autor y un texto", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Si la sentencia es erronea entra aqui
     * @param error error que da el metodo
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
    }

    /**
     * Si la sentencia es correcta entra aqui
     * @param response
     */
    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
    }
}
