package com.example.appfe;
import android.content.Intent;
import android.os.Bundle;
import com.example.appfe.Interface.personaInterface;
import com.example.appfe.Models.personaModel;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerAlumnos extends AppCompatActivity  {
    private ListView listViewUsuarios;
    private PersonaAdapter adapter;

    private Button salirbtn;
    private int academiaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listaalumnos);

        academiaId = getIntent().getIntExtra("ACADEMIA_ID", -1);

        // Verificar que se haya recibido un id_academia válido
        if (academiaId == -1) {
            Toast.makeText(this, "Error al obtener el ID de la academia", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        salirbtn = findViewById(R.id.btnsalir);
        salirbtn.setOnClickListener(v -> {
            Intent intent = new Intent(VerAlumnos.this, Maindocente.class);
            intent.putExtra("ACADEMIA_ID", academiaId);
            startActivity(intent);
        });

        listViewUsuarios = findViewById(R.id.listViewUsuarios);

        // Inicializar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.7:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        personaInterface apiService = retrofit.create(personaInterface.class);

        // Hacer la petición para obtener solo los alumnos de la academia
        apiService.getAlumnosPorAcademia(academiaId).enqueue(new Callback<List<personaModel>>() {
            @Override
            public void onResponse(Call<List<personaModel>> call, Response<List<personaModel>> response) {
                if (response.isSuccessful()) {
                    List<personaModel> alumnos = response.body();

                    // Configurar el adaptador personalizado y asignarlo al ListView
                    adapter = new PersonaAdapter(VerAlumnos.this, alumnos);
                    listViewUsuarios.setAdapter(adapter);

                    // Configurar el evento de click en los alumnos del ListView
                    listViewUsuarios.setOnItemClickListener((parent, view, position, id) -> {
                        // Obtener el alumno seleccionado
                        personaModel selectedPersona = (personaModel) parent.getItemAtPosition(position);

                        // Mostrar el cuadro de confirmación
                        new AlertDialog.Builder(VerAlumnos.this)
                                .setTitle("Confirmar Edición")
                                .setMessage("¿Estás seguro de que deseas editar este alumno?")
                                .setPositiveButton("Sí", (dialog, which) -> {
                                    // Pasar los datos necesarios a la nueva actividad
                                    Intent intent = new Intent(VerAlumnos.this, EditStudentActivity.class);
                                    intent.putExtra("ID_PERSONA", selectedPersona.getId_persona());  // Pasar id_persona
                                    intent.putExtra("ID_USUARIO", selectedPersona.getId_usuario());  // Pasar id_usuario
                                    startActivity(intent);
                                })
                                .setNegativeButton("No", null)
                                .show();
                    });
                } else {
                    Toast.makeText(VerAlumnos.this, "Error al recuperar datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<personaModel>> call, Throwable t) {
                Toast.makeText(VerAlumnos.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
