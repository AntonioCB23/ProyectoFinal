package com.example.ancobra.proyectofinal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.print.PrintDocumentAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ResponseCache;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NotasPen extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{
    //OPCIONES DE MENU
    private static final int ADD = Menu.FIRST;
    private static final int EXIST = Menu.FIRST +2;

    public static final int MY_DEFAULT_TIMEOUT = 5000;//TIMEOUT DE LA CONSULTA
    ListView lvNotas; //LISTVIEW
    ProgressDialog progreso; //VENTANA DE CARGA
    RequestQueue request; //REQUEST PARA GESTIONAR LA CONSULA
    JsonObjectRequest jsonObjectRequest; // jsonObjectRequest PARA GESTIONAR LA CONSULTA A LA BD
    ArrayList<Nota> arrayNotas; //ARRAY DE LA CLASE NOTA DONDE SE GUARDARAN LAS NOTAS
    SharedPreferences prefs; //PREFERENCIAS
    SharedPreferences.Editor editor; //EDITOR DE PREFENCIAS
    String ip; //IP QUE SE UTILIZA


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notaspendientes);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setTitle("WeUnite > Notas pendientes");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5641cb")));

        //INICIALIZACION DE VARIABLES Y CARGA DE DATOS
        lvNotas = findViewById(R.id.lvNotas);
        prefs = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();
        ip = prefs.getString("ip","");
        arrayNotas = new ArrayList<>();
        request = Volley.newRequestQueue(this);
        cargaDatos();

        //CLICK DEL ELEMENTO DEL LISTVIEW
        lvNotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),VerNota.class);
                intent.putExtra("autor",arrayNotas.get(i).getAutor());
                intent.putExtra("texto",arrayNotas.get(i).getTexto());
                intent.putExtra("urge",arrayNotas.get(i).getUrgente());
                startActivity(intent);
            }
        });
    }

    /**
     * Carga los datos que existen en la tabla externa
     */
    private void cargaDatos() {
        progreso = new ProgressDialog(this);
        progreso.setMessage("Consultando.. Si tarda demasiado debería comprobar que la dirección ip sea correcta");
        progreso.show();
        String url = "http://"+ip+"/WebService/conexion.php";
        jsonObjectRequest = new JsonObjectRequest(url,null,this,this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.add(jsonObjectRequest);

    }

    @Override
    public void onErrorResponse(VolleyError error) {
    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide(); //OCULTA LA VENTANA DE CARGA PUESTO QUE SE HA CARGADO LOS DATOS CORRECTAMENTE
        Nota nota;
        try {
            JSONArray json = response.getJSONArray("notas");//OBTENEMOS EL JSONARRAY
            for (int i = 0; i < json.length() ; i++) {
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);
                nota = new Nota(jsonObject.optString("Autor"),jsonObject.optString("Contenido"),jsonObject.optString("Urgente"));
                arrayNotas.add(nota);//AÑADIMOS LA NOTA PREVIAMENTE INICIALIZADA AL ARRAY
            }

            //AÑADIMOS AL ADAPTADOR PERSONALIZADO EL ARRAY DE NOTAS Y SE LO ASIGNAMOS AL LISTVIEW
            Adaptador adap = new Adaptador(NotasPen.this, arrayNotas);
            lvNotas.setAdapter(adap);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Añade las opciones de menu en la parte superior
     * @param menu menu a gestionar
     * @return true
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        menu.add(1,ADD,0,R.string.menu_crear);
        menu.add(2,EXIST,0,R.string.menu_salir);
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
            case ADD:
                Intent intent= new Intent(getApplicationContext(),AddNota.class);
                intent.putExtra("type","addExterno");
                startActivity(intent);
                return true ;
            case EXIST:
                finish();
                return true ;
            default:
                return onOptionsItemSelected(item);
        }
    }
}