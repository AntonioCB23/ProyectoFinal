package com.example.ancobra.proyectofinal;

public class Nota {
    private String autor;
    private String texto;
    private boolean urgente;

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

    public boolean isUrgente() {
        return urgente;
    }

    public void setUrgente(boolean urgente) {
        this.urgente = urgente;
    }

    public Nota(String autor, String texto, boolean urgente) {
        this.autor = autor;
        this.texto = texto;
        this.urgente = urgente;
    }
}
