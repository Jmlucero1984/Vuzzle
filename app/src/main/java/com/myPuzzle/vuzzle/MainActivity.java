package com.myPuzzle.vuzzle;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.res.Resources.getSystem;
import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.Bitmap.Config.RGB_565;
import static android.graphics.BitmapFactory.decodeResource;
import static android.os.Build.VERSION.SDK_INT;

import static java.lang.System.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.myPuzzle.vuzzle.Factory;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnIntegerChangeListener {

    ListView milista;
    static public float factor = 0.8F;
    int miview = 0;
   // ArrayList<String[]> misBoards = new ArrayList<>();
    private int num_grupo=0,consecutivo , ultimo_grupo_actualizado, num_largo, num_alto;
    private Handler handler = new Handler();
    private String eleccion;
    ProgressDialog pd;
    ImageView image;
    String nombre_tablero;
    ArrayList<String[]> misTablerosInstalados = new ArrayList<String[]>();
    public static int ultimo;
    ObservableInteger Fase;
    AnimatedVectorDrawable avd_prensa;
    AnimatedVectorDrawable avd_install;
    AnimatedVectorDrawable avd_instrucciones;
    ImageView prensa;
    Boolean anim_prensa=false;
    Boolean anim_install=false;
    Boolean anim_instrucciones=false;
    TextView informacion_1;
    TextView informacion_2;
    TextView informacion_3;
    TextView informacion_4;
    float screen_density;
    File dir_vuzzle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        miview = R.layout.list_view_item;
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
/*
        misBoards.add(new String[]{"img_level_1_thumb", "Mi dulce Adiós"});
        misBoards.add(new String[]{"img_level_2_thumb","Le Grand Bouclé"});
        misBoards.add(new String[]{"img_level_3_thumb","De caña o betabel!"});
        misBoards.add(new String[]{"img_level_4_thumb","Old Fashion Cops"});
        misBoards.add(new String[]{"img_level_5_thumb","Alone in the beach"});*/

        informacion_1 = (TextView)findViewById(R.id.info_1) ;
        informacion_2 = (TextView)findViewById(R.id.info_2) ;
        informacion_3 = (TextView)findViewById(R.id.info_3) ;
        informacion_4 = (TextView)findViewById(R.id.info_4) ;
        pd=new ProgressDialog(this,R.style.AlertDialogCustom);

        FileOutputStream outputStream = null;

        Fase =new ObservableInteger();
        Fase.setOnIntegerChangeListener(new OnIntegerChangeListener() {
            @Override
            public void onIntegerChanged(int newValue) {
                if (newValue == 1) {
                    Procesar_Fase_2();

                }
                if (newValue == 2) {
                    Procesar_Fase_3();

                }
                if (newValue == 3) {

                    avd_prensa.stop();
                    anim_prensa = false;
                    avd_prensa.setAlpha(0);
                    prensa.setImageDrawable(avd_instrucciones);
                    anim_instrucciones= true;
                    avd_instrucciones.start();
                    avd_instrucciones.setAlpha(255);

                }
                if (newValue == 4) {
                    /** llamada cuando se cancela alguno de los pasos de fabricación**/

                    avd_prensa.stop();
                    anim_prensa = false;
                    avd_prensa.setAlpha(0);
                    prensa.setImageDrawable(avd_instrucciones);
                    anim_instrucciones= true;
                    avd_instrucciones.start();
                    avd_instrucciones.setAlpha(255);
                }
                if (newValue == 5) {

                    avd_install.stop();
                    anim_install = false;
                    avd_install.setAlpha(0);
                    prensa.setImageDrawable(avd_instrucciones);
                    anim_instrucciones=true;
                    avd_instrucciones.start();
                    avd_instrucciones.setAlpha(255);

                }
            }
        });
        prensa= (ImageView) findViewById(R.id.cargador_prensa);
        avd_prensa = (AnimatedVectorDrawable)  prensa.getDrawable();
        avd_prensa.setAlpha(0);
        avd_prensa.registerAnimationCallback (new Animatable2.AnimationCallback(){
            public void onAnimationEnd(Drawable drawable){
               if(!anim_prensa) {
                   avd_prensa.stop();
                   avd_prensa.setAlpha(0);
               } else {
                   avd_prensa.start();
               }
            }
        });
       avd_install = (AnimatedVectorDrawable) getResources().getDrawable( R.drawable.install);
        avd_install.setAlpha(0);
        avd_install.registerAnimationCallback (new Animatable2.AnimationCallback(){
            public void onAnimationEnd(Drawable drawable){
                if(!anim_install) {
                    avd_install.stop();
                    avd_install.setAlpha(0);
                } else {
                    avd_install.start();
                }
            }
        });


        avd_instrucciones = (AnimatedVectorDrawable) getResources().getDrawable( R.drawable.instrucciones);
        avd_instrucciones.setAlpha(0);
        avd_instrucciones.registerAnimationCallback (new Animatable2.AnimationCallback(){
            public void onAnimationEnd(Drawable drawable){
                if(!anim_instrucciones) {
                    avd_instrucciones.stop();
                    avd_instrucciones.setAlpha(0);
                } else {
                    avd_instrucciones.start();
                }
            }
        });


        WindowManager wm = (WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int screen_width = metrics.widthPixels;
        int screen_height = metrics.heightPixels;



        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        String toastMsg;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                toastMsg = "LARGE";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                toastMsg = "NORMAL";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                toastMsg = "SMALL";
                break;
            default:
                toastMsg = "Out of range";
        }

        String manufacturer = Build.MANUFACTURER;
        screen_density=metrics.density;



        String model = Build.MODEL;
        informacion_1.setText(toastMsg+" : "+screen_width+" x "+screen_height);
        informacion_2.setText(manufacturer+" "+model);
        informacion_3.setText("DNSDPI: "+metrics.densityDpi+" | DENS: "+metrics.density);
        informacion_4.setText("SDK: " + android.os.Build.VERSION.SDK_INT);


        File file = Environment.getExternalStorageDirectory();
        dir_vuzzle = new File(file.getAbsolutePath() + "/VuzzleGame");
        if (!dir_vuzzle.exists()) {
            chequearPermisos();

        } else {
            prensa.setImageDrawable(avd_instrucciones);
            anim_instrucciones=true;
            avd_instrucciones.start();
            avd_instrucciones.setAlpha(255);
        }




    }
    public void instalar () {
        dir_vuzzle.mkdirs();
        prensa.setImageDrawable(avd_install);
        avd_install.setAlpha(255);
        avd_install.start();
        anim_install = true;
        new unPackStuff(this, Fase).execute();

    };
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("PERMISOS"," LOS PERMISOS OTORGADOS SON:");
        Log.i("PERMISOS", grantResults+"");
        Log.i("PERMISOS", permissions+"");
        Log.i("PERMISOS", requestCode+"");
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            Log.i("PERMISOS", "-----------------");
        } else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.i("PERMISOS", "SIII  VAMOS MANAOS");
            if (SDK_INT >= Build.VERSION_CODES.R) {
                dialogoPermisos();
            } else {
                instalar();
            }

        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        instalar();

    }
    public void jugar(View vista) {


        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VuzzleGame/Factory/ready_flag");
        if (file.exists()) {
            Intent jugar = new Intent(this, main_game.class);


            startActivity(jugar);

        } else {
            Toast.makeText(getApplicationContext(), "No hay tablero cargado", Toast.LENGTH_LONG).show();
        }


    }



    public  ArrayList<String []> obtenerTablerosInstalados (String lista) {
        String texto = "";
        File filetxt = Environment.getExternalStorageDirectory();
        String fileNametxt = filetxt.getAbsolutePath() + "/VuzzleGame";
        File gpxfile = new File(fileNametxt, "list.lt");
        StringBuilder text = new StringBuilder();
        String [] datos_item = new String[7];
        ArrayList<String[]> tableros_instalados = new ArrayList<String []>();

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
        Log.i("SALIDA","TEXTO: "+texto);
        String items[] = texto.split("::");

        String datos_de_item[] = new String[7];
        for(int i=0; i<items.length; i++) {
            datos_de_item=items[i].split(",");

            tableros_instalados.add(datos_de_item);

        }

        return tableros_instalados;

    }



    public void onButtonShowPopupWindowClick(View view) {

        misTablerosInstalados = obtenerTablerosInstalados("lista.lt");
        // inflate the layout of the popup window
        Log.i("SALIDA", "LO OBTENIDO ES: "+misTablerosInstalados.get(0)[1]);
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.games_list, null);
         milista = popupView.findViewById(R.id.showroom);



        ViewGroup.LayoutParams params = milista.getLayoutParams();
        params.height=(int) (screen_density*151* misTablerosInstalados.size());
        milista.setLayoutParams(params);
        ArrayAdapter adapter = new PersonAdapter(this, miview, misTablerosInstalados);
        milista.setAdapter(adapter);


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
        milista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(c, spacecrafts.get(position), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext(),R.style.AlertDialogCustom);
                builder.setTitle("Elegiste la pieza:");
                FrameLayout miframe = new FrameLayout(adapter.getContext());
                LinearLayoutCompat milay = new LinearLayoutCompat(adapter.getContext());
                milay.setOrientation(LinearLayoutCompat.VERTICAL);
                miframe.setPadding(50, 10, 50, 10);
                TextView mitexto= new TextView(adapter.getContext());
                eleccion= misTablerosInstalados.get(position)[0];
                mitexto.setTextSize(18);
                mitexto.setPadding(4,15,4,4);
                mitexto.setGravity(Gravity.CENTER);
                mitexto.setText( eleccion);
                nombre_tablero=eleccion;

                milay.addView(mitexto);

                miframe.addView(milay);

                builder.setView(miframe);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        dialogInterface.dismiss();
                        popupWindow.dismiss();

                        File file = Environment.getExternalStorageDirectory();
                        File dir = new File(file.getAbsolutePath() + "/VuzzleGame/Factory");
                        if (dir.exists()) {
                            dir.delete();

                        }

                        File fileToDelete = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/VuzzleGame/VuzzleBigData.data");
                        if (fileToDelete.exists()) {
                            fileToDelete.delete();

                        }

                        Procesar_Fase_1(misTablerosInstalados.get(position)[1]);
                        prensa.setImageDrawable(avd_prensa);
                        anim_prensa=true;
                        avd_prensa.setAlpha(255);
                        avd_prensa.start();

                        anim_instrucciones=false;
                        avd_instrucciones.stop();
                        avd_instrucciones.setAlpha(0);
                    }
                });

                AlertDialog ad= builder.create();
                ad.show();
            }
        });

    }
