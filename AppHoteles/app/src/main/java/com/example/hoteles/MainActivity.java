package com.example.hoteles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionList{

    Button inicioSesion, registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicioSesion = findViewById(R.id.inicioSesion);
        registro = findViewById(R.id.registro);


                Cambio_imagenes cambio_imagenes = new Cambio_imagenes();
                cambio_imagenes.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.zoom_back_in,R.anim.zoom_back_out).replace(R.id.container1, cambio_imagenes).addToBackStack(null).commit();


        inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_login fragment_login = new Fragment_login();
                fragment_login.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.zoom_back_in,R.anim.zoom_back_out).replace(R.id.container1, fragment_login).addToBackStack(null).commit();
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_registro fragment_registro = new Fragment_registro();
                fragment_registro.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.zoom_back_in,R.anim.zoom_back_out).replace(R.id.container1, fragment_registro).addToBackStack(null).commit();

            }
        });

    }

    @Override
    public void onFragmentMessage(String TAG, String data) {
        if (TAG.equals("TAGFragment_login")){
//            Toast.makeText(this, "info recibida"+data, Toast.LENGTH_SHORT).show();
//            acabar=data;
        }
    }
}

