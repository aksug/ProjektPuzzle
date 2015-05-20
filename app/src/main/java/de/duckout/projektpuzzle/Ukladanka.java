package de.duckout.projektpuzzle;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Marta on 2015-05-12.
 */
public class Ukladanka extends Activity {
    private int poziom_trudnosci;
    private int nr_zdjecia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Plansza_WyborObrazka.ukladanka_start = true;
        setContentView(R.layout.activity_ukladanka);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            poziom_trudnosci = extras.getInt("POZIOM_TRUDNOSCI");
            nr_zdjecia = extras.getInt("OBRAZEK");


        }
        //Toast.makeText(getApplicationContext(), "START", Toast.LENGTH_SHORT).show();
    }

    public void koniec_gry(View view) {
        Plansza_WyborObrazka.ukladanka_koniec = true;
        finish();
        startActivity(getIntent());
        Toast.makeText(getApplicationContext(), "KONIEC", Toast.LENGTH_SHORT).show();
    }
}
