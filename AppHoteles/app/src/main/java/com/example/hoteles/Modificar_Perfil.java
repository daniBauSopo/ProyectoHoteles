package com.example.hoteles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_OK;


public class Modificar_Perfil extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText nombre,contraseña,direccion,telefono;
    private Uri fotos;
    private final static int ESCOGER_FOTO=1;
    private ImageView foto;

    private Button modificar,cancelar;

    private DatabaseReference ref;
    private StorageReference sto;

    private OnFragmentInteractionListener mListener;

    public Modificar_Perfil() {
    }

    public static Modificar_Perfil newInstance(String param1, String param2) {
        Modificar_Perfil fragment = new Modificar_Perfil();
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
        View vista = inflater.inflate(R.layout.fragment_modificar__perfil, container, false);

        ref = FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();

        fotos=null;
        nombre=vista.findViewById(R.id.modificar_nombre);
        contraseña=vista.findViewById(R.id.modificar_contrasena);
        direccion=vista.findViewById(R.id.modificar_direccion);
        telefono=vista.findViewById(R.id.modificar_telefono);

        foto=vista.findViewById(R.id.imageView);
        modificar=vista.findViewById(R.id.confirmar_modificar);
        cancelar=vista.findViewById(R.id.salir_modificar);

        final SharedPreferences recibo_habitacion = this.getActivity().getSharedPreferences("datos_usuario",Context.MODE_PRIVATE);

        final String cod_cliente = recibo_habitacion.getString("id_cliente","");
        final String nombre1 = recibo_habitacion.getString("nombre","");

        ref.child("hoteles").child("clientes").child(cod_cliente).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ref.child("hoteles").child("clientes").child(cod_cliente).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            Cliente cliente = dataSnapshot.getValue(Cliente.class);
                            nombre.setText(cliente.getUsername());
                            contraseña.setText(cliente.getContraseña());
                            direccion.setText(cliente.getDireccion());
                            telefono.setText(cliente.getTelefono());

                            sto.child("hoteles").child("fotos_clientes").child(cod_cliente).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(getContext()).load(uri).into(foto);
                                    fotos=uri;
                                }
                            });

                        }catch (NullPointerException nps){

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String val_nom=nombre.getText().toString();
                final String val_contraseña=contraseña.getText().toString();
                final String val_direccion=direccion.getText().toString();
                final String val_telefono=telefono.getText().toString();

                if (!val_nom.isEmpty() && !val_contraseña.isEmpty() && !val_telefono.isEmpty()){
                    if (nombre1.equals(val_nom)){
                        ref.child("hoteles").child("clientes").child(cod_cliente).child("username").setValue(val_nom);
                        ref.child("hoteles").child("clientes").child(cod_cliente).child("contraseña").setValue(val_contraseña);
                        ref.child("hoteles").child("clientes").child(cod_cliente).child("direccion").setValue(val_direccion);
                        ref.child("hoteles").child("clientes").child(cod_cliente).child("telefono").setValue(val_telefono);

                        sto.child("hoteles").child("fotos_clientes").child(cod_cliente).putFile(fotos);

                        Toast.makeText(getContext(), "Perfil modificado perfectamente", Toast.LENGTH_SHORT).show();

                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_usu,new Perfil_Usuario()).commit();

                    }else {
                        ref.child("hoteles").child("clientes").orderByChild("username").equalTo(val_nom).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()){
                                    Toast.makeText(getContext(), "Ya existe este usuario", Toast.LENGTH_SHORT).show();
                                }else {
                                    ref.child("hoteles").child("clientes").child(cod_cliente).child("username").setValue(val_nom);
                                    ref.child("hoteles").child("clientes").child(cod_cliente).child("contraseña").setValue(val_contraseña);
                                    ref.child("hoteles").child("clientes").child(cod_cliente).child("direccion").setValue(val_direccion);
                                    ref.child("hoteles").child("clientes").child(cod_cliente).child("telefono").setValue(val_telefono);

                                    sto.child("hoteles").child("fotos_clientes").child(cod_cliente).putFile(fotos);

                                    Toast.makeText(getContext(), "Perfil modificado perfectamente", Toast.LENGTH_SHORT).show();

                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_usu,new Perfil_Usuario()).commit();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }else {
                    Toast.makeText(getContext(), "Rellena todos los datos obligatorios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,ESCOGER_FOTO);
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getBaseContext(),Activity_usuario.class);
                getActivity().startActivity(i);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return vista;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ESCOGER_FOTO && resultCode==RESULT_OK){
            fotos=data.getData();
            foto.setImageURI(fotos);
            Toast.makeText(getContext(),"Imagen seleccionada",Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(getContext(),"Error",Toast.LENGTH_LONG).show();
        }
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
