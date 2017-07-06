package cjcompany.nutridog;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Jackson on 7/6/2017.
 */
public class SearchableActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            findMatchingDogFood(query);
        }
    }

    public ArrayAdapter<String> findMatchingDogFood(String query){
        //when searching, cut off first index, which corresponds to brand
        ArrayList<String> matchingResults = new ArrayList<String>();



        return null; //TODO change to actual adapter handle
    }
}
