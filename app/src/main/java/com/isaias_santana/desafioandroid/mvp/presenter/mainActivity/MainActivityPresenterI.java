package com.isaias_santana.desafioandroid.mvp.presenter.mainActivity;

import com.isaias_santana.desafioandroid.domain.People;

import java.util.List;

/**
 * Created by isaias on 29/05/17.
 */

public interface MainActivityPresenterI
{
    /**
     * Interface com os métodos disponíveis para o model
     */
    interface PresenterToModel
    {
        void setPeople(List<People> peoples,String page);
    }

    /**
     * Interface com os métodos disponíveis para a View.
     */
    interface PresenterToView
    {
        void getPeoples();
        void getMoreData();
        List<People> getDatas();
    }
}
