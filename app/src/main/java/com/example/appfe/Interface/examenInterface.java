package com.example.appfe.Interface;

import com.example.appfe.Models.examenModel;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface examenInterface {

    @GET("/examen")
    Call<List<examenModel>> getExamenes();
}
