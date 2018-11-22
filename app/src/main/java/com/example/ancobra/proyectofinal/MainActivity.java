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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;

public class MainActivity extends AppCompatActivity  implements Response.Listener<JSONObject>,Response.ErrorListener{

    EditText txtIp; //EDTITEXT DE IP
    Button btnOff, btn; //BOTON DE MODO OFFLINE y EL DE ACCEDER
    SharedPreferences prefs; //PREFERENCIAS
    SharedPreferences.Editor editor; //EDITOR DE PREFENCIAS
    StringBuffer buffer; //BUFFER DE STRING PARA LA GESTION DE LA IP
    boolean comprob, valida; //BOOLEAN DE COMPROBACION
    ProgressDialog progreso; //VENTANA DE CARGA
    RequestQueue request; //REQUEST PARA GESTIONAR LA CONSULA
    JsonObjectRequest jsonObjectRequest; // jsonObjectRequest PARA GESTIONAR LA CONSULTA A LA BD
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ESTE CODIGO SE REPETIRA POR TODAS LAS ACTIVITY, PUESTO QUE GESTIONA EL FULLSCREEN DEL DISPOSITIVO ENTRE OTROS ASPECTOS
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//ACCIONA EL ACTIVITY A PANTALLLA COMPLETA
        getSupportActionBar().setIcon(R.drawable.icon); //PONE EL ICONO EN LA PARTE SUPERIOR IZQUIERDA
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000"))); //COLOREA LA BARRA SUPERIOR DEL COLOR DESEADO



        request = Volley.newRequestQueue(this);

        comprob=true;
        valida=false;
        btn = (Button) findViewById(R.id.btnAcceder);
        btnOff = findViewById(R.id.btnOffline);
        txtIp = findViewById(R.id.txtIp);

        //OBTENCION DE PREFERENCIAS Y SU EDITOR
        prefs = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();

        txtIp.setText(prefs.getString("ip","")); //SITUA EN EL EDITTEXT LA ULTIMA IP INTRODUCIDA

        //CLICK DEL BOTON ACCEDER
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ipPuntos(txtIp.getText().toString())){
                    cargaDatos();
                }else if (ipSinPuntos(txtIp.getText().toString()) && comprob){
                txtIp.setText(buffer.toString());
                    cargaDatos();
            }else{
                //VENTANA DE ERROR
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Error");
                builder.setMessage("Ip no válida, introduzca la ip en formato ###.###.#.## o sin puntos");
                builder.setCancelable(false);
                builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }

            }
        });

        //BOTON DE MODO OFFLINE
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("off",true);
                editor.commit();
                Intent intent= new Intent(MainActivity.this, MenuApp.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Comprueba que la cadena sea numerica, y en su caso, introduce los puntos en los espacios para que la ip sea correcta
     * @param ip ip introducida sin puntos
     * @return devuelve si la ip es correcta
     */
    private boolean ipSinPuntos(String ip){
        buffer = new StringBuffer();
        Boolean b = false;
        if (ip.length()==9){
            for (int i = 0; i < ip.length(); i++) {
                if(ip.charAt(i)>=48 && ip.charAt(i)<=57){
                    buffer.append(ip.charAt(i));
                    if(i==2 ||i==5||i==6){
                        buffer.append(".");
                    }
                    b = true;
                }else {
                    break;
                }
            }
        }else{

        }
        return b;
    }

    /**
     * Comprueba que la ip posee el formato correcto (###.###.#.##)
     * @param ip ip asociada que se comprobara
     * @return devuelve si la ip es valida o no
     */
    private boolean ipPuntos(String ip){
        Boolean b = false;
        if (ip.length()==12){
            for (int i = 0; i < ip.length(); i++) {
                if(ip.charAt(i)>=46 && ip.charAt(i)<=57){
                    if(i==3 ||i==7||i==9){
                        if(ip.charAt(i)==46){
                        }else{
                            b=false;
                            comprob=false;
                            break;
                        }
                    }
                    b = true;
                }else {
                    b=false;
                    break;
                }
            }
        }else{
        }
        return b;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Error");
        builder.setMessage("Error en la conexión, compruebe su ip y/o conexión");
        builder.setCancelable(false);
        builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                valida = false;
            }
        });
        builder.show();
    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
        valida = true;
        Log.i("Response:","Entra");
        if(!prefs.getBoolean("introducir",false)){
            if(valida){
                //Intent it= new Intent(MainActivity.this, Login.class);
                //startActivity(it);
            }
            if(ipPuntos(txtIp.getText().toString())){
                editor.putBoolean("off",false);//SITUA SI LA APP SE ACCIONA EN MODO OFFLINE
                editor.putString("ip", txtIp.getText().toString()); //GUARDA LA IP INTRODUCIDA PARA LA PROXIMA VEZ QUE SE ACCIONE LA APP
                editor.commit(); //GUARDA LOS CAMBIOS
                if(valida){
                    Intent it2= new Intent(MainActivity.this, Login.class);
                    startActivity(it2); //LANZA EL MENU
                }

            }else if (ipSinPuntos(txtIp.getText().toString()) && comprob){
                editor.putBoolean("off",false);
                editor.putString("ip", buffer.toString());
                editor.commit();

            }else{
                //VENTANA DE ERROR
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Error");
                builder.setMessage("Ip no válida, introduzca la ip en formato ###.###.#.## o sin puntos");
                builder.setCancelable(false);
                builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }

        }else{
            Intent intent= new Intent(MainActivity.this, VerNota.class);
            Bundle bundle = getIntent().getExtras();
            //Bundle bundle = getIntent().getExtras();
            intent.putExtra("autor",bundle.getString("autor"));
            intent.putExtra("texto",bundle.getString("texto"));
            intent.putExtra("urge",bundle.getString("urge"));
            startActivity(intent);
        }
    }
    private void cargaDatos() {
        progreso = new ProgressDialog(this);
        progreso.setMessage("Comprobando la conexión");
        progreso.show();
        String url = "http://"+txtIp.getText().toString().trim()+"/WebService/comprueba.php";
        jsonObjectRequest = new JsonObjectRequest(url,null,this,this);
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                0));
        request.add(jsonObjectRequest);

    }
}
