package com.myPuzzle.vuzzle;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

public class ConformaPiezas {


    public static Bitmap Conforma(String pos, Bitmap pieza, Bitmap eraserN, Bitmap eraserE, Bitmap eraserS, Bitmap eraserO) {

        Bitmap pieza_conformada=null;
        switch (pos) {
            case "ESQ_SI":
                Log.i("COLOR","SE LLAMA A ESQ_SI");

                pieza_conformada= sustraerInf(pieza,eraserS);
                pieza_conformada= sustraerLatDer(pieza_conformada,eraserE);
                pieza_conformada= sustraerEsquinas(pieza_conformada,"234");


                break;

            case "ESQ_SD":
                Log.i("COLOR","SE LLAMA A ESQ_SD");
                pieza_conformada=sustraerInf(pieza,eraserS);
               pieza_conformada= sustraerLatIzq(pieza_conformada,eraserO);
                pieza_conformada= sustraerEsquinas(pieza_conformada,"134");

                break;

            case "ESQ_ID":
                Log.i("COLOR","SE LLAMA A ESQ_ID");

                pieza_conformada= sustraerLatIzq(pieza,eraserO);
                pieza_conformada=sustraerSup(pieza_conformada,eraserN);
                pieza_conformada= sustraerEsquinas(pieza_conformada,"124");
                break;

            case "CENTER":
                Log.i("COLOR","SE LLAMA A ESQ_ID");

                pieza_conformada= sustraerLatDer(pieza,eraserE);
                pieza_conformada=sustraerSup(pieza_conformada,eraserN);
                pieza_conformada= sustraerInf(pieza_conformada,eraserS);
                pieza_conformada= sustraerLatIzq(pieza_conformada,eraserO);
                pieza_conformada= sustraerEsquinas(pieza_conformada,"1234");


                break;

            case "ESQ_II":
                Log.i("COLOR","SE LLAMA A ESQ_II");

                pieza_conformada= sustraerLatDer(pieza,eraserE);
                pieza_conformada=sustraerSup(pieza_conformada,eraserN);
                pieza_conformada= sustraerEsquinas(pieza_conformada,"123");

            break;

            case "CENTER_SUP":

                pieza_conformada=sustraerInf(pieza,eraserS);
                pieza_conformada= sustraerLatDer(pieza_conformada,eraserE);
                pieza_conformada= sustraerLatIzq(pieza_conformada,eraserO);
                pieza_conformada= sustraerEsquinas(pieza_conformada,"1234");

                break;

            case "CENTER_INF":


                pieza_conformada= sustraerLatDer(pieza,eraserE);
                pieza_conformada= sustraerSup(pieza_conformada,eraserN);
                pieza_conformada= sustraerLatIzq(pieza_conformada,eraserO);
                pieza_conformada= sustraerEsquinas(pieza_conformada,"1234");

                break;

            case "CENTER_IZQ":


                pieza_conformada= sustraerLatDer(pieza,eraserE);
                pieza_conformada= sustraerSup(pieza_conformada,eraserN);
                pieza_conformada= sustraerInf(pieza_conformada,eraserS);
                pieza_conformada= sustraerEsquinas(pieza_conformada,"1234");

                break;

            case "CENTER_DER":


                pieza_conformada= sustraerLatIzq(pieza,eraserO);
                pieza_conformada=sustraerSup(pieza_conformada,eraserN);
                pieza_conformada= sustraerInf(pieza_conformada,eraserS);
                pieza_conformada= sustraerEsquinas(pieza_conformada,"1234");

                break;

        }
        if(pieza_conformada==null){
            Log.i("COLOR","NO HAY PIEZA CONFORMADA!");
        }
        return pieza_conformada;

    }



   private static Bitmap sustraerInf (Bitmap pieza, Bitmap eraser) {
       Log.i("COLOR", "SE LLAMA SUSTRAER_LATDER");
       Bitmap img;

       int   color11, color12;

       Matrix matrix = new Matrix();
       matrix.postRotate(180);
       Bitmap pieza2 = Bitmap.createBitmap(pieza, 0, 0, pieza.getWidth(), pieza.getHeight(), matrix, true);
       pieza2.setHasAlpha(true);

       for (int i = 0; i < eraser.getHeight(); i++) {
           for (int j = 25; j < eraser.getWidth()-25; j++) {

               color11= eraser.getPixel(j,i);
               int R = (color11 >>> 32) & 0xff; // or color >>> 24




               color12= pieza2.getPixel(j,i);
               int pA = (color12 >>> 24) & 0xff; // or color >>> 24
               int pR = (color12 >> 16) & 0xff;
               int pG = (color12 >>  8) & 0xff;
               int pB = (color12      ) & 0xff;


               if(R<255) {
                   pieza2.setPixel(j,i,Color.argb(R, pR, pG, pB));
               }

           }

       }
       img = pieza2.copy(pieza2.getConfig(), true);
       matrix = new Matrix();
       matrix.postRotate(180);
       img = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
       return img;
   }

