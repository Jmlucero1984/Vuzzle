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
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class unPackStuff extends AsyncTask<Void, Integer, Integer> {

    ProgressDialog pd;
    int count;
    Context c;
    int progreso = 0;
    ObservableInteger fase;
    boolean cancelada;

    int loader_adicional=0;
ArrayList<int[]> dimensiones = new ArrayList<int []>();





    public unPackStuff(Context c, ObservableInteger fase) {
        this.c = c;
        this.fase=fase;


    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dimensiones.add(new int[]{300,300,550,550});
        dimensiones.add(new int[]{300,231,850,650});
        dimensiones.add(new int[]{250,400,650,1050});
        dimensiones.add(new int[]{300,200,1650,1050});
        dimensiones.add(new int[]{300,250,1750,1350});
        pd = new ProgressDialog(c,R.style.AlertDialogCustom);


        pd.setTitle("INSTALANDO");
        pd.setMessage("Abriendo la caja...");
        pd.setIndeterminate(true);
        pd.setCancelable(true);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                try {
                    cancelada=true;
                    fase.set(5);
                    this.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                Toast.makeText(c,"PROCESO INTERRUMPIDO!",Toast.LENGTH_LONG).show();
            }
        });
        pd.show();

        File filebg = Environment.getExternalStorageDirectory();
        File dir = new File(filebg.getAbsolutePath() + "/VuzzleGame/bgd");
        if (!dir.exists()) {
            dir.mkdir();

            loader_adicional=100;


        }


        count = 100+loader_adicional;



    }

    @Override
    protected Integer doInBackground(Void... params) {





            Bitmap fondo_base = Bitmap.createBitmap(decodeResource(c.getResources(), R.drawable.fondo_madera));
            fondo_base = Bitmap.createScaledBitmap(fondo_base, 900, 900, true);
            int largo_desplegado = 1800;
            int alto_desplegado = 1800;
            Bitmap fondo_desplegado = Bitmap.createBitmap(largo_desplegado, alto_desplegado, ARGB_8888);
            int parcial=0;

            for (int i = 0; i < fondo_base.getHeight(); i++) {
                parcial++;
                for (int j = 0; j < fondo_base.getWidth(); j++) {
                    int color_src = fondo_base.getPixel(j, i);
                    fondo_desplegado.setPixel(j, i, color_src);
                    fondo_desplegado.setPixel(largo_desplegado - j-1, i + 0, color_src);
                    fondo_desplegado.setPixel(largo_desplegado - j-1, alto_desplegado - i-1, color_src);
                    fondo_desplegado.setPixel(j, alto_desplegado - i-1, color_src);
                }
                if(parcial>9) {
                    progreso++;
                    parcial=0;
                    Log.i("SALIDA","PORCENTAJE_PRIMERA: "+progreso+"%");
                }

                publishProgress((int) ((progreso / (float) count) * 100));
                if (isCancelled()) break;
            }
            saveToGallery_jpg(fondo_desplegado, "background");


             for (int i = 1; i < 6; i++) {
                 String nombre_thumb = "img_level_" + i + "_thumb";
                 Bitmap bitmap = Bitmap.createBitmap(decodeResource(c.getResources(), c.getResources().getIdentifier(nombre_thumb, "drawable", c.getPackageName())));
                 bitmap = Bitmap.createScaledBitmap(bitmap, dimensiones.get(i-1)[0], dimensiones.get(i-1)[1], true);

                 saveUnpack(bitmap, nombre_thumb);

                 String nombre_full = "img_level_" + i;
                 Bitmap bitmap2 = Bitmap.createBitmap(decodeResource(c.getResources(), c.getResources().getIdentifier(nombre_full, "drawable", c.getPackageName())));
                 bitmap2 = Bitmap.createScaledBitmap(bitmap2, dimensiones.get(i-1)[2], dimensiones.get(i-1)[3], true);
                saveUnpack(bitmap2, nombre_full);

                 progreso+=20;
                 Log.i("SALIDA","PORCENTAJE_SEGUNDA: "+progreso+"%");
                 publishProgress((int) ((progreso / (float) count) * 100));
                 if (isCancelled()) break;
             }


        return count;

    }




    protected void onProgressUpdate(Integer... progress) {
        pd.setMessage("Abriendo la caja... " + progress[0] + "%");

    }

    protected void onPostExecute(Integer count) {
        super.onPostExecute(count);
        crearLista();

        if(!cancelada) {
            Toast.makeText(c, "INSTALACION COMPLETA", Toast.LENGTH_SHORT).show();
        }

        fase.set(5);
        pd.dismiss();



    }

    private void crearLista() {
        FileWriter writer = null;
        File filetxt = Environment.getExternalStorageDirectory();
        String fileNametxt = filetxt.getAbsolutePath() + "/VuzzleGame";
        File gpxfile = new File(fileNametxt, "list.lt");
        String listado =
                "Mi dulce Adiós,img_level_1_thumb,300,300,img_level_1,550,550::"+
                "Le Grand Bouclé,img_level_2_thumb,300,231,img_level_2,850,650::"+
                "De caña o betabel!,img_level_3_thumb,250,400,img_level_3,650,1050::"+
                "Old Fashion Cops,img_level_4_thumb,300,200,img_level_4,1650,1050::"+
                "Alone in the beach,img_level_5_thumb,300,250,img_level_5,1750,1350";



        try {
            writer = new FileWriter(gpxfile);
            writer.append(listado);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void saveUnpack(Bitmap myBitmap, String nombre) {

        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/VuzzleGame");


        String filename = nombre;// + ".jpg";
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
    private void saveToGallery_jpg(Bitmap myBitmap, String nombre) {

        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/VuzzleGame/bgd");


        String filename = nombre;// + ".jpg";
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
