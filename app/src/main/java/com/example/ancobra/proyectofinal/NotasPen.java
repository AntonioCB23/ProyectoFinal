package com.example.ancobra.proyectofinal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
    private static final int ADD = Menu.FIRST;
    private static final int DELETE = Menu.FIRST +1;
    private static final int EXIST = Menu.FIRST +2;
    ListView lvNotas;

    Button btnHit;
    TextView txtJson;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notaspendientes);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00a6a8")));

        lvNotas = findViewById(R.id.lvNotas);

        btnHit = (Button) findViewById(R.id.btnHit);
        txtJson = (TextView) findViewById(R.id.tvJsonItem);


        request = Volley.newRequestQueue(this);

        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarWebService();
                //new JsonTask().execute("http://192.168.0.32/WebService/conexion.php");
            }
        });
    }

    private void cargarWebService() {
        progreso = new ProgressDialog(this);
        progreso.setMessage("Consultando");
        progreso.show();
        String url = "http://192.168.0.32/WebService/conexion.php";

        jsonObjectRequest = new JsonObjectRequest(url,null,this,this);
        request.add(jsonObjectRequest);

    }

    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
        Log.i("Response: ", ""+response);

        Nota nota = new Nota();

        try {
            JSONArray json = response.getJSONArray("notas");
            JSONObject jsonObject = null;
            jsonObject = json.getJSONObject(0);
            nota.setAutor(jsonObject.optString("Autor"));
            nota.setTexto(jsonObject.optString("Contenido"));
            nota.setUrgente(jsonObject.optString("Urgente"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        txtJson.setText(nota.getAutor()+" "+nota.getTexto()+" "+nota.getUrgente());
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        menu.add(1,ADD,0,R.string.menu_crear);
        menu.add(2,EXIST,0,R.string.menu_salir);
        super.onCreateOptionsMenu(menu);
        return true;
    }
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
