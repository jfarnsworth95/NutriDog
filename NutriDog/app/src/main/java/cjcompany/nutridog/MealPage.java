package cjcompany.nutridog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Jackson on 6/29/2017.
 */
public class MealPage extends AppCompatActivity {

    String meal;
    String date;
    String petName;
    String petID;

    private ListView lv;
    private EditText et;
    ArrayAdapter<String> adapter;


    //TODO make sure info is saved when device moves away from app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //should be passed string var of "Breakfast", "Lunch", or "Dinner"
        Intent intent = getIntent();
        meal = intent.getStringExtra(PetInfoPage.MEAL);
        date = intent.getStringExtra(PetInfoPage.DATE);
        petID = intent.getStringExtra(PetInfoPage.PET_ID);
        petName = intent.getStringExtra(PetInfoPage.PET_NAME);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        lv = (ListView)findViewById(R.id.listView);
        et = (EditText)findViewById(R.id.search_bar);
        adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.food_name_item);
        lv.setAdapter(adapter);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().equals("")){
                    adapter.clear();
                }else {
                    SearchableActivity sa = new SearchableActivity();
                    String query = s.toString();
                    ArrayList<String> arrayList = sa.findMatchingDogFood(query);
                    adapter = sa.toArrayAdapter(arrayList);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //doesnt need anything
            }

            @Override
            public void afterTextChanged(Editable s) {
                //doesnt need anything
            }
        });
    }

    // get data
    public void getOnSubmit(View view){
        EditText foodNameField = (EditText)findViewById(R.id.search_bar);
        String foodName = foodNameField.getText().toString();

        EditText calorieField = (EditText)findViewById(R.id.mealCalories);
        String calories = calorieField.getText().toString();
        modifyFile(foodName,calories);
    }

    //modify file for pet data
    public void modifyFile(String foodName, String Calories){
        String[] splitDate = date.split(",");
        String day = splitDate[0];
        String month = splitDate[1];
        String year = splitDate[2];

        BufferedReader br = null;
        try{
            FileInputStream fis = openFileInput(petName + "_data_" + petID + ".csv");
            br = new BufferedReader(new InputStreamReader(fis));

            String str = br.readLine();
            if(str.equals("")){
                System.err.println("Shits fucked brah");
            }
            while (str != null){
                //TODO search for date
                String[] fileDateSplit = str.split(",");
                if(fileDateSplit[0].equals(day) && fileDateSplit[1].equals(month) && fileDateSplit[2].equals(year)){
                    //TODO currently in desired date, modify based on meal
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
    }


}