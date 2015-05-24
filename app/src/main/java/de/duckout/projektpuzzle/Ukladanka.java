package de.duckout.projektpuzzle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class Ukladanka extends Activity {
    private int poziom_trudnosci;
    private int nr_zdjecia;
    private Wytnij widokUkladanki;
    private Handler handler;
    private Runnable refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Plansza_WyborObrazka.ukladanka_start = true;
        //setContentView(R.layout.activity_ukladanka);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            poziom_trudnosci = extras.getInt("POZIOM_TRUDNOSCI");
            nr_zdjecia = extras.getInt("OBRAZEK");


        }
       // nr_zdjecia=4;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.widokUkladanki = new Wytnij(this, poziom_trudnosci + 1,nr_zdjecia);
//        Wytnij w
        handler = new Handler(Looper.getMainLooper());
        refresh = new Runnable() {
            @Override
            public void run() {
                widokUkladanki.aktualizujPolozeniePuzzli();
                widokUkladanki.invalidate();
            }
        };
        setContentView(widokUkladanki);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(refresh);
                }
            }
        }).start();
        //  Log.d("nasz poziom trudnosci to :", " "+String.valueOf(poziom_trudnosci));
        //Toast.makeText(getApplicationContext(), "poziom trudnosci to: " + String.valueOf(poziom_trudnosci), Toast.LENGTH_SHORT).show();
    }

 /*   public void koniec_gry(View view) {
        Plansza_WyborObrazka.ukladanka_koniec = true;
        finish();
        startActivity(getIntent());
        Toast.makeText(getApplicationContext(), "KONIEC", Toast.LENGTH_SHORT).show();
    }


}*/

}
