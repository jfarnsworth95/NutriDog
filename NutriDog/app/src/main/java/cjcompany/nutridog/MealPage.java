package cjcompany.nutridog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    /**
     * Get data and save to pet data file
     */
    public void getOnSubmit(View view){
        EditText foodNameField = (EditText)findViewById(R.id.search_bar);
        String foodName = foodNameField.getText().toString();

        EditText calorieField = (EditText)findViewById(R.id.mealCalories);
        String calories = calorieField.getText().toString();
        modifyFile(foodName,calories);
    }

    /**
     * modify file for pet data, can be easily updated to allow user to modify meals in history
     *
     */
    public void modifyFile(String foodName, String foodCalories){
        String[] splitDate = date.split(",");
        String day = splitDate[0];
        String month = splitDate[1];
        String year = splitDate[2];

        BufferedReader br = null;
        try{
            FileInputStream fis = openFileInput(petName + "_data_" + petID + ".csv");
            br = new BufferedReader(new InputStreamReader(fis));

            FileOutputStream outputStream = openFileOutput("tempFile.csv", Context.MODE_PRIVATE);

            String str = br.readLine();
            if(str.equals("")){
                System.err.println("Error in PetInfoPage file date creation method");
            }else {
                while (str != null) {
                    //write to tmp file
                    outputStream.write(str.getBytes());

                    //search for date
                    String[] fileDateSplit = str.split(",");
                    if (fileDateSplit[0].equals(day) && fileDateSplit[1].equals(month) && fileDateSplit[2].equals(year)) {

                        //currently in desired date, modify based on meal
                        str = br.readLine(); //move to food
                        String[] foods = str.split(",");
                        str = br.readLine(); //move to calories
                        String[] calories = str.split(",");

                        if(meal.equals("Breakfast")){
                            foods[0] = foodName;
                            stringListToCSV_String(foods);
                            calories[0] = foodCalories;
                            stringListToCSV_String(calories);
                        }else if(meal.equals("Lunch")){
                            foods[1] = foodName;
                            stringListToCSV_String(foods);
                            calories[1] = foodCalories;
                            stringListToCSV_String(calories);
                        }else if(meal.equals("Dinner")){
                            foods[2] = foodName;
                            stringListToCSV_String(foods);
                            calories[2] = foodCalories;
                            stringListToCSV_String(calories);
                        }else if(meal.equals("Snack")){
                            foods[3] = foodName;
                            stringListToCSV_String(foods);
                            calories[3] = foodCalories;
                            stringListToCSV_String(calories);
                        }else{
                            System.err.println("Unexpected tag recieved for meal");
                        }


                        //reset and move on
                        br.readLine(); //exercise
                        str = br.readLine(); //back to date for next entry


                    }else{
                        br.readLine(); //food
                        br.readLine(); //calories
                        br.readLine(); //exercise
                        str = br.readLine(); //back to date for next entry
                    }
                }

                //delete old file, rename new file
                boolean didDelete = deleteFile(petName + "_Data_" + petID + ".csv");
                File newFile = new File(getFilesDir(), "tempFile.csv");
                Boolean didRename = newFile.renameTo(new File(getFilesDir(),"/" + petName + "_Data_" + petID + ".csv"));

                Log.i("PetsPage","Old File Deleted: " + didDelete);
                Log.i("PetsPage","Temp File Renamed: " + didRename);
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

    private String stringListToCSV_String(String[] strLst){
        String str = "";
        if(strLst.length == 0){
            return "";
        }else {
            str = strLst[0];
            for (int i = 1; i < strLst.length; i++) {
                str += "," + strLst[i];
            }
        }
        return str;
    }



}