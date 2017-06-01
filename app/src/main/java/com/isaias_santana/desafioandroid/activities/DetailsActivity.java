package com.isaias_santana.desafioandroid.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.isaias_santana.desafioandroid.R;
import com.isaias_santana.desafioandroid.domain.People;
import com.isaias_santana.desafioandroid.mvp.presenter.detailActivity.DetailActityPresenter;
import com.isaias_santana.desafioandroid.mvp.view.DetailActivityViewI;

/**
 * Created by isaias on 29/05/17.
 */
public class DetailsActivity extends MvpAppCompatActivity
                             implements DetailActivityViewI
{

    @InjectPresenter
    DetailActityPresenter presenter;

    private FloatingActionButton fab;
    private boolean isFavorite;
    private People people;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        people = getIntent().getParcelableExtra("people");
        presenter.setPeople(people);
        presenter.setContext(this);

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(people.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews(people);

        isFavorite = false;

        Log.d("people",people.getName());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
       if(!isFavorite) presenter.isFavorito();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)
    {
        outState.putBoolean("isFavorite",isFavorite);
        outState.putParcelable("people",people);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        isFavorite =  savedInstanceState.getBoolean("isFavorite");
        people     =  savedInstanceState.getParcelable("people");
    }

    private void initViews(People p)
    {
        //Campos de texto

        ((TextView) findViewById(R.id.tv_nome_personagem)).setText(p.getName());
        ((TextView) findViewById(R.id.tv_detalhes_planeta_natal)).setText(p.getHomeWorld());
        ((TextView) findViewById(R.id.tv_detalhes_especie)).setText(p.getSpecies().get(0));
        ((TextView) findViewById(R.id.tv_detalhes_nascimento)).setText(p.getBirthYear());
        ((TextView) findViewById(R.id.tv_detalhes_altura)).setText(p.getHeight()+" cm");
        ((TextView) findViewById(R.id.tv_detalhes_peso)).setText(p.getMass()+" Kg");
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
                presenter.favoritar();
            }
        });
    }

    private Context getContext(){
        return this;
    }

    @Override
    public void atualizarFAB(boolean isFavorite)
    {
        if(isFavorite)
        {
            int color = ContextCompat.getColor(getContext(),android.R.color.holo_orange_light);
            DrawableCompat.setTint(DrawableCompat.wrap(fab.getDrawable()), color);
            this.isFavorite = true;
        }
        else
        {
            int color = ContextCompat.getColor(getContext(),android.R.color.darker_gray);
            DrawableCompat.setTint(DrawableCompat.wrap(fab.getDrawable()), color);
            this.isFavorite = false;
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSneakBar(String message)
    {
        Snackbar.make(fab,message,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void sendBroadCast()
    {
        sendBroadcast(new Intent("FAVORITO").putExtra("people", (Parcelable) people));
    }
}
