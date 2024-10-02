package com.example.appfe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appfe.Interface.actividadInterface;
import com.example.appfe.Interface.asignacionInterface;
import com.example.appfe.Interface.leccionInterface;
import com.example.appfe.Interface.personaInterface;
import com.example.appfe.Interface.examenInterface;
import com.example.appfe.Models.actividadModel;
import com.example.appfe.Models.asignacionModel;
import com.example.appfe.Models.leccionModel;
import com.example.appfe.Models.personaModel;
import com.example.appfe.Models.examenModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Asignarleccion extends AppCompatActivity {

    private AutoCompleteTextView alumnoDropdown, leccionDropdown, actividadDropdown, evaluacionDropdown;
    private Button asignarbtn, salirbtn;
    private int academiaId;

    private Retrofit retrofit;
    private personaInterface personaApi;
    private leccionInterface leccionApi;
    private actividadInterface actividadApi;
    private examenInterface examenApi;
    private asignacionInterface asignacionApi;

    // Mapeos para obtener los IDs
    private Map<String, Integer> alumnoMap = new HashMap<>();
    private Map<String, Integer> leccionMap = new HashMap<>();
    private Map<String, Integer> actividadMap = new HashMap<>();
    private Map<String, Integer> examenMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asignar_actividades);

        academiaId = getIntent().getIntExtra("ACADEMIA_ID", -1);

        // Verificar que se haya recibido un id_academia válido
        if (academiaId == -1) {
            Toast.makeText(this, "Error al obtener el ID de la academia", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.7:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Inicializar APIs
        personaApi = retrofit.create(personaInterface.class);
        leccionApi = retrofit.create(leccionInterface.class);
        actividadApi = retrofit.create(actividadInterface.class);
        examenApi = retrofit.create(examenInterface.class);
        asignacionApi = retrofit.create(asignacionInterface.class);

        // Inicializar vistas
        alumnoDropdown = findViewById(R.id.auto_completetxt);
        leccionDropdown = findViewById(R.id.auto_completetxt2);
        actividadDropdown = findViewById(R.id.auto_completetxt3);
        evaluacionDropdown = findViewById(R.id.auto_completetxt4);
        asignarbtn = findViewById(R.id.asignarbtn);
        salirbtn = findViewById(R.id.btnsalir);

        salirbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Asignarleccion.this, Maindocente.class);
            intent.putExtra("ACADEMIA_ID", academiaId);
            startActivity(intent);
        });

        // Poblar las listas desplegables
        poblarAlumnos();
        poblarLecciones();
        poblarActividades();
        poblarEvaluaciones();

        // Acción al presionar el botón "Asignar"
        asignarbtn.setOnClickListener(v -> {
            // Capturar los datos seleccionados
            String alumnoSeleccionado = alumnoDropdown.getText().toString();
            String leccionSeleccionada = leccionDropdown.getText().toString();
            String actividadSeleccionada = actividadDropdown.getText().toString();
            String evaluacionSeleccionada = evaluacionDropdown.getText().toString();

            // Obtener los IDs correspondientes
            Integer id_alumno = obtenerIdAlumno(alumnoSeleccionado);
            Integer id_leccion = obtenerIdLeccion(leccionSeleccionada);
            Integer id_actividad = obtenerIdActividad(actividadSeleccionada);
            Integer id_examen = evaluacionSeleccionada.isEmpty() ? null : obtenerIdExamen(evaluacionSeleccionada);

            // Log para depurar los datos
            Log.d("Datos a enviar", "ID Alumno: " + id_alumno);
            Log.d("Datos a enviar", "ID Lección: " + id_leccion);
            Log.d("Datos a enviar", "ID Actividad: " + id_actividad);
            Log.d("Datos a enviar", "ID Examen: " + id_examen);

            // Verificar que todos los datos sean válidos antes de hacer la solicitud
            if (id_alumno == null || id_leccion == null || id_actividad == null) {
                Toast.makeText(Asignarleccion.this, "Por favor seleccione todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear el objeto de asignación
            asignacionModel nuevaAsignacion = new asignacionModel(id_leccion, id_actividad, id_alumno, id_examen);

            // Realizar la solicitud POST para guardar la asignación
            Call<asignacionModel> call = asignacionApi.crearAsignacion(nuevaAsignacion);
            call.enqueue(new Callback<asignacionModel>() {
                @Override
                public void onResponse(Call<asignacionModel> call, Response<asignacionModel> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(Asignarleccion.this, "Asignación creada con éxito", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            Log.e("API Error", "Error: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(Asignarleccion.this, "Error al crear la asignación", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<asignacionModel> call, Throwable t) {
                    Toast.makeText(Asignarleccion.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                    Log.e("API Failure", t.getMessage(), t);
                }
            });
        });

    }
        private void poblarAlumnos() {
        Call<List<personaModel>> call = personaApi.getAlumnosPorAcademia(academiaId);
        call.enqueue(new Callback<List<personaModel>>() {
            @Override
            public void onResponse(Call<List<personaModel>> call, Response<List<personaModel>> response) {
                if (response.isSuccessful()) {
                    List<personaModel> alumnos = response.body();
                    if (alumnos != null && !alumnos.isEmpty()) {
                        List<String> nombresAlumnos = new ArrayList<>();
                        for (personaModel alumno : alumnos) {
                            String nombreCompleto = alumno.getNombre() + " " + alumno.getApellido();
                            nombresAlumnos.add(nombreCompleto);
                            alumnoMap.put(nombreCompleto, alumno.getId_persona().intValue()); // Guardar el ID del alumno
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Asignarleccion.this, android.R.layout.simple_dropdown_item_1line, nombresAlumnos);
                        alumnoDropdown.setAdapter(adapter);
                    } else {
                        Toast.makeText(Asignarleccion.this, "No se encontraron alumnos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Asignarleccion.this, "Error al cargar alumnos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<personaModel>> call, Throwable t) {
                Toast.makeText(Asignarleccion.this, "Fallo al conectar con la API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void poblarLecciones() {
        Call<List<leccionModel>> call = leccionApi.getLecciones();
        call.enqueue(new Callback<List<leccionModel>>() {
            @Override
            public void onResponse(Call<List<leccionModel>> call, Response<List<leccionModel>> response) {
                if (response.isSuccessful()) {
                    List<leccionModel> lecciones = response.body();
                    if (lecciones != null && !lecciones.isEmpty()) {
                        List<String> titulosLecciones = new ArrayList<>();
                        for (leccionModel leccion : lecciones) {
                            titulosLecciones.add(leccion.getTitulo());  // Añadir el título al dropdown
                            leccionMap.put(leccion.getTitulo(), leccion.getId_leccion()); // Guardar el mapeo título-ID
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Asignarleccion.this, android.R.layout.simple_dropdown_item_1line, titulosLecciones);
                        leccionDropdown.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(Asignarleccion.this, "Error al cargar lecciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<leccionModel>> call, Throwable t) {
                Toast.makeText(Asignarleccion.this, "Fallo al conectar con la API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void poblarActividades() {
        Call<List<actividadModel>> call = actividadApi.getActividades();
        call.enqueue(new Callback<List<actividadModel>>() {
            @Override
            public void onResponse(Call<List<actividadModel>> call, Response<List<actividadModel>> response) {
                if (response.isSuccessful()) {
                    List<actividadModel> actividades = response.body();
                    if (actividades != null && !actividades.isEmpty()) {
                        List<String> titulosActividades = new ArrayList<>();
                        for (actividadModel actividad : actividades) {
                            titulosActividades.add(actividad.getTitulo());
                            actividadMap.put(actividad.getTitulo(), actividad.getId_actividad()); // Guardar el ID de la actividad
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Asignarleccion.this, android.R.layout.simple_dropdown_item_1line, titulosActividades);
                        actividadDropdown.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(Asignarleccion.this, "Error al cargar actividades", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<actividadModel>> call, Throwable t) {
                Toast.makeText(Asignarleccion.this, "Fallo al conectar con la API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void poblarEvaluaciones() {
        Call<List<examenModel>> call = examenApi.getExamenes();
        call.enqueue(new Callback<List<examenModel>>() {
            @Override
            public void onResponse(Call<List<examenModel>> call, Response<List<examenModel>> response) {
                if (response.isSuccessful()) {
                    List<examenModel> examenes = response.body();
                    if (examenes != null && !examenes.isEmpty()) {
                        List<String> titulosExamenes = new ArrayList<>();
                        for (examenModel examen : examenes) {
                            titulosExamenes.add(examen.getTitulo());
                            examenMap.put(examen.getTitulo(), examen.getId_examen()); // Guardar el ID del examen
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Asignarleccion.this, android.R.layout.simple_dropdown_item_1line, titulosExamenes);
                        evaluacionDropdown.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(Asignarleccion.this, "Error al cargar evaluaciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<examenModel>> call, Throwable t) {
                Toast.makeText(Asignarleccion.this, "Fallo al conectar con la API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Obtener el ID del alumno seleccionado
    private Integer obtenerIdAlumno(String nombreCompleto) {
        return alumnoMap.getOrDefault(nombreCompleto, null); // Devuelve el ID del alumno o null si no se encuentra
    }

    // Obtener el ID de la lección seleccionada
    private Integer obtenerIdLeccion(String tituloLeccion) {
        // Verificar si el título de la lección existe en el mapa y devolver su ID
        return leccionMap.getOrDefault(tituloLeccion, null); // Devuelve null si no encuentra el título
    }


    // Obtener el ID de la actividad seleccionada
    private Integer obtenerIdActividad(String tituloActividad) {
        return actividadMap.getOrDefault(tituloActividad, null); // Devuelve el ID de la actividad o null si no se encuentra
    }

    // Obtener el ID del examen seleccionado
    private Integer obtenerIdExamen(String tituloExamen) {
        return examenMap.getOrDefault(tituloExamen, null); // Devuelve el ID del examen o null si no se encuentra
    }
}
