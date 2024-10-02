package com.example.appfe;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import com.example.appfe.Interface.personaInterface;
import com.example.appfe.Models.personaModel;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class eliminarlaumno extends AppCompatActivity {
    private ListView listViewUsuarios;
    private PersonaAdapter adapter;
    private personaInterface apiService;
    private Button salirbtn;
    private int academiaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eliminar_alumno);

        academiaId = getIntent().getIntExtra("ACADEMIA_ID", -1);

        // Verificar que se haya recibido un id_academia válido
        if (academiaId == -1) {
            Toast.makeText(this, "Error al obtener el ID de la academia", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        salirbtn = findViewById(R.id.btnsalir);
        salirbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(eliminarlaumno.this, Maindocente.class);
                intent.putExtra("ACADEMIA_ID", academiaId);
                startActivity(intent);
            }
        });

        listViewUsuarios = findViewById(R.id.listViewUsuarios);

        // Inicializar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.7:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(personaInterface.class);

        // Obtener la lista de alumnos
        apiService.getAlumnosPorAcademia(academiaId).enqueue(new Callback<List<personaModel>>() {
            @Override
            public void onResponse(Call<List<personaModel>> call, Response<List<personaModel>> response) {
                if (response.isSuccessful()) {
                    List<personaModel> alumnos = response.body();
                    // Configurar el adaptador personalizado y asignarlo al ListView
                    adapter = new PersonaAdapter(eliminarlaumno.this, alumnos);
                    listViewUsuarios.setAdapter(adapter);

                    // Configurar el listener para clic en los items de la lista
                    listViewUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            personaModel alumnoSeleccionado = alumnos.get(position);
                            mostrarDialogoConfirmacion(alumnoSeleccionado);
                        }
                    });
                } else {
                    Toast.makeText(eliminarlaumno.this, "Error al recuperar datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<personaModel>> call, Throwable t) {
                Toast.makeText(eliminarlaumno.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para mostrar el diálogo de confirmación
    private void mostrarDialogoConfirmacion(final personaModel alumno) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación de eliminación");
        builder.setMessage("¿Estás seguro de que deseas eliminar a " + alumno.getNombre() + " " + alumno.getApellido() + "?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarAlumno(alumno);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Método para eliminar al alumno usando la API
    private void eliminarAlumno(personaModel alumno) {
        apiService.deletePersona(alumno.getId_persona()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(eliminarlaumno.this, "Alumno eliminado exitosamente", Toast.LENGTH_SHORT).show();
                    // Aquí podrías actualizar la lista de alumnos eliminando el alumno eliminado del adaptador
                    adapter.remove(alumno);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(eliminarlaumno.this, "Error al eliminar al alumno", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(eliminarlaumno.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
