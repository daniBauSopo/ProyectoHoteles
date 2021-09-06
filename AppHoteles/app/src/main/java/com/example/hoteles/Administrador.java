package com.example.hoteles;

public class Administrador {
    public String nombre,contraseña;

    public Administrador(){
        this.nombre="";
        this.contraseña="";
    }
    public Administrador(String nombre,String contraseña){
        this.nombre=nombre;
        this.contraseña=contraseña;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
