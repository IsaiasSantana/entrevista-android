package com.isaias_santana.desafioandroid.mvp.presenter.mainActivity;


import android.content.Context;

import android.os.AsyncTask;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import com.isaias_santana.desafioandroid.domain.DBHelper;
import com.isaias_santana.desafioandroid.domain.People;
import com.isaias_santana.desafioandroid.mvp.model.mainActivity.MainActivityModel;
import com.isaias_santana.desafioandroid.mvp.view.MainActivityViewI;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Isaías Santana on 29/05/17.
 * email: isds.santana@gmail.com
 */

@InjectViewState
public class MainActivityPresenter extends MvpPresenter<MainActivityViewI>
        implements MainActivityPresenterI.PresenterToModel,
                   MainActivityPresenterI.PresenterToView
{
    private final MainActivityModel model;
    private DBHelper dbHelper;
    private boolean isInternetConnection;

    /*Para realizar paginação*/
    private String nextPage;

    /*Lista de personagens recuperados*/
    private List<People> peoples;

    /*Flag para indicar a view que está buscando por mais dados*/
    private boolean isLoading;

    public MainActivityPresenter()
    {
        model     = new MainActivityModel(this);
        nextPage  = "1"; // primeira página.
        peoples   = new ArrayList<>();
        isLoading = false;
    }

    /*** Métodos para o model ***/

    @Override
    public void setPeople(List<People> peoples, String page)
    {


        //Remove o progressBar na primeira busca.
        if(this.peoples.isEmpty())
            getViewState().hideProgressBar();

        //Quando o model retorna os dados. Salvo na lista this.peoples. Serve para manter os
        //elementos quando a Activity é recriada.
        if(nextPage != null)
        {
            String paginaAnterior = String.valueOf(getPage(nextPage));

            nextPage = page;

            for(People p : peoples)
            {
                p.setPage(paginaAnterior);
                this.peoples.add(p);
                saveDBInterno(p);
            }

            getViewState().updateRecyclerView(peoples);
            //Fim da leitura
            isLoading = false;
        }
        else
        {
            isLoading = true;
        }
    }

    public void setIsloading(boolean isLoading){
        this.isLoading = isLoading;
    }

    /*** Métodos para a View ***/

    @Override
    public void getPeoples()
    {
        Log.d("MainActivityPresenter","Chamado");
        //Se a lista está vazia, busca os primeiros personagens.
        if(peoples.isEmpty())
        {
            Log.d("MainActivityPresenter","Chamado lista vazia");
            getViewState().showProgressBar();
            model.getPeoples(getPage(nextPage));

        }
        else
        {
            Log.d("presenterMain","Lista não vazia");
            //Activity está sendo recriada. Então devolve a lista com os dados de antes.
          //  getViewState().updateRecyclerView(peoples);
        }
    }

    @Override
    public void getMoreData()
    {
        getViewState().internetConnection();
        if(isInternetConnection && !isLoading && nextPage != null)
        {
            Log.d("MainActivityPresenter","tem internet getMoreData");

          //  getViewState().addNullToAdapter();

            //Está buscando os dados. Isso proíbe tentar buscar mais personagens
            //antes que a busca atual retorne.
            isLoading = true;

            model.getPeoples(getPage(nextPage));
            return;
        }

        if(!isInternetConnection && nextPage != null && !isLoading)
        {
            getAllDataBaseLocal();
        }
    }

    public void setContext(Context context)
    {
        if(dbHelper == null)
            dbHelper = new DBHelper(context);
    }

    public void setInternetConnection(boolean internetConnection)
    {
        this.isInternetConnection = internetConnection;
    }

    @Override
    public List<People> getDatas() {
        return this.peoples;
    }

    public boolean isLoading(){
        return isLoading;
    }

    private int getPage(String page)
    {
        return Integer.parseInt(page.substring(page.length()-1,page.length()));
    }

    /**
     * Realiza uma consulta no banco procurando todos os favoritos do usário.
     */
    public void getFavorites()
    {
        new AsyncTask<Void, Void, List<People>>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                isLoading = true;
            }

            @Override
            protected List<People> doInBackground(Void... params)
            {
                List<People> peoples = dbHelper.favorites();
                if(peoples != null) return peoples;
                return null;
            }

            @Override
            protected void onPostExecute(List<People> peoples) {
                super.onPostExecute(peoples);
                if(peoples != null)
                {
                    getViewState().filter(peoples);
                }
            }
        }.execute();
    }

    /*Salva */
    private void saveDBInterno(final People p)
    {
        new AsyncTask<Void,Void,Void>()
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                People people;
                people = dbHelper.exists(p.getName());

                if( people != null) // personagem existe.
                {
                    Log.d("mainPresenter",people.getName()+" existe");
                    p.setId(people.getId());
                    p.setIsFavorite(people.getIsFavorite());
                    people.setHomeWorld(p.getHomeWorld());
                    people.setSpecies(p.getSpecies());
                    dbHelper.save(people);
                }
                else
                {
                    //personagem não existe, salva o personagem no banco.
                    Log.d("mainPresenter",p.getName()+" "+p.getIsFavorite()+" "+p.getPage()+" Não existe.");
                    long id = dbHelper.save(p);
                    p.setId(id);
                }
                return null;
            }
        }.execute();
    }

    /**
     * Busca pelos dados dos personagens no banco local.
     */
    private void getAllDataBaseLocal()
    {
        new AsyncTask<Void, Void, List<People>>()
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                isLoading = true;
                getViewState().addNullToAdapter();
            }

            @Override
            protected List<People> doInBackground(Void... params)
            {
                List<People> peoples = dbHelper.getAllPeoples(nextPage);
                if(peoples != null)
                    for(People p : peoples)
                    {
                        MainActivityPresenter.this.peoples.add(p);
                        nextPage = String.valueOf(Integer.valueOf(p.getPage())+1);
                    }
                return peoples;
            }

            @Override
            protected void onPostExecute(List<People> aVoid)
            {
                super.onPostExecute(aVoid);
                getViewState().removeNullFromAdapter();
            //    getViewState().hideProgressBar();
               if(aVoid != null) getViewState().updateRecyclerView(aVoid);
                isLoading = false;
            }
        }.execute();
    }
}