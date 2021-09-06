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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NovedadesAdaptador extends RecyclerView.Adapter<NovedadesAdaptador.NovedadesItem>{

    private ArrayList<Habitacion> habitacionArrayList;
    Context context;
    NovedadesAdaptador.NovedadesItem novedadesItem;
    LayoutInflater layoutInflater;
    private DatabaseReference ref;

    private final int limite = 3;

    public NovedadesAdaptador(Context context, ArrayList<Habitacion> habitacions){
        this.context=context;
        this.habitacionArrayList=habitacions;
        layoutInflater=LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public NovedadesAdaptador.NovedadesItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.novedades_adapter,parent,false);

        final NovedadesAdaptador.NovedadesItem novedadesItem = new NovedadesAdaptador.NovedadesItem(view);


        return novedadesItem;
    }

    @Override
    public void onBindViewHolder(@NonNull final NovedadesAdaptador.NovedadesItem holder, final int position) {

        ref= FirebaseDatabase.getInstance().getReference();

        final Habitacion habitacion = habitacionArrayList.get(position);
        novedadesItem = (NovedadesItem)holder;
        novedadesItem.nombre.setText(habitacion.getNombre());
        novedadesItem.precio.setText(habitacion.getPrecio()+""+"€");
        novedadesItem.foto_habitacion.setImageURI(habitacion.getFotos());
        Glide.with(context).load(habitacion.getFotos()).into(novedadesItem.foto_habitacion);


//        if (habitacionArrayList.get(position).getDisponibilidad().equalsIgnoreCase("si")){
//            Glide.with(context).load(habitacion.getFotos()).into(novedadesItem.foto_habitacion);
//            novedadesItem.filtro.setVisibility(View.INVISIBLE);
//        }else if (habitacionArrayList.get(position).getDisponibilidad().equalsIgnoreCase("no")){
//            Glide.with(context).load(habitacion.getFotos()).into(habitacionItem.foto_habitacion);
//        }



        novedadesItem.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickHabitacion clickHabitacion = new ClickHabitacion();
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                SharedPreferences datos_habitacion = context.getSharedPreferences("datos_habi",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = datos_habitacion.edit();
                editor.putString("habitacion_ide", habitacionArrayList.get(position).getId());
                editor.putString("nombre_ha",habitacionArrayList.get(position).getNombre());
                editor.putString("descripcion_ha",habitacionArrayList.get(position).getDescripcion());
                editor.putString("precio_ha",habitacionArrayList.get(position).getPrecio()+"");
                editor.putString("categoria_ha",habitacionArrayList.get(position).getCategoria());
                editor.putString("disponible_ha",habitacionArrayList.get(position).getDisponibilidad());
                editor.putString("imagen_ha",habitacionArrayList.get(position).getFotos().toString());
                editor.putString("estrellas_ha",habitacionArrayList.get(position).getEstrellas()+"");
                editor.putString("latitud_ha",habitacionArrayList.get(position).getLatitud());
                editor.putString("longitud_ha",habitacionArrayList.get(position).getLongitud());
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
                manager.beginTransaction().addSharedElement(novedadesItem.foto_habitacion,novedadesItem.foto_habitacion.getTransitionName()).replace(R.id.frame_usu,clickHabitacion).commit();
            }
        });

        ref.child("hoteles").child("valoraciones").orderByChild("id_habitacion").equalTo(habitacionArrayList.get(position).getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int numHijos = (int) dataSnapshot.getChildrenCount();
                List<Float> nums = new ArrayList<Float>();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Float pojo_valoracion = dataSnapshot1.child("puntuacion").getValue(Float.class);

                    nums.add(pojo_valoracion);

                    Float[] miarray = new Float[nums.size()];

                    miarray = nums.toArray(miarray);

                    float media=0;

                    for (Float f:miarray){
                        media = media+f;
                        System.out.println(media);
                    }
                    media=media/miarray.length;



                    ref.child("hoteles").child("habitaciones").child(habitacionArrayList.get(position).getId()).child("valoracion").setValue(media);
                    

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        novedadesItem.valoracion.setText(habitacion.getValoracion()+"");


    }

    @Override
    public int getItemCount() {
        if (habitacionArrayList.size() > limite){
            return limite;
        }else {
            return habitacionArrayList.size();
        }
    }

    public class NovedadesItem extends RecyclerView.ViewHolder{
        public TextView nombre,precio,valoracion;
        public ImageView foto_habitacion;
        public LinearLayout layout;

        public NovedadesItem(final View itemView){
            super(itemView);
            this.nombre=
                    itemView.findViewById(R.id.nove_habitacion);
            this.precio=
                    itemView.findViewById(R.id.nove_precio);
            this.foto_habitacion=
                    itemView.findViewById(R.id.imagenNovedad);
            this.valoracion=
                    itemView.findViewById(R.id.nove_val);
            this.layout=
                    itemView.findViewById(R.id.novedades_layout);

        }
    }


}
