package com.example.ancobra.proyectofinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

/**
 * Clase que controla el registro del usuario en la app
 */
public class Register extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    EditText USER, PWD, PWD2; //EDITTEXT DEL ACTIVITY
    Button REGISTER; //BOTONES DEL ACTIVITY
    AdapBDLogin DB; //ADAPTADOR QUE CONTROLARA LA BD INTERNA
    SharedPreferences prefs; //PREFERENCIAS
    SharedPreferences.Editor editor; //EDITOR DE PREFENCIASS
    ProgressDialog progreso; //VENTANA DE CARGA
    String ip; //STRINGS AUXILIARES
    RequestQueue request; //REQUEST UTILIZADO PARA REALIZAR LA CONSULTA A LA BD
    JsonObjectRequest jsonObjectRequest; //JSON UTILIZADO EN LA CONSULTA

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setTitle("WeUnite > "+getString(R.string.regUser));
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
                if (!USER.getText().toString().equals("") && !PWD.getText().toString().equals("")&& !PWD2.getText().toString().equals("")){
                    if(PWD.getText().toString().equals(PWD2.getText().toString())){
                        insertarUsuario();
                        insertaBDremota();
                        Intent it= new Intent(Register.this, Login.class);
                        startActivity(it);
                    }else{
                        muestraError(""+getString(R.string.pwdError));
                    }
                }else{
                    muestraError(""+getString(R.string.campos));
                }

            }
        });
    }

    /**
     * Inserta el usuario en la bd interna de usuarios
     */
    protected void insertarUsuario(){
        DB = new AdapBDLogin(this);
        String user, pass;
        user = USER.getText().toString().trim();
        pass = PWD.getText().toString().trim();
        DB.addUser(user,pass);
    }

    /**
     * Metodo que gestiona el registro del usuario en la bd externa
     */
    private void insertaBDremota() {
        progreso = new ProgressDialog(this);
        progreso.setMessage(""+R.string.inserta_progress);
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
        muestraError(error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
    }

    /**
     * Funcion que unicamente saca un AlertDialog de 1 boton que tendra el texto que queramos
     * @param textoError texto a mostrar en el cuadro de dialogo
     */
    private void muestraError(String textoError){
        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
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
