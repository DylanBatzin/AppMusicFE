package com.example.appfe.Models;

import com.google.gson.annotations.SerializedName;

public class usuarioModel {

    @SerializedName("idUsuario")
    private int id_usuario;

    private String user;
    private String email;
    private String password;


    public usuarioModel(String user, String email, String password) {
        this.user = user;
        this.email = email;
        this.password = password;
    }

    // Getters y Setters

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
