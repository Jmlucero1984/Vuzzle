package com.myPuzzle.vuzzle;

import android.app.Activity;
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
import java.util.ArrayList;

public class Reducer extends AsyncTask<Void, Integer, Integer> {

    private String  nombre_salida;
    ProgressDialog pd3;
    int count;
    Context c;
    int progreso = 0;
    Bitmap salida, bitmap;
    Boolean cancelada=false;


    ArrayList<String> piezas;


    ObservableInteger terceraFase;


    public Reducer (Context c, ArrayList<String> piezas, ProgressDialog pd, ObservableInteger terceraFase) {
        this.terceraFase = terceraFase;
        this.pd3=pd;
        this.piezas=piezas;
        this.c = c;


    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        pd3.setTitle("PASO 3/3");
        pd3.setMessage("Reduciendo piezas...");
        pd3.setIndeterminate(true);
        pd3.setCancelable(true);
        pd3.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                try {
                    this.finalize();
                    cancelada=true;
                    terceraFase.set(4);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                Toast.makeText(c,"PROCESO INTERRUMPIDO!",Toast.LENGTH_LONG).show();
            }
        });
        pd3.show();



        count = piezas.size()*2;


    }

    @Override
    protected Integer doInBackground(Void... params) {




        for(String i:piezas) {

            File dirDest = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VuzzleGame/Factory/"+i );
            bitmap = BitmapFactory.decodeFile(dirDest.toString());

            int width= (int) (150*MainActivity.factor);// (int) bitmap.getWidth()/2;
            int heigth=(int) (150*MainActivity.factor);// (int) bitmap.getHeight()/2;
            Bitmap salida = Bitmap.createScaledBitmap(bitmap,width,heigth,true);
            String nombre_salida=i+"_red_";
            saveToGallery(salida,nombre_salida);
            progreso++;
                publishProgress((int) ((progreso / (float) count) * 100));
                if (isCancelled())
                {
                    cancelada=true;
                    terceraFase.set(4);
                    break;
                }
            }
        for (String i : piezas) {
            File fileToDelete = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VuzzleGame/Factory/" + i );
            fileToDelete .delete();

            progreso++;
            publishProgress((int) ((progreso / (float) count) * 100));
            if (isCancelled())
            {
                terceraFase.set(4);
                cancelada=true;
                break;
            }

        }

        return count;
    }

    protected void onProgressUpdate(Integer... progress) {
       if(progress[0]<50) {
            pd3.setMessage("Reduciendo..." + progress[0] + "%");
        }else {
             pd3.setMessage("Limpiando residuos... " + progress[0] + "%");
        }

    }

    protected void onPostExecute(Integer count) {
        super.onPostExecute(count);
        ready_flag();





        pd3.dismiss();
        if(!cancelada) {
            Toast.makeText(c, "TODO LISTO!" , Toast.LENGTH_SHORT).show();
            terceraFase.set(3);
        }


    }

    private void saveToGallery(Bitmap myBitmap, String nombre) {

        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/VuzzleGame/Factory");
        dir.mkdirs();
        if (nombre.equals("")) {
            nombre = System.currentTimeMillis() + "";
        }
        ;
        String filename = nombre ;
        File outFile = new File(dir, filename);
        try {
            outputStream = new FileOutputStream(outFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        myBitmap.setHasAlpha(true);
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
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

    private void ready_flag() {
        FileWriter writer = null;
        File filetxt = Environment.getExternalStorageDirectory();
        String fileNametxt = filetxt.getAbsolutePath() + "/VuzzleGame/Factory";
        File gpxfile = new File(fileNametxt, "ready_flag");


        try {
            writer = new FileWriter(gpxfile);
            writer.append("");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






}
