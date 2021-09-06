package com.example.hoteles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdminAdapter_Reserva extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<Reserva> reservas;
    private TextView nombre_cliente;
    private TextView nom_habitacion;
    private TextView fecha;
    private ImageView imagen;

    public AdminAdapter_Reserva(Context context,int resource,ArrayList<Reserva> reserva){
        super(context,resource,reserva);
        this.context=context;
        this.resource=resource;
        this.reservas=reserva;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent){
        View v = convertView;
        if (v==null){
            v= LayoutInflater.from(context).inflate(R.layout.reservaadapter_admin,null);
        }

        nombre_cliente=v.findViewById(R.id.nombrecliente_adminadapter_reserva);
        nom_habitacion=v.findViewById(R.id.nombrehabitacion_adminadapter_reserva);
        imagen=v.findViewById(R.id.imageView_adminadapter_reserva);
        fecha=v.findViewById(R.id.fecha_adminadapter_reserva);


        Reserva pojo_reserva = reservas.get(position);
        nombre_cliente.setText(pojo_reserva.getNombre_cliente());
        nom_habitacion.setText(pojo_reserva.getNombre_habitacion());
        fecha.setText(pojo_reserva.getFecha());

//        Glide.with(context).load(pojo_reserva.).into(imagen);
        return v;
    }
}
