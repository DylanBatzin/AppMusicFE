package com.example.appfe.Interface;

import com.example.appfe.Models.leccionModel;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface leccionInterface {

    @GET("/leccion")
    Call<List<leccionModel>> getLecciones();
}
