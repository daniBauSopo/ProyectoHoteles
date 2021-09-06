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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ValoracionAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<Valoracion> valoracions;
    private TextView nom_habitacion,nom_cliente,opinion;
    private TextView fecha;
    private ImageView imagen;
    Uri fotos;

    private StorageReference sto;


    public ValoracionAdapter(Context context,int resource,ArrayList<Valoracion> valoracions){
        super(context,resource,valoracions);
        this.context=context;
        this.resource=resource;
        this.valoracions=valoracions;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent){
        View v = convertView;
        if (v==null){
            v= LayoutInflater.from(context).inflate(R.layout.valoraciones_adapter,null);
        }

        sto= FirebaseStorage.getInstance().getReference();

        nom_habitacion=v.findViewById(R.id.val_nomha);
        nom_cliente=v.findViewById(R.id.val_nomcli);
        imagen=v.findViewById(R.id.circulo_imagen);
        fecha=v.findViewById(R.id.val_fecha);
        opinion=v.findViewById(R.id.val_opinion);

        fotos=null;
        Valoracion pojo_valoracion = valoracions.get(position);
        nom_habitacion.setText(pojo_valoracion.getNombre_habitacion());
        nom_cliente.setText(pojo_valoracion.getUsername());
        fecha.setText(pojo_valoracion.getFecha());
        opinion.setText(pojo_valoracion.getDescripcion());


        Glide.with(getContext()).load(pojo_valoracion.getFoto()).into(imagen);




//        Habitacion pojo_habitacion = habitacions.get(position);
//        Glide.with(context).load(pojo_habitacion.getFotos()).into(imagen);

//        Glide.with(context).load(pojo_reserva.).into(imagen);
        return v;
    }
}
