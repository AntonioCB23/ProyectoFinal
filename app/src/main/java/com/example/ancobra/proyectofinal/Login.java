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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase Login contiene los componentes necesarios para realizar el login de usuarios
 */
public class Login extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {
    EditText USER, PWD; //EDITTEXT DEL ACTIVITY
    Button LOGIN,REGISTER; //BOTONES DEL ACTIVITY
    SharedPreferences prefs; //PREFERENCIAS
    SharedPreferences.Editor editor; //EDITOR DE PREFENCIASS
    ProgressDialog progreso; //VENTANA DE CARGA
    String ip; //STRINGS AUXILIARES
    RequestQueue request; //REQUEST UTILIZADO PARA REALIZAR LA CONSULTA A LA BD
    JsonObjectRequest jsonObjectRequest; //JSON UTILIZADO EN LA CONSULTA
    boolean valido = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setTitle("WeUnite > "+getString(R.string.login));
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
        USER.requestFocus();
        //BOTON DE LOGIN
        LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Si hay algun campo vacio no hace nada
                if(!USER.getText().toString().equals("") && !PWD.getText().toString().equals("") ){
                    comprobarUser();
                }else{
                    muestraError(""+getString(R.string.campos));
                }
            }
        });

        //BOTON DE REGISTRO
        REGISTER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it= new Intent(Login.this, Register.class);
                startActivity(it);
            }
        });
    }

    /**
     * Funcion que comprueba que existe el usuario en la bd externa
     */
    private void comprobarUser(){
        progreso = new ProgressDialog(this);
        progreso.setMessage(""+getString(R.string.cargando));
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
    /**
     * Si la conexion con el Response no es valida entra aqui
     * @param error error que da el response
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        muestraError("Datos no encontrados");
    }

    /**
     * Si la conexion con el Response es valida entra aqui
     * @param response valor que retorna el response
     */
    @Override
    public void onResponse(JSONObject response) {
        progreso.hide(); //OCULTA LA VENTANA DE CARGA PUESTO QUE SE HA CARGADO LOS DATOS CORRECTAMENTE
        try {
            JSONArray json = response.getJSONArray("usuario");//OBTENEMOS EL JSONARRAY
            for (int i = 0; i < json.length() ; i++) {
                valido = true; //Si hay algún registro en la bd pues valida el usuario
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

    /**
     * Funcion que unicamente saca un AlertDialog de 1 boton que tendra el texto que queramos
     * @param textoError texto a mostrar en el cuadro de dialogo
     */
    private void muestraError(String textoError){
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle("Error");
        builder.setMessage(textoError);
        builder.setCancelable(false);
        builder.setPositiveButton(""+getString(R.string.alert_entendido), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                valido = false;
            }
        });
        builder.show();
    }
}
