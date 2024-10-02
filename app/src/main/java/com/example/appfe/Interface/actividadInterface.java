package com.example.appfe.Interface;

import com.example.appfe.Models.actividadModel;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface actividadInterface {

    @GET("/actividad")
    Call<List<actividadModel>> getActividades();
}
