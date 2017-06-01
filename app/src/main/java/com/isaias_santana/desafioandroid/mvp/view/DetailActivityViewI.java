package com.isaias_santana.desafioandroid.mvp.view;

import com.arellomobile.mvp.MvpView;

/**
 * @author Isaías Santana on 30/05/17.
 *         email: isds.santana@gmail.com
 */

public interface DetailActivityViewI extends MvpView
{
    /**
     * Atualiza a cor do FloatActionButton.
     * @param isFavorite um flag booleano. true significa que um personagem é favorito do
     *                   usuário. Um false, o oposto.
     */
    void atualizarFAB(boolean isFavorite);

    /**
     * Exibe uma mensagem de Toast
     * @param message a mensagem a ser exibida.
     */
    void showToast(String message);

    /**
     * Exibe uma mensagem usando o Sneack
     * @param message a mensagem a ser exibida.
     */
    void showSneakBar(String message);

    /**
     * Envia uma mensagem de Broadcast carregando uma
     * Intent com o objeto que o usuário tira do seus favoritos.
     */
    void sendBroadCast();
}
