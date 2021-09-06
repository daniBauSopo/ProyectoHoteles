package com.example.hoteles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaItem>{
    private ArrayList<CategoriasHotel> categoriasHotels;
    Context context;
    LayoutInflater layoutInflater;
    CategoriaItem categoriaItem;

    private final int limite = 4;

    public CategoriaAdapter(Context context,ArrayList<CategoriasHotel> cateHotel){
        this.context=context;
        this.categoriasHotels=cateHotel;
        layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CategoriaItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoria_adapter,parent,false);

        final CategoriaItem categoriaItem = new CategoriaItem(view);



        return categoriaItem;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriaItem holder,final int position) {
        final CategoriasHotel categoriasHotel = categoriasHotels.get(position);
        categoriaItem = (CategoriaItem)holder;
        categoriaItem.nombre_cate.setText(categoriasHotel.getNombre());
        categoriaItem.cate_fo.setImageURI(categoriasHotel.getFotos());
        Glide.with(context).load(categoriasHotel.getFotos()).into(categoriaItem.cate_fo);

        categoriaItem.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filtro_habitaciones filtro_habitaciones = new Filtro_habitaciones();
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                SharedPreferences categoria_nombre = context.getSharedPreferences("categorias_datos",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = categoria_nombre.edit();
                editor.putString("nombre_categoria", categoriasHotels.get(position).getNombre());
                editor.commit();
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1,15);
                animation.setInterpolator(interpolator);
                holder.itemView.startAnimation(animation);
                manager.beginTransaction().setCustomAnimations(R.anim.zoom_forward_in,R.anim.zoom_forward_out).replace(R.id.frame_usu,filtro_habitaciones).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        if (categoriasHotels.size()>limite){
            return limite;
        }else {
            return categoriasHotels.size();
        }
    }

    public class CategoriaItem extends RecyclerView.ViewHolder{
        public TextView nombre_cate;
        public ImageView cate_fo;
        public LinearLayout layout;

        public CategoriaItem(final View itemView){
            super(itemView);
            this.nombre_cate=
                    itemView.findViewById(R.id.nombre_adapter);
            this.cate_fo=
                    itemView.findViewById(R.id.fot_cat);
            this.layout=
                    itemView.findViewById(R.id.categoria_layout);
        }
    }

}
