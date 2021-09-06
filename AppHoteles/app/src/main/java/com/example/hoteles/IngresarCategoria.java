package com.example.hoteles;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_OK;


public class IngresarCategoria extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private DatabaseReference reference;
    private StorageReference sto;
    private EditText nom_cat;
    private Button metercat,no_cat;
    private Uri fotos;
    private final static int ESCOGER_FOTO=1;
    private ImageView foto;

    public IngresarCategoria() {
    }


    public static IngresarCategoria newInstance(String param1, String param2) {
        IngresarCategoria fragment = new IngresarCategoria();
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

        View v =  inflater.inflate(R.layout.fragment_ingresar_categoria, container, false);

        reference = FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();

        nom_cat=v.findViewById(R.id.meter_category);
        metercat=v.findViewById(R.id.categorias_si);
        no_cat=v.findViewById(R.id.categorias_no);

        foto=v.findViewById(R.id.fotoCategoria);

        fotos=null;

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,ESCOGER_FOTO);
            }
        });

        metercat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String val_category = nom_cat.getText().toString();
                reference.child("hoteles").child("categorias").orderByChild("nombre").equalTo(val_category).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()){
                            Toast.makeText(getContext(), "Esta categoria ya existe", Toast.LENGTH_SHORT).show();
                        }else {
                            CategoriasHotel new_categoria = new CategoriasHotel(val_category);

                            String clave = reference.child("hoteles").child("categorias").push().getKey();
                            reference.child("hoteles").child("categorias").child(clave).setValue(new_categoria);
                            sto.child("hoteles").child("fotos_categorias").child(clave).putFile(fotos);

                            Toast.makeText(getContext(), "Categoria ingresada correctamente", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getContext().getApplicationContext(),MainAdmin.class);
                            getActivity().startActivity(i);
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        no_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext().getApplicationContext(),MainAdmin.class);
                getActivity().startActivity(i);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ESCOGER_FOTO && resultCode==RESULT_OK){
            fotos=data.getData();
            foto.setImageURI(fotos);
            Toast.makeText(getContext(),"Imagen correctamente seleccionada",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
        }
    }
}
