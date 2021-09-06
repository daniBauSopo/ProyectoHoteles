package com.example.hoteles;

import android.net.Uri;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

import sun.bob.mcalendarview.vo.DateData;

public class Reserva  {
    public final static int ENVIADA = 0;
    public final static int ACEPTADA = 1;
    public final static int RECHAZADA = 2;
    public final static int TERMINADA = 3;

    public String id,nombre_habitacion,nombre_cliente,id_habitacion,id_cliente,fecha,fecha_entrada,fecha_salida;
    public int estado;
    public double precioT;
    public boolean estado_notificado;
    public Uri fotos;
//    public ArrayList<String> dias_reserva;

    public Reserva(){
        this.id="";
        this.nombre_habitacion="";
        this.nombre_cliente="";
        this.id_habitacion="";
        this.id_cliente="";
        this.fecha="";
        this.fotos = null;
        this.estado=0;
        this.estado_notificado=false;
        this.fecha_entrada="";
        this.fecha_salida="";
        this.precioT=0;
//        this.dias_reserva=new ArrayList<String>();
    }

    public Reserva( String nombre_habitacion, String nombre_cliente, String id_habitacion, String id_cliente, String fecha,int estado,String fecha_entrada,String fecha_salida,double precioT) {
        this.id = "";
        this.nombre_habitacion = nombre_habitacion;
        this.nombre_cliente = nombre_cliente;
        this.id_habitacion = id_habitacion;
        this.id_cliente = id_cliente;
        this.fecha = fecha;
        this.fotos = null;
        this.estado = estado;
        this.estado_notificado = false;
        this.fecha_entrada=fecha_entrada;
        this.fecha_salida=fecha_salida;
        this.precioT=precioT;
//        this.dias_reserva=dias_reserva;
    }

//    public ArrayList<String> getDias_reserva() {
//        return dias_reserva;
//    }
//
//    public void setDias_reserva(ArrayList<String> dias_reserva) {
//        this.dias_reserva = dias_reserva;
//    }


    public double getPrecioT() {
        return precioT;
    }

    public void setPrecioT(double precioT) {
        this.precioT = precioT;
    }

    public Uri getFotos() {
        return fotos;
    }

    public void setFotos(Uri fotos) {
        this.fotos = fotos;
    }

    public String getFecha_entrada() {
        return fecha_entrada;
    }

    public void setFecha_entrada(String fecha_entrada) {
        this.fecha_entrada = fecha_entrada;
    }

    public String getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(String fecha_salida) {
        this.fecha_salida = fecha_salida;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre_habitacion() {
        return nombre_habitacion;
    }

    public void setNombre_habitacion(String nombre_habitacion) {
        this.nombre_habitacion = nombre_habitacion;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public boolean isEstado_notificado() {
        return estado_notificado;
    }

    public void setEstado_notificado(boolean estado_notificado) {
        this.estado_notificado = estado_notificado;
    }
}
