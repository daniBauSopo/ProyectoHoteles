package com.example.hoteles;

import android.content.Context;
import android.content.SharedPreferences;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.Explode;
import android.transition.TransitionSet;
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
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HabitacionGridAdapter extends RecyclerView.Adapter<SampleHabitacionGridAdapter> {
    private ArrayList<Habitacion> habitacionArrayList;
    Context context;
    LayoutInflater layoutInflater;
    private DatabaseReference ref;
    int lastPosition = -1;

    public HabitacionGridAdapter(Context context, ArrayList<Habitacion> habitacions){
        this.context=context;
        this.habitacionArrayList=habitacions;
        layoutInflater=LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public SampleHabitacionGridAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


       View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_habitacion_adapter,null);
       SampleHabitacionGridAdapter hga = new SampleHabitacionGridAdapter(layoutView);
       return hga;

    }

    @Override
    public void onBindViewHolder(@NonNull final SampleHabitacionGridAdapter holder, final int position) {
        ref= FirebaseDatabase.getInstance().getReference();

        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

                final Habitacion habitacion = habitacionArrayList.get(position);
            holder.nombre.setText(habitacion.getNombre());
            holder.precio.setText(habitacion.getPrecio() + "" + "€");
            holder.foto_habitacion.setImageURI(habitacion.getFotos());
        Glide.with(context).load(habitacion.getFotos()).apply(requestOptions).into(holder.foto_habitacion);
            holder.hotel.setText(habitacion.getHotel());
        holder.valoracionT.setText(habitacion.getValoracion()+"");


            if (habitacionArrayList.get(position).getDisponibilidad().equalsIgnoreCase("si")) {
                holder.dispo.setText("Disponible");
            } else if (habitacionArrayList.get(position).getDisponibilidad().equalsIgnoreCase("no")) {
                holder.dispo.setText("No Disponible");
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClickHabitacion clickHabitacion = new ClickHabitacion();
                    FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                    SharedPreferences datos_habitacion = context.getSharedPreferences("datos_habi", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = datos_habitacion.edit();
                    editor.putString("habitacion_ide", habitacionArrayList.get(position).getId());
                    editor.putString("nombre_ha", habitacionArrayList.get(position).getNombre());
                    editor.putString("descripcion_ha", habitacionArrayList.get(position).getDescripcion());
                    editor.putString("precio_ha", habitacionArrayList.get(position).getPrecio() + "");
                    editor.putString("categoria_ha", habitacionArrayList.get(position).getCategoria());
                    editor.putString("disponible_ha", habitacionArrayList.get(position).getDisponibilidad());
                    editor.putString("imagen_ha", habitacionArrayList.get(position).getFotos().toString());
                    editor.putString("estrellas_ha", habitacionArrayList.get(position).getEstrellas() + "");
                    editor.putString("latitud_ha", habitacionArrayList.get(position).getLatitud());
                    editor.putString("longitud_ha", habitacionArrayList.get(position).getLongitud());
                    editor.commit();
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1,15);
                    animation.setInterpolator(interpolator);
                    holder.itemView.startAnimation(animation);
                    TransitionSet transitionSet = new TransitionSet();
                    transitionSet.addTransition(new ChangeImageTransform());
                    transitionSet.addTransition(new ChangeBounds());
                    transitionSet.setDuration(800);

                    clickHabitacion = new ClickHabitacion();
                    clickHabitacion.setSharedElementEnterTransition(transitionSet);
                    clickHabitacion.setSharedElementReturnTransition(transitionSet);
                    Explode fade = new Explode();
                    fade.setStartDelay(800);
                    clickHabitacion.setEnterTransition(fade);


                    manager.beginTransaction().addSharedElement(holder.foto_habitacion,holder.foto_habitacion.getTransitionName()).replace(R.id.frame_usu, clickHabitacion).commit();
                }
            });



        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_rv);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }

//            try {
//                ref.child("hoteles").child("valoraciones").orderByChild("id_habitacion").equalTo(habitacionArrayList.get(position).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                int numHijos = (int) dataSnapshot.getChildrenCount();
//                        List<Float> nums = new ArrayList<Float>();
//                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
//                            Float pojo_valoracion = dataSnapshot1.child("puntuacion").getValue(Float.class);
//
//                            nums.add(pojo_valoracion);
//
//                            Float[] miarray = new Float[nums.size()];
//
//                            miarray = nums.toArray(miarray);
//
//                            float media=0;
//
//                            for (Float f:miarray){
//                                media = media+f;
//                                System.out.println(media);
//                            }
//                            media=media/miarray.length;
//
//
//
//                            ref.child("hoteles").child("habitaciones").child(habitacionArrayList.get(position).getId()).child("valoracion").setValue(media);
//
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }catch (IndexOutOfBoundsException asa){
//
//            }





    }

    @Override
    public int getItemCount() {
            return habitacionArrayList.size();
    }

}
