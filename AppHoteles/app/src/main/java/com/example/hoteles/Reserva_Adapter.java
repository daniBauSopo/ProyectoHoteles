package com.example.hoteles;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Reserva_Adapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<Reserva> reservas;
    private ArrayList<Habitacion> habitacionArrayList;
    private TextView nom_habitacion,precioTotal;
    private TextView fecha;
    private ImageView imagen,estado_enreserva;
    Uri fotos;



    public Reserva_Adapter(Context context,int resource,ArrayList<Reserva> reserva){
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
            v= LayoutInflater.from(context).inflate(R.layout.reservadapter,null);
        }


        nom_habitacion=v.findViewById(R.id.textView3_hab);
        imagen=v.findViewById(R.id.imagenhabi_reserva);
        fecha=v.findViewById(R.id.textView4_da);
        estado_enreserva=v.findViewById(R.id.estado_enreserva);
        precioTotal=v.findViewById(R.id.textView8_preTotal);

        fotos=null;
        Reserva pojo_reserva = reservas.get(position);
        nom_habitacion.setText(pojo_reserva.getNombre_habitacion());
        fecha.setText(pojo_reserva.getFecha());
        if (pojo_reserva.getEstado()==1){
            estado_enreserva.setImageResource(R.drawable.aceptada);
        }else if (pojo_reserva.getEstado()==2){
            estado_enreserva.setImageResource(R.drawable.rechazada);
        }else if(pojo_reserva.getEstado()==0){
            estado_enreserva.setImageResource(R.drawable.espera);
        }else if(pojo_reserva.getEstado()==3){
            estado_enreserva.setImageResource(R.drawable.terminar);
        }


                    Glide.with(getContext()).load(pojo_reserva.getFotos()).into(imagen);


        precioTotal.setText(pojo_reserva.getPrecioT()+""+"€");








//        Habitacion pojo_habitacion = habitacions.get(position);
//        Glide.with(context).load(pojo_habitacion.getFotos()).into(imagen);

//        Glide.with(context).load(pojo_reserva.).into(imagen);
        return v;
    }
}
