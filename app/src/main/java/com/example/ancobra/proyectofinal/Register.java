package com.example.ancobra.proyectofinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Register extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {
    EditText USER, PWD, PWD2; //EDITTEXT DEL ACTIVITY
    Button REGISTER; //BOTONES DEL ACTIVITY
    AdapBDLogin DB;
    SharedPreferences prefs; //PREFERENCIAS
    SharedPreferences.Editor editor; //EDITOR DE PREFENCIASS
    ProgressDialog progreso; //VENTANA DE CARGA
    String database, ip; //STRINGS AUXILIARES
    RequestQueue request; //REQUEST UTILIZADO PARA REALIZAR LA CONSULTA A LA BD
    JsonObjectRequest jsonObjectRequest; //JSON UTILIZADO EN LA CONSULTA
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setTitle("WeUnite > Registrar Usuario");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5641cb")));

        //INICIALIZACION DE VARIABLES Y OBTENCION DE DATOS VARIOS
        prefs = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();

        ip = prefs.getString("ip","");
        USER = findViewById(R.id.editText_Username);
        PWD = findViewById(R.id.editText_contra1);
        PWD2 = findViewById(R.id.editText_contra2);
        REGISTER = findViewById(R.id.btnReg);
        request = Volley.newRequestQueue(this);

        REGISTER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertarUsuario();
                insertaBDremota();
                Intent it= new Intent(Register.this, Login.class);
                startActivity(it);
            }
        });
    }

    protected void insertarUsuario(){
        DB = new AdapBDLogin(this);
        String user, pass,pass2;
        user = USER.getText().toString().trim();
        pass = PWD.getText().toString().trim();
        pass2 = PWD2.getText().toString().trim();

        if (!user.equals("") && !pass.equals("")&& !pass2.equals("")){
            if(pass.equals(pass2)){
                DB.addUser(user,pass);
            }
        }else{

        }
    }
    /**
     * Metodo que gestiona el añadido de la nota a la bd externa
     */
    private void insertaBDremota() {
        progreso = new ProgressDialog(this);
        progreso.setMessage("Enviando datos....");
        progreso.show();
        String url="";

        //COMPRUEBA SI EL CHECK DE URGENTE ESTA ACTIVO, ASI REALIZA UNA CONSULTA U OTRA
            url = "http://"+ip+"/WebService/regUsuario.php?user="+USER.getText().toString()+
                    "&pwd="+PWD.getText().toString();
        url = url.replace(" ","%20");//REMPLAZA LOS ESPACIOS PORQUE SINO DARIA ERROR
        Log.i("Response: ",url);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
    }
}
