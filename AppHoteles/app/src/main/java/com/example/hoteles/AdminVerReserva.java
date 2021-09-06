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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class AdminVerReserva extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private DatabaseReference ref;
    private StorageReference sto;

    TextView correo,nombre_habitacion,fecha,fechaentrada,fechasalida;
    Button aceptarreserva,cancelarreserva,salir;

    private OnFragmentInteractionListener mListener;

    public AdminVerReserva() {
        // Required empty public constructor
    }

    public static AdminVerReserva newInstance(String param1, String param2) {
        AdminVerReserva fragment = new AdminVerReserva();
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
        View vista = inflater.inflate(R.layout.fragment_admin_ver_reserva, container, false);

        ref = FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();

        correo=vista.findViewById(R.id.nombredelcliente);
        nombre_habitacion=vista.findViewById(R.id.nombredehabitacion);
        fecha=vista.findViewById(R.id.fechadereserva);
        fechaentrada=vista.findViewById(R.id.fechadeentrada);
        fechasalida=vista.findViewById(R.id.fechadesalida);

        salir=vista.findViewById(R.id.salir_adminreserva);
        aceptarreserva=vista.findViewById(R.id.pasaraceptado);
        cancelarreserva=vista.findViewById(R.id.pasarechazado);

        SharedPreferences info_reserva = this.getActivity().getSharedPreferences("ver_reserva", Context.MODE_PRIVATE);

        final String cod_reserva = info_reserva.getString("reserva_id","");

        final String cod_habitacion = info_reserva.getString("cod_ha","");

        final int estado = info_reserva.getInt("estado_reserva",0);

        ref.child("hoteles").child("reservas").child(cod_reserva).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ref.child("hoteles").child("reservas").child(cod_reserva).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       try {
                           Reserva r = dataSnapshot.getValue(Reserva.class);
                           correo.setText(r.getNombre_cliente());
                           nombre_habitacion.setText(r.getNombre_habitacion());
                           fecha.setText(r.getFecha());
                           fechaentrada.setText(r.getFecha_entrada());
                           fechasalida.setText(r.getFecha_salida());

                       }catch (NullPointerException npe){

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

        cancelarreserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (estado==0){
                ref.child("hoteles").child("reservas").child(cod_reserva).child("estado").setValue(Reserva.RECHAZADA);
                ref.child("hoteles").child("reservas").child(cod_reserva).child("estado_notificado").setValue(true);
            }else {
                    Toast.makeText(getContext(), "Ya no se puede cambiar el estado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        aceptarreserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (estado==0){
                ref.child("hoteles").child("reservas").child(cod_reserva).child("estado").setValue(Reserva.ACEPTADA);
                ref.child("hoteles").child("reservas").child(cod_reserva).child("estado_notificado").setValue(true);

                ref.child("hoteles").child("habitaciones").child(cod_habitacion).child("disponibilidad").setValue("No");

            }else {
                    Toast.makeText(getContext(), "Ya no se puede cambiar el estado", Toast.LENGTH_SHORT).show();

                }
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getBaseContext(),MainAdmin.class);
                getActivity().startActivity(i);
                getActivity().getSupportFragmentManager().popBackStack();
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
        void onFragmentInteraction(Uri uri);
    }
}
