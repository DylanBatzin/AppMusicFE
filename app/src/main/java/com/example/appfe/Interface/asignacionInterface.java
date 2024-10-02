package com.example.appfe.Interface;

import com.example.appfe.Models.asignacionModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface asignacionInterface {
    @POST("/asignaciones")
    Call<asignacionModel> crearAsignacion(@Body asignacionModel asignacion);
}
