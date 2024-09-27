package com.example.appfe.Interface;
import java.util.List;
import  com.example.appfe.Models.usuarioModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
public interface usuarioInterface {

    @GET("/usuario")
    Call<List<usuarioModel>> getUsuario();

    @POST("/usuario")
    Call<Void> crearUsuario(@Body usuarioModel usuario);
}
