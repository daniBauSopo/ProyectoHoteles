package com.example.hoteles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainAdmin extends AppCompatActivity {

    Button añadir,clientes,ver,verReservas,meterCategorias,salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        añadir=findViewById(R.id.meterhabi);
        clientes=findViewById(R.id.verclientes);
        ver=findViewById(R.id.verhabi);
        verReservas=findViewById(R.id.verreserva);
        meterCategorias=findViewById(R.id.categorias);

        salir=findViewById(R.id.salir);

        añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MeterHabitacion meterHabitacion = new MeterHabitacion();
                meterHabitacion.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.container_admin,meterHabitacion).commit();
            }
        });
        meterCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IngresarCategoria ingresarCategoria= new IngresarCategoria();
                ingresarCategoria.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.container_admin,ingresarCategoria).commit();
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaHabitaciones_Admin listaHabitaciones_admin = new ListaHabitaciones_Admin();
                listaHabitaciones_admin.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.container_admin,listaHabitaciones_admin).commit();
            }
        });

        verReservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaReservasAdmin listaReservasAdmin = new ListaReservasAdmin();
                listaReservasAdmin.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.container_admin,listaReservasAdmin).commit();
            }
        });

        clientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaClientes listaClientes = new ListaClientes();
                listaClientes.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.container_admin,listaClientes).commit();
            }
        });

    }


}
