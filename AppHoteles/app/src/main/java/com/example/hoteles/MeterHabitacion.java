package com.example.hoteles;

import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class MeterHabitacion extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    EditText nombre,precio,descripcion,longitud,latitud,hotel;
    Button cancelar,ingresar;
    Spinner categoria,disponible;
    RatingBar estrellas;
//    RadioGroup valoracion;
//    RadioButton Like,disLike;
    private Uri fotos;
    private final static int ESCOGER_FOTO=1;
    private ImageView foto;
    private float estrellas_nuevo;

    private DatabaseReference ref;
    private StorageReference sto;


    public MeterHabitacion() {
    }

    public static MeterHabitacion newInstance(String param1, String param2) {
        MeterHabitacion fragment = new MeterHabitacion();
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
        final View v = inflater.inflate(R.layout.fragment_meter_habitacion, container, false);

        nombre=v.findViewById(R.id.nombrehab);
        precio=v.findViewById(R.id.precio);
        descripcion=v.findViewById(R.id.descripcion);
        hotel=v.findViewById(R.id.nombrehotel);

        longitud=v.findViewById(R.id.longitud);
        latitud=v.findViewById(R.id.latitud);

        cancelar=v.findViewById(R.id.cancelarmeter);
        ingresar=v.findViewById(R.id.ingresarha);


        disponible=v.findViewById(R.id.disponible_spinner);

        estrellas=v.findViewById(R.id.ratingStar);
//
//        valoracion=v.findViewById(R.id.valoracion);
//        Like=v.findViewById(R.id.Like);
//        disLike=v.findViewById(R.id.disLike);

        foto=v.findViewById(R.id.foto_ingreso);

        ref = FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();

        fotos=null;


        String [] disp = {"Si", "No"};

        ArrayAdapter<CharSequence> adapterDispo = new ArrayAdapter<CharSequence>(getContext(), R.layout.support_simple_spinner_dropdown_item, disp);

        disponible.setAdapter(adapterDispo);


        ref.child("hoteles").child("categorias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> cate = new ArrayList<String>();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String pojo_cate = snapshot.child("nombre").getValue(String.class);
                    cate.add(pojo_cate);
                }
                categoria=v.findViewById(R.id.categoria_spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,cate);
                categoria.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,ESCOGER_FOTO);
            }
        });

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String opcion="";

                final String nomHabi = nombre.getText().toString();
                final String precioHabi = precio.getText().toString();
                final String descripcionHabi = descripcion.getText().toString();
                final String categoriaHabi = categoria.getSelectedItem().toString();
                final String disponibleHabi = disponible.getSelectedItem().toString();
                final String val_latitud=latitud.getText().toString();
                final String val_longitud=longitud.getText().toString();
                final String val_hotel = hotel.getText().toString();
//                if (Like.isChecked()){
//                    opcion="Like";
//                }
//                if (disLike.isChecked()){
//                    opcion="disLike";
//                }

                estrellas.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        estrellas_nuevo=ratingBar.getRating();
                    }
                });
                if (!nomHabi.isEmpty() && !precioHabi.isEmpty() && !descripcionHabi.isEmpty() && !categoriaHabi.isEmpty() && !disponibleHabi.isEmpty() && !val_latitud.isEmpty() && !val_longitud.isEmpty() && !val_hotel.isEmpty()){
                    if (fotos !=null){
//                        final String finalOpcion = opcion;
                        ref.child("hoteles").child("habitaciones").orderByChild("nombre").equalTo(nomHabi).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()){
                                    Toast.makeText(getContext(), "Esta ya existe", Toast.LENGTH_SHORT).show();
                                }else{
                                    double preciook=Double.parseDouble(precioHabi);

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                    Date date = new Date();

                                    final String fecha = dateFormat.format(date);

                                    Habitacion new_habitacion = new Habitacion(nomHabi,categoriaHabi,disponibleHabi,0,preciook,descripcionHabi,val_latitud,val_longitud,fecha,val_hotel);
                                    new_habitacion.setEstrellas(estrellas.getRating());

                                    String clave = ref.child("hoteles").child("habitaciones").push().getKey();
                                    ref.child("hoteles").child("habitaciones").child(clave).setValue(new_habitacion);
                                    sto.child("hoteles").child("fotos_habitacion").child(clave).putFile(fotos);

                                    Toast.makeText(getContext(), "Habitacion insertada con exito", Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(getContext().getApplicationContext(),MainAdmin.class);
                                    getActivity().startActivity(i);
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        Toast.makeText(getContext(), "Escoge una foto", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Campo vacio", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getBaseContext(),MainAdmin.class);
                getActivity().startActivity(i);
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


        return v;
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
}
