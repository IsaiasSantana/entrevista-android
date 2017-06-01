package com.isaias_santana.desafioandroid.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.isaias_santana.desafioandroid.R;
import com.isaias_santana.desafioandroid.adapters.PeopleAdapter;
import com.isaias_santana.desafioandroid.domain.People;
import com.isaias_santana.desafioandroid.mvp.presenter.favoritosActivity.FavoritosActivityPresenter;
import com.isaias_santana.desafioandroid.mvp.view.FavoritosActivityViewI;
import com.isaias_santana.desafioandroid.util.DividerItemDecoration;
import com.isaias_santana.desafioandroid.util.ItemClickSupport;

import java.util.List;

public class FavoritosActivity extends MvpAppCompatActivity implements FavoritosActivityViewI
{
    private final PeopleAdapter peopleAdapter = new PeopleAdapter();
    @InjectPresenter
    FavoritosActivityPresenter presenter;
    private TextView semFavoritos;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initRecyclerView();
        presenter.setContext(this);

        semFavoritos = (TextView) findViewById(R.id.id_favoritos_vazio);
    }


    @Override
    public void showProgressDialog(int message)
    {
        if(progressDialog == null)
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getResources().getString(message));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog()
    {
        if(progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    private void initRecyclerView()
    {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(peopleAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        //Eventos de click
        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v)
                    {
                        Intent intent = new Intent(FavoritosActivity.this,DetailsActivity.class);

                        People p =  peopleAdapter.getPeople(position);
                        intent.putExtra("people",(Parcelable) p);
                        startActivity(intent);
                    }
                });

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        presenter.buscarFavoritos();

    }

    @Override
    public void showMensagemSemFavoritos()
    {
        semFavoritos.setVisibility(View.VISIBLE);
    }

    @Override
    public void esconderMensagemSemFavoritos()
    {
        semFavoritos.setVisibility(View.GONE);
    }

    @Override
    public void updateRecyclerView(List<People> peoples)
    {
        for(People p : peoples)
        {
            peopleAdapter.add(p);
        }
    }
}
