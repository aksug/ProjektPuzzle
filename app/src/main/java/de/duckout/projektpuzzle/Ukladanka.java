package de.duckout.projektpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;

public class Ukladanka extends Activity {
    private static final int REQ_CODE = 123;
    private int poziom_trudnosci;
    private int nr_zdjecia;
    private Gra widokGry;
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
        this.widokGry = new Gra(this, poziom_trudnosci + 1,nr_zdjecia);
        handler = new Handler(Looper.getMainLooper());
        refresh = new Runnable() {
            @Override
            public void run() {
                if (widokGry.aktualizujPolozeniePuzzli()) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("wygrana", true);
                    setResult(REQ_CODE, resultIntent);
                    finish();
                }
                widokGry.invalidate();
            }
        };
        setContentView(widokGry);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(refresh);
                }
            }
        }).start();

    }
}
