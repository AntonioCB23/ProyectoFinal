package com.example.ancobra.proyectofinal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;




public class Adaptador extends BaseAdapter {
    ArrayList<Nota> Notas;
    LayoutInflater inflador;

    public Adaptador(Context contexto, ArrayList<Nota> lista) {
        this.inflador = LayoutInflater.from(contexto);
        this.Notas = lista;
    }
    @Override
    public int getCount() {
        return Notas.size();
    }
    // Devuelve el elemento asociado con la posición en el ListView
    @Override
    public Object getItem(int position) {
        return Notas.get(position);
    }
    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder contenedor = null;
        if (convertView == null){
            convertView = inflador.inflate(R.layout.fila, null);
            contenedor = new ViewHolder();
            contenedor.txtAutor = (TextView) convertView.findViewById(R.id.textView2);
            contenedor.txtDescripcion=(TextView) convertView.findViewById(R.id.textView3);
            convertView.setTag(contenedor);
        } else contenedor=(ViewHolder)convertView.getTag();
        Nota nota = Notas.get(position);
        contenedor.txtAutor.setText(nota.getAutor());
        if(nota.getTexto().length()>50){
            contenedor.txtDescripcion.setText(nota.getTexto().substring(0,50)+"...");
        }else{
            contenedor.txtDescripcion.setText(nota.getTexto());
        }
        return convertView;
    }
    class ViewHolder {
        TextView txtAutor, txtDescripcion;
    }
}
