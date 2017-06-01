package com.isaias_santana.desafioandroid.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.isaias_santana.desafioandroid.domain.People;

import java.util.List;

/**
 * Created by isaias on 29/05/17.
 * Esta interface contém os métodos que o presenter pode acessar.
 */

public interface MainActivityViewI extends MvpView
{
    void updateRecyclerView(List<People> peoples);
    void showProgressBar();
    void hideProgressBar();
    void addNullToAdapter();
    void removeNullFromAdapter();
    void clearDatasAdapater();
    void internetConnection();
    void filter(List<People> peoples);
}
