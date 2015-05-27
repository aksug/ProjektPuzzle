package de.duckout.projektpuzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sylwia on 20.05.15.
 */
public class Wytnij extends View {

    private ArrayList<Bitmap> bitmapy;
    private Bitmap bitmapOrginalna;
    private int wymiarBitmapy;

    private int[][] wygraneUlozenie;
    private int[][] obrazki;

    private int wymiarObrazka;
    private int przesunieciePlanszyX;
    private int przesunieciePlanszyY;

    private int iloscPodzialow;
    private int nr_zdjecia;

    private boolean puzzelWTrakciePrzesuwania;
    private float obecnyLewyRogPuzzla_X;
    private float obecnyGornyRogPuzzla_Y;
    private float ostatecznyLewyRogPuzzla_X;
    private float ostatecznyGoryRogPuzzla_Y;
    private int poprzedniPustyX;
    private int poprzedniPustyY;
    private int dotknietyID;


    public Wytnij(Context context, int iloscPodzialow, int nr_zdjecia) {
        super(context);

        this.iloscPodzialow = iloscPodzialow;
        this.nr_zdjecia = nr_zdjecia;
        obrazki = new int[iloscPodzialow][iloscPodzialow];
        setFocusableInTouchMode(true);
        setFocusable(true);
    }

    @Override
    protected void onSizeChanged(int s, int w, int staras, int staraw) {
        super.onSizeChanged(s, w, staras, staraw);
        int szerokoscEkranu = s;
        int wysokoscEkranu = w;

        if (wysokoscEkranu > szerokoscEkranu) {
            wymiarBitmapy = szerokoscEkranu - 50;
        } else wymiarBitmapy = wysokoscEkranu - 50;

        wymiarObrazka = wymiarBitmapy / iloscPodzialow;
    }


