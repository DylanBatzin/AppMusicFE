package com.example.appfe.Interface;

import java.util.List;
import com.example.appfe.Models.usuarioModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface usuarioInterface {

    @GET("/usuario")
    Call<List<usuarioModel>> getUsuario();

    @POST("/usuario")
    Call<Void> crearUsuario(@Body usuarioModel usuario);

    // Agregar la solicitud PUT para actualizar usuario
    @PUT("/usuario/EditarUsuario/{id}")
    Call<usuarioModel> updateUsuario(@Path("id") int id, @Body usuarioModel usuario);
    @GET("/usuario/{id}")
    Call<usuarioModel> getUsuarioById(@Path("id") Long id);
}
