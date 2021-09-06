package com.example.hoteles;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Random;

public class UltimaValoraciones_Adapter extends RecyclerView.Adapter<UltimaValoraciones_Adapter.UltimaItem> {
    private ArrayList<Valoracion> valoracions;
    Context context;
    UltimaValoraciones_Adapter.UltimaItem ultimaItem;
    LayoutInflater layoutInflater;

    private final int limite = 5;

    public UltimaValoraciones_Adapter(Context context, ArrayList<Valoracion> valoracions) {
        this.context=context;
        this.valoracions=valoracions;
        layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public UltimaItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ultimas_valoraciones,parent,false);

        final UltimaValoraciones_Adapter.UltimaItem ultimaItem = new UltimaValoraciones_Adapter.UltimaItem(view);

        return ultimaItem;
    }

    @Override
    public void onBindViewHolder(@NonNull UltimaItem holder, int position) {
        final Valoracion valoracion = valoracions.get(position);
        ultimaItem = (UltimaItem)holder;
        ultimaItem.val.setText(valoracion.getDescripcion());
        ultimaItem.imagenproducto.setImageURI(valoracion.getFoto());

        Glide.with(context).load(valoracion.getFoto()).into(ultimaItem.imagenproducto);

        int newColor = getRandomColor();

        if (isColorDark(newColor)){
            ultimaItem.puntuacion.setTextColor(holder.itemView.getContext().getColor(R.color.verdeGuapo2));
        }else {
            ultimaItem.puntuacion.setVisibility(View.VISIBLE);
            Drawable buttonDrawable = ultimaItem.puntuacion.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            DrawableCompat.setTint(buttonDrawable, newColor);
            ultimaItem.puntuacion.setBackground(buttonDrawable);

            ultimaItem.puntuacion.setText(valoracion.getPuntuacion()+"");

            ultimaItem.linearLayout.setOrientation(LinearLayout.VERTICAL);
        }
    }

    public int getRandomColor() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }

    public boolean isColorDark(int color){
        double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.5){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public int getItemCount() {
        if (valoracions.size() > limite){
            return limite;
        }else {
            return valoracions.size();
        }
    }


    public class UltimaItem extends RecyclerView.ViewHolder{
        public TextView val;
        Button puntuacion;
        public ImageView imagenproducto;
        public LinearLayout linearLayout;

        public UltimaItem(final View view){
            super(view);
            this.val=
                    view.findViewById(R.id.ultimadescripcion);
            this.puntuacion=
                    view.findViewById(R.id.valoracion_ultima);
            this.imagenproducto=
                    view.findViewById(R.id.image_ultima);
            this.linearLayout=
                    view.findViewById(R.id.layout_ultimas_novedades);
        }

    }
}
