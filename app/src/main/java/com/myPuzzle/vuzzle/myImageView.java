package com.myPuzzle.vuzzle;


import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.animation.AccelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.Nullable;


@SuppressLint("AppCompatCustomView")
public class myImageView extends ImageView implements ScaleGestureDetector.OnScaleGestureListener {
    private int myid;
    private String PIEZANORTE, PIEZAOESTE, PIEZAESTE, PIEZASUR;
    private int num_grupo = -1;
    private float Xinicial;
    private Mode mode = Mode.NONE;
    public static int pieza_en_uso = -1;
    boolean estadoBloqueado = false;
    float angulo = 0.0F;
    float posX1 = 0.0F;
    float posY1 = 0.0F;
    float difY2acumulada = 0.0F;

    public void setJuego_completo(boolean juego_completo) {
        this.juego_completo = juego_completo;
    }

    boolean juego_completo = false;

    float posX2 = 0.0F;
    float posY2 = 0.0F;
    float posvertical = 0.0F;
    float posverticalanterior = 0.0F;
    float poshorizontalanterior = 0.0F;

    float pointerXincial = 0.0F;
    float pointerYincial = 0.0F;
    boolean animacion_en_curso = false;

    public boolean getEstadoBloqueado() {
        return estadoBloqueado;
    }

    public void setEstadoBloqueado(boolean estadoBloqueado) {
        this.estadoBloqueado = estadoBloqueado;
    }


    private enum Mode {
        NONE,
        ONE,
        TWO
    }

