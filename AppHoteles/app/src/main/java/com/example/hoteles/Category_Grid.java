package com.example.hoteles;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class Category_Grid extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    private CategoryGridAdapter categoryGridAdapter;
    private ArrayList<CategoriasHotel> listaCategorias;
    private CategoriasHotel pojo_categoria;


    private DatabaseReference ref;
    private StorageReference sto;

    private OnFragmentInteractionListener mListener;

    public Category_Grid() {
    }

    public static Category_Grid newInstance(String param1, String param2) {
        Category_Grid fragment = new Category_Grid();
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
        View vista = inflater.inflate(R.layout.fragment_category__grid, container, false);

        ref = FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();


        listaCategorias=new ArrayList<>();

        recyclerView=(RecyclerView)vista.findViewById(R.id.grid_cat_rv);

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
                            categoryGridAdapter.notifyDataSetChanged();

                        }
                    });
                    categoryGridAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LinearLayoutManager manager1 = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(manager1);
        recyclerView.setHasFixedSize(true);
        categoryGridAdapter=new CategoryGridAdapter(getContext(),listaCategorias);
        recyclerView.setAdapter(categoryGridAdapter);


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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
