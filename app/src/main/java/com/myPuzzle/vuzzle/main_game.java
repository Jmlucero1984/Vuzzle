package com.myPuzzle.vuzzle;



import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.BitmapFactory.decodeResource;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class main_game extends AppCompatActivity implements  OnIntegerChangeListener{
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    float factor_pantalla=1F;
    String nombre_tablero;
    int ancho_scrollV_inicial=0;
    int segundos_jugados, segundos_juego_previo;
    static float scale_factor=1F;
    long starttime = 0;
    public float posXinicial, posYinicial;
    private ArrayList<ArrayList<Object>> lista_obj_coord_grupos = new ArrayList<ArrayList<Object>>();
    int tol_lado_pivote= (int) (80*MainActivity.factor);
    int tol_lado_op_pivote= (int) (130*MainActivity.factor);
    int desplazamiento= (int) (102*MainActivity.factor);
    float screen_density;
    private int num_grupo=0,consecutivo , ultimo_grupo_actualizado, num_largo, num_alto;
    private boolean started=false;
    double progreso_juego,piezas_enlazadas=0;
    double total_piezas;

    myImageView mibitmap;
    ArrayList<Integer> dim_fondo_madera = new ArrayList<Integer>();
    ConstraintLayout micargador;
    DisplayMetrics metrics;
    TextView miProgreso, miTiempo;
    Runnable run;
    Boolean tiempo_activo=false;
    FrameLayout miframe_sobre_cargador, miframeprincipal;
    HorizontalScrollView horizontalScrollView;
    ScrollView verticalScrollView;
    Button miboton;
    ArrayList <ArrayList<Integer>> grupos = new ArrayList<ArrayList<Integer>>(1);
    ArrayList<String> lista_id_tag = new ArrayList<String>();
    Handler h2 = new Handler();



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        verifyStoragePermissions(this);
        dim_fondo_madera.add(1200);
        dim_fondo_madera.add(1200);
        miProgreso = (TextView) findViewById(R.id.progreso);
        miTiempo = (TextView) findViewById(R.id.tempo);
        miTiempo.setText(String.format("%02d:%02d:%02d", 00, 00, 00));
        miProgreso.setText("Progreso: 0%");

        micargador = (ConstraintLayout) findViewById(R.id.miConst);
        miframe_sobre_cargador=(FrameLayout) findViewById(R.id.frame_sobre_cargador);



        starttime=System.currentTimeMillis();

       run = new Runnable() {
            @Override
            public void run() {
                if(tiempo_activo) {
                    long millis = System.currentTimeMillis() - starttime;
                    segundos_jugados = (int) (millis / 1000) + segundos_juego_previo;
                    int horas = segundos_jugados / 3600;
                    int minute = (segundos_jugados % 3600) / 60;
                    int secs = segundos_jugados % 60;
                    miTiempo.setText(String.format("%02d:%02d:%02d", horas, minute, secs));
                    h2.postDelayed(this, 500);
                }
            }
        };





        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.myHorizontalScrollView);
        verticalScrollView = (ScrollView) findViewById(R.id.myVerticalScrollView);
        File dirDest = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VuzzleGame/bgd/background");
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        screen_density = metrics.density;
  Bitmap bitmap = BitmapFactory.decodeFile(dirDest.toString());
        BitmapDrawable fondo_madera = new BitmapDrawable(bitmap);
        micargador.setBackground(fondo_madera);
        ViewGroup.LayoutParams params = micargador.getLayoutParams();
        params.height = dim_fondo_madera.get(1);
        params.width = dim_fondo_madera.get(0);
        micargador.setLayoutParams(params);


        miboton = (Button) findViewById((R.id.button));
        miboton.setTag("DRAGGABLE BUTTON");
        miframeprincipal=(FrameLayout) findViewById(R.id.frame_principal);
        miframeprincipal.setDrawingCacheEnabled(true);
        horizontalScrollView.setDrawingCacheEnabled(true);
    }

