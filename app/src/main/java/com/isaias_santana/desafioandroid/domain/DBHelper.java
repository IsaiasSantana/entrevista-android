package com.isaias_santana.desafioandroid.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

/**
 * @author Isaías Santana on 30/05/17.
 *         email: isds.santana@gmail.com
 */

public class DBHelper extends SQLiteOpenHelper
{
    private static final String TAG = "sql";
    public static final String NOME_BANCO = "com.isaias_santana.desafioandroid";
    private static final int VERSAO_BANCO = 1;

    private static final String TABLE_PEOPLE =
                    "CREATE TABLE IF NOT EXISTS " +
                    "people(" +
                            "_id         INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "name        TEXT," +
                            "height      TEXT," +
                            "gender      TEXT," +
                            "mass        TEXT," +
                            "hairColor   TEXT," +
                            "skinColor   TEXT," +
                            "eyeColor    TEXT," +
                            "birthYear   TEXT," +
                            "homeWorld   TEXT," +
                            "species     TEXT," +
                            "is_favorite INTEGER);";



    public DBHelper(Context context) {

        super(context,NOME_BANCO,null,VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d(TAG,"Criando o banco");
        db.execSQL(TABLE_PEOPLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Insere uma nova pessoa no banco ou atualiza caso já exista.
     * @param people o objeto a ser inserido.
     */
    public long save(People people)
    {
        long id = people.getId();

        SQLiteDatabase db = getWritableDatabase();
        try
        {
            ContentValues cv = new ContentValues();
            cv.put("name", people.getName());
            cv.put("height", people.getHeight());
            cv.put("gender", people.getGender());
            cv.put("mass", people.getMass());
            cv.put("hairColor", people.getHairColor());
            cv.put("skinColor", people.getSkinColor());
            cv.put("eyeColor", people.getEyeColor());
            cv.put("birthYear", people.getBirthYear());
            cv.put("homeWorld", people.getHomeWorld());
            cv.put("species", people.getSpecies().get(0));
            cv.put("is_favorite", people.getIsFavorite());

            if (id != 0)
            {
                String _id = String.valueOf(people.getId());
                String[] clausulaWhere = new String[]{_id};
                int count = db.update("people",cv,"_id=?",clausulaWhere);
                return count;
            }
            else{
                // insert into people values...
                id = db.insert("people","",cv);
                return id;
            }
        }
        finally {
            db.close();
        }
    }

    /**
     * Busca pelos personagens favoritos dos usários.
     * @return uma lista contendo os personagens favoritos do usuários. Ou null caso não encontre.
     */
    public List<People> favorites()
    {
        return null;
    }
}
