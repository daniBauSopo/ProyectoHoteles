package com.example.hoteles;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.CategoryGrid> {

    private ArrayList<CategoriasHotel> categoriasHotels;
    Context context;
    LayoutInflater layoutInflater;
    CategoryGridAdapter.CategoryGrid categoriaGrid;

    public CategoryGridAdapter(Context context,ArrayList<CategoriasHotel> cateHotel){
        this.context=context;
        this.categoriasHotels=cateHotel;
        layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CategoryGrid onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_category_adapter,parent,false);

        final CategoryGridAdapter.CategoryGrid categoryGrid = new CategoryGridAdapter.CategoryGrid(view);



        return categoryGrid;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryGrid holder,final int position) {


        final CategoriasHotel categoriasHotel = categoriasHotels.get(position);
        categoriaGrid = (CategoryGridAdapter.CategoryGrid)holder;
        categoriaGrid.nombre_cate_grid.setText(categoriasHotel.getNombre());
        categoriaGrid.cate_fo_grid.setImageURI(categoriasHotel.getFotos());
        Glide.with(context).load(categoriasHotel.getFotos()).into(categoriaGrid.cate_fo_grid);

        categoriaGrid.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filtro_habitaciones filtro_habitaciones = new Filtro_habitaciones();
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                SharedPreferences categoria_nombre = context.getSharedPreferences("categorias_datos",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = categoria_nombre.edit();
                editor.putString("nombre_categoria", categoriasHotels.get(position).getNombre());
                editor.putString("cod_categoria",categoriasHotels.get(position).getId());
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
        return categoriasHotels.size();
    }

    public class CategoryGrid extends RecyclerView.ViewHolder{
        public TextView nombre_cate_grid,numero;
        public ImageView cate_fo_grid;
        public LinearLayout linearLayout;

        public CategoryGrid(final View itemView){
            super(itemView);
            this.nombre_cate_grid=
                    itemView.findViewById(R.id.grid_nombre);
            this.cate_fo_grid=
                    itemView.findViewById(R.id.fot_cat_grid);
            this.linearLayout=
                    itemView.findViewById(R.id.grid_layout);
        }
    }
}
