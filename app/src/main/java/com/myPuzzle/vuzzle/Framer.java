package com.myPuzzle.vuzzle;


import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.BitmapFactory.decodeResource;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;

import android.os.Environment;
import android.util.Log;
import android.view.View;


import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.chrono.Era;
import java.util.ArrayList;


public class Framer extends AsyncTask <Void, Integer, Integer> {


    ProgressDialog pd2;
    int count, num_alto, num_largo, alto, largo;
    Bitmap tablero;
    String nombre_pieza_a_enmarcar;
    Context c;
    int progreso = 0;
    Bitmap fondo_base, astilla;
    Bitmap esquina_sup_izq;



    boolean cancelada =false;




    public Framer (Context c,int num_largo, int num_alto,Bitmap tablero, String nombre_pieza_a_enmarcar) {
         this.num_largo=num_largo;
         this.num_alto = num_alto;
         this.tablero=tablero;
         this.nombre_pieza_a_enmarcar=nombre_pieza_a_enmarcar;

        this.c = c;

    }





    @Override
    protected void onPreExecute() {
        super.onPreExecute();
         largo=num_largo*81+41+63+63;
         alto = num_alto*81+41+63+63;
        fondo_base= Bitmap.createBitmap(largo, alto,ARGB_8888);
        esquina_sup_izq = Bitmap.createBitmap(decodeResource(c.getResources(), R.drawable.marco_sup_izq));
        esquina_sup_izq = Bitmap.createScaledBitmap(esquina_sup_izq, 164, 164, true);
        astilla = Bitmap.createBitmap(decodeResource(c.getResources(), R.drawable.astilla_marco));
        astilla = Bitmap.createScaledBitmap(astilla, 1, 63, true);
        count=100;

        pd2 = new ProgressDialog(c);
        pd2.setTitle("BIEN HECHO!");
        pd2.setMessage("Enmarcando tu juego...");
        pd2.setIndeterminate(true);
        pd2.setCancelable(true);
        pd2.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                try {
                    cancelada=true;

                    this.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                Toast.makeText(c,"PROCESO INTERRUMPIDO!",Toast.LENGTH_LONG).show();
            }
        });
        pd2.show();



    }

    @Override
    protected Integer doInBackground(Void... params) {

        for (int i = 0; i < esquina_sup_izq.getHeight(); i++) {
            for (int j = 0; j < esquina_sup_izq.getWidth(); j++) {
                int color_src = esquina_sup_izq.getPixel(j, i);
                fondo_base.setPixel(j, i, color_src);
                fondo_base.setPixel(largo - j-1, i , color_src);
                fondo_base.setPixel(largo - j-1, alto - i-1, color_src);
                fondo_base.setPixel(j, alto - i-1, color_src);

                if (isCancelled()){
                    //segundaFase.set(4);
                    cancelada=true;
                    break;
                }
            }

        }
        progreso+=25;
        publishProgress((int) ((progreso / (float) count) * 100));

        for (int i = 0; i < astilla.getHeight(); i++) {
            for (int j = 164; j < fondo_base.getWidth()-163; j++) {
                int color_src = astilla.getPixel(0, i);
                fondo_base.setPixel(j, i, color_src);
                fondo_base.setPixel(j, alto - i-1, color_src);

                if (isCancelled()){
                    //segundaFase.set(4);
                    cancelada=true;
                    break;
                }
            }

        }
        progreso+=25;
        publishProgress((int) ((progreso / (float) count) * 100));
        for (int i = 0; i < astilla.getHeight(); i++) {
            for (int j = 164; j < fondo_base.getHeight()-163; j++) {
                int color_src = astilla.getPixel(0, i);
                fondo_base.setPixel(i, j, color_src);
                fondo_base.setPixel(largo-i-1, j, color_src);

                if (isCancelled()){
                    //segundaFase.set(4);
                    cancelada=true;
                    break;
                }
            }

        }
        progreso+=25;
        publishProgress((int) ((progreso / (float) count) * 100));
        for (int i = 0; i < tablero.getHeight(); i++) {
            for (int j =0; j < tablero.getWidth(); j++) {
                int color_src = tablero.getPixel(j, i);
                fondo_base.setPixel(j+63, i+63, color_src);

                if (isCancelled()){
                    //segundaFase.set(4);
                    cancelada=true;
                    break;
                }

            }

        }
        progreso+=25;
        publishProgress((int) ((progreso / (float) count) * 100));

        return count;
    }

    protected void onProgressUpdate(Integer... progress) {
        pd2.setMessage("Enmarcando tu juego... " + progress[0] + "%");

    }

    protected void onPostExecute(Integer count) {
        super.onPostExecute(count);
        saveCapture(fondo_base, nombre_pieza_a_enmarcar);

        pd2.dismiss();
        if(!cancelada) {
            Toast.makeText(c, "JUEGO ENMARCADO!", Toast.LENGTH_SHORT).show();
            //segundaFase.set(2);
        }


    }
    private void saveCapture(Bitmap myBitmap, String nombre) {

        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/VuzzleCompleted");
        dir.mkdirs();
        if (nombre.equals("")) {
            nombre = System.currentTimeMillis() + "";
        }
        ;
        String filename = nombre;
        File outFile = new File(dir, filename);
        try {
            outputStream = new FileOutputStream(outFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        myBitmap.setHasAlpha(true);
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
        try {
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}






