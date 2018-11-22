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
import android.text.LoginFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {
    EditText USER, PWD; //EDITTEXT DEL ACTIVITY
    Button LOGIN,REGISTER; //BOTONES DEL ACTIVITY
    AdapBDLogin DB;
    SharedPreferences prefs; //PREFERENCIAS
    SharedPreferences.Editor editor; //EDITOR DE PREFENCIASS
    ProgressDialog progreso; //VENTANA DE CARGA
    String database, ip; //STRINGS AUXILIARES
    RequestQueue request; //REQUEST UTILIZADO PARA REALIZAR LA CONSULTA A LA BD
    JsonObjectRequest jsonObjectRequest; //JSON UTILIZADO EN LA CONSULTA
    boolean valido = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setTitle("WeUnite > Iniciar Sesión");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5641cb")));

        //INICIALIZACION DE VARIABLES Y OBTENCION DE DATOS VARIOS
        prefs = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();

        ip = prefs.getString("ip","");
        USER = findViewById(R.id.editText_User);
        PWD = findViewById(R.id.editText_Pwd);
        LOGIN = findViewById(R.id.btnLogin);
        REGISTER = findViewById(R.id.btnRegistrar);
        request = Volley.newRequestQueue(this);

        LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobarUser();
            }
        });

        REGISTER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it= new Intent(Login.this, Register.class);
                startActivity(it);
            }
        });
    }

    private void comprobarUser(){
        progreso = new ProgressDialog(this);
        progreso.setMessage("Comprobando....");
        progreso.show();
        String user, pass;
        user = USER.getText().toString().trim();
        pass = PWD.getText().toString().trim();
        String url="";
        if (!user.equals("") && !pass.equals("")){
            url = "http://"+ip+"/WebService/compruebaUsuario.php?user="+USER.getText().toString()+
                    "&pwd="+PWD.getText().toString();
            url = url.replace(" ","%20");//REMPLAZA LOS ESPACIOS PORQUE SINO DARIA ERROR
            Log.i("Response: ",url);
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            request.add(jsonObjectRequest);
        }
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle("Error");
        builder.setMessage("Datos no encontrados");
        builder.setCancelable(false);
        builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                valido = false;
            }
        });
        builder.show();
    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide(); //OCULTA LA VENTANA DE CARGA PUESTO QUE SE HA CARGADO LOS DATOS CORRECTAMENTE
        try {
            JSONArray json = response.getJSONArray("usuario");//OBTENEMOS EL JSONARRAY
            for (int i = 0; i < json.length() ; i++) {
                valido = true;
            }

            if(valido){
                editor.putString("autor",USER.getText().toString());
                editor.commit();
                Intent it2= new Intent(Login.this, MenuApp.class);
                startActivity(it2); //LANZA EL MENU
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
