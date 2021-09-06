package com.example.hoteles;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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


public class ListaReservasAdmin extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ListView lista;
    private DatabaseReference ref;
    private StorageReference sto;
    private AdminAdapter_Reserva adminAdapter_reserva;
    private ArrayList<Reserva>reservaArrayList;

    private OnFragmentInteractionListener mListener;

    public ListaReservasAdmin() {
        // Required empty public constructor
    }


    public static ListaReservasAdmin newInstance(String param1, String param2) {
        ListaReservasAdmin fragment = new ListaReservasAdmin();
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
        View vista = inflater.inflate(R.layout.fragment_lista_reservas_admin, container, false);

        lista=vista.findViewById(R.id.lista_reservas_admin);
        reservaArrayList=new ArrayList<>();
        ref= FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();

        ref.child("hoteles").child("reservas").orderByChild("fecha").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reservaArrayList.clear();
                for (DataSnapshot hijo:dataSnapshot.getChildren()){
                    Reserva pojo_reserva=hijo.getValue(Reserva.class);
                    pojo_reserva.setId(hijo.getKey());
                    reservaArrayList.add(pojo_reserva);
                }
//                for (final Reserva r:reservaArrayList){
//                    sto.child("hoteles").child("fotos_habitacion").child(r.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            r.setFotos(uri);
//                            adminAdapter_reserva.notifyDataSetChanged();
//                        }
//                    });
//                }
                adminAdapter_reserva.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adminAdapter_reserva = new AdminAdapter_Reserva(getContext(),android.R.layout.simple_list_item_1,reservaArrayList);
        lista.setAdapter(adminAdapter_reserva);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Reserva pojo_reserva =(Reserva) parent.getItemAtPosition(position);
                SharedPreferences preferences = getActivity().getSharedPreferences("ver_reserva",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("reserva_id",pojo_reserva.getId());
                editor.putInt("estado_reserva",pojo_reserva.getEstado());
                editor.putString("cod_ha",pojo_reserva.getId_habitacion());
                editor.commit();
                AdminVerReserva adminVerReserva = new AdminVerReserva();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_admin,adminVerReserva).addToBackStack(null).commit();

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
