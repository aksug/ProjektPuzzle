package de.duckout.projektpuzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sylwia on 20.05.15.
 */
public class Wytnij extends View {

    ArrayList<Bitmap> tab;

    public int wysokoscEkranu;
    public int szerokoscEkranu;
    public int[][] obrazki;
    public int iloscPodzialow;
    public int szerokoscObrazka;
    public int wysokoscObrazka;

    Bitmap bitmapOrginalna;
    int dotknietyY;
    int dotknietyX;


    int poprzedniPustyX;
    int poprzedniPustyY;
    int nr_zdjecia;
    int przesunieciePlanszyX;
    int przesunieciePlanszyY;

    boolean puzzelWTrakciePrzesuwania;
    private float obecnyLewyRogPuzzla_X;
    private float obecnyGornyRogPuzzla_Y;
    private float ostatecznyLewyRogPuzzla_X;
    private float ostatecznyGoryRogPuzzla_Y;
    private int dotknietyID;
    private int wymiarBitmapy;

    public Wytnij(Context context, int iloscPodzialow, int nr_zdjecia) {
        super(context);

        this.iloscPodzialow = iloscPodzialow;
        this.nr_zdjecia = nr_zdjecia;
        obrazki = new int[iloscPodzialow][iloscPodzialow];

        setFocusableInTouchMode(true);
        setFocusable(true);

        dotknietyY = poprzedniPustyY;
        dotknietyX = poprzedniPustyX;


    }

    @Override
    protected void onSizeChanged(int s, int w, int staras, int staraw) {
        super.onSizeChanged(s, w, staras, staraw);
        szerokoscEkranu = s;
        wysokoscEkranu = w;


        if (wysokoscEkranu > szerokoscEkranu) {
            wymiarBitmapy = szerokoscEkranu - 50;
        } else wymiarBitmapy = wysokoscEkranu - 50;

        szerokoscObrazka = wymiarBitmapy / iloscPodzialow;
        wysokoscObrazka = wymiarBitmapy / iloscPodzialow;
    }


    public void podzielObrazki(int nr_zdjecia) {

        tab = new ArrayList<>();

        //tutaj przycinam sobie orginalny obrazek. Jesli jest wiekszy niz ekran to go docinamy na wymiarBitmapy,wymiarBitmapy
        bitmapOrginalna = BitmapFactory.decodeResource(getResources(), R.drawable.pobrane + nr_zdjecia);
        bitmapOrginalna = Bitmap.createScaledBitmap(bitmapOrginalna, wymiarBitmapy, wymiarBitmapy, true);
        int pp = (int) bitmapOrginalna.getWidth() / iloscPodzialow; //tak na prawde szerokosc obrazka (malego puzla)
        int qq = (int) bitmapOrginalna.getHeight() / iloscPodzialow; //a tutaj wysokosc puzzla
        //wykrajamu z duzego obrazu orginalnego male puzzelki j*pp i i*qq oznaczaja ich wys i szerokosc
        for (int i = 0; i < iloscPodzialow; i++) {
            for (int j = 0; j < iloscPodzialow; j++) {
                tab.add(Bitmap.createBitmap(bitmapOrginalna, j * pp, i * qq, bitmapOrginalna.getWidth() / iloscPodzialow, bitmapOrginalna.getHeight() / iloscPodzialow));
                obrazki[i][j] = tab.size() - 1; // od razu w odpowiednim miejscu wrzucamy indeks wlasnie dodanego puzzla do arraylisty
            }
        }
        obrazki[iloscPodzialow - 1][iloscPodzialow - 1] = -1;  //ostatniego obrazka nie chcemy rysowac tylko zznaczyc ze jest pusty czyli jako indeks wstawiamy -1
        mieszaj();
    }

