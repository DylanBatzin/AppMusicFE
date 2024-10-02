package com.example.appfe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.appfe.Interface.usuarioInterface;
import com.example.appfe.Models.usuarioModel;
import com.example.appfe.Models.personaModel;
import com.example.appfe.Interface.personaInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText usuariotxt;
    private EditText passwordtxt;
    private Button loginbtn;
    private usuarioInterface usuarioapi;
    private personaInterface personaapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initViews();
        initRetrofit();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticate(usuariotxt.getText().toString(), passwordtxt.getText().toString());
            }
        });
    }

    private void initViews() {
        usuariotxt = findViewById(R.id.usuariotxt);
        passwordtxt = findViewById(R.id.passwordtxt);
        loginbtn = findViewById(R.id.loginbtn);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.7:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        usuarioapi = retrofit.create(usuarioInterface.class);
        personaapi = retrofit.create(personaInterface.class);
    }

    private void authenticate(final String username, final String password) {
        if (usuarioapi != null) {
            usuarioapi.getUsuario().enqueue(new Callback<List<usuarioModel>>() {
                @Override
                public void onResponse(Call<List<usuarioModel>> call, Response<List<usuarioModel>> response) {
                    if (response.isSuccessful()) {
                        usuarioModel currentUser = findUser(response.body(), username, password);
                        if (currentUser != null) {
                            Log.d("AUTH", "ID Usuario autenticado: " + currentUser.getId_usuario());
                            fetchPersonaInfo(currentUser.getId_usuario());
                        } else {
                            showToast("Contraseña incorrecta");
                        }
                    } else {
                        showToast("Error en la respuesta");
                    }
                }
                @Override
                public void onFailure(Call<List<usuarioModel>> call, Throwable t) {
                    handleError(t);
                }
            });
        } else {
            showToast("Error: usuarioapi no está inicializado");
        }
    }

    private usuarioModel findUser(List<usuarioModel> usuarios, String username, String password) {
        for (usuarioModel user : usuarios) {
            if (user.getUser().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private int loggedInUserAcademiaId = -1;  // Almacena el id_academia del usuario logueado

    private void fetchPersonaInfo(int id_usuario) {
        if (personaapi != null) {
            personaapi.getPersona().enqueue(new Callback<List<personaModel>>() {
                @Override
                public void onResponse(Call<List<personaModel>> call, Response<List<personaModel>> response) {
                    if (response.isSuccessful()) {
                        personaModel currentPersona = findPersona(response.body(), id_usuario);
                        if (currentPersona != null) {
                            loggedInUserAcademiaId = currentPersona.getId_academia();  // Guardar el id_academia del usuario logueado
                            navigateToRoleBasedActivity(currentPersona.getTipo());
                        } else {
                            showToast("No se encontró información de la persona");
                        }
                    } else {
                        showToast("Error en la respuesta al obtener la persona");
                    }
                }

                @Override
                public void onFailure(Call<List<personaModel>> call, Throwable t) {
                    showToast("Error de red: " + t.getMessage());
                }
            });
        } else {
            showToast("Error: personaapi no está inicializado");
        }
    }

    private personaModel findPersona(List<personaModel> personas, int id_usuario) {
        for (personaModel persona : personas) {
            if (persona.getId_usuario() == id_usuario) {
                return persona;
            }
        }
        return null;
    }

    private void navigateToRoleBasedActivity(String tipo) {
        Intent intent;
        if ("Alumno".equals(tipo)) {
            intent = new Intent(MainActivity.this, HomeAlumno.class);
        } else if ("Profesor".equals(tipo)) {
            intent = new Intent(MainActivity.this, Maindocente.class);
            intent.putExtra("ACADEMIA_ID", loggedInUserAcademiaId);  // Pasar el id_academia a Maindocente
        } else {
            showToast("Tipo de usuario desconocido");
            return;
        }
        startActivity(intent);
    }


    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void handleError(Throwable t) {
        showToast("Error de red: " + t.getMessage());
        Log.e("ERROR", "Error en la red", t);
    }
}
