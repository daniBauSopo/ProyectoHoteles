package com.example.hoteles;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SampleHabitacionGridAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView nombre,precio,valoracionT,hotel,dispo;
    public ImageView foto_habitacion;
    public LinearLayout layout;

    public SampleHabitacionGridAdapter(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
        this.nombre=
                itemView.findViewById(R.id.nombre_adapter_grid);
        this.precio=
                itemView.findViewById(R.id.precio_adapter_grid);
        this.foto_habitacion=
                itemView.findViewById(R.id.foto_enseña_grid);

        this.dispo=
                itemView.findViewById(R.id.textodisponible);
        this.hotel=
                itemView.findViewById(R.id.hotel_adapter_grid);
        this.valoracionT=
                itemView.findViewById(R.id.valoracion_adapter_grid);
    }

    @Override
    public void onClick(View view) {

    }
}
