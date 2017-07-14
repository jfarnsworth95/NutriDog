package com.cjcompany.nutridog.tests;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import java.util.ArrayList;

import cjcompany.nutridog.SearchableActivity;

/**
 * To work on unit tests, go to Terminal and enter:
 * gradlew.bat connectedAndroidTest
 *
 * To check results, go to Build/reports/AndroidTest/connected/js/index.html
 * https://www.youtube.com/watch?v=z47B1nhC3K0
 *
 */
public class dogFoodSearchTests extends TestCase {

    @SmallTest
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @SmallTest
    public void search_withValidQuery() throws Exception{
        SearchableActivity sa = new SearchableActivity();
        ArrayList<String> al = sa.findMatchingDogFood("ENCORE");
        assertEquals(1,al.size());
    }

    @SmallTest
    public void search_RandomCapitalization() throws Exception{
        SearchableActivity sa = new SearchableActivity();
        ArrayList<String> al = sa.findMatchingDogFood("EnCOre");
        assertEquals(1,al.size());
    }

    @SmallTest
    public void search_withInvalidQuery() throws Exception{
        SearchableActivity sa = new SearchableActivity();
        ArrayList<String> al = sa.findMatchingDogFood("chiken");
        assertTrue(al.isEmpty());
    }

    @SmallTest
    public void search_withBrandQuery() throws Exception{
        SearchableActivity sa = new SearchableActivity();
        ArrayList<String> al = sa.findMatchingDogFood("Artemis");
        assertEquals(15,al.size());
    }

    @SmallTest
    public void search_partialQuery() throws Exception{
        SearchableActivity sa = new SearchableActivity();
        ArrayList<String> al = sa.findMatchingDogFood("chick");
        assertEquals(119,al.size());
    }
}
