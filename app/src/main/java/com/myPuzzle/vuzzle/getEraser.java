package com.myPuzzle.vuzzle;

import static android.graphics.BitmapFactory.decodeResource;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class getEraser extends AppCompatActivity {


    Bitmap eraserBitmap;

    public Bitmap getEraser(Context c, int eraser_name) {
        eraserBitmap = Bitmap.createBitmap(decodeResource(c.getResources(), eraser_name));
        eraserBitmap = Bitmap.createScaledBitmap(eraserBitmap, 150, 50, true);

        return eraserBitmap;
    }



}

        /*
        eraser1H = Bitmap.createBitmap(decodeResource(getResources(), R.drawable.eraser1_h));
        eraser1H = Bitmap.createScaledBitmap(eraser1H, 150, 50, true);
        misErasers.add(eraser1H);
        eraser2M = Bitmap.createBitmap(decodeResource(getResources(), R.drawable.eraser2_m));
        eraser2M = Bitmap.createScaledBitmap(eraser2M, 150, 50, true);
        misErasers.add(eraser2M);
        eraser2H = Bitmap.createBitmap(decodeResource(getResources(), R.drawable.eraser2_h));
        eraser2H = Bitmap.createScaledBitmap(eraser2H, 150, 50, true);
        misErasers.add(eraser2H);

        eraser3M = Bitmap.createBitmap(decodeResource(getResources(), R.drawable.eraser3_m));
        eraser3M = Bitmap.createScaledBitmap(eraser3M, 150, 50, true);
        misErasers.add(eraser3M);
        eraser3H = Bitmap.createBitmap(decodeResource(getResources(), R.drawable.eraser3_h));
        eraser3H = Bitmap.createScaledBitmap(eraser3H, 150, 50, true);
        misErasers.add(eraser3H);

        eraser4M = Bitmap.createBitmap(decodeResource(getResources(), R.drawable.eraser4_m));
        eraser4M = Bitmap.createScaledBitmap(eraser4M, 150, 50, true);
        misErasers.add(eraser4M);

        eraser4H = Bitmap.createBitmap(decodeResource(getResources(), R.drawable.eraser4_h));
        eraser4H = Bitmap.createScaledBitmap(eraser4H, 150, 50, true);
        misErasers.add(eraser4H);

        eraser5M = Bitmap.createBitmap(decodeResource(getResources(), R.drawable.eraser5_m));
        eraser5M = Bitmap.createScaledBitmap(eraser5M, 150, 50, true);
        misErasers.add(eraser5M);
        eraser5H = Bitmap.createBitmap(decodeResource(getResources(), R.drawable.eraser5_h));
        eraser5H = Bitmap.createScaledBitmap(eraser5H, 150, 50, true);
        misErasers.add(eraser5H);
        Log.i("CLASS", "BLAAAAAAA");
        Log.i("CLASS", misErasers.toString());/*
        return misErasers;


    }
}

         */
