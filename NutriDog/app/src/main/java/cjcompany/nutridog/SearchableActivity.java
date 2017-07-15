package cjcompany.nutridog;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            ArrayList<String> al = findMatchingDogFood(query);
            toArrayAdapter(al);
        }
    }

    public ArrayList<String> findMatchingDogFood(String query){

        String dogFoodFile = "cleanDogFood.csv";

        //when searching, cut off first index, which corresponds to brand
        ArrayList<String> matchingResults = new ArrayList<String>();
        BufferedReader br = null;

        try{
            //I'd test this properly, but I've wasted 4 fucking days trying to get Android Studio's
            //stupid setup to work. So, if this breaks, I'll fix it the hard way.
            FileInputStream fis = openFileInput(dogFoodFile);
            fis.read();
            fis.close();



            br = new BufferedReader(new InputStreamReader(fis));

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
                str = br.readLine(); //moves to calories
                str = br.readLine(); //now back to food names
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

        return matchingResults;
    }

    public ArrayAdapter<String> toArrayAdapter(ArrayList<String> al){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,al);
        return adapter;
    }
}
