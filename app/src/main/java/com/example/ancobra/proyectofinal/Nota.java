package com.example.ancobra.proyectofinal;

/**
 * Clase Nota contenedora de los datos de las notas que se gestionaran en la aplicacion
 */
public class Nota {
    private String autor;
    private String texto;
    private String urgente;

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getUrgente() {
        return urgente;
    }

    public Nota(String autor, String texto, String urgente) {
        this.autor = autor;
        this.texto = texto;
        this.urgente = urgente;
    }

    public void setUrgente(String urgente) {
        this.urgente = urgente;

    }
}
