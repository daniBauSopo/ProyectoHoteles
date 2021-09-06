package com.example.hoteles;

import android.net.Uri;

public class Valoracion {
    public String id,id_habitacion,id_cliente,fecha,nombre_cliente,nombre_habitacion,descripcion,username;
    public float puntuacion;
    public int estado;
    public Uri foto;

    public final static int NOVALORADA = 0;
    public final static int VALORADA = 1;

    public Valoracion(){
        this.id="";
        this.id_habitacion="";
        this.id_cliente="";
        this.fecha="";
        this.nombre_cliente="";
        this.nombre_habitacion="";
        this.puntuacion=0;
        this.estado=0;
        this.descripcion="";
        this.foto=null;
        this.username="";
    }

    public Valoracion(String id_habitacion, String id_cliente, String fecha, String nombre_cliente, String nombre_habitacion,String descripcion,int estado,String username) {
        this.id = "";
        this.id_habitacion = id_habitacion;
        this.id_cliente = id_cliente;
        this.fecha = fecha;
        this.nombre_cliente = nombre_cliente;
        this.nombre_habitacion = nombre_habitacion;
        this.puntuacion = puntuacion;
        this.estado=estado;
        this.descripcion=descripcion;
        this.foto=null;
        this.username=username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Uri getFoto() {
        return foto;
    }

    public void setFoto(Uri foto) {
        this.foto = foto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_habitacion() {
        return id_habitacion;
    }

    public void setId_habitacion(String id_habitacion) {
        this.id_habitacion = id_habitacion;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getNombre_habitacion() {
        return nombre_habitacion;
    }

    public void setNombre_habitacion(String nombre_habitacion) {
        this.nombre_habitacion = nombre_habitacion;
    }

    public float getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(float puntuacion) {
        this.puntuacion = puntuacion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
