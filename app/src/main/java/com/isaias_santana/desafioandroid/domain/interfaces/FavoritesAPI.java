package com.isaias_santana.desafioandroid.domain.interfaces;

import com.isaias_santana.desafioandroid.domain.Result;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author Isaías Santana on 31/05/17.
 *         email: isds.santana@gmail.com
 */

public interface FavoritesAPI
{
    String URL_BASE = "http://private-782d3-starwarsfavorites.apiary-mock.com/";

    @POST("favorite/{id}")
    Call<Result> favorite(@Path("id") String id);
}
