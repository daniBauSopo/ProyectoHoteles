package com.example.hoteles;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class Cronometro implements Runnable{
    public int segundos;
    private Handler escribirenUI;
    private ImageView cambio_imagenes;

//    private ProgressBar progressBar;
//    private ObjectAnimator progressAnimator;

    public Cronometro(ImageView cam) {
        segundos = 0;
        cambio_imagenes=cam;
//        progressBar=progress;
        escribirenUI = new Handler();
    }
    public void run(){
        try {


            while (Boolean.TRUE){
                segundos++;
                Thread.sleep(1000);
                if (segundos != 17){
                    if (segundos == 4){
                        segundos++;
                    }if(segundos==8){
                        segundos++;
                    }if (segundos==12){
                        segundos++;
                    }if (segundos==16){
                        segundos=0;
                    }
                }else {
                    segundos=0;
                }
                try {
                    escribirenUI.post(new Runnable() {
                        @Override
                        public void run() {
                            if (segundos==4){
                                cambio_imagenes.setImageResource(R.drawable.primera);
//                                init();
                            }if (segundos==8){
                                cambio_imagenes.setImageResource(R.drawable.segunda);
//                                init();
                            }if (segundos==12){
                                cambio_imagenes.setImageResource(R.drawable.tercera);
//                                init();
                            }if (segundos==16){
                                cambio_imagenes.setImageResource(R.drawable.cuarta);
//                                init();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }catch (OutOfMemoryError oome){

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    private void init(){
//        progressAnimator = ObjectAnimator.ofInt(progressBar,"progress",0,100);
//
//        progressAnimator.setDuration(3060);
//
//        progressAnimator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                progressBar.setVisibility(View.GONE);
//            }
//        });
//
//    }
}

