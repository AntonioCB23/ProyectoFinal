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

/**
 * Clase VerNota que gestiona la visualizacion de la nota seleccionada
 */
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
    private static final int SUBIR = Menu.FIRST+2;
    @Override
    protected void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
        setContentView(R.layout.visnota);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setTitle("WeUnite > "+getString(R.string.ver_nota));
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        menu.add(2,DELETE,0,R.string.menu_borrar);
        if(!prefs.getBoolean("off",true)){ //Si estamos en modo offline no tiene sentido subir la nota al servidor, puesto que no hay conexión ninguna
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case DELETE:
                //MENSAJE DE CONFIRMACION
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(""+getString(R.string.alert_confirm));
                builder.setMessage(""+getString(R.string.borrarNota));
                builder.setCancelable(false);
                builder.setPositiveButton(""+getString(R.string.alert_si), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrar();
                        borrarDatos();
                        muestraError(""+getString(R.string.borrarConfirm));
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        muestraError(""+getString(R.string.datos_cancel));
                    }
                });

                builder.show();
                return true ;
            case SUBIR:
                //MENSAJE DE CONFIRMACION
                AlertDialog.Builder builderSubir = new AlertDialog.Builder(this);
                builderSubir.setTitle(""+getString(R.string.alert_confirm));
                builderSubir.setMessage(""+getString(R.string.alert_quest));
                builderSubir.setCancelable(false);
                builderSubir.setPositiveButton(""+getString(R.string.alert_si), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AUTOR.setText(prefs.getString("autor",""));
                            enviaDatos();
                            muestraError(""+getString(R.string.datos_correcto));
                    }
                });

                builderSubir.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        muestraError(""+R.string.datos_cancel);
                    }
                });

                builderSubir.show();
                    return true;
            default:
                return onOptionsItemSelected(item);
        }
    }
    public void borrar (){
        Intent itAnterior = getIntent();
        if(itAnterior.getStringExtra("offline").equals("Normal")){
            database = "Notas";
        }else{
            database = "notasPersonales";
        }
        db = new BDadap(this,database);
        db.deleteNote(TEXTO.getText().toString());
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
        progreso.setMessage(""+R.string.inserta_progress);
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
    /**
     * Si la sentencia es erronea entra aqui
     * @param error error que da el metodo
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        muestraError(error.toString());
    }

    /**
     * Si la sentencia es correcta entra aqui
     * @param response
     */
    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
    }

    /**
     * Funcion que unicamente saca un AlertDialog de 1 boton que tendra el texto que queramos
     * @param textoError texto a mostrar en el cuadro de dialogo
     */
    private void muestraError(String textoError){
        AlertDialog.Builder builder = new AlertDialog.Builder(VerNota.this);
        builder.setTitle("Error");
        builder.setMessage(textoError);
        builder.setCancelable(false);
        builder.setPositiveButton(""+getString(R.string.alert_entendido), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
        builder.show();
    }
}
