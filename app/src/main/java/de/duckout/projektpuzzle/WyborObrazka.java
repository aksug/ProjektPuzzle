package de.duckout.projektpuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by Marta on 2015-05-12.
 */
public class WyborObrazka extends Activity{

    ListView listaObrazkow;
    ArrayList<Integer> obrazkiId;
    private Button button_start;

    RelativeLayout ukladankaLayout;
    private ScaleGestureDetector scaleGestureDetector;// -> do skalowania obrazka
    private float wspolczynnikSkali = 1;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wybor_obrazka);
        scaleGestureDetector = new ScaleGestureDetector(this, new MojScaleListener());

        obrazkiId = new ArrayList<Integer>();
        obrazkiId.add(R.drawable.pobrane);
        obrazkiId.add(R.drawable.pobrane2);
        obrazkiId.add(R.drawable.pobrane3);
        AdapterObrazki adapter = new AdapterObrazki(this, obrazkiId);
        adapter.notifyDataSetChanged();
        listaObrazkow = (ListView) findViewById(R.id.listView);
        listaObrazkow.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Plansza_WyborObrazka.poziom_trudnosci = extras.getInt("POZIOM_TRUDNOSCI");
        }


        listaObrazkow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("lista.OnClickListener", "parent=" + parent + ", position=" + position + "id=" + id);
                Plansza_WyborObrazka.nr_zdjecia = position;
                Plansza_WyborObrazka.start = true;
                finish();
                startActivity(getIntent());
            }
        });
    /*    lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                                             @Override
                                             public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                                 Log.d("onLongClick", "yeah");
                                                 Toast.makeText(getApplicationContext(), "LONG CLICK, ", Toast.LENGTH_SHORT).show();
                                                 return true;
                                             }
                                         }
        );
*/
        ukladankaLayout = (RelativeLayout)
                findViewById(R.id.relativeLayout);

        ukladankaLayout.setOnTouchListener(
                new RelativeLayout.OnTouchListener() {

                    private String PRZESUNIECIE = "PRZESUNIECIE", ZOOM = "ZOOM", SPOCZYNEK = "SPOCZYNEK";
                    private static final int BRAK_WSKAZNIKA_DOTYKAJACEGO_EKRAN = -1 ;

                    private String rodzajRuchu;
                    private float ostatnioDotknietyX = 0;//wsporzedne dotniecia ekranu
                    private float ostatnioDotknietyY = 0;
                    private float przesuniecieX = 0;//wartosci przesuniec
                    private float przesuniecieY = 0;
                    private int aktywnyWskaznikId = BRAK_WSKAZNIKA_DOTYKAJACEGO_EKRAN;


                    public boolean onTouch(View v,
                                           MotionEvent event) {
                        scaleGestureDetector.onTouchEvent(event);

                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN: {
                                Log.d("MotionEvent", "ACTION_DOWN");
                                //palec dotknal ekranu
                                //jedyna mozliwosc to przesuniecie
                                rodzajRuchu = PRZESUNIECIE;

                                ostatnioDotknietyX = event.getX(); /**-poprzedniePrzesX;//wsporzedne punktu dotkniecia*/
                                ostatnioDotknietyY = event.getY();/*-poprzedniePrzesY;*/
                                //zapisuje id pointera (do przesuniecia)
                                aktywnyWskaznikId = event.getPointerId(0);
                                break;
                            }
                            case MotionEvent.ACTION_MOVE: {
                                Log.d("MotionEvent", "ACTION_MOVE");
                                final int indeksWskaznikaDotykEkran = event.findPointerIndex(aktywnyWskaznikId);
                                final float x = event.getX(indeksWskaznikaDotykEkran);
                                final float y = event.getY(indeksWskaznikaDotykEkran);
                                //wykonaj ruch jesli ScaleGestureDetector nie przetwarza poprzedniego ruchu
                                if (!scaleGestureDetector.isInProgress()) {
                                    //obilcz odleglosc przesuniecia
                                    final float dx = x - ostatnioDotknietyX;
                                    final float dy = y - ostatnioDotknietyY;
                                    przesuniecieX += dx;
                                    przesuniecieY += dy;
                                    ukladankaLayout.invalidate();
                                }
                                //zapamietaj aktualna pozycje dotkniecia do nastenego etapu ruchu
                                ostatnioDotknietyX = x;
                                ostatnioDotknietyY = y;
                                break;
                            }
                            case MotionEvent.ACTION_POINTER_DOWN: {
                                Log.d("MotionEvent", "ACTION_POINTER_DOWN");
                                //   drugi palec na ekranie
                                rodzajRuchu = ZOOM;
                                break;
                            }
                            case MotionEvent.ACTION_UP: {
                                Log.d("MOVE: ", "ACTION_UP");
                                // wszystkie palce przestaja dotykac ekranu
                                rodzajRuchu = SPOCZYNEK;
                                aktywnyWskaznikId = BRAK_WSKAZNIKA_DOTYKAJACEGO_EKRAN;
                                break;
                            }
                            case MotionEvent.ACTION_POINTER_UP: {
                                Log.d("MotionEvent", "ACTION_POINTER_UP");
                                //drugi wcisniety palec przestaje dotykac ekranu, ale pierwszy nadal dotyka
                                //wyodrebnienie indeksu wskaznika ktory przestal dotykac ekranu
                                final int wskIndeks = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                                final int wskaznikId = event.getPointerId(wskIndeks);
                                if (wskaznikId == aktywnyWskaznikId) {
                                    // uaktulalniam nowy aktywny wskaznik iustawiam odpowiednio parametry// This was our active pointer going up. Choose a new
                                    final int nowyWskaznikIndeks = wskIndeks == 0 ? 1 : 0;
                                    ostatnioDotknietyX = event.getX(nowyWskaznikIndeks);
                                    ostatnioDotknietyY = event.getY(nowyWskaznikIndeks);
                                    aktywnyWskaznikId = event.getPointerId(nowyWskaznikIndeks);
                                }
                                rodzajRuchu = PRZESUNIECIE;
                                break;
                            }
                        }
                        //odswiezamy jedynie gdy skalujemy lub przeuwamy sie
                        if ((rodzajRuchu.equals(PRZESUNIECIE) && wspolczynnikSkali != 1) || rodzajRuchu.equals(ZOOM)) {
                            ukladankaLayout.setTranslationX(przesuniecieX);
                            ukladankaLayout.setTranslationY(przesuniecieY);
                            float przemieszczenieZ = (float) Math.sqrt(przesuniecieX*przesuniecieX+przesuniecieY*przesuniecieY);
                        //    ukladankaLayout.setTranslationZ(przemieszczenieZ);
                            ukladankaLayout.setScaleY(wspolczynnikSkali);
                            ukladankaLayout.setScaleX(wspolczynnikSkali);
                            ukladankaLayout.invalidate();
                        }
                        return true;
                    }

                }
        );

    }


    public void uruchom_gre(View view) {

       // button_start = (Button) findViewById(R.id.start);
       // Toast.makeText(getApplicationContext(), "START", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),Ukladanka.class);

        intent.putExtra("OBRAZEK", Plansza_WyborObrazka.nr_zdjecia);
        intent.putExtra("POZIOM_TRUDNOSCI", Plansza_WyborObrazka.poziom_trudnosci);
        startActivity(intent);

    }



    private class MojScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float MIN_ZOOM = 0.9f;
        private float MAX_ZOOM = 2.0f;
        @Override//kiedy nastepuje Zoom'ing
        public boolean onScale(ScaleGestureDetector detector) {
            wspolczynnikSkali *= detector.getScaleFactor();
            wspolczynnikSkali = Math.max(MIN_ZOOM, Math.min(wspolczynnikSkali, MAX_ZOOM));
            ukladankaLayout.invalidate();
            return true;
        }
    }

}
