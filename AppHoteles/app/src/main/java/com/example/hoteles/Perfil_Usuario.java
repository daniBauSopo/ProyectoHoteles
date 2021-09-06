package com.example.hoteles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class Perfil_Usuario extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private DatabaseReference ref;
    private StorageReference sto;

    TextView username,correo,telefono,direccion,fecha;
    Button modificarperfil,salirperfil;
    ImageView imageView,salir;
    Uri fotos;

    private ArrayList<Valoracion> valoracionArrayList;
    private ValoracionAdapter valoracionAdapter;
    private ListView listView;

    private OnFragmentInteractionListener mListener;

    public Perfil_Usuario() {
        // Required empty public constructor
    }


    public static Perfil_Usuario newInstance(String param1, String param2) {
        Perfil_Usuario fragment = new Perfil_Usuario();
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
        View vista = inflater.inflate(R.layout.fragment_perfil__usuario, container, false);

        ref= FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();

        username=vista.findViewById(R.id.nombre_usuario);
        correo=vista.findViewById(R.id.correo_usuario);
        telefono=vista.findViewById(R.id.telefono_usuario);
        direccion=vista.findViewById(R.id.direccion_usuario);
        fecha=vista.findViewById(R.id.fecha_usuario);
        salir=vista.findViewById(R.id.imagensalir);

        listView=vista.findViewById(R.id.listavaloraciones);



        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

        fotos=null;

        imageView=vista.findViewById(R.id.imagenUsuario);

        modificarperfil=vista.findViewById(R.id.modificar_perfil);
        salirperfil=vista.findViewById(R.id.salir_perfil);

        final SharedPreferences recibo_habitacion = this.getActivity().getSharedPreferences("datos_usuario",Context.MODE_PRIVATE);

        final String cod_cliente = recibo_habitacion.getString("id_cliente","");

        ref.child("hoteles").child("clientes").child(cod_cliente).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ref.child("hoteles").child("clientes").child(cod_cliente).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Cliente cliente = dataSnapshot.getValue(Cliente.class);
                        username.setText(cliente.getUsername());
                        correo.setText(cliente.getEmail());
                        telefono.setText(cliente.getTelefono());
                        direccion.setText(cliente.getDireccion());
                        fecha.setText(cliente.getFecha());

                        sto.child("hoteles").child("fotos_clientes").child(cod_cliente).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(getContext()).load(uri).into(imageView);
                                    fotos=uri;
                                }
                            });
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

        salirperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getBaseContext(),Activity_usuario.class);
                getActivity().startActivity(i);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        modificarperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_usu,new Modificar_Perfil()).commit();

            }
        });

        valoracionArrayList=new ArrayList<>();
        ref.child("hoteles").child("valoraciones").orderByChild("id_cliente").equalTo(cod_cliente).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                valoracionArrayList.clear();
                for (DataSnapshot son:dataSnapshot.getChildren()){
                    Valoracion pojo_valoracion = son.getValue(Valoracion.class);
                    pojo_valoracion.setId(son.getKey());
                    valoracionArrayList.add(pojo_valoracion);
                }
                for (final Valoracion v : valoracionArrayList){
                    sto.child("hoteles").child("fotos_habitacion").child(v.getId_habitacion()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            v.setFoto(uri);
                            valoracionAdapter.notifyDataSetChanged();
                        }
                    });
                }

                valoracionAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        valoracionAdapter = new ValoracionAdapter(getContext(),android.R.layout.simple_list_item_1,valoracionArrayList);
        listView.setAdapter(valoracionAdapter);


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
