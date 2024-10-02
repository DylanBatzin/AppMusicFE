package com.example.appfe.Models;

public class personaModel {
    private Long id_persona;
    private int id_usuario;
    private int id_academia;
    private String nombre;
    private String apellido;
    private String tipo;


    // Constructor con parámetros
    public personaModel(int id_usuario, int id_academia, String nombre, String apellido, String tipo) {
        this.id_usuario = id_usuario;
        this.id_academia = id_academia;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipo = tipo;
    }
    public Long getId_persona() {
        return id_persona;
    }

    public void setId_persona(Long id_persona) {
        this.id_persona = id_persona;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_academia() {
        return id_academia;
    }

    public void setId_academia(int id_academia) {
        this.id_academia = id_academia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}

