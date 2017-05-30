package com.isaias_santana.desafioandroid;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Isaias Santana on 29/05/2017
 * email: isds.santana@gmail.com
 */
public class MyTestsTest
{
    /**
     * Um simples caso de teste.
     * @throws Exception
     */
    @Test
    public void getPage() throws Exception
    {
        assertEquals(1, getPage("http://swapi.co/api/people/?page=1"));
        assertEquals(2,getPage("2"));
    }

    @Test
    public void getIdPlanetOrSpecie() throws Exception
    {
        assertEquals("9",getIdPlanetOrSpecie("http://swapi.co/api/planets/9/","planets"));
        assertEquals("1",getIdPlanetOrSpecie("http://swapi.co/api/planets/1/","planets"));
        assertEquals("129",getIdPlanetOrSpecie("http://swapi.co/api/species/129/","species"));
    }

    private int getPage(String page)
    {
        return Integer.parseInt(page.substring(page.length()-1,page.length()));
    }

    private String getIdPlanetOrSpecie(String url,String tag)
    {
       return url.substring(url.lastIndexOf(tag),url.length()).split("/")[1];

    }
}