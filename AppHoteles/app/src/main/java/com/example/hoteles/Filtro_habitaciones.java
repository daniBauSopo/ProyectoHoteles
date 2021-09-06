package com.example.hoteles;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.lang.UCharacter;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class Filtro_habitaciones extends Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    private HabitacionGridAdapter habitacionGridAdapter;
    private ArrayList<Habitacion> list;
    private Habitacion pojo_habitacion;

    Context context;

    private StaggeredGridLayoutManager _sGridLayoutManager;


    private Spinner ordenarprecio;
    private ImageView imagencategoria,amborguesa;
    private TextView total;
    private LinearLayout layoutAnimado;
    private RadioGroup estrellas;
    private RadioButton una,dos,tres,cuatro,cinco;
    private Uri fotos;

    private DatabaseReference ref;
    private StorageReference sto;

    private OnFragmentInteractionListener mListener;

    public Filtro_habitaciones() {
    }

    public static Filtro_habitaciones newInstance(String param1, String param2) {
        Filtro_habitaciones fragment = new Filtro_habitaciones();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_filtro_habitaciones, container, false);

        final SharedPreferences categorias_datos = this.getActivity().getSharedPreferences("categorias_datos",Context.MODE_PRIVATE);

        final String nom_cat = categorias_datos.getString("nombre_categoria","");
        final String cod_cate = categorias_datos.getString("cod_categoria","");



        ref = FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();

        ref = FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();
        fotos=null;
        final DrawerLayout drawerLayout = vista.findViewById(R.id.drawerlayout);

        vista.findViewById(R.id.imagemenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        final NavigationView navigationView = vista.findViewById(R.id.navigationview);
        navigationView.setItemIconTintList(null);


        list = new ArrayList<>();

        recyclerView=(RecyclerView) vista.findViewById(R.id.rv_filtro);


        String [] stars = {"", "1 estrella", "2 estrellas", "3 estrellas", "4 estrellas", "5 estrellas"};

        Spinner sp = (Spinner) navigationView.getMenu().findItem(R.id.naigation_esrellas).getActionView();
        sp.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,stars));
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }else if (position==1){
                    ref.child("hoteles").child("habitaciones").orderByChild("categoria").equalTo(nom_cat).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list.clear();
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                pojo_habitacion = snapshot.getValue(Habitacion.class);
                                float estrellas = pojo_habitacion.getEstrellas();
                                if (estrellas==1) {
                                    pojo_habitacion.setId(snapshot.getKey());
                                    list.add(pojo_habitacion);
                                }
                            }
                            for (final Habitacion habitacion:list){
                                sto.child("hoteles").child("fotos_habitacion").child(habitacion.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        habitacion.setFotos(uri);
                                        habitacionGridAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                            habitacionGridAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    habitacionGridAdapter=new HabitacionGridAdapter(getContext(),list);
                    _sGridLayoutManager  = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(_sGridLayoutManager);
                    recyclerView.setAdapter(habitacionGridAdapter);

                    drawerLayout.closeDrawer(Gravity.LEFT);
                }else if (position==2){
                    ref.child("hoteles").child("habitaciones").orderByChild("categoria").equalTo(nom_cat).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list.clear();
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                pojo_habitacion = snapshot.getValue(Habitacion.class);
                                float estrellas = pojo_habitacion.getEstrellas();
                                if (estrellas==2) {
                                    pojo_habitacion.setId(snapshot.getKey());
                                    list.add(pojo_habitacion);
                                }
                            }
                            for (final Habitacion habitacion:list){
                                sto.child("hoteles").child("fotos_habitacion").child(habitacion.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        habitacion.setFotos(uri);
                                        habitacionGridAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                            habitacionGridAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    habitacionGridAdapter=new HabitacionGridAdapter(getContext(),list);
                    _sGridLayoutManager  = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(_sGridLayoutManager);
                    recyclerView.setAdapter(habitacionGridAdapter);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }else if (position==3){
                    ref.child("hoteles").child("habitaciones").orderByChild("categoria").equalTo(nom_cat).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list.clear();
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                pojo_habitacion = snapshot.getValue(Habitacion.class);
                                float estrellas = pojo_habitacion.getEstrellas();
                                if (estrellas==3) {
                                    pojo_habitacion.setId(snapshot.getKey());
                                    list.add(pojo_habitacion);
                                }
                            }
                            for (final Habitacion habitacion:list){
                                sto.child("hoteles").child("fotos_habitacion").child(habitacion.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        habitacion.setFotos(uri);
                                        habitacionGridAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                            habitacionGridAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    habitacionGridAdapter=new HabitacionGridAdapter(getContext(),list);
                    _sGridLayoutManager  = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(_sGridLayoutManager);
                    recyclerView.setAdapter(habitacionGridAdapter);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }else if (position==4){
                    ref.child("hoteles").child("habitaciones").orderByChild("categoria").equalTo(nom_cat).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list.clear();
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                pojo_habitacion = snapshot.getValue(Habitacion.class);
                                float estrellas = pojo_habitacion.getEstrellas();
                                if (estrellas==4) {
                                    pojo_habitacion.setId(snapshot.getKey());
                                    list.add(pojo_habitacion);
                                }
                            }
                            for (final Habitacion habitacion:list){
                                sto.child("hoteles").child("fotos_habitacion").child(habitacion.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        habitacion.setFotos(uri);
                                        habitacionGridAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                            habitacionGridAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    habitacionGridAdapter=new HabitacionGridAdapter(getContext(),list);
                    _sGridLayoutManager  = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(_sGridLayoutManager);
                    recyclerView.setAdapter(habitacionGridAdapter);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }else if (position==5){
                    ref.child("hoteles").child("habitaciones").orderByChild("categoria").equalTo(nom_cat).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            list.clear();
                            for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                pojo_habitacion = snapshot.getValue(Habitacion.class);
                                float estrellas = pojo_habitacion.getEstrellas();
                                if (estrellas==5) {
                                    pojo_habitacion.setId(snapshot.getKey());
                                    list.add(pojo_habitacion);
                                }
                            }
                            for (final Habitacion habitacion:list){
                                sto.child("hoteles").child("fotos_habitacion").child(habitacion.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        habitacion.setFotos(uri);
                                        habitacionGridAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                            habitacionGridAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    habitacionGridAdapter=new HabitacionGridAdapter(getContext(),list);
                    _sGridLayoutManager  = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(_sGridLayoutManager);
                    recyclerView.setAdapter(habitacionGridAdapter);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ref.child("hoteles").child("habitaciones").orderByChild("categoria").equalTo(nom_cat).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    pojo_habitacion = snapshot.getValue(Habitacion.class);
                    pojo_habitacion.setId(snapshot.getKey());
                    list.add(pojo_habitacion);
                }
                for (final Habitacion habitacion:list){
                    sto.child("hoteles").child("fotos_habitacion").child(habitacion.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            habitacion.setFotos(uri);
                            habitacionGridAdapter.notifyDataSetChanged();
                        }
                    });
                }
                habitacionGridAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        habitacionGridAdapter=new HabitacionGridAdapter(getContext(),list);
        _sGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(_sGridLayoutManager);
        recyclerView.setAdapter(habitacionGridAdapter);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.mascaro:
                        ref.child("hoteles").child("habitaciones").orderByChild("categoria").equalTo(nom_cat).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                list.clear();
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    pojo_habitacion = snapshot.getValue(Habitacion.class);
                                    double precio = pojo_habitacion.getPrecio();
                                    if (precio>=100) {
                                        pojo_habitacion.setId(snapshot.getKey());
                                        list.add(pojo_habitacion);
                                    }
                                }
                                for (final Habitacion habitacion:list){
                                    sto.child("hoteles").child("fotos_habitacion").child(habitacion.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            habitacion.setFotos(uri);
                                            habitacionGridAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                                habitacionGridAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        habitacionGridAdapter=new HabitacionGridAdapter(getContext(),list);
                        _sGridLayoutManager  = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(_sGridLayoutManager);
                        recyclerView.setAdapter(habitacionGridAdapter);
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        return true;
                    case R.id.masbarato:
                        ref.child("hoteles").child("habitaciones").orderByChild("categoria").equalTo(nom_cat).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                list.clear();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    pojo_habitacion = snapshot.getValue(Habitacion.class);
                                    double precio = pojo_habitacion.getPrecio();
                                    if (precio > 0 && precio < 100) {
                                        pojo_habitacion.setId(snapshot.getKey());
                                        list.add(pojo_habitacion);
                                    }
                                }
                                for (final Habitacion habitacion : list) {
                                    sto.child("hoteles").child("fotos_habitacion").child(habitacion.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            habitacion.setFotos(uri);
                                            habitacionGridAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                                habitacionGridAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        habitacionGridAdapter = new HabitacionGridAdapter(getContext(), list);
                        _sGridLayoutManager  = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(_sGridLayoutManager);
                        recyclerView.setAdapter(habitacionGridAdapter);
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        return true;
                        default:
                            return true;
                }
            }
        });

        return vista;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