    Animator.AnimatorListener milistener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            animacion_en_curso = false;

        }

        @Override
        public void onAnimationCancel(Animator animation) {
            animacion_en_curso = false;


        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };


    public void setHorizontalScrollView(HorizontalScrollView horizontalScrollView) {
        this.horizontalScrollView = horizontalScrollView;
    }

    HorizontalScrollView horizontalScrollView;

    public void setVerticalScrollView(ScrollView verticalScrollView) {
        this.verticalScrollView = verticalScrollView;
    }

    ScrollView verticalScrollView;

    public float getXinicial() {
        return Xinicial;
    }

    public void setXinicial(float xinicial) {
        Xinicial = xinicial;
    }

    public float getYinicial() {
        return Yinicial;
    }

    public void setYinicial(float yinicial) {
        Yinicial = yinicial;
    }

    private float Yinicial;

    private ObservableInteger obsInt;
    ScaleGestureDetector gestureDetector;


    public myImageView(Context context) {
        super(context);

        gestureDetector = new ScaleGestureDetector(context, this);


    }

    public myImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public myImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

            event.offsetLocation(event.getRawX() - event.getX(), event.getRawY() - event.getY());

            if (pieza_en_uso == -1) {
                pieza_en_uso = myid;
            }
            if (pieza_en_uso == myid &&(!juego_completo)) {

                getParent().requestDisallowInterceptTouchEvent(true);
                int anchoScrollHorizontal = horizontalScrollView.getWidth();
                int altoScrollVertical = verticalScrollView.getHeight();
                // int margenHorizontal = anchoScrollHorizontal/8;
                //int margenVertical = altoScrollVertical/8;
                int ancho_lienzo = verticalScrollView.getChildAt(0).getMeasuredWidth();
                int alto_lienzo = verticalScrollView.getChildAt(0).getMeasuredHeight();
                int maxScrollVertical = verticalScrollView.getChildAt(0).getMeasuredHeight() - horizontalScrollView.getMeasuredHeight();
                int maxScrollHorizontal = horizontalScrollView.getChildAt(0).getMeasuredWidth() - horizontalScrollView.getMeasuredWidth();

                int horizontalScrollTOP = (int) horizontalScrollView.getY();
                int horizontalScrollLEFT = (int) horizontalScrollView.getX();
                int horizontalScrollBOTTOM = (int) horizontalScrollView.getY() + horizontalScrollView.getHeight();
                int horizontalScrollRIGTH = (int) horizontalScrollView.getX() + horizontalScrollView.getWidth();


                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:

                        mode = Mode.ONE;
                        this.bringToFront();
                        this.setScaleX(1.8F);
                        this.setScaleY(1.8F);
                        Log.i("COORD", "ESTE TIENE UNA ROTACION DE: " + this.getRotation());
                        pointerYincial = event.getY();
                        pointerXincial = event.getX();

                        break;
                    case MotionEvent.ACTION_MOVE:

                        if (mode == Mode.ONE) {
                            float posxnueva = event.getX();
                            float posynueva = event.getY();
                            this.setX(this.getX() + (posxnueva - pointerXincial) * main_game.scale_factor);
                            this.setY(this.getY() + (posynueva - pointerYincial) * main_game.scale_factor);
                            pointerXincial = posxnueva;
                            pointerYincial = posynueva;


                            //if(posxnueva<margenHorizontal&&(horizontalScrollView.getScrollX()>0)) {
                            if (event.getRawX() < horizontalScrollLEFT + 100) {
                                Log.i("SCROLL", horizontalScrollView.getScrollX() + "");
                                horizontalScrollView.smoothScrollBy((int) -(15 / main_game.scale_factor), 0);
                                this.setTranslationX(this.getX() - 15);

                            } else if (event.getRawX() > (horizontalScrollRIGTH - 100) && (horizontalScrollView.getScrollX() < maxScrollHorizontal)) {
                                Log.i("SCROLL", horizontalScrollView.getScrollX() + "");
                                horizontalScrollView.smoothScrollBy((int) +(15 / main_game.scale_factor), 0);
                                this.setTranslationX(this.getX() + 15);

                            }

                            Log.i("SCROLL", "PIEZA X :" + this.getX());
                            Log.i("SCROLL", "PIEZA Y :" + this.getY());

                            //if((posynueva<margenVertical)&&(verticalScrollView.getScrollY()>0)) {
                            if (event.getRawY() < horizontalScrollTOP + 100) {
                                verticalScrollView.smoothScrollBy(0, (int) -(15 / main_game.scale_factor));
                                this.setTranslationY(this.getY() - 15);
                                // } else if (posynueva>(altoScrollVertical-margenVertical)&&(verticalScrollView.getScrollY()<maxScrollVertical)) {
                            } else if (event.getRawY() > (horizontalScrollBOTTOM - 100) && (verticalScrollView.getScrollY() < maxScrollVertical)) {
                                verticalScrollView.smoothScrollBy(0, (int) +(15 / main_game.scale_factor));
                                this.setTranslationY(this.getY() + 15);
                            }
                            if (this.getX() < 20) {
                                this.setX(40);
                                this.setScaleX(1.0F);
                                this.setScaleY(1.0F);
                                mode = Mode.NONE;
                            } else if (this.getX() > ancho_lienzo - (this.getWidth() / 2)) {
                                this.setScaleX(1.0F);
                                this.setScaleY(1.0F);
                                mode = Mode.NONE;
                                this.setX(ancho_lienzo - this.getWidth() - 30);
                            }
                            if (this.getY() < 0) {
                                this.setScaleX(1.0F);
                                this.setScaleY(1.0F);
                                this.setY(20);
                                mode = Mode.NONE;

                            } else if (this.getY() > alto_lienzo - (this.getHeight() / 2)) {
                                this.setScaleX(1.0F);
                                this.setScaleY(1.0F);
                                this.setY(alto_lienzo - this.getHeight() - 30);
                                mode = Mode.NONE;
                            }

                        }


                        if (mode == Mode.TWO) {
                            if ((estadoBloqueado == false) && (animacion_en_curso == false)) {
                                posX1 = event.getX();
                                posY1 = event.getY();
                                posX2 = event.getX(1);
                                posY2 = event.getY(1);

                                posvertical = (float) (((posX1 - posX2) * Math.sin(Math.toRadians(angulo))) + ((posY1 - posY2) * Math.cos(Math.toRadians(angulo))));
                                float diferenciaVertical = posvertical - posverticalanterior;
                                Log.i("GEST", "Diferencia Vertical: " + diferenciaVertical);
                                difY2acumulada += diferenciaVertical;
                                Log.i("GEST", "Diferencia ACUMULADA: " + difY2acumulada);


                                if (difY2acumulada > 60) {
                                    Log.i("GEST", "-30");
                                    animacion_en_curso = true;
                                    AccelerateInterpolator miInterpolator = new AccelerateInterpolator(1);

                                    // if(this.getRotation()==-315) {
                                    //    this.setRotation(+45);
                                    // }
                                    if (poshorizontalanterior > 0) {
                                        Log.i("OUT", "ACUM POS");
                                        Log.i("OUT", "ROTAION : " + this.getRotation());
                                        Log.i("OUT", "poshoriz: " + poshorizontalanterior);
                                        Log.i("OUT", "eventX: " + event.getX());
                                        Log.i("OUT", "LADO DERECHO -45");
                                        this.animate().rotation(this.getRotation() - 45).setInterpolator(miInterpolator).setListener(milistener).setDuration(150);
                                    } else {
                                        Log.i("OUT", "ACUM POS");
                                        Log.i("OUT", "ROTAION : " + this.getRotation());
                                        Log.i("OUT", "poshoriz: " + poshorizontalanterior);
                                        Log.i("OUT", "eventX: " + event.getX());
                                        Log.i("OUT", "LADO DERECHO +45");
                                        this.animate().rotation(this.getRotation() + 45).setInterpolator(miInterpolator).setListener(milistener).setDuration(150);
                                    }

                                    difY2acumulada = 0;


                                } else if (difY2acumulada < -60) {
                                    Log.i("GEST", "+30");
                                    animacion_en_curso = true;


                                    AccelerateInterpolator miInterpolator = new AccelerateInterpolator(1);

                                    //if(this.getRotation()==315) {
                                    //    this.setRotation(-45);
                                    /// }
                                    if (poshorizontalanterior > 0) {
                                        Log.i("OUT", "ACUM NEGATIVO");
                                        Log.i("OUT", "ROTAION : " + this.getRotation());
                                        Log.i("OUT", "poshoriz: " + poshorizontalanterior);
                                        Log.i("OUT", "eventX: " + event.getX());
                                        this.animate().rotation(this.getRotation() + 45).setInterpolator(miInterpolator).setListener(milistener).setDuration(150);
                                        Log.i("OUT", "LADO DERECHO +45");
                                    } else {
                                        Log.i("OUT", "ACUM NEGATIVO");
                                        Log.i("OUT", "ROTAION : " + this.getRotation());
                                        Log.i("OUT", "poshoriz: " + poshorizontalanterior);
                                        Log.i("OUT", "eventX: " + event.getX());
                                        this.animate().rotation(this.getRotation() - 45).setInterpolator(miInterpolator).setListener(milistener).setDuration(150);
                                        Log.i("OUT", "LADO IZQUIERDO -45");
                                    }
                                    difY2acumulada = 0;
                                }
                                posverticalanterior = posvertical;
                            }
                        }
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        Log.i("SCROLL", "POS X = " + this.getX());
                        Log.i("SCROLL", "POS Y = " + this.getY());
                        Log.i("SCROLL", "RAW X = " + event.getRawX());
                        Log.i("SCROLL", "RAW Y = " + event.getRawY());
                        Log.i("SCROLL", "BOTTON = " + horizontalScrollBOTTOM);
                        Log.i("SCROLL", "RIGHT = " + horizontalScrollRIGTH);


                        angulo = this.getRotation();
                        posX1 = event.getX();
                        posY1 = event.getY();
                        posX2 = event.getX(1);
                        posY2 = event.getY(1);
                        difY2acumulada = 0;
                        posverticalanterior = (float) (((posX1 - posX2) * Math.sin(Math.toRadians(angulo))) + ((posY1 - posY2) * Math.cos(Math.toRadians(angulo))));
                        poshorizontalanterior = (float) (((posX1 - posX2) * Math.cos(Math.toRadians(angulo + 180))) - ((posY1 - posY2) * Math.sin(Math.toRadians(angulo + 180))));
                        mode = Mode.TWO;

                        break;
                    case MotionEvent.ACTION_POINTER_UP:


                        mode = Mode.ONE;


                        break;


                    case MotionEvent.ACTION_UP:

                        pieza_en_uso = -1;
                        this.setScaleX(1.0F);
                        this.setScaleY(1.0F);
                        if (this.getX() < 30) {
                            this.setX(50);
                            this.setScaleX(1.0F);
                            this.setScaleY(1.0F);
                            mode = Mode.NONE;
                        } else if (this.getX() > ancho_lienzo - (this.getWidth())) {
                            this.setScaleX(1.0F);
                            this.setScaleY(1.0F);
                            mode = Mode.NONE;
                            this.setX(ancho_lienzo - this.getWidth() - 30);
                        }
                        if (this.getY() < 30) {
                            this.setScaleX(1.0F);
                            this.setScaleY(1.0F);
                            this.setY(50);
                            mode = Mode.NONE;

                        } else if (this.getY() > alto_lienzo - (this.getHeight())) {
                            this.setScaleX(1.0F);
                            this.setScaleY(1.0F);
                            this.setY(alto_lienzo - this.getHeight() - 30);
                            mode = Mode.NONE;
                        }


                        obsInt.set(myid);


                        break;

                }
                gestureDetector.onTouchEvent(event);
                return true;

            } else {
                return false;
            }
        }




        public void setObsInt (ObservableInteger obsInt){
            this.obsInt = obsInt;

        }

        public String damePiezaNorte () {
            return PIEZANORTE;

        }
        public String damePiezaOeste () {
            return PIEZAOESTE;

        }
        public String damePiezaSur () {
            return PIEZASUR;

        }
        public String damePiezaEste () {
            return PIEZAESTE;

        }
        public int dameGrupo () {
            return num_grupo;

        }
        public int dameId () {
            return myid;
        }
        public void setPiezaNorte (String piezaNorte){
            PIEZANORTE = piezaNorte;

        }
        public void setPiezaOeste (String piezaOeste){
            PIEZAOESTE = piezaOeste;

        }
        public void setPiezaSur (String piezaSur){
            PIEZASUR = piezaSur;

        }
        public void setPiezaEste (String piezaEste){
            PIEZAESTE = piezaEste;

        }
        public void setGrupo ( int grupo){
            num_grupo = grupo;

        }
        public void seteaId ( int elid){
            myid = elid;
        }


        @Override
        public boolean onScale (ScaleGestureDetector detector){
            Log.i("OUT", " onScale ");
            return false;
        }

        @Override
        public boolean onScaleBegin (ScaleGestureDetector detector){
            Log.i("OUT", " onScaleBegin");
            return false;
        }

        @Override
        public void onScaleEnd (ScaleGestureDetector detector){
            Log.i("OUT", " onScaleEnd");

        }
    }


