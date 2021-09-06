package com.example.hoteles;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Activity_usuario extends AppCompatActivity {

    private NotificationManager mNotificationManager;
    private DatabaseReference ref;
    private StorageReference sto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        BottomNavigationView bottomNavigationView = findViewById(R.id.boton_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.frame_usu,new Listar_Habitaciones()).commit();

        mNotificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        ref= FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();

        ref.child("hoteles").child("reservas").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Reserva reserva = dataSnapshot.getValue(Reserva.class);
                String id_reserva = dataSnapshot.getKey();
                String cod_cliente = reserva.getId_cliente();

                if (reserva.getEstado()==Reserva.ACEPTADA && reserva.getId_cliente().equals(cod_cliente) && reserva.isEstado_notificado()){
                    notificar(id_reserva,reserva.getNombre_habitacion(),"Reserva Aceptada");
                }else if (reserva.getEstado()==Reserva.RECHAZADA && reserva.getId_cliente().equals(cod_cliente) && reserva.isEstado_notificado()){
                    notificar(id_reserva,reserva.getNombre_habitacion(),"Reserva Rechazada");
                }else if (reserva.getEstado()==Reserva.TERMINADA && reserva.getId_cliente().equals(cod_cliente) && reserva.isEstado_notificado()){
                    notificar(id_reserva,reserva.getNombre_habitacion(),"Reserva Terminada");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void notificar(String id_reserva, String descripcion, String estado){
        //Creamos notificación
        //Creamos un metodo con 3 parametros, mensaje, texto y activity destino
        Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());

        //Añadimos icono a la notificación (icono de nuestra app normalmente)
        mBuilder.setSmallIcon(R.drawable.activo);


        //Añadimos título a la notificación
        mBuilder.setContentTitle(descripcion);

        //Añadimos texto a la notificación
        mBuilder.setContentText(estado);

        //Añadimos imagen a la notificación, pero tenemos que convertirla a Bitmap
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.onlinebooking);
        mBuilder.setLargeIcon(bmp);

        //Para hacer desaparecer la notificación cuando se pulse sobre esta y se abra la Activity de destino
        mBuilder.setAutoCancel(true);

        //Sonido notificación por defecto
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        //Para que vibre el dispositivo
        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });


        mNotificationManager.notify(id_reserva.hashCode(), mBuilder.build());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment seleccion = null;

            switch (menuItem.getItemId()){
                case R.id.fragHome:
                    seleccion = new Listar_Habitaciones();
                    Fade fade = new Fade();
                    fade.setDuration(1000);
                    seleccion.setExitTransition(fade);
                    seleccion.setEnterTransition(fade);
                    break;
                case R.id.fragReserva:
                    seleccion = new ListaReservas();
                    Slide slide = new Slide();
                    slide.setDuration(1000);
                    seleccion.setExitTransition(slide);
                    seleccion.setEnterTransition(slide);
                    break;
                case R.id.fragCategoria:
                    seleccion = new Category_Grid();
                    Explode explode = new Explode();
                    explode.setDuration(1000);
                    seleccion.setExitTransition(explode);
                    seleccion.setEnterTransition(explode);
                    break;
                case R.id.fragPerfil:
                    seleccion = new Perfil_Usuario();
                    fade = new Fade();
                    fade.setDuration(1000);
                    seleccion.setExitTransition(fade);
                    seleccion.setEnterTransition(fade);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_usu,seleccion).addToBackStack(null).commit();

            return true;
        }
    };
}
