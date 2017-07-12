package cjcompany.nutridog;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 *  Turing, why hast tho forsaken me?
 */
public class SearchableActivityTest {

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void search_withValidQuery() throws Exception{
        SearchableActivity sa = new SearchableActivity();
        ArrayList<String> al = sa.findMatchingDogFood("ENCORE");
        assertEquals(1,al.size());
    }

    @Test
    public void search_RandomCapitalization() throws Exception{
        SearchableActivity sa = new SearchableActivity();
        ArrayList<String> al = sa.findMatchingDogFood("EnCOre");
        assertEquals(1,al.size());
    }

    @Test
    public void search_withInvalidQuery() throws Exception{
        SearchableActivity sa = new SearchableActivity();
        ArrayList<String> al = sa.findMatchingDogFood("chiken");
        assertTrue(al.isEmpty());
    }

    @Test
    public void search_withBrandQuery() throws Exception{
        SearchableActivity sa = new SearchableActivity();
        ArrayList<String> al = sa.findMatchingDogFood("Artemis");
        assertEquals(15,al.size());
    }

    @Test
    public void search_partialQuery() throws Exception {
        SearchableActivity sa = new SearchableActivity();
        ArrayList<String> al = sa.findMatchingDogFood("chick");
        assertEquals(119, al.size());
    }
}