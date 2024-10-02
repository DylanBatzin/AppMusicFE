package com.example.appfe.Interface;

import com.example.appfe.Models.personaModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface personaInterface {

    @GET("/persona")
    Call<List<personaModel>> getPersona();

    @POST("/persona")
    Call<Void> crearpersona(@Body personaModel persona);

    @GET("/persona/alumnosPorAcademia")
    Call<List<personaModel>> getAlumnosPorAcademia(@Query("idAcademia") int idAcademia);

    @DELETE("/persona/personadelete/{id}")
    Call<Void> deletePersona(@Path("id") Long id);

    // Agregar la solicitud PUT para actualizar persona
    @PUT("/persona/EdiatarPersona/{id}")
    Call<personaModel> updatePersona(@Path("id") Long id, @Body personaModel persona);
    @GET("/persona/{id}")
    Call<personaModel> getPersonaById(@Path("id") Long id);
}
