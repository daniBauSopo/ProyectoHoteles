package com.example.hoteles;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class JugadorLista_Admin extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<Cliente> clientes;
    TextView nombre_de_ususario,correo_de_usuario,fecha_de_usuario,telefono_de_usuario,direccion_de_usuario;
    CircleImageView imagen;
    Button banear,liberar;
    Uri foto;

    private DatabaseReference ref;
    private StorageReference sto;

    public JugadorLista_Admin(Context context,int resource,ArrayList<Cliente> clientes){
        super(context,resource,clientes);
        this.context=context;
        this.resource=resource;
        this.clientes=clientes;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.cliente_item, null);
        }

        ref = FirebaseDatabase.getInstance().getReference();
        sto= FirebaseStorage.getInstance().getReference();

        nombre_de_ususario=v.findViewById(R.id.nombre_de_ususario);
        correo_de_usuario=v.findViewById(R.id.correo_de_usuario);
        fecha_de_usuario=v.findViewById(R.id.fecha_de_usuario);
        telefono_de_usuario=v.findViewById(R.id.telefono_de_usuario);
        direccion_de_usuario=v.findViewById(R.id.direccion_de_usuario);
        imagen=v.findViewById(R.id.imagencliente_adapter);
        banear=v.findViewById(R.id.banear);
        liberar=v.findViewById(R.id.liberar);
        foto=null;

        final Cliente pojo_cliente = clientes.get(position);
        nombre_de_ususario.setText(pojo_cliente.getUsername());
        correo_de_usuario.setText(pojo_cliente.getEmail());
        fecha_de_usuario.setText(pojo_cliente.getFecha());
        telefono_de_usuario.setText(pojo_cliente.getTelefono());
        direccion_de_usuario.setText(pojo_cliente.getDireccion());
        Glide.with(getContext()).load(pojo_cliente.getFoto()).into(imagen);



        banear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pojo_cliente.isBaneado()){
                    Toast.makeText(getContext(), "Ya esta baneado", Toast.LENGTH_SHORT).show();
                }else if (!pojo_cliente.isBaneado()){
                    ref.child("hoteles").child("clientes").child(clientes.get(position).getId()).child("baneado").setValue(true);
                }
            }
        });

        liberar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pojo_cliente.isBaneado()){
                    ref.child("hoteles").child("clientes").child(clientes.get(position).getId()).child("baneado").setValue(false);
                }else if (!pojo_cliente.isBaneado()){
                    Toast.makeText(getContext(), "Este usuario esta libre", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return v;

    }




    }