    public void mieszaj() {
        int a = iloscPodzialow - 1; //indeksy (zawsze wiemy gdzie obecnie jest puste miejsce a jego wspozedne to a,b)
        int b = iloscPodzialow - 1;
        int i = 0;
        Random los = new Random();
        int wylosowany;
        int wylosowany1;
        while (i < 100000) {
            wylosowany = los.nextInt(2); //jak 0 to nasze a-1, jak 1 to a+1
            wylosowany1 = los.nextInt(2);//jak 0 to nasze b-1, jak 1 to b+1
            if (wylosowany == 0) {
                if (a - 1 >= 0) { //czy nasz puzzel z lewej strony nie wychodzi poza tablice , jesli nie to wez lewego i zamien z pustym
                    obrazki[a][b] = obrazki[a - 1][b];
                    obrazki[a - 1][b] = -1;
                    zapiszWspolrzednePustego(a - 1, b);
                    a--; //zaznaczamy ze teraz pusty jest o indeksie a,b ale a przesunelo sie nam w lewo(b czyli nr wieersza ten sam)
                } else if (wylosowany1 == 0 && b - 1 >= 0) { //jezeli z lewej wychodzimy poxza tab to wez mien wiersz(jak 0 to w dol a "a" zostaje to samo)
                    obrazki[a][b] = obrazki[a][b - 1];
                    obrazki[a][b - 1] = -1;
                    b--;
                    zapiszWspolrzednePustego(a, b - 1);
                } else if (wylosowany1 == 1 && b + 1 < iloscPodzialow) {
                    obrazki[a][b] = obrazki[a][b + 1];
                    obrazki[a][b + 1] = -1;
                    b++;
                    zapiszWspolrzednePustego(a, b + 1);
                }
            } else {
                if (a + 1 < iloscPodzialow) { //w prawo chcemy go przesunac i analogicznie
                    obrazki[a][b] = obrazki[a + 1][b];
                    obrazki[a + 1][b] = -1;
                    a++;
                    zapiszWspolrzednePustego(a + 1, b);
                } else if (wylosowany1 == 0 && b - 1 >= 0) {
                    obrazki[a][b] = obrazki[a][b - 1];
                    obrazki[a][b - 1] = -1;
                    b--;
                    zapiszWspolrzednePustego(a, b - 1);
                } else if (wylosowany1 == 1 && b + 1 > iloscPodzialow) {
                    obrazki[a][b] = obrazki[a][b + 1];
                    obrazki[a][b + 1] = -1;
                    b++;
                    zapiszWspolrzednePustego(a, b + 1);
                }
            }
            i++;
        }
    }

    private void zapiszWspolrzednePustego(int pierwszaWsp, int drugaWsp) {
        poprzedniPustyX = drugaWsp;
        poprzedniPustyY = pierwszaWsp;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(tab == null)
            podzielObrazki(nr_zdjecia);
        canvas.drawColor(Color.BLACK); //rysujemy tlo

        /***************************RYSOWANIE PUZZLI*****************************************************/
        przesunieciePlanszyX = canvas.getWidth() / 2 - bitmapOrginalna.getWidth() / 2;
        przesunieciePlanszyY = canvas.getHeight() / 2 - bitmapOrginalna.getHeight() / 2;

        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        for (int i = 0; i < iloscPodzialow; i++) {//rysujemy tablice dwu wym o indeksach z arraylist(tab)
            //nie rysuj na -1
            for (int j = 0; j < iloscPodzialow; j++)
                if (obrazki[i][j] > -1) {

                        canvas.drawBitmap(tab.get(obrazki[i][j]), j * wysokoscObrazka + przesunieciePlanszyX, i * wysokoscObrazka + przesunieciePlanszyY, paint);
                }else{
            if(puzzelWTrakciePrzesuwania  ) {//TODO
                canvas.drawBitmap(tab.get(dotknietyID), obecnyLewyRogPuzzla_X, obecnyGornyRogPuzzla_Y, paint);
            }

            }

        }

    }


