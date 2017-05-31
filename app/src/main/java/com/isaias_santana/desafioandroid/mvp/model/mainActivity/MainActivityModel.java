package com.isaias_santana.desafioandroid.mvp.model.mainActivity;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;


import com.isaias_santana.desafioandroid.domain.DBHelper;
import com.isaias_santana.desafioandroid.domain.People;
import com.isaias_santana.desafioandroid.domain.PeopleResult;
import com.isaias_santana.desafioandroid.domain.Planet;
import com.isaias_santana.desafioandroid.domain.Specie;
import com.isaias_santana.desafioandroid.domain.interfaces.PeopleAPI;
import com.isaias_santana.desafioandroid.mvp.presenter.mainActivity.MainActivityPresenterI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author  Isaías Santana on 29/05/17.
 * email: isds.santana@gmail.com
 */

public class MainActivityModel implements MainActivityModelI
{
    private static final String BASE_URL = "http://swapi.co/api/";
    private final MainActivityPresenterI.PresenterToModel presenter;
    private final PeopleAPI peopleAPI;
    private final Handler handler;


    public MainActivityModel(MainActivityPresenterI.PresenterToModel presenter)
    {
        this.presenter = presenter;
        peopleAPI = buildRetrofit().create(PeopleAPI.class);
        handler = new Handler();
    }

    @Override
    public void getPeoples(final int page)
    {

        handler.post(new Runnable() { //Para evitar erros durante a paginação.
            @Override
            public void run()
            {
                Call<PeopleResult> call =  peopleAPI.getPeoples(page);

                call.enqueue(new Callback<PeopleResult>() {
                    @Override
                    public void onResponse(@NonNull Call<PeopleResult> call, @NonNull Response<PeopleResult> response) {
                        if (response.isSuccessful())
                        {
                            PeopleResult peopleResult = response.body();

                            assert peopleResult != null;
                            List<People> peoples = peopleResult.getPeoples();

                            if(peoples != null && peoples.get(0)!= null)
                            {
                                Log.d("model","Sucesso!");
                                presenter.setPeople(peoples,peopleResult.getNext());
                                getHomeWorld(peoples);
                                getSpecie(peoples);
                            }
                            else presenter.setPeople(null,null);

                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<PeopleResult> call, @NonNull Throwable t)
                    {
                        t.printStackTrace();
                        presenter.setPeople(null,null);
                    }
                }); // Fim do Callback<PeopleResult>().
            }
        }); //fim Handler e Runnable;
    }

    private Retrofit buildRetrofit()
    {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void getHomeWorld(final List<People> peoples)
    {
        for(final People p : peoples)
        {
            if(p.getHomeWorld() == null || p.getHomeWorld().isEmpty())
            {
                p.setHomeWorld("Desconhecido");
            }
            else
            {
                Call<Planet> call = peopleAPI
                        .getPlanetName(getIdPlanetOrSpecie(p.getHomeWorld(),"planets"));
                call.enqueue(new Callback<Planet>() {
                    @Override
                    public void onResponse(Call<Planet> call, Response<Planet> response)
                    {
                        if(response.isSuccessful())
                        {
                            p.setHomeWorld(response.body().getName());
                        }
                    }

                    @Override
                    public void onFailure(Call<Planet> call, Throwable t) {

                    }
                });
            }
        }

    }

    private void getSpecie(final List<People> peoples)
    {
        for(final People p : peoples)
        {
            if(p.getSpecies().isEmpty())
            {
                ArrayList<String> species = new ArrayList<String>();
                species.add("Não especificado.");
                p.setSpecies(species);
            }
            else
            {
                Call<Specie> call = peopleAPI
                        .getEspecieName(getIdPlanetOrSpecie(p.getSpecies().get(0),"species"));
                call.enqueue(new Callback<Specie>() {
                    @Override
                    public void onResponse(Call<Specie> call, Response<Specie> response)
                    {
                        if(response.isSuccessful())
                        {
                            ArrayList<String> species = new ArrayList<String>();
                            species.add(response.body().getName());
                            p.setSpecies(species);
                        }
                    }

                    @Override
                    public void onFailure(Call<Specie> call, Throwable t) {

                    }
                });
            }
        }

    }

    private String getIdPlanetOrSpecie(String url,String tag)
    {
        return url.substring(url.lastIndexOf(tag),url.length()).split("/")[1];
    }
}
