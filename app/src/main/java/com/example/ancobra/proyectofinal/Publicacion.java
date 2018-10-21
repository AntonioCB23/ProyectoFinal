package com.example.ancobra.proyectofinal;

public class Publicacion {
String contenido, autor;
        boolean urgente;

        public Publicacion(String contenido, String autor, boolean urgente){
            this.contenido=contenido;
            this.autor = autor;
                    this.urgente=urgente;
        }
    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public boolean isUrgente() {
        return urgente;
    }

    public void setUrgente(boolean urgente) {
        this.urgente = urgente;
    }
}
