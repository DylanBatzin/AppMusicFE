package com.example.appfe.Models;
import com.google.gson.annotations.SerializedName;
public class asignacionModel {
    @SerializedName("leccion")
    private Integer id_leccion;

    @SerializedName("id_actividad")
    private Integer id_actividad;

    @SerializedName("id_alumno")
    private Integer id_alumno;

    @SerializedName("id_examen")
    private Integer id_examen; // Puede ser null

    public asignacionModel(Integer id_leccion, Integer id_actividad, Integer id_alumno, Integer id_examen) {
        this.id_leccion = id_leccion;
        this.id_actividad = id_actividad;
        this.id_alumno = id_alumno;
        this.id_examen = id_examen;
    }

    // Getters y setters
    public Integer getId_leccion() {
        return id_leccion;
    }

    public void setId_leccion(Integer id_leccion) {
        this.id_leccion = id_leccion;
    }

    public Integer getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(Integer id_actividad) {
        this.id_actividad = id_actividad;
    }

    public Integer getId_alumno() {
        return id_alumno;
    }

    public void setId_alumno(Integer id_alumno) {
        this.id_alumno = id_alumno;
    }

    public Integer getId_examen() {
        return id_examen;
    }

    public void setId_examen(Integer id_examen) {
        this.id_examen = id_examen;
    }
}