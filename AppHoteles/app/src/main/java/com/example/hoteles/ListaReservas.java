package com.example.hoteles;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ListaReservas extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private DatabaseReference ref;
    private StorageReference sto;

    private RadioGroup grupo;
    private RadioButton aceptada,rechazada,sinvalor,terminadas;

    private ArrayList<Reserva> reservaArrayList;
    private Reserva_Adapter reserva_adapter;
    private ListView listView;
    private float nueva_puntuacion;

    private Reserva reserva;

    private OnFragmentInteractionListener mListener;

    public ListaReservas() {

    }

    public static ListaReservas newInstance(String param1, String param2) {
        ListaReservas fragment = new ListaReservas();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_lista_reservas, container, false);

        ref = FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();

        aceptada=vista.findViewById(R.id.checkBox_aceptada);
        rechazada=vista.findViewById(R.id.checkBox_rechazada);
        sinvalor=vista.findViewById(R.id.checkBox3_interrogante);
        terminadas=vista.findViewById(R.id.terminada);

        grupo=vista.findViewById(R.id.radiogrupo);

        listView = vista.findViewById(R.id.lista_reservas);

        final SharedPreferences recibo_habitacion = this.getActivity().getSharedPreferences("datos_usuario",Context.MODE_PRIVATE);

        final String cod_cliente = recibo_habitacion.getString("id_cliente","");

        final String username = recibo_habitacion.getString("nombre","");


            reservaArrayList=new ArrayList<>();
            ref.child("hoteles").child("reservas").orderByChild("id_cliente").equalTo(cod_cliente).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    reservaArrayList.clear();
                    for (DataSnapshot son:dataSnapshot.getChildren()){
                        Reserva pojo_reserva = son.getValue(Reserva.class);
                        pojo_reserva.setId(son.getKey());
                        reservaArrayList.add(pojo_reserva);
                    }
                    for (final Reserva r : reservaArrayList){
                        sto.child("hoteles").child("fotos_habitacion").child(r.getId_habitacion()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                r.setFotos(uri);
                                reserva_adapter.notifyDataSetChanged();
                            }
                        });
                    }

                    reserva_adapter.notifyDataSetChanged();
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            reserva_adapter = new Reserva_Adapter(getContext(),android.R.layout.simple_list_item_1,reservaArrayList);
            listView.setAdapter(reserva_adapter);


           grupo.clearCheck();

           grupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(RadioGroup group, int checkedId) {
                   if (aceptada.isChecked()){
                       ref.child("hoteles").child("reservas").orderByChild("id_cliente").equalTo(cod_cliente).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               reservaArrayList.clear();
                               for (DataSnapshot son:dataSnapshot.getChildren()){
                                   Reserva pojo_reserva = son.getValue(Reserva.class);
                                   int stado = pojo_reserva.getEstado();
                                   if (stado==1) {
                                       pojo_reserva.setId(son.getKey());
                                       reservaArrayList.add(pojo_reserva);
                                   }
                               }
                               for (final Reserva r : reservaArrayList){
                                   sto.child("hoteles").child("fotos_habitacion").child(r.getId_habitacion()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                       @Override
                                       public void onSuccess(Uri uri) {
                                           r.setFotos(uri);
                                           reserva_adapter.notifyDataSetChanged();
                                       }
                                   });
                               }

                               reserva_adapter.notifyDataSetChanged();
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });
                       reserva_adapter = new Reserva_Adapter(getContext(),android.R.layout.simple_list_item_1,reservaArrayList);
                       listView.setAdapter(reserva_adapter);
                   }
                   else if (rechazada.isChecked()){
                       ref.child("hoteles").child("reservas").orderByChild("id_cliente").equalTo(cod_cliente).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               reservaArrayList.clear();
                               for (DataSnapshot son:dataSnapshot.getChildren()){
                                   Reserva pojo_reserva = son.getValue(Reserva.class);
                                   int stado = pojo_reserva.getEstado();
                                   if (stado==2) {
                                       pojo_reserva.setId(son.getKey());
                                       reservaArrayList.add(pojo_reserva);
                                   }
                               }
                               for (final Reserva r : reservaArrayList){
                                   sto.child("hoteles").child("fotos_habitacion").child(r.getId_habitacion()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                       @Override
                                       public void onSuccess(Uri uri) {
                                           r.setFotos(uri);
                                           reserva_adapter.notifyDataSetChanged();
                                       }
                                   });
                               }

                               reserva_adapter.notifyDataSetChanged();
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });
                       reserva_adapter = new Reserva_Adapter(getContext(),android.R.layout.simple_list_item_1,reservaArrayList);
                       listView.setAdapter(reserva_adapter);
                   }
                   else if (sinvalor.isChecked()){
                       ref.child("hoteles").child("reservas").orderByChild("id_cliente").equalTo(cod_cliente).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               reservaArrayList.clear();
                               for (DataSnapshot son:dataSnapshot.getChildren()){
                                   Reserva pojo_reserva = son.getValue(Reserva.class);
                                   int stado = pojo_reserva.getEstado();
                                   if (stado==0) {
                                       pojo_reserva.setId(son.getKey());
                                       reservaArrayList.add(pojo_reserva);
                                   }
                               }
                               for (final Reserva r : reservaArrayList){
                                   sto.child("hoteles").child("fotos_habitacion").child(r.getId_habitacion()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                       @Override
                                       public void onSuccess(Uri uri) {
                                           r.setFotos(uri);
                                           reserva_adapter.notifyDataSetChanged();
                                       }
                                   });
                               }

                               reserva_adapter.notifyDataSetChanged();
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });
                       reserva_adapter = new Reserva_Adapter(getContext(),android.R.layout.simple_list_item_1,reservaArrayList);
                       listView.setAdapter(reserva_adapter);

                   }else if (terminadas.isChecked()){
                       ref.child("hoteles").child("reservas").orderByChild("id_cliente").equalTo(cod_cliente).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               reservaArrayList.clear();
                               for (DataSnapshot son:dataSnapshot.getChildren()){
                                   Reserva pojo_reserva = son.getValue(Reserva.class);
                                   int stado = pojo_reserva.getEstado();
                                   if (stado==3) {
                                       pojo_reserva.setId(son.getKey());
                                       reservaArrayList.add(pojo_reserva);
                                   }
                               }
                               for (final Reserva r : reservaArrayList){
                                   sto.child("hoteles").child("fotos_habitacion").child(r.getId_habitacion()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                       @Override
                                       public void onSuccess(Uri uri) {
                                           r.setFotos(uri);
                                           reserva_adapter.notifyDataSetChanged();
                                       }
                                   });
                               }

                               reserva_adapter.notifyDataSetChanged();
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });
                       reserva_adapter = new Reserva_Adapter(getContext(),android.R.layout.simple_list_item_1,reservaArrayList);
                       listView.setAdapter(reserva_adapter);

                       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                           @Override
                           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                               final Reserva reserva =(Reserva) parent.getItemAtPosition(position);
                               ref.child("hoteles").child("valoraciones").orderByChild("id_cliente").equalTo(cod_cliente).addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                       if (dataSnapshot.hasChildren()){
                                           for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                               Valoracion pojo_valoracion = snapshot.getValue(Valoracion.class);
                                               String cod_habitacion = pojo_valoracion.getId_habitacion();
                                               String cod_cliente = pojo_valoracion.getId_cliente();
                                               
                                               if (cod_habitacion.equals(reserva.getId_habitacion()) && cod_cliente.equals(reserva.getId_cliente()) && pojo_valoracion.getEstado()==Valoracion.VALORADA){
                                                   Toast.makeText(getContext(), "Valoracion existe", Toast.LENGTH_SHORT).show();
                                               }else {
                                                   final AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());

                                                   LayoutInflater inflater = getActivity().getLayoutInflater();

                                                   View v = inflater.inflate(R.layout.dialog_valorar,null);

                                                   dialogo.setView(v);

                                                   final AlertDialog dialog = dialogo.create();
                                                   dialog.show();

                                                   Button valorar = (Button)v.findViewById(R.id.button_valorar);
                                                   TextView nombrehabitacion = (TextView) v.findViewById(R.id.val_nombredehabitacion);
                                                   final EditText descripcion = (EditText) v.findViewById(R.id.descrip);
                                                   final RatingBar puntuacion = (RatingBar) v.findViewById(R.id.estrellavaloracion);


                                                   nombrehabitacion.setText(reserva.getNombre_habitacion());


                                                   valorar.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           final String des = descripcion.getText().toString();

                                                           puntuacion.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                                               @Override
                                                               public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                                                   nueva_puntuacion = ratingBar.getRating();
                                                               }
                                                           });
                                                           if (!descripcion.getText().toString().isEmpty() && puntuacion.getRating() != 0) {
                                                               ref.child("hoteles").child("valoraciones").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                   @Override
                                                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                                                       Date date = new Date();

                                                                       final String fecha = dateFormat.format(date);


                                                                       Valoracion new_valoracion = new Valoracion(reserva.getId_habitacion(), reserva.getId_cliente(), fecha, reserva.getNombre_cliente(), reserva.getNombre_habitacion(), des,Valoracion.VALORADA,username);
                                                                       new_valoracion.setPuntuacion(puntuacion.getRating());

                                                                       String clave = ref.child("hoteles").child("valoraciones").push().getKey();
                                                                       ref.child("hoteles").child("valoraciones").child(clave).setValue(new_valoracion);


                                                                       Toast.makeText(getContext(), "Valoracion realizada exitosamente", Toast.LENGTH_SHORT).show();

                                                                       dialog.dismiss();
                                                                   }

                                                                   @Override
                                                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                   }
                                                               });
                                                           }
                                                       }
                                                   });
                                               }
                                           }
                                       }else {

                                           final AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());

                                           LayoutInflater inflater = getActivity().getLayoutInflater();

                                           View v = inflater.inflate(R.layout.dialog_valorar,null);

                                           dialogo.setView(v);

                                           final AlertDialog dialog = dialogo.create();
                                           dialog.show();

                                           Button valorar = (Button)v.findViewById(R.id.button_valorar);
                                           TextView nombrehabitacion = (TextView) v.findViewById(R.id.val_nombredehabitacion);
                                           final EditText descripcion = (EditText) v.findViewById(R.id.descrip);
                                           final RatingBar puntuacion = (RatingBar) v.findViewById(R.id.estrellavaloracion);


                                           nombrehabitacion.setText(reserva.getNombre_habitacion());


                                           valorar.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   final String des = descripcion.getText().toString();

                                                   puntuacion.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                                       @Override
                                                       public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                                           nueva_puntuacion = ratingBar.getRating();
                                                       }
                                                   });
                                                   if (!descripcion.getText().toString().isEmpty() && puntuacion.getRating() != 0) {
                                                       ref.child("hoteles").child("valoraciones").addListenerForSingleValueEvent(new ValueEventListener() {
                                                           @Override
                                                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                               SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                                               Date date = new Date();

                                                               final String fecha = dateFormat.format(date);


                                                               Valoracion new_valoracion = new Valoracion(reserva.getId_habitacion(), reserva.getId_cliente(), fecha, reserva.getNombre_cliente(), reserva.getNombre_habitacion(), des,Valoracion.VALORADA,username);
                                                               new_valoracion.setPuntuacion(puntuacion.getRating());

                                                               String clave = ref.child("hoteles").child("valoraciones").push().getKey();
                                                               ref.child("hoteles").child("valoraciones").child(clave).setValue(new_valoracion);

                                                               Toast.makeText(getContext(), "Valoracion realizada exitosamente", Toast.LENGTH_SHORT).show();

                                                               dialog.dismiss();
                                                           }

                                                           @Override
                                                           public void onCancelled(@NonNull DatabaseError databaseError) {

                                                           }
                                                       });
                                                   }
                                               }
                                           });




                                       }
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                   }
                               });

                           }
                       });


                   }
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
