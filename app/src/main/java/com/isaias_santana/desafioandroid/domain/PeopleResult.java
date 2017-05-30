package com.isaias_santana.desafioandroid.domain;

import java.util.List;

/**
 * Created by isaias on 29/05/17.
 */

public class PeopleResult
{
    /**
     * Serve como um flag para paginação.
     */
    private String next;

    /**
     * Resultados da chamda a API do swapi
     */
    private List<People> results;

    public PeopleResult(){}

    public void setPeoples(List<People> results)
    {
        this.results = results;
    }

    public List<People> getPeoples()
    {
        return this.results;
    }

    public void setNext(String next)
    {
        this.next = next;
    }

    public String getNext(){
        return next;
    }
}
