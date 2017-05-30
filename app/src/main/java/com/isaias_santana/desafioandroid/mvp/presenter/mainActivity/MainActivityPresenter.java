package com.isaias_santana.desafioandroid.mvp.presenter.mainActivity;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
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
        if(page != null)
        {
            Log.d("model","página anterior: "+getPage(page));
            nextPage = page;
            Log.d("model","página atual: "+getPage(page));
            for(People p : peoples)
            {
                this.peoples.add(p);
            }

            getViewState().updateRecyclerView(peoples);
            //Fim da leitura
            isLoading = false;
        }
        else
        {
            nextPage = null;
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

}
