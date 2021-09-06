package com.example.hoteles;

import android.net.Uri;

public class Cliente {
    public String id,email,contraseña,username,telefono,fecha,direccion;
    public boolean baneado;
    public Uri foto;

    public Cliente(){
        this.id = "";
        this.email = "";
        this.contraseña = "";
        this.username = "";
        this.telefono = "";
        this.fecha = "";
        this.foto = null;
        this.baneado=false;
        this.direccion="";
    }

    public Cliente(String email, String contraseña, String username, String telefono,String fecha,String direccion) {
        this.id = "";
        this.email = email;
        this.contraseña = contraseña;
        this.username = username;
        this.telefono = telefono;
        this.fecha = fecha;
        this.foto = null;
        this.baneado=false;
        this.direccion=direccion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isBaneado() {
        return baneado;
    }

    public void setBaneado(boolean baneado) {
        this.baneado = baneado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Uri getFoto() {
        return foto;
    }

    public void setFoto(Uri foto) {
        this.foto = foto;
    }
}
