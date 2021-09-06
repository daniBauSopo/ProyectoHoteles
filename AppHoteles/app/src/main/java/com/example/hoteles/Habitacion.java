package com.example.hoteles;

import android.net.Uri;

public class Habitacion {
    public String id,nombre,categoria,disponibilidad,descripcion,latitud,longitud,fecha,hotel;
    public float estrellas;
    public Uri fotos;
    public double precio;
    public float valoracion;

    public Habitacion(){
        this.id = "";
        this.nombre = "";
        this.descripcion="";
        this.categoria = "";
        this.disponibilidad = "";
        this.valoracion = 0;
        this.estrellas = 0;
        this.fotos = null;
        this.precio = 0;
        this.latitud="";
        this.longitud="";
        this.fecha="";
        this.hotel="";
    }

    public Habitacion(String nombre, String categoria, String disponibilidad, float valoracion, double precio,String descripcion, String latitud, String longitud,String fecha,String hotel) {
        this.id = "";
        this.nombre = nombre;
        this.descripcion=descripcion;
        this.categoria = categoria;
        this.disponibilidad = disponibilidad;
        this.valoracion = valoracion;
        this.estrellas = estrellas;
        this.fotos = null;
        this.precio = precio;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha=fecha;
        this.hotel=hotel;
    }



    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public float getValoracion() {
        return valoracion;
    }

    public void setValoracion(float valoracion) {
        this.valoracion = valoracion;
    }

    public float getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(float estrellas) {
        this.estrellas = estrellas;
    }

    public Uri getFotos() {
        return fotos;
    }

    public void setFotos(Uri fotos) {
        this.fotos = fotos;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