    public void aktualizujPolozeniePuzzli() {//move()
        if (puzzelWTrakciePrzesuwania) {
            if(obecnyLewyRogPuzzla_X != ostatecznyLewyRogPuzzla_X){
                if(ostatecznyLewyRogPuzzla_X>obecnyLewyRogPuzzla_X){
                    obecnyLewyRogPuzzla_X++;
                }else{
                    obecnyLewyRogPuzzla_X--;
                }
            }else if(obecnyGornyRogPuzzla_Y != ostatecznyGoryRogPuzzla_Y){
                if(ostatecznyGoryRogPuzzla_Y>obecnyGornyRogPuzzla_Y){
                    obecnyGornyRogPuzzla_Y++;
                }else{
                    obecnyGornyRogPuzzla_Y--;
                }
            }else {
                obrazki[poprzedniPustyY][poprzedniPustyX] = dotknietyID;
                puzzelWTrakciePrzesuwania = false;
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP && puzzelWTrakciePrzesuwania == false) {
            int wspX = (int) ((event.getX() - przesunieciePlanszyX) / szerokoscObrazka);
            int wspY = (int) ((event.getY() - przesunieciePlanszyY) / wysokoscObrazka); //wys->szer

            obecnyLewyRogPuzzla_X = wspX *  szerokoscObrazka+ przesunieciePlanszyX;
            obecnyGornyRogPuzzla_Y = wspY *wysokoscObrazka+przesunieciePlanszyY;

            dotknietyX = wspX;
            dotknietyY = wspY;
            if((event.getX() - przesunieciePlanszyX) < 0 || (event.getX() - przesunieciePlanszyX) > wymiarBitmapy) {
                Log.d("OUT", "WSCHOD_ZACHOD");
            }
            else if( (event.getY() - przesunieciePlanszyY) < 0 || (event.getY() - przesunieciePlanszyY) > wymiarBitmapy) {
                Log.d("OUT", "POLNOC_POLUDNIE");
            }
            else if (wspY - 1 >= 0 && obrazki[wspY - 1][wspX] == -1) {

                ostatecznyLewyRogPuzzla_X =  wspX *  szerokoscObrazka+ przesunieciePlanszyX;
                ostatecznyGoryRogPuzzla_Y = (wspY -1)*  wysokoscObrazka+ przesunieciePlanszyY;

                poprzedniPustyX = wspX;
                poprzedniPustyY = wspY - 1;

                puzzelWTrakciePrzesuwania = true;
                dotknietyID = obrazki[dotknietyY][dotknietyX];
                obrazki[dotknietyY][dotknietyX] = -1;

            } else if (wspX - 1 >= 0 && obrazki[wspY][wspX - 1] == -1) {

                ostatecznyLewyRogPuzzla_X =  (wspX - 1)*  szerokoscObrazka+ przesunieciePlanszyX;
                ostatecznyGoryRogPuzzla_Y = wspY*  wysokoscObrazka+ przesunieciePlanszyY;

                poprzedniPustyX = wspX - 1;
                poprzedniPustyY = wspY;
                dotknietyID = obrazki[dotknietyY][dotknietyX];
                obrazki[dotknietyY][dotknietyX] = -1;
                puzzelWTrakciePrzesuwania = true;

            } else if (wspX + 1 < iloscPodzialow && obrazki[wspY][wspX + 1] == -1) {
                ostatecznyLewyRogPuzzla_X =  (wspX + 1)*  szerokoscObrazka+ przesunieciePlanszyX;
                ostatecznyGoryRogPuzzla_Y = wspY*  wysokoscObrazka+ przesunieciePlanszyY;

                poprzedniPustyX = wspX + 1;
                poprzedniPustyY = wspY;
                dotknietyID = obrazki[dotknietyY][dotknietyX];
                puzzelWTrakciePrzesuwania = true;
                obrazki[dotknietyY][dotknietyX] = -1;
            } else if (wspY + 1 < iloscPodzialow && obrazki[wspY + 1][wspX] == -1) {
                ostatecznyLewyRogPuzzla_X =  wspX *  szerokoscObrazka+ przesunieciePlanszyX;
                ostatecznyGoryRogPuzzla_Y = (wspY + 1)*  wysokoscObrazka+ przesunieciePlanszyY;

                poprzedniPustyX = wspX;
                poprzedniPustyY = wspY + 1;
                dotknietyID = obrazki[dotknietyY][dotknietyX];
                puzzelWTrakciePrzesuwania = true;
                obrazki[dotknietyY][dotknietyX] = -1;
            }




        }


        return true;
    }


}

