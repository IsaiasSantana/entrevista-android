package com.isaias_santana.desafioandroid.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.isaias_santana.desafioandroid.R;
import com.isaias_santana.desafioandroid.domain.People;

/**
 * Created by isaias on 29/05/17.
 */
public class DetailsActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        People p = getIntent().getParcelableExtra("people");

        getSupportActionBar().setTitle(p.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews(p);

        isFavorite = false;

        Log.d("people",p.getName());
    }

    private void initViews(People p)
    {
        //Campos de texto

        ((TextView) findViewById(R.id.tv_nome_personagem)).setText(p.getName());
        ((TextView) findViewById(R.id.tv_detalhes_planeta_natal)).setText(p.getHomeWorld());
        ((TextView) findViewById(R.id.tv_detalhes_especie)).setText(p.getSpecies().get(0));
        ((TextView) findViewById(R.id.tv_detalhes_nascimento)).setText(p.getBirthYear());
        ((TextView) findViewById(R.id.tv_detalhes_altura)).setText(p.getHeight());
        ((TextView) findViewById(R.id.tv_detalhes_peso)).setText(p.getMass());
        ((TextView) findViewById(R.id.tv_detalhes_genero)).setText(p.getGender());
        ((TextView) findViewById(R.id.tv_detalhes_cabelo)).setText(p.getHairColor());
        ((TextView) findViewById(R.id.tv_detalhes_cor_pele)).setText(p.getSkinColor());
        ((TextView) findViewById(R.id.tv_detalhes_olhos)).setText(p.getEyeColor());

        //Floating Action Button
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isFavorite)
                {
                    int color = ContextCompat.getColor(getContext(),android.R.color.holo_orange_light);
                    DrawableCompat.setTint(DrawableCompat.wrap(fab.getDrawable()), color);
                    isFavorite = true;
                }
                else{
                    int color = ContextCompat.getColor(getContext(),android.R.color.darker_gray);

                    DrawableCompat.setTint(DrawableCompat.wrap(fab.getDrawable()), color);
                    isFavorite = false;
                }
            }
        });

    }

    private Context getContext(){
        return this;
    }
}
