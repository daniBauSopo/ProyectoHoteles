package com.example.hoteles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Fragment_registro extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextInputLayout til_correo;
    TextInputEditText usuario,telefono,contra,rep_contra;
    EditText email;
    Button registrarte;
    private DatabaseReference ref;

    public Fragment_registro() {
    }

    public static Fragment_registro newInstance(String param1, String param2) {
        Fragment_registro fragment = new Fragment_registro();
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
        View v = inflater.inflate(R.layout.fragment_registro, container, false);

        ref = FirebaseDatabase.getInstance().getReference();

        usuario=v.findViewById(R.id.nikname);
        email=v.findViewById(R.id.correo_electronico);
        telefono=v.findViewById(R.id.telefono);
        contra=v.findViewById(R.id.contraseña_registro);
        rep_contra=v.findViewById(R.id.contraseña_repeticion);

        registrarte=v.findViewById(R.id.boton_registrarse);

        registrarte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String val_usuario = usuario.getText().toString();
                final String val_email = email.getText().toString();
                final String val_telefono = telefono.getText().toString();
                final String val_contraseña = contra.getText().toString();
                final String val_rep_contra = rep_contra.getText().toString();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = new Date();

                final String fecha = dateFormat.format(date);

                if (!val_usuario.isEmpty() && !val_email.isEmpty() && !val_telefono.isEmpty() && !val_contraseña.isEmpty() && !val_rep_contra.isEmpty()){
                    ref.child("hoteles").child("clientes").orderByChild("email").equalTo(val_email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()){
                                Toast.makeText(getContext(), "Este usuario ya existe", Toast.LENGTH_SHORT).show();
                            }else{
                                if (val_contraseña.equals(val_rep_contra)){
                                    Cliente new_cliente = new Cliente(val_email,val_contraseña,val_usuario,val_telefono,fecha,"");

                                    String clave = ref.child("hoteles").child("clientes").push().getKey();
                                    ref.child("hoteles").child("clientes").child(clave).setValue(new_cliente);

                                    Toast.makeText(getContext(), "Nuevo usuario registrado", Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(getActivity().getBaseContext(),MainActivity.class);
                                    getActivity().startActivity(i);
                                    getActivity().getSupportFragmentManager().popBackStack();

                                }
                                else {
                                    Toast.makeText(getContext(), "Contraseñas no son iguales", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    Toast.makeText(getContext(), "Todos los datos son obligatorios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }
}
