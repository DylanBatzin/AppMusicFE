package com.example.appfe.Models;

public class examenModel {
    private int id_examen;
    private int id_leccion;
    private String preguntas;
    private String respuestas;
    private String titulo;

    // Getters y Setters
    public int getId_examen() {
        return id_examen;
    }

    public void setId_examen(int id_examen) {
        this.id_examen = id_examen;
    }

    public int getId_leccion() {
        return id_leccion;
    }

    public void setId_leccion(int id_leccion) {
        this.id_leccion = id_leccion;
    }

    public String getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(String preguntas) {
        this.preguntas = preguntas;
    }

    public String getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(String respuestas) {
        this.respuestas = respuestas;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
