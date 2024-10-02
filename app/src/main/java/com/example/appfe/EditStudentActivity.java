package com.example.appfe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appfe.Interface.personaInterface;
import com.example.appfe.Interface.usuarioInterface;
import com.example.appfe.Models.personaModel;
import com.example.appfe.Models.usuarioModel;

public class EditStudentActivity extends AppCompatActivity {
    private EditText userEditText, emailEditText, passwordEditText, confirmPasswordEditText, nameEditText, lastNameEditText;
    private Button updateButton;
    private Long personaId;
    private int userId;
    private int academiaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editaralumno);

        // Inicializar vistas
        userEditText = findViewById(R.id.userregtxt);
        emailEditText = findViewById(R.id.correotxt);
        passwordEditText = findViewById(R.id.passwordtxt);
        confirmPasswordEditText = findViewById(R.id.confirmpasswordtxt);
        nameEditText = findViewById(R.id.nametxt);
        lastNameEditText = findViewById(R.id.lastnametxt);
        updateButton = findViewById(R.id.loginbtn);

        // Obtener los datos pasados desde VerAlumnos
        personaId = getIntent().getLongExtra("ID_PERSONA", -1);
        userId = getIntent().getIntExtra("ID_USUARIO", -1);

        // Cargar datos de persona y usuario
        getPersonaData(personaId);
        getUsuarioData(userId);

        // Manejar la actualización cuando el botón es pulsado
        updateButton.setOnClickListener(v -> updateDetails());
    }

    // Método para obtener los datos de persona por su ID
    private void getPersonaData(Long personaId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.7:8080/") // Asegúrate de que esta URL sea la correcta
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        personaInterface apiService = retrofit.create(personaInterface.class);

        apiService.getPersonaById(personaId).enqueue(new Callback<personaModel>() {
            @Override
            public void onResponse(Call<personaModel> call, Response<personaModel> response) {
                if (response.isSuccessful()) {
                    personaModel persona = response.body();
                    nameEditText.setText(persona.getNombre());
                    lastNameEditText.setText(persona.getApellido());
                } else {
                    Toast.makeText(EditStudentActivity.this, "Error al cargar datos de persona", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<personaModel> call, Throwable t) {
                Toast.makeText(EditStudentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para obtener los datos de usuario por su ID
    private void getUsuarioData(long userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.7:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        usuarioInterface apiService = retrofit.create(usuarioInterface.class);

        apiService.getUsuarioById(userId).enqueue(new Callback<usuarioModel>() {
            @Override
            public void onResponse(Call<usuarioModel> call, Response<usuarioModel> response) {
                if (response.isSuccessful()) {
                    usuarioModel usuario = response.body();
                    userEditText.setText(usuario.getUser());
                    emailEditText.setText(usuario.getEmail());
                    passwordEditText.setText(usuario.getPassword());
                    confirmPasswordEditText.setText(usuario.getPassword());
                } else {
                    Toast.makeText(EditStudentActivity.this, "Error al cargar datos de usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<usuarioModel> call, Throwable t) {
                Toast.makeText(EditStudentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para actualizar los datos de persona y usuario
    private void updateDetails() {
        String nombre = nameEditText.getText().toString();
        String apellido = lastNameEditText.getText().toString();
        String usuario = userEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Actualizar los datos de persona y usuario
        updatePersona(nombre, apellido);
        updateUsuario(usuario, email, password);
    }
    private void goBackToMaindocente() {
        Intent intent = new Intent(EditStudentActivity.this, Maindocente.class);
        intent.putExtra("ACADEMIA_ID", academiaId);  // Pasar el ACADEMIA_ID para mostrar el layout correcto
        startActivity(intent);
        finish();  // Cerrar la actividad actual para evitar que el usuario vuelva con el botón "Atrás"
    }

    // Método para actualizar los datos de persona
    private void updatePersona(String nombre, String apellido) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.7:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        personaInterface apiService = retrofit.create(personaInterface.class);
        personaModel updatedPersona = new personaModel(userId, -1, nombre, apellido, "Alumno");

        apiService.updatePersona(personaId, updatedPersona).enqueue(new Callback<personaModel>() {
            @Override
            public void onResponse(Call<personaModel> call, Response<personaModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditStudentActivity.this, "Datos de persona actualizados", Toast.LENGTH_SHORT).show();
                    // Llamar al método para regresar
                    goBackToMaindocente();
                } else {
                    Toast.makeText(EditStudentActivity.this, "Error al actualizar persona", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<personaModel> call, Throwable t) {
                Toast.makeText(EditStudentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para actualizar los datos de usuario
    private void updateUsuario(String usuario, String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.7:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        usuarioInterface apiService = retrofit.create(usuarioInterface.class);
        usuarioModel updatedUsuario = new usuarioModel(usuario, email, password);

        apiService.updateUsuario(userId, updatedUsuario).enqueue(new Callback<usuarioModel>() {
            @Override
            public void onResponse(Call<usuarioModel> call, Response<usuarioModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditStudentActivity.this, "Datos de usuario actualizados", Toast.LENGTH_SHORT).show();
                    // Llamar al método para regresar
                    goBackToMaindocente();
                } else {
                    Toast.makeText(EditStudentActivity.this, "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<usuarioModel> call, Throwable t) {
                Toast.makeText(EditStudentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

