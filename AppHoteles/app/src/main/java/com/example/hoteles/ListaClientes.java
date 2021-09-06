package com.example.hoteles;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class ListaClientes extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private DatabaseReference ref;
    private StorageReference sto;

    private ListView listView;
    private ArrayList<Cliente> clientesArrayList;
    private JugadorLista_Admin jugadorLista_admin;

    private OnFragmentInteractionListener mListener;

    public ListaClientes() {
        // Required empty public constructor
    }

    public static ListaClientes newInstance(String param1, String param2) {
        ListaClientes fragment = new ListaClientes();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_clientes, container, false);

        ref = FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();

        listView = view.findViewById(R.id.listaclientes);

        clientesArrayList = new ArrayList<>();
        ref.child("hoteles").child("clientes").orderByChild("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clientesArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Cliente pojo_cliente = dataSnapshot1.getValue(Cliente.class);
                    pojo_cliente.setId(dataSnapshot1.getKey());
                    clientesArrayList.add(pojo_cliente);
                }
                for (final Cliente c:clientesArrayList){
                    sto.child("hoteles").child("fotos_clientes").child(c.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            c.setFoto(uri);
                            jugadorLista_admin.notifyDataSetChanged();
                        }
                    });
                }
                jugadorLista_admin.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        jugadorLista_admin = new JugadorLista_Admin(getContext(),android.R.layout.simple_list_item_1,clientesArrayList);
        listView.setAdapter(jugadorLista_admin);

        return view;
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
