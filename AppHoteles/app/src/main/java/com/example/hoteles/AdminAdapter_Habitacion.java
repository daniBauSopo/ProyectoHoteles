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

public class AdminAdapter_Habitacion extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<Habitacion> habitacions;
    private TextView nombre;
    private TextView precio;
    private TextView cate;
    private ImageView imagen;

    public AdminAdapter_Habitacion(Context context,int resource,ArrayList<Habitacion> habitacions){
        super(context,resource,habitacions);
        this.context=context;
        this.resource=resource;
        this.habitacions=habitacions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent){
        View v = convertView;
        if (v==null){
            v= LayoutInflater.from(context).inflate(R.layout.habitacionadapter_admin,null);
        }

        nombre=v.findViewById(R.id.nombre_adminadapter);
        precio=v.findViewById(R.id.precio_adminadapter);
        imagen=v.findViewById(R.id.imageView_adminadapter);
        cate=v.findViewById(R.id.categoria_adminadapter);


        Habitacion pojo_habitaciob = habitacions.get(position);
        nombre.setText(pojo_habitaciob.getNombre());
        precio.setText(pojo_habitaciob.getPrecio()+"");
        cate.setText(pojo_habitaciob.getCategoria());

        Glide.with(context).load(pojo_habitaciob.getFotos()).into(imagen);
        return v;
    }
}
