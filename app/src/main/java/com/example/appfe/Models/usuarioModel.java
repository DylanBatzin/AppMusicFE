package com.example.appfe.Models;

public class usuarioModel {
    public usuarioModel(int id_usuario, String user, String email, String password) {
        this.id_usuario = id_usuario;
        this.user = user;
        this.email = email;
        this.password = password;
    }

    private int id_usuario;
    private String user;
    private String email;
    private String password;
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
