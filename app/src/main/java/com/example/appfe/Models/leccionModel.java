package com.example.appfe.Models;

public class leccionModel {
    private int id_leccion;
    private int id_instrumento;
    private String link;
    private String titulo;

    // Getters y Setters
    public int getId_leccion() {
        return id_leccion;
    }

    public void setId_leccion(int id_leccion) {
        this.id_leccion = id_leccion;
    }

    public int getId_instrumento() {
        return id_instrumento;
    }

    public void setId_instrumento(int id_instrumento) {
        this.id_instrumento = id_instrumento;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}

