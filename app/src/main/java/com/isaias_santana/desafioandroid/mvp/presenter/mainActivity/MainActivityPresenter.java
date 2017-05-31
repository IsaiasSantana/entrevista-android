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

    /*Para realizar paginação*/
    private String nextPage;

    /*Lista de pessoas recuperadas*/
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

        getViewState().removeNullFromAdapter(); //remove o progressBar do recyclerView.

        //Remove o progressBar na primeira busca.
        if(this.peoples.isEmpty())
            getViewState().hideProgressBar();

        //Quando o model retorna os dados. Salvo na lista this.peoples. Serve para manter os
        //elementos quando a Activity é recriada.
        if(nextPage != null)
        {
            Log.d("model","página anterior: "+nextPage);
            nextPage = page;
            Log.d("model","página atual: "+page);
            for(People p : peoples)
            {
                Log.d("model","personagem: "+p.getName());
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


    /*** Métodos para a View ***/

    @Override
    public void getPeoples()
    {

        //Se a lista está vazia, busca os primeiros personagens.
        if(peoples.isEmpty())
        {
            getViewState().showProgressBar();
            getMoreData();
        }
        else
        {
            Log.d("presenterMain","Lista não vazia");
            //Activity está sendo recriada. Então devolve a lista com os dados de antes.
            getViewState().updateRecyclerView(peoples);
        }
    }

    @Override
    public void getMoreData()
    {
        if(!isLoading && nextPage != null)
        {
            getViewState().addNullToAdapter();

            //Está buscando os dados. Isso proíbe tentar buscar mais personagens
            //antes que a busca atual retorne.
            isLoading = true;

            //view colocar progressbar no recyclerview
            model.getPeoples(getPage(nextPage));
        }

    }

    public void setContext(Context context)
    {
        if(dbHelper == null)
            dbHelper = new DBHelper(context);
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

    private void saveDBInterno(final People p)
    {
        new AsyncTask<Void,Void,Void>()
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                People people;
                people = dbHelper.exists(p.getName());

                if( people != null) // personagem existe. Seta o id e o campo de favorito.
                {
                    Log.d("mainPresenter",people.getName()+" "+people.getIsFavorite()+" "+people.getId()+" existe");
                    p.setId(people.getId());
                    p.setIsFavorite(people.getIsFavorite());
                }
                else
                {
                    //personagem não existe, salva o personagem no banco.
                    Log.d("mainPresenter",p.getName()+" "+p.getIsFavorite()+" "+p.getId()+" Não existe.");
                    long id = dbHelper.save(p);
                    p.setId(id);
                }
                return null;
            }
        }.execute();
    }

}
