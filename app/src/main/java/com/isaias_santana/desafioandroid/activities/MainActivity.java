package com.isaias_santana.desafioandroid.activities;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.isaias_santana.desafioandroid.R;
import com.isaias_santana.desafioandroid.adapters.PeopleAdapter;
import com.isaias_santana.desafioandroid.domain.People;
import com.isaias_santana.desafioandroid.fragments.DialogExitApp;
import com.isaias_santana.desafioandroid.mvp.presenter.mainActivity.MainActivityPresenter;
import com.isaias_santana.desafioandroid.mvp.view.MainActivityViewI;
import com.isaias_santana.desafioandroid.util.DividerItemDecoration;
import com.isaias_santana.desafioandroid.util.ItemClickSupport;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  Isaías Santana on 29/05/17.
 * email: isds.santana@gmail.com
 */
public class MainActivity extends MvpAppCompatActivity
        implements MainActivityViewI, SearchView.OnQueryTextListener,
        DialogExitApp.NoticeDialogListener
{

    @InjectPresenter
    MainActivityPresenter presenter;
    private  PeopleAdapter peopleAdapter;
    private  ProgressBar progressBar;
    private final String LOG = "MainActivty";
    private boolean isPause;
    private boolean listagemFavoritos;
    private BroadcastReceiver receiver;
    private Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_main_activity);
        peopleAdapter = new PeopleAdapter();
        initRecyclerView();

        isPause = false;
        presenter.setContext(this);

        initDrawer(toolbar,savedInstanceState);
        initReceiver();
        registerReceiver(receiver, new IntentFilter("FAVORITO"));
        Log.d(LOG,"onCreate() chamado");
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Log.d(LOG,"onStart() chamado");
    }

    @Override
    protected void onPause() {
        super.onPause();
        setPause(true);
        Log.d(LOG,"OnPause() chamado");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
       if(!isPause()) presenter.getPeoples();
        else
            setPause(true);
        Log.d(LOG,"onResume() chamado");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d(LOG,"onStop() chamado");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_favorite:
                if(!item.isChecked())
                {
                    item.setChecked(true);
                    presenter.getFavorites();
                    listagemFavoritos = true;
                }else{
                    peopleAdapter.setFilter(presenter.getDatas());
                    item.setChecked(false);
                    presenter.setIsloading(false);
                    listagemFavoritos = false;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void clearDatasAdapater()
    {
        peopleAdapter.clear();
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
                Intent intent = new Intent(MainActivity.this,DetailsActivity.class);

                People p =  peopleAdapter.getPeople(position);
                intent.putExtra("people",(Parcelable) p);
                startActivity(intent);
            }
        });

        recyclerView.addOnScrollListener(initScrollListener());
    }

    /************ Listener de texto ************/

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

    /**********************************************/

    @Override
    public void internetConnection()
    {
        presenter.setInternetConnection(isNetworkAvailable());
    }

    /**
     * Substitui os dados do adapter por outros
     * @param peoples a nova lista do adapter.
     */
    public void filter(List<People> peoples)
    {
        peopleAdapter.setFilter(peoples);
    }


    /*Filtro para busca pelo nome*/
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
            if (text.startsWith(query) || text.contains(query))
            {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    /**
     * Adiciona o listener de scroll para o recycler view.
     */
    private RecyclerView.OnScrollListener initScrollListener()
    {
        return  new RecyclerView.OnScrollListener()
        {
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
                    Log.d(LOG,"onScrolled() chamado");

                    presenter.getMoreData();
                }
            }
        };
    }

    private void setPause(boolean isAttch){
        this.isPause = isAttch;
    }

    private boolean isPause(){
        return isPause;
    }

    //Quando o usuários desfavorita um personagem esse receiver intercepta a mensagem
    // e dispara uma thread para procurar o objeto e remové-lo da lista.
    private void initReceiver()
    {
        receiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {

                People p = intent.getParcelableExtra("people");
                Log.d("Receive",p.getName());
               if(listagemFavoritos)
                   removeFavoritoLista(p);
            }
        };
    }

    /**
     * Esse método é disparado pelo receiver dinâmico desta classe.
     * Ele é chamdo quando o usuário na tela principal tira dos seus favoritos um personagem
     * O método recebe o objeto e o remove da lista de favoritos.
     * @param p O objeto escolhido pelo usuário.
     */
    private void removeFavoritoLista(final People p)
    {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < peopleAdapter.getItemCount(); i++)
                {
                    People people = peopleAdapter.getPeople(i);
                    if(p.getName().equals(people.getName()))
                    {
                        peopleAdapter.removeItem(i);
                        break;
                    }
                }
            }
        });
    }

    /**
     * Verifica se há conexão com a internet.
     * @return true se possui.
     */
    private boolean isNetworkAvailable()
    {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /*
    * Inicializa o menu lateral.
    * */
    private void initDrawer(Toolbar toolbar,Bundle savedInstanceState)
    {

        // Cria o AccountHeader
       AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.primary)
                .addProfiles( new ProfileDrawerItem()
                        .withName("Darth Vader")
                        .withEmail("darthvader@aforca.com.br")
                        .withIcon(ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.darth)).withIdentifier(1))
                .withSavedInstance(savedInstanceState)
                .build();

        // Itens que estarão no drawer

        PrimaryDrawerItem item2 = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName(R.string.favoritos)
                .withIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_star_outline));

        SecondaryDrawerItem item3 = new SecondaryDrawerItem()
                .withIdentifier(3)
                .withName(R.string.exit_app)
                .withIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_logout));

        //Cria o drawer
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(accountHeader)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withTranslucentStatusBar(false)
                .addDrawerItems(
                        item2,
                        new DividerDrawerItem(),
                        item3
                )
                .withSelectedItem(-1)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        switch ((int)drawerItem.getIdentifier())
                        {

                            case 2:
                                closeDrawer();
                                startActivity(new Intent(MainActivity.this,FavoritosActivity.class));
                                return true;

                            case 3:
                                DialogExitApp dialogExitApp = new DialogExitApp();
                                dialogExitApp.show(getSupportFragmentManager(),"exitApp");
                                return true;
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

    }

    private void closeDrawer(){
        if(drawer != null && drawer.isDrawerOpen())
        {
            drawer.closeDrawer();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment) {
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialogFragment) {

    }
}
