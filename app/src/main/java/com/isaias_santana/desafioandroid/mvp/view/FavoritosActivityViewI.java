package com.isaias_santana.desafioandroid.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.isaias_santana.desafioandroid.domain.People;

import java.util.List;

/**
 * @author Isaías Santana on 31/05/17.
 *         email: isds.santana@gmail.com
 */

public interface FavoritosActivityViewI extends MvpView
{
    void updateRecyclerView(List<People> peoples);
    void showMensagemSemFavoritos();
    void esconderMensagemSemFavoritos();
    void showProgressDialog(int message);
    void hideProgressDialog();
}
