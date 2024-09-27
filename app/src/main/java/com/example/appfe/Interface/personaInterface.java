package com.example.appfe.Interface;
import com.example.appfe.Models.personaModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import java.util.List;

public interface personaInterface {

    @GET("/persona")
    Call<List<personaModel>> getPersona();

    @POST("/persona")
    Call<Void> crearpersona(@Body personaModel persona);
}
