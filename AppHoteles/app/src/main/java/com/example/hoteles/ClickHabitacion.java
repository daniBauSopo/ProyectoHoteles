package com.example.hoteles;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.tapadoo.alerter.Alerter;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.vo.DateData;
import sun.bob.mcalendarview.vo.MarkedDates;


public class ClickHabitacion extends Fragment implements OnMapReadyCallback{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView nombre,descripcion,precio,categoria,disponible,valoracion,hotel;
    private ImageView imagenHabitacion;
    private Uri foto;
    private RatingBar mirarEstrellas;
    private MCalendarView calendarView;

    private Button cancelar,reservar;

    private ScrollView scrollView;
    private CardView cardView;


    LocationManager locationManager;
    Location location;

     MapView mapView;
     GoogleMap map;

    TextView entrada,salida;

    Calendar calendario = Calendar.getInstance();


    private DatabaseReference ref;
    private StorageReference sto;


    private OnFragmentInteractionListener mListener;

    public ClickHabitacion() {
    }

    public static ClickHabitacion newInstance(String param1, String param2) {
        ClickHabitacion fragment = new ClickHabitacion();
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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_click_habitacion, container, false);

        final SharedPreferences recibo_habitacion = this.getActivity().getSharedPreferences("datos_habi",Context.MODE_PRIVATE);

        final String cod_habitacion = recibo_habitacion.getString("habitacion_ide","");
        final String nombre_ha = recibo_habitacion.getString("nombre_ha","");
        final String descripcion_ha = recibo_habitacion.getString("descripcion_ha","");
        final String precio_ha = recibo_habitacion.getString("precio_ha","");
        final String categoria_ha = recibo_habitacion.getString("categoria_ha","");
        final String disponible_ha = recibo_habitacion.getString("disponible_ha","");
        final String hotel_ha = recibo_habitacion.getString("hotel_ha","");

        final String imagen_String = recibo_habitacion.getString("imagen_ha","");
        Uri imagen_ha = Uri.parse(imagen_String);
        final String estrellas_ha = recibo_habitacion.getString("estrellas_ha","");
        float estrella = Float.parseFloat(estrellas_ha);

        final SharedPreferences datos_usuario = this.getActivity().getSharedPreferences("datos_usuario",Context.MODE_PRIVATE);

        final String cod_usuario = datos_usuario.getString("id_cliente","");
        final String username = datos_usuario.getString("username","");

        final double precioT=Double.parseDouble(precio_ha);



        nombre=vista.findViewById(R.id.nombre_info);
        descripcion=vista.findViewById(R.id.descripcion_info);
        precio=vista.findViewById(R.id.precio_info);
        categoria=vista.findViewById(R.id.categoria_info);
        disponible=vista.findViewById(R.id.dispo_info);
        hotel=vista.findViewById(R.id.hotel_info);

        valoracion=vista.findViewById(R.id.texto_valoracion);
        imagenHabitacion=vista.findViewById(R.id.imagenClik);

        reservar=vista.findViewById(R.id.reservar);
        cancelar=vista.findViewById(R.id.salir_click);


        scrollView=vista.findViewById(R.id.scrollviewclick);
        cardView=vista.findViewById(R.id.cardviewAnimada);


