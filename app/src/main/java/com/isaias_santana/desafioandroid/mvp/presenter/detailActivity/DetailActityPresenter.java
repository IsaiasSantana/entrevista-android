package com.isaias_santana.desafioandroid.mvp.presenter.detailActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.isaias_santana.desafioandroid.domain.DBHelper;
import com.isaias_santana.desafioandroid.domain.People;
import com.isaias_santana.desafioandroid.domain.Result;
import com.isaias_santana.desafioandroid.domain.interfaces.FavoritesAPI;
import com.isaias_santana.desafioandroid.mvp.view.DetailActivityViewI;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private Context context;

    public DetailActityPresenter()
    {
        handler = new Handler();
    }

    @Override
    public void setContext(Context context)
    {
        this.context = context;
        if(dbHelper == null)
            dbHelper = new DBHelper(context);
        else {
            dbHelper = null;
            dbHelper = new DBHelper(context);
        }

        //Checa se ficou algum personagem sem favoritar em starwarsfavorites por falta de conexão.
        SharedPreferences prefs = context.getSharedPreferences("local",0);
        long id = prefs.getLong(people.getName(),-1);
        if(id != -1 && isNetworkAvailable())
        {
            sendAPIFavorite(String.valueOf(id));
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(people.getName());
            editor.apply();
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
                        getViewState().showSneakBar("Adicionado aos favoritos!");
                        if(isNetworkAvailable())
                            sendAPIFavorite(String.valueOf(p.getId()));
                        else
                        {
                            SharedPreferences sharedPreferences = context.getSharedPreferences("local",0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putLong(p.getName(),p.getId());
                            editor.apply();
                        }
                    }
                    else // deixou de ser favorito.
                    {
                        p.setIsFavorite(0);
                        dbHelper.save(p);
                        Log.d("detail",people.getIsFavorite()+" deixou de ser favorito");
                        getViewState().atualizarFAB(false);
                        getViewState().showSneakBar("Removido dos favoritos!");
                        getViewState().sendBroadCast();
                    }
                }
            }
        });
    }

    private void checkDBHelper()
    {
        final String error = "Banco local não inicializado. " +
                "Chame presenter.setContext(Context) no onCreate().";
        if(dbHelper == null)
            throw new NullPointerException(error);
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void sendAPIFavorite(final String id)
    {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
                          @Override
                          public Response intercept(@NonNull Interceptor.Chain chain) throws IOException
                          {
                              Request original = chain.request();

                              Request request = original.newBuilder()
                                      .method(original.method(), original.body())
                                      .build();

                              return chain.proceed(request);
                          }
                      });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FavoritesAPI.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        FavoritesAPI favoritesAPI = retrofit.create(FavoritesAPI.class);
        Call<Result> call = favoritesAPI.favorite(id);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull retrofit2.Response<Result> response)
            {
                if(response.code() == 201)
                {
                    getViewState().showToast(response.body().getMessage());
                }

                if(response.code() == 400)
                {

                    getViewState().showToast(response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {

            }
        });
    }
}
