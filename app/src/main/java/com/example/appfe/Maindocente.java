package com.example.appfe;
import com.example.appfe.VerAlumnos;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Maindocente extends AppCompatActivity {

    private Button registrarBtn, verbtn, eliminarbtn, Agregrabtn;
    private int loggedInUserAcademiaId;  // Definir la variable para almacenar el id_academia

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maind);

        // Recibir el id_academia desde MainActivity o la actividad que te llevó aquí
        loggedInUserAcademiaId = getIntent().getIntExtra("ACADEMIA_ID", -1);

        // Verificar que se haya recibido un id_academia válido
        if (loggedInUserAcademiaId == -1) {
            Toast.makeText(this, "Error al obtener el ID de la academia", Toast.LENGTH_SHORT).show();
            finish();  // Terminar la actividad si no se recibió un id_academia válido
            return;
        }

        registrarBtn = findViewById(R.id.Registrar);
        verbtn = findViewById(R.id.Verbtn);
        eliminarbtn = findViewById(R.id.Eliminarbtn);
        Agregrabtn = findViewById(R.id.agregrabtn);

        registrarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Maindocente.this, RegistroAlumnoActivity.class);
                intent.putExtra("ACADEMIA_ID", loggedInUserAcademiaId);  // Pasar el id_academia a RegistroAlumnoActivity
                startActivity(intent);
            }
        });

       verbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(Maindocente.this, VerAlumnos.class);
              intent.putExtra("ACADEMIA_ID", loggedInUserAcademiaId);  // Pasar el id_academia a RegistroAlumnoActivity
                startActivity(intent);
            }
        });

       eliminarbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(Maindocente.this, eliminarlaumno.class);
               intent.putExtra("ACADEMIA_ID", loggedInUserAcademiaId);  // Pasar el id_academia a RegistroAlumnoActivity
               startActivity(intent);
           }
       });

        Agregrabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Maindocente.this, Asignarleccion.class);
                intent.putExtra("ACADEMIA_ID", loggedInUserAcademiaId);  // Pasar el id_academia a RegistroAlumnoActivity
                startActivity(intent);
            }
        });
    }
}