    private static Bitmap sustraerLatDer (Bitmap pieza, Bitmap eraser){
        Log.i("COLOR","SE LLAMA SUSTRAER_LATDER");
        Bitmap img;

        int   color11, color12;

        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        Bitmap pieza2= Bitmap.createBitmap(pieza, 0, 0, pieza.getWidth(), pieza.getHeight(), matrix, true);
        pieza2.setHasAlpha(true);

        for (int i=0; i<eraser.getHeight(); i++) {
            for (int j = 25; j < eraser.getWidth()-25; j++) {

                color11= eraser.getPixel(j,i);
                int R = (color11 >>> 32) & 0xff; // or color >>> 24




                color12= pieza2.getPixel(j,i);
                int pA = (color12 >>> 24) & 0xff; // or color >>> 24
                int pR = (color12 >> 16) & 0xff;
                int pG = (color12 >>  8) & 0xff;
                int pB = (color12      ) & 0xff;


                if(R<255) {
                    pieza2.setPixel(j,i,Color.argb(R, pR, pG, pB));
                }


            }

        }
        img = pieza2.copy(pieza2.getConfig(), true);
        matrix = new Matrix(); matrix.postRotate(90);
        img= Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return img;
    }
    private static Bitmap sustraerLatIzq (Bitmap pieza, Bitmap eraser){
        Log.i("COLOR","SE LLAMA SUSTRAER_LATDER");
        Bitmap img;
        int color11, color12;

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap pieza2= Bitmap.createBitmap(pieza, 0, 0, pieza.getWidth(), pieza.getHeight(), matrix, true);
        pieza2.setHasAlpha(true);

        for (int i=0; i<eraser.getHeight(); i++) {
            for (int j = 25; j < eraser.getWidth()-25; j++) {

                color11= eraser.getPixel(j,i);
                int R = (color11 >>> 32) & 0xff; // or color >>> 24

                color12= pieza2.getPixel(j,i);
                int pA = (color12 >>> 24) & 0xff; // or color >>> 24
                int pR = (color12 >> 16) & 0xff;
                int pG = (color12 >>  8) & 0xff;
                int pB = (color12      ) & 0xff;

                if(R<255) {
                    pieza2.setPixel(j,i,Color.argb(R, pR, pG, pB));
                }
            }

        }
        img = pieza2.copy(pieza2.getConfig(), true);
        matrix = new Matrix(); matrix.postRotate(-90);
        img= Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return img;
    }

    private static Bitmap sustraerSup (Bitmap pieza, Bitmap eraser){

        Bitmap img;
        int color11, color12;

        Matrix matrix = new Matrix();
        matrix.postRotate(0);
        Bitmap pieza2= Bitmap.createBitmap(pieza, 0, 0, pieza.getWidth(), pieza.getHeight(), matrix, true);




        for (int i=0; i<eraser.getHeight(); i++) {
            for (int j = 25; j < eraser.getWidth()-25; j++) {

                color11= eraser.getPixel(j,i);
                int R = (color11 >>> 32) & 0xff; // or color >>> 24

                color12= pieza2.getPixel(j,i);
                int pA = (color12 >>> 24) & 0xff; // or color >>> 24
                int pR = (color12 >> 16) & 0xff;
                int pG = (color12 >>  8) & 0xff;
                int pB = (color12      ) & 0xff;

                if(R<255) {
                    pieza2.setPixel(j,i,Color.argb(R, pR, pG, pB));
                }
              //  Log.i("COLOR","Y: "+i+"  X: "+j+"  Colores: A="+A+"/R="+R+"/G="+G+"/B="+B );


            }

        }

        img = pieza2.copy(pieza2.getConfig(), true);
        matrix = new Matrix(); matrix.postRotate(0);
        img= Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return img;
    }

    private static Bitmap sustraerEsquinas (Bitmap pieza,String esquinas){


        Bitmap img;

        int A = 0,  color12;

        Matrix matrix = new Matrix();

        Bitmap pieza2= Bitmap.createBitmap(pieza, 0, 0, pieza.getWidth(), pieza.getHeight(), matrix, true);
        pieza2.setHasAlpha(true);

        if(esquinas.contains("1")) {


        for (int i=0; i<25; i++) {
            for (int j = 0; j< 25; j++) {

                color12= pieza2.getPixel(j,i);
                int pA = (color12 >>> 24) & 0xff; // or color >>> 24
                int pR = (color12 >> 16) & 0xff;
                int pG = (color12 >>  8) & 0xff;
                int pB = (color12      ) & 0xff;


                    pieza2.setPixel(j,i,Color.argb(0, pR, pG, pB));

                }

            }
        }
        if(esquinas.contains("2")) {
        for (int i=0; i<25; i++) {
            for (int j = 125; j < 150; j++) {

                color12 = pieza2.getPixel(j, i);
                int pA = (color12 >>> 24) & 0xff; // or color >>> 24
                int pR = (color12 >> 16) & 0xff;
                int pG = (color12 >> 8) & 0xff;
                int pB = (color12) & 0xff;


                pieza2.setPixel(j, i, Color.argb(0, pR, pG, pB));

            }
         }

        }
        if(esquinas.contains("4")) {
        for (int i=125; i<150; i++) {
            for (int j = 0; j < 25; j++) {

                color12 = pieza2.getPixel(j, i);
                int pA = (color12 >>> 24) & 0xff; // or color >>> 24
                int pR = (color12 >> 16) & 0xff;
                int pG = (color12 >> 8) & 0xff;
                int pB = (color12) & 0xff;


                pieza2.setPixel(j, i, Color.argb(0, pR, pG, pB));

            }
        }

        }
        if(esquinas.contains("3")){
            for (int i = 125; i < 150; i++) {
                for (int j = 125; j < 150; j++) {

                    color12 = pieza2.getPixel(j, i);
                    int pA = (color12 >>> 24) & 0xff; // or color >>> 24
                    int pR = (color12 >> 16) & 0xff;
                    int pG = (color12 >> 8) & 0xff;
                    int pB = (color12) & 0xff;


                    pieza2.setPixel(j, i, Color.argb(0, pR, pG, pB));

                }

            }
        }

        img = pieza2.copy(pieza2.getConfig(), true);



        return img;
    }
}
