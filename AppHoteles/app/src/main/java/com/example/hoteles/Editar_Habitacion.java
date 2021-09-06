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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.StringResourceValueReader;
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
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class Editar_Habitacion extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText nom,des,pre,longitud,latitud,hotel;
    private Spinner spinner_dispo,spinner_cate;
    private Uri fotos;
    RatingBar editar_estrellas;
    private final static int ESCOGER_FOTO=1;
    private ImageView foto;
    private float rating_nuevo;

    private Button modificar,salir;

    private DatabaseReference ref;
    private StorageReference sto;


    private OnFragmentInteractionListener mListener;

    public Editar_Habitacion() {
        // Required empty public constructor
    }

    public static Editar_Habitacion newInstance(String param1, String param2) {
        Editar_Habitacion fragment = new Editar_Habitacion();
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
        View vista = inflater.inflate(R.layout.fragment_editar__habitacion, container, false);

        nom=vista.findViewById(R.id.editar_nombreha);
        des=vista.findViewById(R.id.editar_descripcion);
        pre=vista.findViewById(R.id.editar_precio);
        spinner_dispo=vista.findViewById(R.id.spinner_editardisponible);
        spinner_cate=vista.findViewById(R.id.spinner_editarcategoria);
        editar_estrellas=vista.findViewById(R.id.editar_estrellas);
        fotos=null;
        foto=vista.findViewById(R.id.imageView_editarfo);
        modificar=vista.findViewById(R.id.modificar);
        salir=vista.findViewById(R.id.cancelarmodi);
        hotel=vista.findViewById(R.id.editar_nombrehotel);

        longitud=vista.findViewById(R.id.editar_longitud);
        latitud=vista.findViewById(R.id.editar_latitud);

        ref = FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();

        String [] disp = {"Si", "No"};

        ArrayAdapter<CharSequence> adapterDispo = new ArrayAdapter<CharSequence>(getContext(), R.layout.support_simple_spinner_dropdown_item, disp);

        spinner_dispo.setAdapter(adapterDispo);

        SharedPreferences preferences = getActivity().getSharedPreferences("pasar_editarhabi", Context.MODE_PRIVATE);

        final String codigo_habitacion = preferences.getString("habitacion_ide","");

        final String stringcategoria = preferences.getString("spinner_cat","");

        final String nombre_habitacion = preferences.getString("nombre_habitacion","");

        ref.child("hoteles").child("habitaciones").child(codigo_habitacion).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ref.child("hoteles").child("habitaciones").child(codigo_habitacion).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            Habitacion h = dataSnapshot.getValue(Habitacion.class);
                            nom.setText(h.getNombre());
                            des.setText(h.getDescripcion());
                            pre.setText(h.getPrecio()+"");
                            longitud.setText(h.getLongitud());
                            latitud.setText(h.getLatitud());
                            hotel.setText(h.getHotel());
                            if(h.getDisponibilidad().equalsIgnoreCase("si")){
                                spinner_dispo.setSelection(0);
                            }else if (h.getDisponibilidad().equalsIgnoreCase("no")){
                                spinner_dispo.setSelection(1);
                            }
                            editar_estrellas.setRating(h.getEstrellas());

                            sto.child("hoteles").child("fotos_habitacion").child(codigo_habitacion).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(getActivity()).load(uri).into(foto);
                                    fotos=uri;
                                }
                            });

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

        ref.child("hoteles").child("categorias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String>  cat = new ArrayList<String>();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String pojo_categoria = snapshot.child("nombre").getValue(String.class);
                    cat.add(pojo_categoria);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,cat);
                spinner_cate.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String val_nom=nom.getText().toString();
                final String val_des=des.getText().toString();
                final double val_pre=Double.parseDouble(pre.getText().toString());
                final String val_categoria=spinner_cate.getSelectedItem().toString();
                final String val_dispo=spinner_dispo.getSelectedItem().toString();
                final String val_latitud=latitud.getText().toString();
                final String val_longitud=longitud.getText().toString();
                final String val_hotel = hotel.getText().toString();

                editar_estrellas.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        rating_nuevo=ratingBar.getRating();
                    }
                });

                if (!val_nom.isEmpty() && !val_des.isEmpty() && !pre.getText().toString().isEmpty() && !val_categoria.isEmpty() &&
                !val_dispo.isEmpty() && !val_latitud.isEmpty() && !val_longitud.isEmpty() && !val_hotel.isEmpty()){
                    if (nombre_habitacion.equals(val_nom)){

//                        ref.child("hoteles").child("habitaciones").child(codigo_habitacion).child("nombre").setValue(val_nom);
//                        ref.child("hoteles").child("habitaciones").child(codigo_habitacion).child("descripcion").setValue(val_des);
//                        ref.child("hoteles").child("habitaciones").child(codigo_habitacion).child("precio").setValue(val_pre);
//                        ref.child("hoteles").child("habitaciones").child(codigo_habitacion).child("categoria").setValue(val_categoria);
//                        ref.child("hoteles").child("habitaciones").child(codigo_habitacion).child("estrellas").setValue(rating_nuevo);
//                        ref.child("hoteles").child("habitaciones").child(codigo_habitacion).child("disponibilidad").setValue(val_dispo);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date date = new Date();

                        final String fecha = dateFormat.format(date);

                        Habitacion mod_habi = new Habitacion(val_nom,val_categoria,val_dispo,0,val_pre,val_des,val_latitud,val_longitud,fecha,val_hotel);
                        mod_habi.setEstrellas(editar_estrellas.getRating());
                        ref.child("hoteles").child("habitaciones").child(codigo_habitacion).setValue(mod_habi);
//
                        sto.child("hoteles").child("fotos_habitacion").child(codigo_habitacion).putFile(fotos);

                        Toast.makeText(getContext(), "Habitacion modificada perfectamente", Toast.LENGTH_SHORT).show();
                        Intent ipg = new Intent(getActivity(), MainAdmin.class);
                        startActivity(ipg);
                    }else {
                        ref.child("hoteles").child("habitaciones").orderByChild("nombre").equalTo(val_nom).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()){
                                    Toast.makeText(getContext(), "Ya existe el producto con el mismo nombre", Toast.LENGTH_SHORT).show();
                                }else {

//                                    ref.child("hoteles").child("habitaciones").child(codigo_habitacion).child("nombre").setValue(val_nom);
//                                    ref.child("hoteles").child("habitaciones").child(codigo_habitacion).child("descripcion").setValue(val_des);
//                                    ref.child("hoteles").child("habitaciones").child(codigo_habitacion).child("precio").setValue(val_pre);
//                                    ref.child("hoteles").child("habitaciones").child(codigo_habitacion).child("categoria").setValue(val_categoria);
//                                    ref.child("hoteles").child("habitaciones").child(codigo_habitacion).child("estrellas").setValue(editar_estrellas);
//                                    ref.child("hoteles").child("habitaciones").child(codigo_habitacion).child("disponibilidad").setValue(val_dispo);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                    Date date = new Date();

                                    final String fecha = dateFormat.format(date);

                                    Habitacion mod_habi = new Habitacion(val_nom,val_categoria,val_dispo,0,val_pre,val_des,val_latitud,val_longitud,fecha,val_hotel);
                                    mod_habi.setEstrellas(editar_estrellas.getRating());
                                    ref.child("hoteles").child("habitaciones").child(codigo_habitacion).setValue(mod_habi);

                                    sto.child("hoteles").child("fotos_habitacion").child(codigo_habitacion).putFile(fotos);

                                    Toast.makeText(getContext(), "Habitacion modificada perfectamente", Toast.LENGTH_SHORT).show();
                                    Intent ipg = new Intent(getActivity(), MainAdmin.class);
                                    startActivity(ipg);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }else {
                    Toast.makeText(getContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();

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
