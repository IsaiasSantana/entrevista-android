package com.isaias_santana.desafioandroid.mvp.presenter.detailActivity;

import android.content.Context;
//import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.isaias_santana.desafioandroid.domain.DBHelper;
import com.isaias_santana.desafioandroid.domain.People;
import com.isaias_santana.desafioandroid.mvp.view.DetailActivityViewI;

/**
 * @author Isaías Santana on 30/05/17.
 *         email: isds.santana@gmail.com
 */

@InjectViewState
public class DetailActityPresenter extends MvpPresenter<DetailActivityViewI>
                                   implements DetailActivityPresenterI.PresenterToView
{

    private DBHelper dbHelper;
    private People people;
    private Handler handler;

    public DetailActityPresenter(){
        handler = new Handler();
    }
    @Override
    public void setContext(Context context)
    {
        if(dbHelper == null)
            dbHelper = new DBHelper(context);
        else {
            dbHelper = null;
            dbHelper = new DBHelper(context);
        }
    }

    @Override
    public void favoritar()
    {
        addToFavoriteOrRemove();
    }

    @Override
    public void isFavorito()
    {
        checkDBHelper();
        handler.post(new Runnable() {
            @Override
            public void run()
            {
                if(dbHelper.isfFavorite(people)){
                    Log.d("detail",people.getName()+" é favorito!");
                    getViewState().atualizarFAB(true);
                }
                else{
                    Log.d("detail",people.getName()+" Não é favorito!");
                    getViewState().atualizarFAB(false);
                }
            }
        });
//        new AsyncTask<Void, Void, Boolean>()
//        {
//            @Override
//            protected Boolean doInBackground(Void... params)
//            {
//                if(dbHelper.isfFavorite(people))
//                {
//                    Log.d("detail",people.getName()+" é favorito!");
//                    return true;
//                }
//                else
//                {
//                    Log.d("detail",people.getName()+" Não é favorito!");
//                    return false;
//                }
//            }
//
//            @Override
//            protected void onPostExecute(Boolean aBoolean) {
//                super.onPostExecute(aBoolean);
//                if(aBoolean)
//                {
//                    getViewState().atualizarFAB(true);
//                }
//                else{
//                    getViewState().atualizarFAB(false);
//                }
//            }
//        }.execute();
    }

    @Override
    public void setPeople(People p) {
        this.people = p;
    }

    private void addToFavoriteOrRemove()
    {
        checkDBHelper();
        handler.post(new Runnable() {
            @Override
            public void run()
            {
                People p;
                if((p = dbHelper.exists(people.getName()) ) != null)
                {
                    Log.d("detail","isFavorite == "+p.getIsFavorite());
                    if(p.getIsFavorite() == 0) //Ainda não é favorito.
                    {
                        p.setIsFavorite(1);
                        dbHelper.save(p);
                        Log.d("detail",people.getIsFavorite()+" Agora é favorito");
                        getViewState().atualizarFAB(true);
                    }
                    else // deixou de ser favorito.
                    {
                        p.setIsFavorite(0);
                        dbHelper.save(p);
                        Log.d("detail",people.getIsFavorite()+" deixou de ser favorito");
                        getViewState().atualizarFAB(false);
                    }
                }
            }
        });
//        new AsyncTask<Void, Void, Boolean>() {
//            @Override
//            protected Boolean doInBackground(Void... params)
//            {
//                if(!dbHelper.isfFavorite(people))
//                {
//                    people.setIsFavorite(1);
//                    Log.d("detail",people.getIsFavorite()+" Agora é favorito");
//                    dbHelper.save(people);
//                    return true;
//                }
//                else
//                {
//                    Log.d("detail",people.getIsFavorite()+" Não é mais favorito");
//                    return false;
//                }
//
//            }
//
//            @Override
//            protected void onPostExecute(Boolean aBoolean)
//            {
//                super.onPostExecute(aBoolean);
//                Log.d("detail",aBoolean.toString());
//
//                if (aBoolean)
//                {
//                    getViewState().atualizarFAB(true);
//                }
//                else {
//                    getViewState().atualizarFAB(false);
//                }
//            }
//        }.execute();
    }

    private void checkDBHelper()
    {
        final String error = "Banco local não inicializado. " +
                "Chame presenter.setContext(Context) no onCreate().";
        if(dbHelper == null)
            throw new NullPointerException(error);
    }
}
