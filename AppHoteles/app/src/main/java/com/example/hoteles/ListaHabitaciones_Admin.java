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


public class ListaHabitaciones_Admin extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ListView lista;
    private DatabaseReference ref;
    private StorageReference sto;
    private AdminAdapter_Habitacion adminAdapter_habitacion;
    private ArrayList<Habitacion> habitacions;

    private OnFragmentInteractionListener mListener;

    public ListaHabitaciones_Admin() {
        // Required empty public constructor
    }

    public static ListaHabitaciones_Admin newInstance(String param1, String param2) {
        ListaHabitaciones_Admin fragment = new ListaHabitaciones_Admin();
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
        View vista = inflater.inflate(R.layout.fragment_lista_habitaciones__admin, container, false);

        lista=vista.findViewById(R.id.listha_admin);
        habitacions=new ArrayList<>();
        ref= FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();

        ref.child("hoteles").child("habitaciones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                habitacions.clear();
                for (DataSnapshot hijo:dataSnapshot.getChildren()){
                    Habitacion pojo_habitacion=hijo.getValue(Habitacion.class);
                    pojo_habitacion.setId(hijo.getKey());
                    habitacions.add(pojo_habitacion);
                }
                for (final Habitacion h:habitacions){
                    sto.child("hoteles").child("fotos_habitacion").child(h.getId()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            h.setFotos(uri);
                            adminAdapter_habitacion.notifyDataSetChanged();
                        }
                    });
                }
                adminAdapter_habitacion.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adminAdapter_habitacion = new AdminAdapter_Habitacion(getContext(),android.R.layout.simple_list_item_1,habitacions);
        lista.setAdapter(adminAdapter_habitacion);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Habitacion pojo_habitacion =(Habitacion)parent.getItemAtPosition(position);
                SharedPreferences preferences = getActivity().getSharedPreferences("pasar_editarhabi",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("habitacion_ide",pojo_habitacion.getId());
                editor.putString("spinner_cat",pojo_habitacion.getCategoria());
                editor.putString("nombre_habitacion",pojo_habitacion.getNombre());
                editor.commit();
                Editar_Habitacion editar_habitacion = new Editar_Habitacion();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_admin,editar_habitacion).addToBackStack(null).commit();

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
