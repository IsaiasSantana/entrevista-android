package com.isaias_santana.desafioandroid.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.isaias_santana.desafioandroid.R;
import com.isaias_santana.desafioandroid.adapters.PeopleAdapter;
import com.isaias_santana.desafioandroid.domain.People;
import com.isaias_santana.desafioandroid.mvp.presenter.mainActivity.MainActivityPresenter;
import com.isaias_santana.desafioandroid.mvp.view.MainActivityViewI;
import com.isaias_santana.desafioandroid.util.DividerItemDecoration;
import com.isaias_santana.desafioandroid.util.ItemClickSupport;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  Isaías Santana on 29/05/17.
 * email: isds.santana@gmail.com
 */
public class MainActivity extends MvpAppCompatActivity
        implements MainActivityViewI, SearchView.OnQueryTextListener
{

    @InjectPresenter
    MainActivityPresenter presenter;
    private final PeopleAdapter peopleAdapter = new PeopleAdapter();
    private  ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_main_activity);
        initRecyclerView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        presenter.getPeoples();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;

        if (searchItem != null)
        {
            searchView = (SearchView) searchItem.getActionView();
        }

        if (searchView != null)
        {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(this);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void updateRecyclerView(List<People> peoples)
    {
        for(People p: peoples)
        {
            peopleAdapter.add(p);
        }
    }

    @Override
    public void showProgressBar()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar()
    {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void addNullToAdapter()
    {
        peopleAdapter.add(null);
    }

    @Override
    public void removeNullFromAdapter()
    {
        peopleAdapter.removeItem(peopleAdapter.getItemCount()-1);
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

            }
        });

        //Listener para o Scroll da lista

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisible = llm.findLastCompletelyVisibleItemPosition() + 1;

                if(recyclerView.getAdapter().getItemCount() == lastVisible && !presenter.isLoading())
                {
                    presenter.getMoreData();
                }
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        List<People> listaFiltrada = filter(presenter.getDatas(),newText);
        peopleAdapter.setFilter(listaFiltrada);
        return true;
    }

    private List<People> filter(List<People> models, String query)
    {
        query = query.toLowerCase();
        final List<People> filteredModelList = new ArrayList<>();
        final Collator instance = Collator.getInstance();
        instance.setStrength(Collator.NO_DECOMPOSITION);
        for (People model : models)
        {
            final String text = model.getName().toLowerCase();
           // int result = instance.compare(text.substring(0,query.length()),query);
            if (text.startsWith(query))
            {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}