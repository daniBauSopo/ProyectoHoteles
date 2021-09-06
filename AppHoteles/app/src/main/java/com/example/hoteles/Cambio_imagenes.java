package com.example.hoteles;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class Cambio_imagenes extends Fragment{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageView cambio_imagenes;
//    private ProgressBar progressBar;
//    private ObjectAnimator progressAnimator;


    private OnFragmentInteractionList mListener;

    public Cambio_imagenes() {
    }

    public static Cambio_imagenes newInstance(String param1, String param2) {
        Cambio_imagenes fragment = new Cambio_imagenes();
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
        View vista =  inflater.inflate(R.layout.fragment_cambio_imagenes, container, false);

        cambio_imagenes= vista.findViewById(R.id.cambio_img);
//        progressBar=vista.findViewById(R.id.progress_pasar);


        Cronometro cronometro = new Cronometro(cambio_imagenes);

        new Thread(cronometro).start();


        return vista;
    }


    public void onButtonPressed(String data) {
        if (mListener != null) {
            mListener.onFragmentMessage("TAGCambio_imagenes",data);
            System.out.println(data+"lol");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionList) {
            mListener = (OnFragmentInteractionList) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionList");
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
