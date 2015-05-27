package de.duckout.projektpuzzle;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Marta on 2015-05-12.
 */

public class AdapterObrazki extends ArrayAdapter<Integer> {

    private ArrayList<Integer> listaObrazkow;
    private Activity activity;

    public AdapterObrazki(Activity activity, ArrayList<Integer> lista) {
        super(activity, R.layout.obrazek, lista);
        this.activity = activity;
        listaObrazkow = lista;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater polacz = activity.getLayoutInflater();            ///przez niego dostaje sie do swojego obiektu
        View widok = polacz.inflate(R.layout.obrazek, null, true);
        final ImageView imageView = (ImageView) widok.findViewById(R.id.imageView);

        imageView.setImageResource(listaObrazkow.get(position));

        return widok;
    }


}

