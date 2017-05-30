package com.isaias_santana.desafioandroid.domain.interfaces;

import com.isaias_santana.desafioandroid.domain.Planet;
import com.isaias_santana.desafioandroid.domain.Specie;
import com.isaias_santana.desafioandroid.domain.PeopleResult;



import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author  Isaías Santana on 29/05/17.
 * email: isds.santana@gmail.com
 */

public interface PeopleAPI
{
    /**
     * Faz as buscas dos personagens.
     * @param page o id da página com os primeiros personagens.
     * @return um Call com ou null caso falhe.
     */
    @GET("people/")
    Call<PeopleResult> getPeoples(@Query("page") int page);

    @GET("species/{id}")
    Call<Specie> getEspecieName(@Path("id") String id);

    @GET("planets/{id}")
    Call<Planet> getPlanetName(@Path("id") String id);
}