/*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {


            ViewGroup.LayoutParams params_HScroll = horizontalScrollView.getLayoutParams();
             params_HScroll.width = metrics.heightPixels-50;
            params_HScroll.height= metrics.widthPixels-100;
            horizontalScrollView.setLayoutParams(params_HScroll);

           // Toast.makeText(MainActivity.this, "Landscape Mode", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {



            ViewGroup.LayoutParams params_HScroll = horizontalScrollView.getLayoutParams();
            params_HScroll.height = metrics.heightPixels-150;
            params_HScroll.width = metrics.widthPixels-50;
            horizontalScrollView.setLayoutParams(params_HScroll);

            //Toast.makeText(MainActivity.this, "Portrait Mode", Toast.LENGTH_SHORT).show();
        }
    }*/


    @Override
    protected void onPause() {

        super.onPause();
        if (started) {

           h2.removeCallbacks(run);
           tiempo_activo=false;



            lista_obj_coord_grupos = new ArrayList<>();

            for (String i : lista_id_tag) {
                ArrayList<Object> item = new ArrayList<Object>();
                myImageView objeto = (myImageView) micargador.findViewWithTag(i);
                item.add(i);
                item.add(objeto.getX());
                item.add(objeto.getY());
                item.add(objeto.dameGrupo());
                item.add(objeto.getRotation());
                item.add(objeto.getEstadoBloqueado());
                lista_obj_coord_grupos.add(item);

            }
            ArrayList<Object> obj=new ArrayList<>();
            segundos_juego_previo=segundos_jugados;
            ObjectOutput out;
            try {

                File outFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/VuzzleGame/VuzzleBigData.data");
                out = new ObjectOutputStream(new FileOutputStream(outFile));
                obj.add(lista_obj_coord_grupos);
                obj.add(lista_id_tag);
                obj.add(grupos);
                obj.add(num_grupo);
                obj.add(micargador.getScaleX());
                obj.add(micargador.getScaleY());
                obj.add(scale_factor);
                obj.add(segundos_juego_previo);
                obj.add(piezas_enlazadas);

                out.writeObject(obj);
                out.close();

                Toast.makeText(getApplicationContext(),"Juego Guardado",Toast.LENGTH_SHORT).show();

            }  catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void zoomOut(View v) {
        if(micargador.getScaleX()>0.4) {
            horizontalScrollView.invalidate();
            verticalScrollView.invalidate();
            float scale=micargador.getScaleX();
            scale-=0.1F;
            micargador.setScaleX((float) scale);
            micargador.setScaleY((float) scale);
            scale_factor = (float) (1/scale);
            horizontalScrollView.requestLayout();
            verticalScrollView.requestLayout();
        }


    }
    public void zoomIn(View v) {
        if(micargador.getScaleX()<1.0) {
            horizontalScrollView.invalidate();
            verticalScrollView.invalidate();
            float scale=micargador.getScaleX();
            scale+=0.1F;
            micargador.setScaleX((float) scale);
            micargador.setScaleY((float) scale);
            horizontalScrollView.requestLayout();
            verticalScrollView.requestLayout();
            scale_factor = (float) (1/scale);
        }
        /*
        miframeprincipal.buildDrawingCache();

        Bitmap captura = Bitmap.createBitmap(miframeprincipal.getDrawingCache());
        Bitmap recorte = Bitmap.createBitmap(captura, 0, 0, 550, 550);
        saveCapture(recorte,"ultimo_logro.jpg");
        miframeprincipal.destroyDrawingCache();


        horizontalScrollView.buildDrawingCache();
        Bitmap captura2 = Bitmap.createBitmap(horizontalScrollView.getDrawingCache());
        Bitmap recorte2 = Bitmap.createBitmap(captura2, 0, 0, 550, 550);
        saveCapture(recorte2,"ultimo_logro2.jpg");
        horizontalScrollView.destroyDrawingCache();*/




    }


    @Override
    protected void onResume() {
        super.onResume();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/VuzzleGame/VuzzleBigData.data");
        if (file.exists()) {
            colocar();
            Context context = getApplicationContext();
            ArrayList<ArrayList<Object>> recup_lista_obj_coords_groups = new ArrayList<>();
            ArrayList <ArrayList<Integer>> gruposrecuperados = new ArrayList<>();
            ObjectInputStream in = null;
            ArrayList<Object> recharged=new ArrayList<>();

            try {
                File inFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/VuzzleGame/VuzzleBigData.data");
                in = new ObjectInputStream(new FileInputStream(inFile));
                recharged= (ArrayList<Object>) in.readObject();
                in.close();
            }  catch (Exception e) {
                e.printStackTrace();
            }


            Log.i("OUT", "Los Datos Recuperados son: "+recharged);
            recup_lista_obj_coords_groups =(ArrayList<ArrayList<Object>>) recharged.get(0);
            lista_id_tag=(ArrayList<String>) recharged.get(1);
            for(ArrayList arrayList:recup_lista_obj_coords_groups) {
                myImageView objeto = (myImageView) micargador.findViewWithTag((String) arrayList.get(0));
                objeto.setX((float) arrayList.get(1));
                objeto.setY((float) arrayList.get(2));
                objeto.setGrupo((int) arrayList.get(3));
                objeto.setRotation((float) arrayList.get(4));
                objeto.setEstadoBloqueado((boolean) arrayList.get(5));
                objeto.setXinicial(objeto.getX());
                objeto.setYinicial(objeto.getY());

            }
            grupos= (ArrayList <ArrayList<Integer>>) recharged.get(2);
            num_grupo= (int) recharged.get(3);
            micargador.setScaleX((float) recharged.get(4));
            micargador.setScaleY((float) recharged.get(5));
            scale_factor=(float) recharged.get(6);
            segundos_juego_previo=(int) recharged.get(7);
            piezas_enlazadas=(double) recharged.get(8);
            ProgresoJuego(0);


            starttime=System.currentTimeMillis();

        }

    }

    /** INFLATE A POP UP
     onButtonShowPopupWindowClick(v);
     **/

    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pop_up, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void boton_colocar(View view) {
        colocar();
        progreso_juego=0;
        piezas_enlazadas=0;
        scale_factor=1F;
        ProgresoJuego(0);

    }



    @SuppressLint("ClickableViewAccessibility")
    public void colocar() {

        File archivo = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VuzzleGame/Factory/Piezas.txt");

        if(archivo.exists()) {
            starttime=System.currentTimeMillis();
            segundos_jugados=0;
            segundos_juego_previo=0;
            tiempo_activo=true;
            h2.postDelayed(run, 500);
            consecutivo = 0;
            num_grupo = 0;
            grupos = new ArrayList<ArrayList<Integer>>(1);
            lista_id_tag = new ArrayList<String>(1);
            lista_obj_coord_grupos = new ArrayList<ArrayList<Object>>();
            micargador.removeAllViews();
            started = true;
            String cadena = leer_archivo("Piezas.txt");
            ArrayList<String> mispiezas = obt_nomb_pzs(cadena);
            int horizontal = 0, vertical = 0;
            float dimXColocable_cargador = micargador.getWidth() - 100;
            float dimYColocable_cargador = micargador.getHeight() - 100;

            if (mispiezas.size() > 120 && mispiezas.size() <= 200) {

                ViewGroup.LayoutParams params = micargador.getLayoutParams();
                params.height = (int) (1600);
                params.width =(int) (1600);
                micargador.setLayoutParams(params);
                dimXColocable_cargador = params.height - 100;
                dimYColocable_cargador = params.width - 100;

            } else if (mispiezas.size() > 200 && mispiezas.size() <= 300) {
                ViewGroup.LayoutParams params = micargador.getLayoutParams();
                params.height = (int) (2000);
                params.width =(int) (2000);
                micargador.setLayoutParams(params);
                dimXColocable_cargador = params.height - 100;
                dimYColocable_cargador = params.width - 100;
            }
            else if (mispiezas.size() > 300) {
                ViewGroup.LayoutParams params = micargador.getLayoutParams();
                params.height = (int) (2400);
                params.width =(int) (2400);
                micargador.setLayoutParams(params);
                dimXColocable_cargador = params.height - 100;
                dimYColocable_cargador = params.width - 100;
            }


            for (String i : mispiezas) {
                String dims[] = i.split("_");
                vertical = Integer.parseInt(dims[1]);
                horizontal = Integer.parseInt(dims[2]);


                String nom_piez_red = i + "_red_";

                Bitmap bitmap = null;
                File dirDest = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VuzzleGame/Factory/" + nom_piez_red);

                bitmap = BitmapFactory.decodeFile(dirDest.toString());
                mibitmap = new myImageView(getApplicationContext());
                String tag = "Pieza_" + vertical + "_" + horizontal;
                mibitmap.setTag(tag);
                mibitmap.seteaId(consecutivo);
                lista_id_tag.add(tag);
                consecutivo++;
                if (vertical == 0) {
                    mibitmap.setPiezaNorte("");
                } else {
                    mibitmap.setPiezaNorte("Pieza_" + (vertical - 1) + "_" + horizontal);
                }
                if (vertical == num_alto - 1) {
                    mibitmap.setPiezaSur("");
                } else {
                    mibitmap.setPiezaSur("Pieza_" + (vertical + 1) + "_" + horizontal);
                }
                if (horizontal == 0) {
                    mibitmap.setPiezaOeste("");
                } else {
                    mibitmap.setPiezaOeste("Pieza_" + vertical + "_" + (horizontal - 1));
                }
                if (horizontal == num_largo - 1) {
                    mibitmap.setPiezaEste("");
                } else {
                    mibitmap.setPiezaEste("Pieza_" + vertical + "_" + (horizontal + 1));
                }

                mibitmap.setImageBitmap(bitmap);


                mibitmap.setX(dimXColocable_cargador * ((float) Math.random()));
                mibitmap.setY(dimYColocable_cargador * ((float) Math.random()));
                mibitmap.setXinicial(mibitmap.getX());
                mibitmap.setYinicial(mibitmap.getY());
                mibitmap.setJuego_completo(false);
                mibitmap.setHorizontalScrollView(horizontalScrollView);
                mibitmap.setVerticalScrollView(verticalScrollView);


                obsInt.setOnIntegerChangeListener(new OnIntegerChangeListener() {
                    @Override
                    public void onIntegerChanged(int newValue) {
                        chequear(newValue);
                    }
                });
                mibitmap.setObsInt(obsInt);

                //mibitmap.setOnLongClickListener(this);


                micargador.addView(mibitmap);
                mibitmap.setRotation(dameAnguloAletorio());
                // micargador.setOnDragListener(this);
                ViewGroup.LayoutParams params_VScroll = verticalScrollView.getLayoutParams();
                ancho_scrollV_inicial=params_VScroll.width;


            }
        } else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogCustom);
            builder.setTitle("ATENCIÓN!");
            FrameLayout miframe = new FrameLayout(this);
            LinearLayoutCompat milay = new LinearLayoutCompat(this);
            milay.setOrientation(LinearLayoutCompat.VERTICAL);
            miframe.setPadding(50, 10, 50, 10);
            TextView mitexto= new TextView(this);

            mitexto.setTextSize(18);
            mitexto.setPadding(4,15,4,4);
            mitexto.setGravity(Gravity.LEFT);
            mitexto.setText("Primero debes seleccionar el tablero y generar las piezas...");
            milay.addView(mitexto);
            miframe.addView(milay);
            builder.setView(miframe);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {

                    dialogInterface.dismiss();
                }


            });

            AlertDialog ad= builder.create();
            ad.show();

        }

    }
    ObservableInteger obsInt = new ObservableInteger();

    public void chequear(int newvalue) {
        ConstraintLayout container =micargador;
        String tag = lista_id_tag.get(newvalue);
        myImageView vw = container.findViewWithTag(tag);
        float posx_this = vw.getX();
        float posy_this = vw.getY();
        float posx_vecino = 0F;
        float posy_vecino = 0F;
        vw.setX(posx_this);
        vw.setY(posy_this);
        myImageView pieza_actual = (myImageView) vw;
        Boolean enlazado = false;

        myImageView pieza_enlazada = null;
        String piezacercana = pieza_actual.damePiezaNorte();
        if (!(piezacercana.equals("")) && (!enlazado)&&(pieza_actual.getRotation()==0||((Math.abs(pieza_actual.getRotation()))%360==0))) {
            myImageView vecino = (myImageView) container.findViewWithTag(piezacercana);

            posx_vecino = vecino.getX();
            posy_vecino = vecino.getY();

            int diferencia_x = (int) Math.abs(posx_this - posx_vecino);
            int diferencia_y = (int) (posy_this - posy_vecino);

            if ((diferencia_x < tol_lado_pivote) && (diferencia_y > 0 && diferencia_y < tol_lado_op_pivote)&&(vecino.getRotation()==0||((Math.abs(vecino.getRotation()))%360==0)))  {
                vecino.setEstadoBloqueado(true);
                vw.setEstadoBloqueado(true);
                vw.setX(posx_vecino);
                vw.setY(posy_vecino + desplazamiento);


                enlazado = true;
                Log.i("OUT", "Pieza Actual: " + pieza_actual.dameId() + " enlace NORTE");
                pieza_enlazada = vecino;
            }
        }
        piezacercana = pieza_actual.damePiezaEste();

        if (!(piezacercana.equals("")) && (!enlazado)&&(pieza_actual.getRotation()==0||((Math.abs(pieza_actual.getRotation()))%360==0))){
            myImageView vecino = (myImageView) container.findViewWithTag(piezacercana);

            posx_vecino = vecino.getX();
            posy_vecino = vecino.getY();

            int diferencia_x = (int) (posx_vecino - posx_this);
            int diferencia_y = (int) Math.abs(posy_this - posy_vecino);

            if ((diferencia_y < tol_lado_pivote) && (diferencia_x > 0 && diferencia_x < tol_lado_op_pivote)&&(vecino.getRotation()==0||((Math.abs(vecino.getRotation()))%360==0)))  {
                vw.setEstadoBloqueado(true);
                vecino.setEstadoBloqueado(true);
                vw.setX(posx_vecino - desplazamiento);
                vw.setY(posy_vecino);
                enlazado = true;
                Log.i("OUT", "Pieza Actual: " + pieza_actual.dameId() + " enlace ESTE");
                pieza_enlazada = vecino;
            }
        }
        piezacercana = pieza_actual.damePiezaSur();
        if (!(piezacercana.equals("")) && (!enlazado)&&(pieza_actual.getRotation()==0||((Math.abs(pieza_actual.getRotation()))%360==0))){
            myImageView vecino = (myImageView) container.findViewWithTag(piezacercana);

            posx_vecino = vecino.getX();
            posy_vecino = vecino.getY();

            int diferencia_x = (int) Math.abs(posx_this - posx_vecino);
            int diferencia_y = (int) (posy_vecino - posy_this);

            if ((diferencia_x < tol_lado_pivote) && (diferencia_y > 0 && diferencia_y < tol_lado_op_pivote)&&(vecino.getRotation()==0||((Math.abs(vecino.getRotation()))%360==0)))  {
                vecino.setEstadoBloqueado(true);
                vw.setEstadoBloqueado(true);
                vw.setX(posx_vecino);
                vw.setY(posy_vecino - desplazamiento);


                enlazado = true;
                Log.i("OUT", "Pieza Actual: " + pieza_actual.dameId() + " enlace SUR");
                pieza_enlazada = vecino;
            }
        }
        piezacercana = pieza_actual.damePiezaOeste();
        if (!(piezacercana.equals("")) && (!enlazado)&&(pieza_actual.getRotation()==0||((Math.abs(pieza_actual.getRotation()))%360==0))) {
            myImageView vecino = (myImageView) container.findViewWithTag(piezacercana);

            posx_vecino = vecino.getX();
            posy_vecino = vecino.getY();

            int diferencia_y = (int) Math.abs(posy_this - posy_vecino);
            int diferencia_x = (int) (posx_this - posx_vecino);

            if ((diferencia_y < tol_lado_pivote) && (diferencia_x > 0 && diferencia_x < tol_lado_op_pivote)&&(vecino.getRotation()==0||((Math.abs(vecino.getRotation()))%360==0))) {
                vecino.setEstadoBloqueado(true);
                vw.setEstadoBloqueado(true);
                vw.setX(posx_vecino + desplazamiento);
                vw.setY(posy_vecino);


                enlazado = true;
                Log.i("OUT", "Pieza Actual: " + pieza_actual.dameId() + " enlace OESTE");
                pieza_enlazada = vecino;
            }
        }


        Log.i("OUT", "NUMERO DE PIEZA: " + pieza_actual.dameId());
        posXinicial=vw.getXinicial();
        posYinicial=vw.getYinicial();
        vw.setXinicial(vw.getX( ));
        vw.setYinicial(vw.getY());



        if ((pieza_actual.dameGrupo() != -1)) {
            Log.i("OUT", "Grupo de Pieza Actual: " + pieza_actual.dameGrupo());
            if (grupos.get(pieza_actual.dameGrupo()).size() > 1) {
                Log.i("OUT", "Tamaño de Grupo de Pieza Actual: " + grupos.get(pieza_actual.dameGrupo()).size());
                float trasladox= pieza_actual.getX()-posXinicial;
                float trasladoy= pieza_actual.getY()-posYinicial;
                moverGrupo(container, grupos.get(pieza_actual.dameGrupo()), pieza_actual, trasladox, trasladoy);
            }
        }

        if (enlazado) {
            if ((pieza_enlazada.dameGrupo() == -1) && (pieza_actual.dameGrupo() == -1)) {
                ArrayList<Integer> par = new ArrayList<Integer>(1);
                par.add(pieza_enlazada.dameId());
                par.add(pieza_actual.dameId());
                Log.i("CASO","CASO A");
                tilde(vw);
                ProgresoJuego(1);
                grupos.add(par);
                pieza_actual.setGrupo(num_grupo);
                pieza_enlazada.setGrupo(num_grupo);
                num_grupo++;

            } else if ((pieza_enlazada.dameGrupo() == -1) && (pieza_actual.dameGrupo() != -1)) {
                grupos.get(pieza_actual.dameGrupo()).add(pieza_enlazada.dameId());
                pieza_enlazada.setGrupo(pieza_actual.dameGrupo());
                ultimo_grupo_actualizado = pieza_actual.dameGrupo();
                Log.i("CASO","CASO B");
                tilde(vw);
                ProgresoJuego(1);
            } else if ((pieza_enlazada.dameGrupo() != -1) && (pieza_actual.dameGrupo() == -1)) {
                grupos.get(pieza_enlazada.dameGrupo()).add(pieza_actual.dameId());
                Log.i("CASO","CASO C");
                tilde(vw);
                ProgresoJuego(1);
                pieza_actual.setGrupo(pieza_enlazada.dameGrupo());
                ultimo_grupo_actualizado = pieza_enlazada.dameGrupo();
            } else if ((pieza_enlazada.dameGrupo() != -1) && (pieza_actual.dameGrupo() != -1) && (pieza_enlazada.dameGrupo() != pieza_actual.dameGrupo())) {
                int grupo_a_mudar = pieza_actual.dameGrupo();
                Log.i("CASO","CASO D");
                tilde(vw);
                ProgresoJuego(1);
                for (int i : grupos.get(grupo_a_mudar)) {
                    grupos.get(pieza_enlazada.dameGrupo()).add(i);
                    myImageView objeto = (myImageView) container.findViewWithTag(lista_id_tag.get(i));
                    objeto.setGrupo(pieza_enlazada.dameGrupo());
                }
                grupos.get(grupo_a_mudar).clear();
                ultimo_grupo_actualizado = pieza_enlazada.dameGrupo();
            }

            Log.i("OUT", "VEAMOS SI TIENE ASIGNADO GRUPO");
            Log.i("OUT", "Pieza Actual. Grupo: " + pieza_actual.dameGrupo());
            Log.i("OUT", "Pieza Enlazada. Grupo: " + pieza_enlazada.dameGrupo());
            if (grupos.get(ultimo_grupo_actualizado).size() == num_largo * num_alto) {
                Toast.makeText(getApplicationContext(), "LO LOGRASTE!!!!", Toast.LENGTH_LONG).show();
                myImageView pieza_0_0 = (myImageView) container.findViewWithTag(lista_id_tag.get(0));
                float pieza_0_0_pos_x_previa= pieza_0_0.getX();
                float pieza_0_0_pos_y_previa= pieza_0_0.getY();
                pieza_0_0.setX((float) ((container.getWidth()-(((num_largo-2)*100)+250)*0.8)*0.5));
                pieza_0_0.setY((float) ((container.getHeight()-(((num_alto-2)*100)+250)*0.8)*0.5));
                float trasladox= pieza_0_0.getX()-pieza_0_0_pos_x_previa;
                float trasladoy= pieza_0_0.getY()-pieza_0_0_pos_y_previa;
                tiempo_activo=false;
                moverGrupo(container, grupos.get(pieza_0_0.dameGrupo()), pieza_0_0, trasladox, trasladoy);
                freezar(container, grupos.get(pieza_0_0.dameGrupo()));

                //crearMarco(pieza_0_0,num_largo,num_alto,micargador);


                Bitmap pageBmp = Bitmap.createBitmap(micargador.getWidth(), micargador.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(pageBmp);
                micargador.draw(canvas);
                int inicio_recorte_x=(int) pieza_0_0.getX()-1;
                int inicio_recorte_y=(int) pieza_0_0.getY()-1;
                int largo_recorte=(num_largo*80)+40+num_largo+1;
                int alto_recorte=(num_alto*80)+40+num_alto+1;

                Bitmap recorte = Bitmap.createBitmap(pageBmp, inicio_recorte_x, inicio_recorte_y, largo_recorte, alto_recorte);
                String nombre_pieza_a_enmarcar=nombre_tablero+".jpg";
              // saveCapture(recorte,nombre_pieza_a_enmarcar+"__");
                new Framer(this,num_largo, num_alto,recorte,nombre_pieza_a_enmarcar).execute();
                //crearMarco(num_largo, num_alto,recorte,nombre_pieza_a_enmarcar);



            }

        }
    }/**
    public void crearMarco(int num_largo, int num_alto,Bitmap tablero, String nombre_pieza_a_enmarcar){
        int largo=num_largo*81+41+63+63;
        int alto = num_alto*81+41+63+63;
        Bitmap fondo_base = Bitmap.createBitmap(largo, alto,ARGB_8888);
        Bitmap esquina_sup_izq = Bitmap.createBitmap(decodeResource(this.getResources(), R.drawable.marco_sup_izq));
        esquina_sup_izq = Bitmap.createScaledBitmap(esquina_sup_izq, 164, 164, true);
        for (int i = 0; i < esquina_sup_izq.getHeight(); i++) {
            for (int j = 0; j < esquina_sup_izq.getWidth(); j++) {
                int color_src = esquina_sup_izq.getPixel(j, i);
                fondo_base.setPixel(j, i, color_src);
                fondo_base.setPixel(largo - j-1, i , color_src);
                fondo_base.setPixel(largo - j-1, alto - i-1, color_src);
                fondo_base.setPixel(j, alto - i-1, color_src);
            }

        }
        Bitmap astilla = Bitmap.createBitmap(decodeResource(this.getResources(), R.drawable.astilla_marco));
        astilla = Bitmap.createScaledBitmap(astilla, 1, 63, true);
        for (int i = 0; i < astilla.getHeight(); i++) {
            for (int j = 164; j < fondo_base.getWidth()-163; j++) {
                int color_src = astilla.getPixel(0, i);
                fondo_base.setPixel(j, i, color_src);
                fondo_base.setPixel(j, alto - i-1, color_src);
            }

        }
        for (int i = 0; i < astilla.getHeight(); i++) {
            for (int j = 164; j < fondo_base.getHeight()-163; j++) {
                int color_src = astilla.getPixel(0, i);
                fondo_base.setPixel(i, j, color_src);
                fondo_base.setPixel(largo-i-1, j, color_src);
            }

        }
        for (int i = 0; i < tablero.getHeight(); i++) {
            for (int j =0; j < tablero.getWidth(); j++) {
                int color_src = tablero.getPixel(j, i);
                fondo_base.setPixel(j+63, i+63, color_src);

            }

        }
        saveCapture(fondo_base, nombre_pieza_a_enmarcar);
    }**/
    public void freezar(ConstraintLayout container, ArrayList<Integer> grupo) {
        for(int i:grupo) {


                myImageView objeto=(myImageView) container.findViewWithTag(lista_id_tag.get(i));
                objeto.setJuego_completo(true);

        }
    }

    public void moverGrupo(ConstraintLayout container, ArrayList<Integer> grupo, myImageView pieza_movida,float trasladox, float trasladoy){

        for(int i:grupo) {
            if(i!=pieza_movida.dameId()){

                myImageView objeto=(myImageView) container.findViewWithTag(lista_id_tag.get(i));

                objeto.setTranslationX(objeto.getX()+trasladox);
                objeto.setTranslationY(objeto.getY()+trasladoy);
                objeto.setXinicial(objeto.getX());
                objeto.setYinicial(objeto.getY());


            }

        }

    }



    public String  leer_archivo(String nombre_Archivo) {
        String texto = "";
        File filetxt = Environment.getExternalStorageDirectory();
        String fileNametxt = filetxt.getAbsolutePath() + "/VuzzleGame/Factory";
        File gpxfile = new File(fileNametxt, nombre_Archivo);
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(gpxfile));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                //text.append('\n');
            }
            texto=text.toString();
            br.close();
        }
        catch (IOException e) {
        }

        return texto;

    }

    public ArrayList<String> obt_nomb_pzs(String texto_leido_archivo) {

        ArrayList<String> listaObjetos = new ArrayList<String>();
        String[] frags  = texto_leido_archivo.split(";");
        String[] num_piezas = frags[0].split("::");
        nombre_tablero=num_piezas[0];
        num_piezas = num_piezas[1].split(",");
        num_alto= Integer.parseInt( num_piezas[0]);
        num_largo= Integer.parseInt( num_piezas[1]);
        total_piezas=(num_alto*num_largo)-1;

        frags=frags[1].split("::");
        frags = frags[1].split(",");

        for(int i=0; i<frags.length; i++) {
            listaObjetos.add(frags[i].toString());

        }

        return listaObjetos;

    }

    public void ProgresoJuego(int adicion) {
        Log.i("SCALE","DATOS DE CONTADOR");
        piezas_enlazadas+=adicion;
        Log.i("SCALE","Total de piezas: "+total_piezas);
        Log.i("SCALE","piezas_enlazadas: "+piezas_enlazadas);
        progreso_juego= ((piezas_enlazadas/total_piezas)*100);
        Log.i("SCALE","Progreso: "+progreso_juego);



        miProgreso.setText("Progreso: "+String.format( "%.2f", progreso_juego)+"%");




    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    public void tilde (View view) {
        LayoutInflater inflater = (LayoutInflater)
        getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.tilde, null);
        ImageView mitilde = popupView.findViewById(R.id.tilde);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it


        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        AnimatedVectorDrawable avd = (AnimatedVectorDrawable)  mitilde.getDrawable();
        avd.registerAnimationCallback (new Animatable2.AnimationCallback(){
                                public void onAnimationEnd(Drawable drawable){
                                   popupWindow.dismiss();
                                }
                            });

        avd.start();

        }

    private void saveCapture(Bitmap myBitmap, String nombre) {

        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/VuzzleGame");
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

    @Override
    public void onIntegerChanged(int newValue) {
        Toast.makeText(getApplicationContext(),"WOWWW", Toast.LENGTH_SHORT).show();
    }

    public int dameAnguloAletorio() {
        int num = (int) ((Math.random() * 10));
        int angulo = 0;

        switch (num) {
            case 0:
            case 10:
                angulo = 0;
                break;
            case 1:
                angulo = 45;
                break;
            case 2:
                angulo = 90;
                break;
            case 3:
                angulo = 135;
                break;
            case 4:
                angulo = 180;
                break;
            case 5:
                angulo = 225;
                break;
            case 6:
                angulo = 270;
                break;
            case 7:
                angulo = 315;
                break;
            case 8:
                angulo = 45;
                break;
            case 9:
                angulo = 180;
                break;



        }
        return angulo;
    }
}