/** ESTA FUNCION QUEDÓ DENTRO DEL PRIEMR ASYNC TASK
    public void desplegarFondo(Context c, int recurso) {

        Bitmap fondo_base = Bitmap.createBitmap(decodeResource(c.getResources(), recurso));
        fondo_base = Bitmap.createScaledBitmap(fondo_base, 900, 900, true);
        int largo_desplegado = 1800;
        int alto_desplegado = 1800;
        Bitmap fondo_desplegado = Bitmap.createBitmap(largo_desplegado, alto_desplegado, ARGB_8888);

        for (int i = 0; i < 900; i++) {
            for (int j = 0; j < 900; j++) {
                int color_src = fondo_base.getPixel(j, i);
                fondo_desplegado.setPixel(j, i, color_src);
                fondo_desplegado.setPixel(largo_desplegado - j, i + 0, color_src);
                fondo_desplegado.setPixel(largo_desplegado - j, alto_desplegado - i, color_src);
                fondo_desplegado.setPixel(j, alto_desplegado - i, color_src);
            }
        }
    }
 **/


    public void Procesar_Fase_1 (String eleccion) {
        FileOutputStream outputStream = null;
        eleccion = eleccion.substring(0,eleccion.length()-6);
        anim_prensa=true;
        avd_prensa.start();
        avd_prensa.setAlpha(255);



        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/VuzzleGame/Factory");
        if (dir.exists()) {
            dir.delete();

        }
        if (!dir.exists()) {
            dir.mkdir();

        }


        Trocer trocer = (Trocer) new Trocer(this, eleccion, pd,  Fase,nombre_tablero).execute();



    }

    public ArrayList<String> obt_nomb_pzs(String texto_leido_archivo) {

        ArrayList<String> listaObjetos = new ArrayList<String>();
        String[] frags  = texto_leido_archivo.split(";");
        String[] num_piezas = frags[0].split("::");
        num_piezas = num_piezas[1].split(",");
        num_alto= Integer.parseInt( num_piezas[0]);
        num_largo= Integer.parseInt( num_piezas[1]);
        frags=frags[1].split("::");
        frags = frags[1].split(",");

        for(int i=0; i<frags.length; i++) {
            listaObjetos.add(frags[i].toString());

        }

        return listaObjetos;

    }
    public void Procesar_Fase_2 () {

        Factory factory =(Factory) new Factory(this, pd, Fase).execute();

    }

    public void Procesar_Fase_3 () {
        String cadena = leer_archivo("Piezas.txt");
        ArrayList<String> piezas= obt_nomb_pzs(cadena);
        Log.i("CLASS", "PIEZAS : "+piezas);


        Reducer reducer =(Reducer) new Reducer(this, piezas,pd, Fase).execute();
        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

            }
        }, 5000);*/



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

    @Override
    public void onIntegerChanged(int newValue) {

    }


    //




    class PersonAdapter extends ArrayAdapter<String[]> {
        private final Context context;
        private final ArrayList<String[]> data;
        private final int layoutResourceId;


        public PersonAdapter(Context context, int layoutResourceId, ArrayList<String[]> data) {
            super(context, layoutResourceId, data);
            this.context = context;
            this.data = data;


            int tam = data.size();
            Toast.makeText(context, "Items cargados: "+ tam , Toast.LENGTH_SHORT).show();
            this.layoutResourceId = layoutResourceId;



        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String[] [] info;
            View row = convertView;
            ViewHolder holder = null;
            String [] descripcion;
            String [] items;

            if(row == null)
            {
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);

                holder = new ViewHolder();

                holder.footerText = (TextView) row.findViewById(R.id.footer);
                holder.iconContainerImage= (ImageView) row.findViewById(R.id.iconContainer);


                row.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)row.getTag();
            }

            holder.footerText.setText(data.get(position)[0]);
            holder.footerText.setGravity(Gravity.CENTER_VERTICAL);
            Bitmap bitmap = null;
            /**
            String nombre_recurso = data.get(position)[0];
            nombre_recurso=nombre_recurso.substring(0,nombre_recurso.length()-4);
            Log.i("CLASS","NOMBRE REC: "+ nombre_recurso );


            bitmap = Bitmap.createBitmap(decodeResource(getResources(), getResources().getIdentifier(nombre_recurso, "drawable", getPackageName())));
            bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);**/
            String nombre_thumb = data.get(position)[1];
            File dirDest = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VuzzleGame/"+nombre_thumb);

            bitmap = BitmapFactory.decodeFile(dirDest.toString());
            holder.iconContainerImage.setImageBitmap(bitmap);


            return row;

        }

        class ViewHolder
        {

            TextView footerText;
            ImageView iconContainerImage;

        }
    }
/**
    public static int getResourceId(String pVariableName, String pResourcename, String pPackageName)
    {
        try {
            return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
 **/
public void anima(View view ) {
    avd_install.setAlpha(1);
    avd_install.start();
    anim_install=true;

}


    public void chequearPermisos() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }
    public void dialogoPermisos() {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
            startActivityForResult(intent, 2296);

        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivityForResult(intent, 2296);

        }
    }









}



