package cjcompany.nutridog;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

        String dogFoodFile = "cleanDogFood.csv";

        //when searching, cut off first index, which corresponds to brand
        ArrayList<String> matchingResults = new ArrayList<String>();
        BufferedReader br = null;

        try{
            FileReader fr = new FileReader("/NutriDog/app/src/main/assets/" + dogFoodFile);
            br = new BufferedReader(fr);

            String str = br.readLine();
            while (str != null){

                //split up file line being read
                String[] theSplit = str.split(",");

                String brandPrefix = theSplit[0] + " - ";
                //if the first index contains the query, return the entire line
                if (theSplit[0].contains(query)){
                    for(int i = 1; i < theSplit.length; i ++){
                        matchingResults.add(brandPrefix + theSplit[i]);
                    }
                //if element in list from file line contains query, add to arraylist
                }else{
                    for(int e = 1; e < theSplit.length; e ++){
                        if(theSplit[e].contains(query)){
                            matchingResults.add(brandPrefix + theSplit[e]);
                        }
                    }
                }
            }
        }
        catch (FileNotFoundException ex){
            System.err.println("DogFood.csv is not at expected location");
        }
        catch (IOException ex1){
            System.err.println("Error while reading file");
        }
        finally{
            try{
                if (br != null){
                    br.close();
                }
            }
            catch(Exception ex2){
                ex2.printStackTrace();
                System.exit(-1);
            }
        }

        //move arraylist to arrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,matchingResults);
        //return arrayAdapter
        return adapter;
    }
}