    public void podzielObrazki(int nr_zdjecia) {
        wygraneUlozenie = new int[iloscPodzialow][iloscPodzialow];
        bitmapy = new ArrayList<>();

        //tutaj przycinam sobie orginalny obrazek. Jesli jest wiekszy niz ekran to go docinamy na wymiarBitmapy,wymiarBitmapy
        bitmapOrginalna = BitmapFactory.decodeResource(getResources(), R.drawable.pobrane + nr_zdjecia);
        bitmapOrginalna = Bitmap.createScaledBitmap(bitmapOrginalna, wymiarBitmapy, wymiarBitmapy, true);
        int pp = (int) bitmapOrginalna.getWidth() / iloscPodzialow; //tak na prawde szerokosc obrazka (malego puzla)
        int qq = (int) bitmapOrginalna.getHeight() / iloscPodzialow; //a tutaj wysokosc puzzla
        //wykrajamu z duzego obrazu orginalnego male puzzelki j*pp i i*qq oznaczaja ich wys i szerokosc
        for (int i = 0; i < iloscPodzialow; i++) {
            for (int j = 0; j < iloscPodzialow; j++) {
                bitmapy.add(Bitmap.createBitmap(bitmapOrginalna, j * pp, i * qq, bitmapOrginalna.getWidth() / iloscPodzialow, bitmapOrginalna.getHeight() / iloscPodzialow));
                obrazki[i][j] = bitmapy.size() - 1; // od razu w odpowiednim miejscu wrzucamy indeks wlasnie dodanego puzzla do arraylisty
                wygraneUlozenie[i][j] = bitmapy.size() - 1;
            }
        }
        obrazki[iloscPodzialow - 1][iloscPodzialow - 1] = -1;  //ostatniego obrazka nie chcemy rysowac tylko zznaczyc ze jest pusty czyli jako indeks wstawiamy -1
        wygraneUlozenie[iloscPodzialow - 1][iloscPodzialow - 1] = -1;
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
                    a--; //zaznaczamy ze teraz pusty jest o indeksie a,b ale a przesunelo sie nam w lewo(b czyli nr wieersza ten sam)
                } else if (wylosowany1 == 0 && b - 1 >= 0) { //jezeli z lewej wychodzimy poxza tab to wez mien wiersz(jak 0 to w dol a "a" zostaje to samo)
                    obrazki[a][b] = obrazki[a][b - 1];
                    obrazki[a][b - 1] = -1;
                    b--;
                } else if (wylosowany1 == 1 && b + 1 < iloscPodzialow) {
                    obrazki[a][b] = obrazki[a][b + 1];
                    obrazki[a][b + 1] = -1;
                    b++;
                }
            } else {
                if (a + 1 < iloscPodzialow) { //w prawo chcemy go przesunac i analogicznie
                    obrazki[a][b] = obrazki[a + 1][b];
                    obrazki[a + 1][b] = -1;
                    a++;
                } else if (wylosowany1 == 0 && b - 1 >= 0) {
                    obrazki[a][b] = obrazki[a][b - 1];
                    obrazki[a][b - 1] = -1;
                    b--;
                } else if (wylosowany1 == 1 && b + 1 > iloscPodzialow) {
                    obrazki[a][b] = obrazki[a][b + 1];
                    obrazki[a][b + 1] = -1;
                    b++;
                }
            }
            i++;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (bitmapy == null) {
            podzielObrazki(nr_zdjecia);
            przesunieciePlanszyX = canvas.getWidth() / 2 - bitmapOrginalna.getWidth() / 2;
            przesunieciePlanszyY = canvas.getHeight() / 2 - bitmapOrginalna.getHeight() / 2;
        }
        canvas.drawColor(Color.BLACK); //rysujemy tlo

        /***************************RYSOWANIE PUZZLI*****************************************************/
        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        for (int i = 0; i < iloscPodzialow; i++) {//rysujemy tablice dwu wym o indeksach z arraylist(tab)
            //nie rysuj na -1
            for (int j = 0; j < iloscPodzialow; j++)
                if (obrazki[i][j] > -1) {

                    canvas.drawBitmap(bitmapy.get(obrazki[i][j]), j * wymiarObrazka + przesunieciePlanszyX, i * wymiarObrazka + przesunieciePlanszyY, paint);
                } else {
                    if (puzzelWTrakciePrzesuwania) {
                        canvas.drawBitmap(bitmapy.get(dotknietyID), obecnyLewyRogPuzzla_X, obecnyGornyRogPuzzla_Y, paint);
                    }
                }
        }
    }


    public boolean aktualizujPolozeniePuzzli() {//move()
        if (puzzelWTrakciePrzesuwania) {
            if (obecnyLewyRogPuzzla_X != ostatecznyLewyRogPuzzla_X) {
                if (ostatecznyLewyRogPuzzla_X > obecnyLewyRogPuzzla_X) {
                    obecnyLewyRogPuzzla_X++;
                } else {
                    obecnyLewyRogPuzzla_X--;
                }
            } else if (obecnyGornyRogPuzzla_Y != ostatecznyGoryRogPuzzla_Y) {
                if (ostatecznyGoryRogPuzzla_Y > obecnyGornyRogPuzzla_Y) {
                    obecnyGornyRogPuzzla_Y++;
                } else {
                    obecnyGornyRogPuzzla_Y--;
                }
            } else {
                obrazki[poprzedniPustyY][poprzedniPustyX] = dotknietyID;
                puzzelWTrakciePrzesuwania = false;
                if (sprawdzCzyWygrana()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean sprawdzCzyWygrana() {

        for (int i = 0; i < iloscPodzialow; i++) {
            for (int j = 0; j < iloscPodzialow; j++) {
                if (obrazki[i][j] != wygraneUlozenie[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP && !puzzelWTrakciePrzesuwania) {
            int wspX = (int) ((event.getX() - przesunieciePlanszyX) / wymiarObrazka);
            int wspY = (int) ((event.getY() - przesunieciePlanszyY) / wymiarObrazka); //wys->szer

            obecnyLewyRogPuzzla_X = wspX * wymiarObrazka + przesunieciePlanszyX;
            obecnyGornyRogPuzzla_Y = wspY * wymiarObrazka + przesunieciePlanszyY;

            int dotknietyX = wspX;
            int dotknietyY = wspY;

            if ((event.getX() - przesunieciePlanszyX) < 0 || (event.getX() - przesunieciePlanszyX) > wymiarBitmapy) {
                // zabezpieczenie przed kliknieciem na pole poza obrazkiem
            } else if ((event.getY() - przesunieciePlanszyY) < 0 || (event.getY() - przesunieciePlanszyY) > wymiarBitmapy) {
                // zabezpieczenie przed kliknieciem na pole poza obrazkiem
            } else if (wspY - 1 >= 0 && obrazki[wspY - 1][wspX] == -1) {

                ostatecznyLewyRogPuzzla_X = wspX * wymiarObrazka + przesunieciePlanszyX;
                ostatecznyGoryRogPuzzla_Y = (wspY - 1) * wymiarObrazka + przesunieciePlanszyY;

                poprzedniPustyX = wspX;
                poprzedniPustyY = wspY - 1;

                puzzelWTrakciePrzesuwania = true;
                dotknietyID = obrazki[dotknietyY][dotknietyX];
                obrazki[dotknietyY][dotknietyX] = -1;

            } else if (wspX - 1 >= 0 && obrazki[wspY][wspX - 1] == -1) {

                ostatecznyLewyRogPuzzla_X = (wspX - 1) * wymiarObrazka + przesunieciePlanszyX;
                ostatecznyGoryRogPuzzla_Y = wspY * wymiarObrazka + przesunieciePlanszyY;

                poprzedniPustyX = wspX - 1;
                poprzedniPustyY = wspY;
                dotknietyID = obrazki[dotknietyY][dotknietyX];
                obrazki[dotknietyY][dotknietyX] = -1;
                puzzelWTrakciePrzesuwania = true;

            } else if (wspX + 1 < iloscPodzialow && obrazki[wspY][wspX + 1] == -1) {
                ostatecznyLewyRogPuzzla_X = (wspX + 1) * wymiarObrazka + przesunieciePlanszyX;
                ostatecznyGoryRogPuzzla_Y = wspY * wymiarObrazka + przesunieciePlanszyY;

                poprzedniPustyX = wspX + 1;
                poprzedniPustyY = wspY;
                dotknietyID = obrazki[dotknietyY][dotknietyX];
                puzzelWTrakciePrzesuwania = true;
                obrazki[dotknietyY][dotknietyX] = -1;
            } else if (wspY + 1 < iloscPodzialow && obrazki[wspY + 1][wspX] == -1) {
                ostatecznyLewyRogPuzzla_X = wspX * wymiarObrazka + przesunieciePlanszyX;
                ostatecznyGoryRogPuzzla_Y = (wspY + 1) * wymiarObrazka + przesunieciePlanszyY;

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


