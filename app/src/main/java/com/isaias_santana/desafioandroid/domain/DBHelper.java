package com.isaias_santana.desafioandroid.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Isaías Santana on 30/05/17.
 *         email: isds.santana@gmail.com
 */

public class DBHelper extends SQLiteOpenHelper
{
    private static final String TAG = "sql";
    private static final String NOME_BANCO = "com.isaias_santana.desafioandroid";
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
                            "is_favorite INTEGER," +
                            "page        TEXT);";



    public DBHelper(Context context) {

        super(context,NOME_BANCO,null,VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d(TAG,"Criando o banco");
        db.execSQL(TABLE_PEOPLE);
        Log.d(TAG,"Tabela criada!");
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
            cv.put("page",people.getPage());

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
        SQLiteDatabase db =  getWritableDatabase();
        String whereArgs[] = new String[]{"1"};
        try
        {
            //SELECT * people WHERE is_favorite = 1
            Cursor c = db.query("people",null,"is_favorite=?",whereArgs,null,null,"name",null);
            return toList(c);
        }
        finally {
            db.close();
        }
    }

    public List<People> getAllPeoples(String next)
    {
        SQLiteDatabase db = getWritableDatabase();
        try
        {
            //SELECT * FROM people WHERE page="name" LIMIT 30
            Cursor c = db.query("people",null,"page=?",new String[]{next},null,null,null,null);
            return toList(c);
        }finally {
            db.close();
        }
    }

    public boolean isfFavorite(People p)
    {
        SQLiteDatabase db =  getWritableDatabase();
        String whereArgs[] = new String[]{p.getName(),"1"};
        try
        {

            //SELECT * people WHERE name = "um nome" && is_favorite = 1
            Cursor c = db.query("people",null,"name=? AND is_favorite=?",whereArgs,null,null,null,null);
            return c.getCount() > 0;
        }
        finally {
            db.close();
        }
    }

    public People exists(String name)
    {
        SQLiteDatabase db = getWritableDatabase();
        try
        {
            Cursor c = db.query("people",null,"name=?",new String[]{name},null,null,null,null);
            return (c.getCount() > 0) ? toList(c).get(0) : null;
        }
        finally {
            db.close();
        }
    }



    private List<People> toList(Cursor c)
    {
        List<People> peoples = new ArrayList<>();
        if(c.moveToFirst())
        {
            do
            {
                People people = new People();

                people.setId(c.getLong(c.getColumnIndex("_id")));
                people.setName(c.getString(c.getColumnIndex("name")));
                people.setHeight(c.getString(c.getColumnIndex("height")));
                people.setGender(c.getString(c.getColumnIndex("gender")));
                people.setMass(c.getString(c.getColumnIndex("mass")));
                people.setHairColor(c.getString(c.getColumnIndex("hairColor")));
                people.setSkinColor(c.getString(c.getColumnIndex("skinColor")));
                people.setEyeColor(c.getString(c.getColumnIndex("eyeColor")));
                people.setBirthYear(c.getString(c.getColumnIndex("birthYear")));
                people.setHomeWorld(c.getString(c.getColumnIndex("homeWorld")));
                people.setPage(c.getString(c.getColumnIndex("page")));

                ArrayList<String> species = new ArrayList<>();
                species.add(c.getString(c.getColumnIndex("species")));

                people.setSpecies(species);
                people.setIsFavorite(c.getInt(c.getColumnIndex("is_favorite")));

                peoples.add(people);
            }while(c.moveToNext());

            return peoples;
        }
        return null;
    }
}
