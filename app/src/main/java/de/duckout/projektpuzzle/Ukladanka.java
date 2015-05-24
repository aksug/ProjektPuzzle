package de.duckout.projektpuzzle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    private static final int REQ_CODE = 123;
    private int poziom_trudnosci;
    private int nr_zdjecia;
    private Wytnij widokUkladanki;
    private Handler handler;
    private Runnable refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            poziom_trudnosci = extras.getInt("POZIOM_TRUDNOSCI");
            nr_zdjecia = extras.getInt("OBRAZEK");


        }
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.widokUkladanki = new Wytnij(this, poziom_trudnosci + 1,nr_zdjecia);
        handler = new Handler(Looper.getMainLooper());
        refresh = new Runnable() {
            @Override
            public void run() {
                if (widokUkladanki.aktualizujPolozeniePuzzli()) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("wygrana", true);
                    setResult(REQ_CODE, resultIntent);
                    finish();
                }
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

    }


}
