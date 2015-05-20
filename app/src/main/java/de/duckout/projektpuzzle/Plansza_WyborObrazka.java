package de.duckout.projektpuzzle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Marta on 2015-05-12.
 */
public class Plansza_WyborObrazka extends View {

    public int x;
    public int y;


    public static boolean start;
    public static int nr_zdjecia;
    public static int poziom_trudnosci;
    public static boolean ukladanka_koniec;
    public static boolean ukladanka_start;

    private Drawable drawable;

    public Plansza_WyborObrazka(Context context,AttributeSet attrs) {
        super(context,attrs);

    }

    public void onDraw(Canvas canvas){

        super.onDraw(canvas);

        x=getWidth();
        y=getHeight();
        Paint paint = new Paint();

        if(start==true) {
            wstawianie_zdjecia(nr_zdjecia, canvas);
            rysowanie_lini(canvas, paint, "#ffffff");
        }
        else {
            paint.setColor(Color.parseColor("#ffffff"));
            canvas.drawRect(10, 10, y - 10, y - 10, paint);
            rysowanie_lini(canvas, paint, "#222222");

        }
        if(ukladanka_start == true) {
            wstawianie_zdjecia(nr_zdjecia, canvas);
            canvas.drawRect(10, 10, 40, 40, paint);
        }
        if(ukladanka_koniec == true) {
            wstawianie_zdjecia(nr_zdjecia, canvas);
        }
//        canvas.setMatrix();
    }
    private void wstawianie_zdjecia(int pozycja, Canvas canvas) {
        drawable = getResources().getDrawable(R.drawable.pobrane+pozycja);
        drawable.setBounds(10, 10, y - 10, y - 10);
        drawable.draw(canvas);
    }
    private void rysowanie_lini(Canvas canvas, Paint paint, String kolor) {
        if(poziom_trudnosci==1) {
            paint.setColor(Color.parseColor(kolor));
            canvas.drawRect((y-20)/2 -1,10,(y-20)/2 +1, y-10, paint);
            canvas.drawRect(10, (y-20)/2 -1,y-10, (y-20)/2 +1, paint);
        }
        else if(poziom_trudnosci==2) {
            paint.setColor(Color.parseColor(kolor));
            canvas.drawRect((y-20)/3 -1,10,(y-20)/3 +1, y-10, paint);
            canvas.drawRect(((y-20)/3)*2 -1,10,((y-20)/3)*2 +1, y-10, paint);

            canvas.drawRect(10, (y-20)/3 -1,y-10, (y-20)/3 +1, paint);
            canvas.drawRect(10, ((y-20)/3)*2 -1,y-10, ((y-20)/3)*2 +1, paint);
        }
        else {
            paint.setColor(Color.parseColor(kolor));
            canvas.drawRect((y-20)/4 -1,10,(y-20)/4 +1, y-10, paint);
            canvas.drawRect(((y-20)/4)*2 -1,10,((y-20)/4)*2 +1, y-10, paint);
            canvas.drawRect(((y-20)/4)*3 -1,10,((y-20)/4)*3 +1, y-10, paint);

            canvas.drawRect(10, (y-20)/4 -1,y-10, (y-20)/4 +1, paint);
            canvas.drawRect(10, ((y-20)/4)*2 -1,y-10, ((y-20)/4)*2 +1, paint);
            canvas.drawRect(10, ((y-20)/4)*3 -1,y-10, ((y-20)/4)*3 +1, paint);

        }
    }

}

