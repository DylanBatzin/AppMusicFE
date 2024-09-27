package com.example.appfe;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private EditText usuariotxt;
    private EditText passwordtxt;
    private Button loginbtn;
    private  usuarioInterface usuarioapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        usuariotxt = findViewById(R.id.usuariotxt);
        passwordtxt = findViewById(R.id.passwordtxt);
        loginbtn = findViewById(R.id.loginbtn);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.7:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        usuarioapi = retrofit.create(usuarioInterface.class);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usuariotxt.getText().toString();
                String password = passwordtxt.getText().toString();
                authenticate(username,password);
            }
        });
    }

    private void authenticate(final String username, final String password) {
        // Asegurarse de que usuarioapi no sea null antes de usarlo
        if (usuarioapi != null) {
            Call<List<usuarioModel>> call = usuarioapi.getUsuario();
            call.enqueue(new Callback<List<usuarioModel>>() {
                @Override
                public void onResponse(Call<List<usuarioModel>> call, Response<List<usuarioModel>> response) {
                    if (response.isSuccessful()) {
                        List<usuarioModel> usuarios = response.body();
                        boolean found = false;
                        for (usuarioModel user : usuarios) {
                            if (user.getUser().equals(username) && user.getPassword().equals(password)) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            Toast.makeText(MainActivity.this, "¡Bienvenido " + username + "!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Error en la respuesta", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<usuarioModel>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }

            });
        } else {
            Toast.makeText(MainActivity.this, "Error: usuarioapi no está inicializado", Toast.LENGTH_SHORT).show();
        }
    }
}