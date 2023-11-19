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
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Trocer extends AsyncTask<Void, Integer, Integer> {

    private String eleccion, nombre_tablero;
    ProgressDialog pd;
    int count;
    Context c;
    int progreso = 0;
    Bitmap bitmap;
    Bitmap croppedBitmap;
    int num_horizontal;
    int num_vertical;
    int loader_adicional=0;
    boolean cancelada=false;



    ObservableInteger primeraFase;


    public Trocer(Context c, String eleccion, ProgressDialog pd, ObservableInteger primeraFase, String nombre_tablero) {
        this.nombre_tablero=nombre_tablero;
        this.pd=pd;
        this.primeraFase = primeraFase;
        this.eleccion = eleccion;
        this.c = c;


    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        //pd = new ProgressDialog(c);



        File file = Environment.getExternalStorageDirectory();
        String fileName = file.getAbsolutePath() + "/VuzzleGame/"+eleccion;
        bitmap = BitmapFactory.decodeFile(fileName);

        FileWriter writer = null;
        File filetxt = Environment.getExternalStorageDirectory();
        String fileNametxt = filetxt.getAbsolutePath() + "/VuzzleGame/Factory";
        File gpxfile = new File(fileNametxt, "Piezas.txt");

        num_horizontal = ((int) (bitmap.getWidth() - 250) / 100) + 2;
         num_vertical = ((int) (bitmap.getHeight() - 250) / 100) + 2;
         count =  num_horizontal*num_vertical ;

        try {
            writer = new FileWriter(gpxfile);
            writer.append(nombre_tablero+"::" + num_vertical + "," + num_horizontal + ";");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pd.setTitle("PASO 1/3");
        pd.setMessage("Dividiendo tablero...");
        pd.setIndeterminate(true);
        pd.setCancelable(true);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                try {
                    cancelada=true;
                    primeraFase.set(4);
                    this.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                Toast.makeText(c,"PROCESO INTERRUMPIDO!",Toast.LENGTH_LONG).show();
            }
        });
        pd.show();

    }

    @Override
    protected Integer doInBackground(Void... params) {


        String nombre;




        for (int i = 0; i < num_vertical; i++) {
            for (int j = 0; j < num_horizontal; j++) {
                croppedBitmap = Bitmap.createBitmap(bitmap, j * 100, i * 100, 150, 150);
                int a = i + 1;
                int b = j + 1;
                nombre = "Puzzle_item_" + a + "_" + b + "";
                saveToGallery(croppedBitmap, nombre);
                progreso++;
                publishProgress((int) ((progreso / (float) count) * 100));

                }

        if (isCancelled()){
            cancelada=true;
            primeraFase.set(4);
            break;
        }
        }


        return count;
    }

    protected void onProgressUpdate(Integer... progress) {
        pd.setMessage("Dividiendo... " + progress[0] + "%");

    }

    protected void onPostExecute(Integer count) {
        super.onPostExecute(count);



        pd.dismiss();
        if(!cancelada) {
            Toast.makeText(c, "PASO 1/3 COMPLETO" , Toast.LENGTH_SHORT).show();
            primeraFase.set(1);
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
        String filename = nombre;
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

}
