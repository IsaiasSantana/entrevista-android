package com.isaias_santana.desafioandroid.mvp.presenter.detailActivity;

import android.content.Context;

import com.isaias_santana.desafioandroid.domain.People;

/**
 * @author Isaías Santana on 30/05/1public7.
 *         email: isds.santana@gmail.com
 */

interface DetailActivityPresenterI
{
    /**
     * Métodos que o presenter disponibiliza para a view
     */
    interface PresenterToView {
        /**
         * O context da Activity atrelada com este Presenter
         *
         * @param context
         */
        void setContext(Context context);


        /**
         * Adiciona o personagem na lista de favoritos do usuário.
         * Ou o remove caso esteja marcado como favorito.
         */
        void favoritar();

        /**
         * Verifica se um dado personagem é favorito do usuário.
         * Caso seja ou não deve informar a view para atualizar o
         * FloatActionButton como favorito ou não.
         */
        void isFavorito();

        /**
         * DetailsActivity passa para o presenter o objeto que está sendo visualizado na tela de
         * detalhes
         * @param p O objeto sendo mostrado na tela de detalhes.
         */
        void setPeople(People p);
    }

}
