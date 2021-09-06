package com.example.hoteles;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class Listar_Habitaciones extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

//    PARTE DE ARRIBA==>HABITACIONES
    RecyclerView recyclerView;
    private HabitacionAdapter habitacionAdapter;
    private ArrayList<Habitacion> list;
    private Habitacion pojo_habitacion;
//      PARTE DE ABAJO==>CATEGORIA
    RecyclerView recyclerView2;
    private CategoriaAdapter categoriaAdapter;
    private ArrayList<CategoriasHotel> listaCategorias;
    private CategoriasHotel pojo_categoria;
//    PARTE MAS ABAJO NOVEDADES
    RecyclerView recyclerView3;
    private NovedadesAdaptador novedadesAdaptador;
    private ArrayList<Habitacion> getList;

//    ABAJO DEL ALL
    RecyclerView recyclerView4;
    private UltimaValoraciones_Adapter ultimaValoraciones_adapter;
    private ArrayList<Valoracion>valoracionArrayList;
    private Valoracion pojo_valoracion;


    private DatabaseReference ref;
    private StorageReference sto;

    private OnFragmentInteractionListener mListener;

    public Listar_Habitaciones() {
    }

    public static Listar_Habitaciones newInstance(String param1, String param2) {
        Listar_Habitaciones fragment = new Listar_Habitaciones();
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
        View vista = inflater.inflate(R.layout.fragment_listar__habitaciones, container, false);

        ref = FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();

        list = new ArrayList<>();

        recyclerView=(RecyclerView) vista.findViewById(R.id.lista_habitaciones);

        ref.child("hoteles").child("habitaciones").orderByChild("nombre").addListenerForSingleValueEvent(new ValueEventListener() {
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
                            habitacionAdapter.notifyDataSetChanged();
                        }
                    });
                }
                habitacionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        habitacionAdapter=new HabitacionAdapter(getContext(),list);
        recyclerView.setAdapter(habitacionAdapter);


        listaCategorias=new ArrayList<>();

        recyclerView2=(RecyclerView)vista.findViewById(R.id.lista_categorias);

        ref.child("hoteles").child("categorias").orderByChild("nombre").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaCategorias.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    pojo_categoria = snapshot.getValue(CategoriasHotel.class);
                    pojo_categoria.setId(snapshot.getKey());
                    listaCategorias.add(pojo_categoria);
                }
                for (final CategoriasHotel categoriasHotel:listaCategorias){
                    sto.child("hoteles").child("fotos_categorias").child(categoriasHotel.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            categoriasHotel.setFotos(uri);
                            categoriaAdapter.notifyDataSetChanged();

                        }
                    });
                    categoriaAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LinearLayoutManager manager1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView2.setLayoutManager(manager1);
        recyclerView2.setHasFixedSize(true);
        categoriaAdapter=new CategoriaAdapter(getContext(),listaCategorias);
        recyclerView2.setAdapter(categoriaAdapter);



        getList=new ArrayList<>();

        recyclerView3=vista.findViewById(R.id.rv_novedades);

        ref.child("hoteles").child("habitaciones").orderByChild("fecha").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Habitacion pojo_habitacion = snapshot.getValue(Habitacion.class);
                    String dispo = pojo_habitacion.getDisponibilidad();
                    if (dispo.equalsIgnoreCase("si")) {
                        pojo_habitacion.setId(snapshot.getKey());
                        getList.add(pojo_habitacion);
                    }
                }
                for (final Habitacion habitacion:getList){
                    sto.child("hoteles").child("fotos_habitacion").child(habitacion.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            habitacion.setFotos(uri);
                            novedadesAdaptador.notifyDataSetChanged();
                        }
                    });
                }
                novedadesAdaptador.notifyDataSetChanged();
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        LinearLayoutManager manager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),manager2.getOrientation());
        recyclerView3.addItemDecoration(dividerItemDecoration);
        recyclerView3.setLayoutManager(manager2);
        recyclerView3.setHasFixedSize(true);
        novedadesAdaptador=new NovedadesAdaptador(getContext(),getList);
        recyclerView3.setAdapter(novedadesAdaptador);


        valoracionArrayList=new ArrayList<>();

        recyclerView4=vista.findViewById(R.id.rv_ultimas_novedades);

        ref.child("hoteles").child("valoraciones").orderByChild("fecha").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                valoracionArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    pojo_valoracion = dataSnapshot1.getValue(Valoracion.class);
                    pojo_valoracion.setId(dataSnapshot1.getKey());
                    valoracionArrayList.add(pojo_valoracion);
                }
                for (final Valoracion v:valoracionArrayList){
                    sto.child("hoteles").child("fotos_habitacion").child(v.getId_habitacion()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            v.setFoto(uri);
                            ultimaValoraciones_adapter.notifyDataSetChanged();
                        }
                    });
                }
                ultimaValoraciones_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LinearLayoutManager manager3 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView4.setLayoutManager(manager3);
        recyclerView4.setHasFixedSize(true);
        ultimaValoraciones_adapter = new UltimaValoraciones_Adapter(getContext(),valoracionArrayList);
        recyclerView4.setAdapter(ultimaValoraciones_adapter);


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
