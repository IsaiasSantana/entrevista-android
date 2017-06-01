package com.isaias_santana.desafioandroid.mvp.presenter.favoritosActivity;

import android.content.Context;
import android.os.AsyncTask;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.isaias_santana.desafioandroid.R;
import com.isaias_santana.desafioandroid.domain.DBHelper;
import com.isaias_santana.desafioandroid.domain.People;
import com.isaias_santana.desafioandroid.mvp.view.FavoritosActivityViewI;


import java.util.List;

/**
 * @author Isaías Santana on 31/05/17.
 *         email: isds.santana@gmail.com
 */

@InjectViewState
public class FavoritosActivityPresenter extends MvpPresenter<FavoritosActivityViewI>
                                        implements FavoritosActivityI
{

    private DBHelper dbHelper;

    @Override
    public void buscarFavoritos()
    {
        serchAllFavorites();
    }

    @Override
    public void setContext(Context context)
    {
        if(dbHelper == null)
            dbHelper = new DBHelper(context);
        else{
            dbHelper = null;
            dbHelper = new DBHelper(context);
        }
    }

    private void serchAllFavorites()
    {
        new AsyncTask<Void, Void, List<People>>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getViewState().esconderMensagemSemFavoritos();
                getViewState().showProgressDialog(R.string.aguarde);
            }

            @Override
            protected List<People> doInBackground(Void... params)
            {
                List<People>  peoples = dbHelper.favorites();
                if(peoples != null)
                {
                    return peoples;
                }
                else{
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<People> aVoid)
            {
                super.onPostExecute(aVoid);
                getViewState().hideProgressDialog();
                if(aVoid != null)
                {
                    getViewState().updateRecyclerView(aVoid);
                }
                else getViewState().showMensagemSemFavoritos();
            }
        }.execute();
    }
}
