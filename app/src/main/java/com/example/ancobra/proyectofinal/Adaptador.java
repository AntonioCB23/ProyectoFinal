package com.example.ancobra.proyectofinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {
    ArrayList<Publicacion> publicaciones;
    LayoutInflater inflador;
    public Adaptador(Context contexto, ArrayList<Publicacion> lista) {
        this.inflador = LayoutInflater.from(contexto);
        this.publicaciones = lista;
    }
    @Override
    public int getCount() {
        return publicaciones.size();
    }
    // Devuelve el elemento asociado con la posición en el ListView
    @Override
    public Object getItem(int position) {
        return publicaciones.get(position);
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
        Publicacion nota = publicaciones.get(position);
        contenedor.txtAutor.setText(nota.getAutor());
        if(nota.getContenido().length()>50){
            contenedor.txtDescripcion.setText(nota.getContenido().substring(0,50)+"...");
        }else{
            contenedor.txtDescripcion.setText(nota.getContenido());
        }
        return convertView;
    }
    class ViewHolder {
        TextView txtAutor, txtDescripcion;
    }
}
