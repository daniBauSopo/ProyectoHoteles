package com.example.hoteles;

import android.net.Uri;

public class CategoriasHotel {
    public String nombre,id;
    public Uri fotos;
    public CategoriasHotel(){
        this.nombre="";
        this.fotos = null;
        this.id = "";
    }

    public CategoriasHotel(String nombre) {
        this.nombre = nombre;
        this.fotos = null;
        this.id = "";
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

    public Uri getFotos() {
        return fotos;
    }

    public void setFotos(Uri fotos) {
        this.fotos = fotos;
    }
}
