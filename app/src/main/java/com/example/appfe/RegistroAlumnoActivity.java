package com.example.appfe;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appfe.Interface.usuarioInterface;
import com.example.appfe.Interface.personaInterface;
import com.example.appfe.Models.usuarioModel;
import com.example.appfe.Models.personaModel;

import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class RegistroAlumnoActivity extends AppCompatActivity {

    private EditText userregtxt, correotxt, passwordtxt, confirmpasswordtxt, nametxt, lastnametxt;
    private MaterialButton registrarBtn;
    private usuarioInterface usuarioapi;
    private personaInterface personaapi;
    private int academiaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regisroalumno);

        // Recibir el id_academia desde Maindocente
        academiaId = getIntent().getIntExtra("ACADEMIA_ID", -1);

        // Verificar que se haya recibido un id_academia válido
        if (academiaId == -1) {
            Toast.makeText(this, "Error al obtener el ID de la academia", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar vistas
        userregtxt = findViewById(R.id.userregtxt);
        correotxt = findViewById(R.id.correotxt);
        passwordtxt = findViewById(R.id.passwordtxt);
        confirmpasswordtxt = findViewById(R.id.confirmpasswordtxt);
        nametxt = findViewById(R.id.nametxt);
        lastnametxt = findViewById(R.id.lastnametxt);
        registrarBtn = findViewById(R.id.loginbtn);

        // Inicializar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.7:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        usuarioapi = retrofit.create(usuarioInterface.class);
        personaapi = retrofit.create(personaInterface.class);

        registrarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    registerUser();
                }
            }
        });
    }

    private boolean validateForm() {
        String password = passwordtxt.getText().toString();
        String confirmPassword = confirmpasswordtxt.getText().toString();
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registerUser() {
        String username = userregtxt.getText().toString();
        String email = correotxt.getText().toString();
        String password = passwordtxt.getText().toString();

        usuarioModel newUser = new usuarioModel(username, email, password);

        usuarioapi.crearUsuario(newUser).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegistroAlumnoActivity.this, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show();
                    fetchUserIdAndRegisterPersona(username); // Obtener el ID del usuario y registrar la persona
                } else {
                    Toast.makeText(RegistroAlumnoActivity.this, "Error al crear el usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegistroAlumnoActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserIdAndRegisterPersona(String username) {
        usuarioapi.getUsuario().enqueue(new Callback<List<usuarioModel>>() {
            @Override
            public void onResponse(Call<List<usuarioModel>> call, Response<List<usuarioModel>> response) {
                if (response.isSuccessful()) {
                    for (usuarioModel user : response.body()) {
                        if (user.getUser().equals(username)) {
                            int userId = user.getId_usuario();
                            registerPersona(userId);
                            break;
                        }
                    }
                } else {
                    Toast.makeText(RegistroAlumnoActivity.this, "Error al obtener el ID del usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<usuarioModel>> call, Throwable t) {
                Toast.makeText(RegistroAlumnoActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerPersona(int userId) {
        String nombre = nametxt.getText().toString();
        String apellido = lastnametxt.getText().toString();
        String tipo = "Alumno";  // El tipo siempre será "Alumno"

        personaModel newPersona = new personaModel(userId, academiaId, nombre, apellido, tipo);

        personaapi.crearpersona(newPersona).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegistroAlumnoActivity.this, "Persona registrada exitosamente", Toast.LENGTH_SHORT).show();
                    finish();  // Puedes cerrar la actividad o redirigir a otra pantalla
                } else {
                    Toast.makeText(RegistroAlumnoActivity.this, "Error al registrar a la persona", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegistroAlumnoActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
