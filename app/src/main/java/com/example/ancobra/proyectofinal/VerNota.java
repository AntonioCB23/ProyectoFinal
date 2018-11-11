package com.example.ancobra.proyectofinal;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.CheckBox;
import android.widget.TextView;

public class VerNota extends AppCompatActivity {
    String autor, texto;
    Boolean urgente;
    TextView AUTOR, TEXTO;
    CheckBox URGE;
    BDadap db;

    private static final int DELETE = Menu.FIRST;
    @Override
    protected void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
        setContentView(R.layout.visnota);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setIcon(R.drawable.icon);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00a6a8")));

        Bundle bundle = this.getIntent().getExtras();

        autor = bundle.getString("autor");
        texto = bundle.getString("texto");
        AUTOR = findViewById(R.id.textView_autor);
        TEXTO = findViewById(R.id.textView_texto);
        URGE = findViewById(R.id.checkBox_Urge);

        AUTOR.setText(autor);
        TEXTO.setText(texto);
        if(bundle.getString("urge").equals("S")){
            URGE.setChecked(true);
        }else{
            URGE.setChecked(false);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        menu.add(1,DELETE,0,R.string.menu_borrar);
        super.onCreateOptionsMenu(menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case DELETE:
                borrar();
                return true ;
            default:
                return onOptionsItemSelected(item);
        }
    }
    public void borrar (){
        db = new BDadap(this);
        db.deleteNote(autor);
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        Intent intent = new Intent(VerNota.this,MenuApp.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