        mapView=vista.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);


        mapView.getMapAsync( this);


        foto=imagen_ha;


        entrada=vista.findViewById(R.id.entrada);
        salida=vista.findViewById(R.id.salida);

        entrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(),R.style.DialogTheme, date, calendario
                        .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        salida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(),R.style.DialogTheme, date2, calendario
                        .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();





            }
        });




        ref= FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();

        mirarEstrellas=vista.findViewById(R.id.mirarEstre);

                            nombre.setText(nombre_ha);
                            descripcion.setText(descripcion_ha);
                            precio.setText(precio_ha+" €"+" (por noche)");
                            categoria.setText(categoria_ha);
                            disponible.setText(disponible_ha);
                            hotel.setText(hotel_ha);
                            mirarEstrellas.setRating(estrella);
                            Glide.with(getContext()).load(imagen_ha).into(imagenHabitacion);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getBaseContext(),Activity_usuario.class);
                getActivity().startActivity(i);
                getActivity().getWindow().setExitTransition(new Explode());

            }
        });

        ref.child("hoteles").child("valoraciones").orderByChild("id_habitacion").equalTo(cod_habitacion).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int numHijos = (int) dataSnapshot.getChildrenCount();
                List<Float> nums = new ArrayList<Float>();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Float pojo_valoracion = dataSnapshot1.child("puntuacion").getValue(Float.class);

                    nums.add(pojo_valoracion);

                    Float[] miarray = new Float[nums.size()];

                    miarray = nums.toArray(miarray);

                    float media=0;

                    for (Float f:miarray){
                        media = media+f;
                        System.out.println(media);
                    }
                    media=media/miarray.length;

                        valoracion.setText(media + "");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ref.child("hoteles").child("reservas").orderByChild("id_habitacion").equalTo(cod_habitacion).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Reserva reserva = dataSnapshot1.getValue(Reserva.class);
                        String salida = reserva.getFecha_salida();
                        String id_reserva = dataSnapshot1.getKey();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date date = new Date();

                        final String fecha = dateFormat.format(date);

                        try {
                            Date date1 = dateFormat.parse(fecha);
                            Date date2 = dateFormat.parse(salida);

                            if (date1.after(date2)) {

                                ref.child("hoteles").child("habitaciones").child(cod_habitacion).child("disponibilidad").setValue("Si");
                                ref.child("hoteles").child("reservas").child(id_reserva).child("estado").setValue(Reserva.TERMINADA);
                                ref.child("hoteles").child("reservas").child(id_reserva).child("estado_notificado").setValue(true);
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY==0){
                    CardView.LayoutParams layoutParams= (CardView.LayoutParams) cardView.getLayoutParams();
                    layoutParams.topMargin=300;
                    
                    cardView.setLayoutParams(layoutParams);
                }else if(scrollY>0){
                    CardView.LayoutParams layoutParams= (CardView.LayoutParams) cardView.getLayoutParams();
                    layoutParams.topMargin=60;
                    cardView.setLayoutParams(layoutParams);
                }
            }
        });


        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String entrada_text= entrada.getText().toString();
                final String salida_text = salida.getText().toString();

                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    Date fechaInicial = dateFormat1.parse(entrada_text);
                    Date fechaFinal=dateFormat1.parse(salida_text);

                    int dias = (int) ((fechaFinal.getTime()-fechaInicial.getTime())/86400000);

                    final double pre = precioT*dias;


                    if (disponible_ha.equalsIgnoreCase("No")) {
                        Toast.makeText(getContext(), "Habitacion llena estos dias", Toast.LENGTH_SHORT).show();
                    } else {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date date = new Date();

                        final String fecha = dateFormat.format(date);


                        if (!entrada.getText().toString().isEmpty() && !salida.getText().toString().isEmpty()) {

                            ref.child("hoteles").child("reservas").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Reserva new_reserva = new Reserva(nombre_ha, username, cod_habitacion, cod_usuario, fecha, Reserva.ENVIADA, entrada_text, salida_text,pre);
                                    String id_reserva = ref.child("hoteles").child("reservas").push().getKey();
                                    ref.child("hoteles").child("reservas").child(id_reserva).setValue(new_reserva);


                                    Toast.makeText(getContext(), "Reserva hecha con exito", Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(getActivity().getBaseContext(), Activity_usuario.class);
                                    getActivity().startActivity(i);
                                    getActivity().getWindow().setExitTransition(new Explode());

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else {
                            Toast.makeText(getContext(), "Las fechas tienen que estar rellenas", Toast.LENGTH_SHORT).show();
                        }
                    }



                } catch (ParseException e) {
                    e.printStackTrace();
                }





            }
        });



        return vista;
    }



    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, monthOfYear);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();
        }

    };

    private void actualizarInput() {
        String formatoDeFecha = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        entrada.setText(sdf.format(calendario.getTime()));

    }

    DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, monthOfYear);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput2();

            final SharedPreferences recibo_habitacion = getActivity().getSharedPreferences("datos_habi",Context.MODE_PRIVATE);

            final String precio_ha = recibo_habitacion.getString("precio_ha","");

            final double precioT=Double.parseDouble(precio_ha);

            final String entrada_text= entrada.getText().toString();
            final String salida_text = salida.getText().toString();
            if (!entrada_text.isEmpty() && !salida_text.isEmpty()) {

                try {

                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

                    Date fechaInicial = dateFormat1.parse(entrada_text);

                    Date fechaFinal = dateFormat1.parse(salida_text);

                    int dias = (int) ((fechaFinal.getTime() - fechaInicial.getTime()) / 86400000);

                    final double pre = precioT * dias;

                    Alerter.create(getActivity())
                            .setTitle("Precio por noche")
                            .setText("Precio total con esas fechas: " + pre)
                            .setIcon(R.drawable.ic_monetization_on_black_24dp)
                            .setBackgroundColorRes(R.color.verdeGuapo2)
                            .setDuration(5000).enableSwipeToDismiss().enableProgress(true).setProgressColorRes(R.color.textColor).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

    };

    private void actualizarInput2() {
        String formatoDeFecha = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        salida.setText(sdf.format(calendario.getTime()));

    }

    @Override
    public void onMapReady(GoogleMap googleMap){

        final SharedPreferences recibo_habitacion = this.getActivity().getSharedPreferences("datos_habi",Context.MODE_PRIVATE);

        final String latitud_ha = recibo_habitacion.getString("latitud_ha","");
        final String longitud_ha = recibo_habitacion.getString("longitud_ha","");
        final String nombre_ha = recibo_habitacion.getString("nombre_ha","");
        double lat = Double.parseDouble(latitud_ha);
        double log = Double.parseDouble(longitud_ha);




        map=googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        LatLng hotel_position = new LatLng(lat,log);
        map.addMarker(new MarkerOptions().position(hotel_position).title(nombre_ha).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marcamap)));
        map.moveCamera(CameraUpdateFactory.newLatLng(hotel_position));
        map.animateCamera(CameraUpdateFactory.zoomTo(11.0f));
        map.getUiSettings().setZoomControlsEnabled(true);
    }


    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onPause(){
        super.onPause();
        mapView.onPause();
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
