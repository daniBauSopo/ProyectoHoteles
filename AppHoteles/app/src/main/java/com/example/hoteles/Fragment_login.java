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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Fragment_login extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    TextInputLayout til_correo;
    TextInputEditText correo,contraseña;
    Button loguearte;
    private DatabaseReference ref;
    private OnFragmentInteractionList mListener;

    public Fragment_login() {
    }

    public static Fragment_login newInstance(String param1, String param2) {
        Fragment_login fragment = new Fragment_login();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        til_correo=v.findViewById(R.id.til_correo);
        correo=v.findViewById(R.id.correo);
        loguearte=v.findViewById(R.id.login);
        contraseña=v.findViewById(R.id.contraseña);

        ref = FirebaseDatabase.getInstance().getReference();

//        String pausa="pausar";
//        onButtonPressed(pausa);


        loguearte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String correo_val = correo.getText().toString();
                final String pass_val = contraseña.getText().toString();

                if (!correo_val.isEmpty() && !pass_val.isEmpty()){
                    ref.child("hoteles").child("clientes").orderByChild("email").equalTo(correo_val).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()){
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                    Cliente cliente = dataSnapshot1.getValue(Cliente.class);
                                    String correo = cliente.getEmail();
                                    String nombre = cliente.getUsername();
                                    if (correo.equals(correo_val)){
                                        String pass = cliente.getContraseña();
                                        if (pass.equals(pass_val)){
                                            boolean baneo = cliente.isBaneado();
                                            if (baneo==false) {
                                                Intent i = new Intent(getContext(), Activity_usuario.class);
                                                String id_cliente = dataSnapshot1.getKey();
                                                SharedPreferences preferences = getActivity().getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("id_cliente", id_cliente);
                                                editor.putString("username", correo);
                                                editor.putString("nombre", nombre);
                                                editor.commit();
                                                startActivity(i);
                                            }else {
                                                Toast.makeText(getContext(), "Este usuario esta bloqueado", Toast.LENGTH_SHORT).show();
                                            }
                                        }else {
                                            Toast.makeText(getContext(), "La contraseña es incorrecta", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(getContext(), "El correo no es correcto", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else {
                                Toast.makeText(getContext(), "Usuario no registrado", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else if(correo_val.isEmpty() && !pass_val.isEmpty()){
                    ref.child("hoteles").child("administrador").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String no_contraseña = dataSnapshot.child("contraseña").getValue().toString();
                                if (pass_val.equals(no_contraseña)) {
                                    Intent i = new Intent(getContext(), MainAdmin.class);
                                    startActivity(i);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    Toast.makeText(getContext(), "Campos obligatorios", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

//    public void requestFocus(View view){
//        if (view.requestFocus()){
//            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
//    }
//
//    private boolean validate_correo(){
//        if (correo.getText().toString().trim().isEmpty()){
//            til_correo.setError("Correo");
//            requestFocus(correo);
//            return false;
//        }
//    return true;
//    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String data) {
        if (mListener != null) {
            mListener.onFragmentMessage("TAGFragment_login", data);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionList) {
            mListener = (OnFragmentInteractionList) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionList");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
