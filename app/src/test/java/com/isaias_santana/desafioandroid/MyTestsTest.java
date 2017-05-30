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

    private int getPage(String page)
    {
        return Integer.parseInt(page.substring(page.length()-1,page.length()));
    }
}