package com.myPuzzle.vuzzle;


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


    public class Factory extends AsyncTask <Void, Integer, Integer> {
        ArrayList<Bitmap> miserasers = new ArrayList<Bitmap>();

        ProgressDialog pd2;
        int count;
        Context c;
        int progreso = 0;
        int[] dim;
        ObservableInteger segundaFase;
        String cadena_nombres = "";
        boolean cancelada =false;




        public Factory (Context c,ProgressDialog pd, ObservableInteger segundaFase) {
            this.segundaFase=segundaFase;
            this.pd2=pd;



            this.c = c;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pd2.setTitle("PASO 2/3");
            pd2.setMessage("Conformando piezas...");
            pd2.setIndeterminate(true);
            pd2.setCancelable(true);
            pd2.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    try {
                        cancelada=true;
                        segundaFase.set(4);
                        this.finalize();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    Toast.makeText(c,"PROCESO INTERRUMPIDO!",Toast.LENGTH_LONG).show();
                }
            });
            pd2.show();
           crear_matrices();
            dim = obtener_num_piezas();
               // Log.e("COLOR", "dim[0]=" + dim[0] + "/dim[1]=" + dim[1]);
            count = dim[0] * dim[1];






        }

        @Override
        protected Integer doInBackground(Void... params) {


            for (int i = 0; i < dim[0]; i++) {
                for (int j = 0; j < dim[1]; j++) {

                    File file = Environment.getExternalStorageDirectory();
                    int fila = i + 1;
                    int columna = j + 1;
                    String nombre_pieza_conf = "Puzzle_" + i + "_" + j;
                    cadena_nombres += nombre_pieza_conf;
                    if (i == dim[0] - 1 && j == dim[1] - 1) {
                        cadena_nombres += ";";
                    } else {
                        cadena_nombres += ",";
                    }
                    String fileName = file.getAbsolutePath() + "/VuzzleGame/Factory/Puzzle_item_" + fila + "_" + columna;
                    Bitmap bitmap = BitmapFactory.decodeFile(fileName);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                    if (i == 0 && j == 0) {
                        Log.i("COLOR", "ESQ_SI archivos: " + fileName);

                        Bitmap img1 = ConformaPiezas.Conforma("ESQ_SI", bitmap, null, miarraylistColumnas.get(0).get(dim[0] - 1).get(0), miarraylistFilas.get(0).get(0).get(0), null);
                        saveToGallery(img1, nombre_pieza_conf);
                        img1.recycle();
                        bitmap.recycle();


                    } else if (i == 0 && j == dim[1] - 1) {
                        Log.i("COLOR", "ESQ_SD archivos: " + fileName);

                        Bitmap img1 = ConformaPiezas.Conforma("ESQ_SD", bitmap, null, null, miarraylistFilas.get(0).get(j).get(0), miarraylistColumnas.get(j - 1).get(dim[0] - 1).get(1));

                        saveToGallery(img1, nombre_pieza_conf);
                        img1.recycle();
                        bitmap.recycle();
                    } else if (i == dim[0] - 1 && j == 0) {
                        Log.i("COLOR", "ESQ_II archivos: " + fileName);
                        Bitmap img1 = ConformaPiezas.Conforma("ESQ_II", bitmap, miarraylistFilas.get(i - 1).get(0).get(1), miarraylistColumnas.get(0).get(0).get(0), null, null);
                        saveToGallery(img1, nombre_pieza_conf);
                        img1.recycle();
                        bitmap.recycle();
                    } else if (i == dim[0] - 1 && j == dim[1] - 1) {
                        Log.i("COLOR", "ESQ_ID archivos: " + fileName);
                        Bitmap img1 = ConformaPiezas.Conforma("ESQ_ID", bitmap, miarraylistFilas.get(i - 1).get(j).get(1), null, null, miarraylistColumnas.get(j - 1).get(0).get(1));
                        saveToGallery(img1, nombre_pieza_conf);
                        img1.recycle();
                        bitmap.recycle();
                    } else if (i == 0 && !(j == dim[1] - 1 || j == 0)) {
                        Log.i("COLOR", "CENTER_SUP archivos: " + fileName);
                        Bitmap img1 = ConformaPiezas.Conforma("CENTER_SUP", bitmap, null, miarraylistColumnas.get(j).get(dim[0] - 1).get(0), miarraylistFilas.get(0).get(j).get(0), miarraylistColumnas.get(j - 1).get(dim[0] - 1).get(1));
                        saveToGallery(img1, nombre_pieza_conf);
                        img1.recycle();
                        bitmap.recycle();
                    } else if (i == dim[0] - 1 && !(j == dim[1] - 1 || j == 0)) {
                        Log.i("COLOR", "CENTER_INF archivos: " + fileName);

                        Bitmap img1 = ConformaPiezas.Conforma("CENTER_INF", bitmap, miarraylistFilas.get(i - 1).get(j).get(1), miarraylistColumnas.get(j).get(0).get(0), null, miarraylistColumnas.get(j - 1).get(0).get(1));
                        saveToGallery(img1, nombre_pieza_conf);
                        img1.recycle();
                        bitmap.recycle();


                    } else if (j == 0 && (0 < i && i < dim[0] - 1)) {
                        Log.i("COLOR", "CENTER IZQ archivos: " + fileName);
                        Bitmap img1 = ConformaPiezas.Conforma("CENTER_IZQ", bitmap, miarraylistFilas.get(i - 1).get(0).get(1), miarraylistColumnas.get(0).get(i - 1).get(0), miarraylistFilas.get(i).get(0).get(0), null);
                        saveToGallery(img1, nombre_pieza_conf);
                        img1.recycle();
                        bitmap.recycle();
                    } else if (j == dim[1] - 1 && !(i == dim[0] - 1 || i == 0)) {
                        Log.i("COLOR", "CENTER_DER archivos: " + fileName);
                        Bitmap img1 = ConformaPiezas.Conforma("CENTER_DER", bitmap, miarraylistFilas.get(i - 1).get(dim[1] - 1).get(1), null, miarraylistFilas.get(i).get(dim[1] - 1).get(0), miarraylistColumnas.get(j - 1).get(i - 1).get(1));
                        saveToGallery(img1, nombre_pieza_conf);
                        img1.recycle();
                        bitmap.recycle();
                    } else if (!(j == dim[1] - 1 || j == 0) && !(i == dim[0] - 1 || i == 0)) {
                        Log.i("COLOR", "CENTER archivos: " + fileName);
                        Bitmap img1 = ConformaPiezas.Conforma("CENTER", bitmap, miarraylistFilas.get(i - 1).get(j).get(1), miarraylistColumnas.get(j).get(i - 1).get(0), miarraylistFilas.get(i).get(j).get(0), miarraylistColumnas.get(j - 1).get(i - 1).get(1));
                        saveToGallery(img1, nombre_pieza_conf);
                        img1.recycle();
                        bitmap.recycle();
                    }


                    String nombre_primitiva ="Puzzle_item_"+(i+1)+"_"+(j+1);


                    File fileToDelete  = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VuzzleGame/Factory/" + nombre_primitiva );
                    fileToDelete  .delete();
                    progreso++;
                    publishProgress((int) ((progreso / (float) count) * 100));

                }

                if (isCancelled()){
                    segundaFase.set(4);
                    cancelada=true;
                    break;
                }
            }


            return count;
        }

        protected void onProgressUpdate(Integer... progress) {
            pd2.setMessage("Conformando piezas... " + progress[0] + "%");

        }

        protected void onPostExecute(Integer count) {
            super.onPostExecute(count);
            registrar_nombres_piezas(cadena_nombres);

            //pd.setMessage("Piezas terminadas: "+count);


            pd2.dismiss();
            if(!cancelada) {
                Toast.makeText(c, "PASO 2/3 COMPLETO", Toast.LENGTH_SHORT).show();
                segundaFase.set(2);
            }


        }
        ArrayList<ArrayList<ArrayList<Bitmap>>> miarraylistFilas;
        ArrayList<ArrayList<ArrayList<Bitmap>>> miarraylistColumnas;
        int num_horizontal;
        int num_vertical;













        public void eliminarPrimitivas(View view) {

            for (int i = 0; i < num_vertical + 1; i++) {
                for (int j = 0; j < num_horizontal + 1; j++) {

                    String fileToEraseName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/VuzzleGame/Factory/Puzzle_item_" + i + "_" + j;
                    Log.i("SV", "PIEZA ELIMINADA:" + fileToEraseName);
                    File toerase = new File(fileToEraseName);
                    toerase.delete();

                }
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

        public ArrayList eraserAletorio() {
            int num = (int) ((Math.random() * 10));
            //Log.i("COLOR","ALEATORIO="+num);
            ArrayList<Bitmap> eraser = new ArrayList<>();
            switch (num) {
                case 0:
                case 10:
                    eraser.add(new getEraser().getEraser(c, R.drawable.eraser1_m));
                    eraser.add(new getEraser().getEraser(c, R.drawable.eraser1_h));

                    break;
                case 1:
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser1_h));
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser1_m));
                    break;
                case 2:
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser2_m));
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser2_h));

                    break;
                case 3:
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser2_h));
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser2_m));

                    break;
                case 4:
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser3_m));
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser3_h));;
                    break;
                case 5:
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser4_m));
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser4_h));;


                    break;
                case 6:
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser4_h));
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser4_m));
                    break;
                case 7:
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser3_h));
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser3_m));;

                    break;
                case 8:
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser5_h));
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser5_m));
                    break;
                case 9:
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser5_m));
                    eraser.add(new getEraser().getEraser(c,R.drawable.eraser5_h));
                    break;
            }
            return eraser;


        }



        public void crear_matrices() {
            int[] num_piezas = obtener_num_piezas();
            miarraylistFilas = new ArrayList<>(num_piezas[0] - 1);
            for (int i = 0; i < num_piezas[0] - 1; i++) {
                ArrayList<ArrayList<Bitmap>> mifila = new ArrayList<>();
                for (int j = 0; j < num_piezas[1]; j++) {
                    mifila.add(eraserAletorio());

                }
                miarraylistFilas.add(mifila);

            }


            miarraylistColumnas = new ArrayList<>(num_piezas[1] - 1);
            for (int i = 0; i < num_piezas[1] - 1; i++) {
                ArrayList<ArrayList<Bitmap>> mifila = new ArrayList<>();
                for (int j = 0; j < num_piezas[0]; j++) {
                    mifila.add(eraserAletorio());

                }
                miarraylistColumnas.add(mifila);

            }

        }

        public int[] obtener_num_piezas() {
            String texto = "";
            File filetxt = Environment.getExternalStorageDirectory();
            String fileNametxt = filetxt.getAbsolutePath() + "/VuzzleGame/Factory";
            File gpxfile = new File(fileNametxt, "Piezas.txt");
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(gpxfile));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                texto = text.toString();
                br.close();
            } catch (IOException e) {
                //You'll need to add proper error handling here
            }

            Log.i("COLOR", "Texto: " + texto);
            String[] num = texto.split(";");
            num = num[0].split("::");
            num = num[1].split(",");
            Log.i("COLOR", "Texto: " + num[0] + "//" + num[1]);
            String numero1 = num[0].trim();
            String numero2 = num[1].trim();
            int num1 = Integer.parseInt(numero1);
            int num2 = Integer.parseInt(numero2);
            int[] piezas = {num1, num2};
            return piezas;

        }





        public void registrar_nombres_piezas(String cadena) {
            String texto = "";
            File filetxt = Environment.getExternalStorageDirectory();
            String fileNametxt = filetxt.getAbsolutePath() + "/VuzzleGame/Factory";
            File gpxfile = new File(fileNametxt, "Piezas.txt");
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(gpxfile));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                texto = text.toString();
                br.close();
            } catch (IOException e) {

            }


            try {
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(texto + "PIEZAS::" + cadena);
                writer.append('\n');
                writer.append("END");
                writer.flush();
                writer.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }




    }






