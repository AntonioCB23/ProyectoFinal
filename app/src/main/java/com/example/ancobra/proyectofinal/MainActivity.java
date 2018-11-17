package com.example.ancobra.proyectofinal;

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

import java.text.DecimalFormat;
import java.text.ParseException;

public class MainActivity extends AppCompatActivity {

    EditText txtIp;
    Button btnOff;
    SharedPreferences prefs; //PREFERENCIAS
    SharedPreferences.Editor editor; //EDITOR DE PREFENCIAS
    private DecimalFormat df;
    StringBuffer buffer;
    boolean comprob;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.btnAcceder);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        comprob=true;
        //df = new DecimalFormat("###.###.#.##");
        btnOff = findViewById(R.id.btnOffline);
        txtIp = findViewById(R.id.txtIp);

        prefs = this.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        editor = prefs.edit();

        txtIp.setText(prefs.getString("ip",""));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Response: ","---"+comprob);
                if(ipPuntos(txtIp.getText().toString())){
                    editor.putBoolean("off",false);
                    editor.putString("ip", txtIp.getText().toString());
                    editor.commit();
                    Intent intent= new Intent(MainActivity.this, MenuApp.class);
                    startActivity(intent);
                }else if (ip(txtIp.getText().toString()) && comprob){
                    editor.putBoolean("off",false);
                editor.putString("ip", buffer.toString());
                editor.commit();
                Intent intent= new Intent(MainActivity.this, MenuApp.class);
                startActivity(intent);
                }else{
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
    private boolean ip(String ip){
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
        Log.i("Response: ",buffer.toString());
        return b;
    }

    private boolean ipPuntos(String ip){
        Boolean b = false;
        if (ip.length()==12){
            Log.i("Response: ","I'm in");
            for (int i = 0; i < ip.length(); i++) {
                if(ip.charAt(i)>=46 && ip.charAt(i)<=57){
                    if(i==3 ||i==7||i==9){
                        if(ip.charAt(i)==46){
                        }else{
                            Log.i("Response: ",""+ip.charAt(i));
                            b=false;
                            comprob=false;
                            break;
                        }
                    }
                    b = true;
                }else {
                    Log.i("Response: ","errorsito");
                    b=false;
                    break;
                }
            }
        }else{
        }
        return b;
    }
}
