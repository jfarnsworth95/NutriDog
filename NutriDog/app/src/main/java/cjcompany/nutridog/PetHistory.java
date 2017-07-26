package cjcompany.nutridog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Jackson on 7/24/2017.
 */
public class PetHistory extends AppCompatActivity {

    String petName;
    String petID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        petName = intent.getStringExtra(PetInfoPage.PET_NAME);
        petID = intent.getStringExtra(PetInfoPage.PET_ID);

        setContentView(R.layout.activity_history);
        //TODO collect data from file to display [if null, failure occured, abort with finish()]
        //TODO inject data from file into view
    }

    public ArrayList<String[]> collectData(){
        BufferedReader br = null;
        ArrayList<String[]> dataFromFile = new ArrayList<String[]>();

        try{
            //File name is DogName_Data_UniqueID.csv
            FileInputStream fis = openFileInput(petName + "_Data_" + petID + ".csv");
            br = new BufferedReader(new InputStreamReader(fis));
            String str = br.readLine();


            //pull out individual data for each day
            while(str != null) {
                if (!str.equals("")) {
                    String[] dataForDay = new String[4];
                    dataForDay[0] = str; //day,month,year,totalCalories

                    str = br.readLine();
                    dataForDay[1] = str; //food names for each meal (b,l,d,s)

                    str = br.readLine();
                    dataForDay[2] = str; //food caloires for each meal (b,l,d,s)

                    str = br.readLine();
                    dataForDay[3] = str; //exercises

                    dataFromFile.add(dataForDay);
                }
                br.close();
                fis.close();

                return dataFromFile;
            }
        }catch (FileNotFoundException ex){
            System.err.println("INDIVIDUAL PET DATA FILE MISSING: REMOVED SINCE STARTUP");
        }catch(IOException ex1){
            System.err.println("UNEXPECTED IO EXCEPTION");
        }finally {
            try{
                if(br != null){
                    br.close();
                }
            }catch(IOException ex){
                System.err.println("Could not close buffered reader");
            }
        }
        return null;
    }

    public void injectData(ArrayList<String[]> dataForDays){
        //for getting name of month
        Calendar c = Calendar.getInstance();

        //goes from most recent (bottom of doc) to least recent (top of doc)
        for(int i = dataForDays.size() -1; i > 0; i --){
            TextView tv = new TextView(this);
            String[] currentDataList = dataForDays.get(i);

            String[] firstLine = currentDataList[0].split(",");
            String monthName = getMonthName(Integer.getInteger(firstLine[1]));
            String date = monthName + " " + firstLine[0] + " " + firstLine[2];

            String[] meals = currentDataList[1].split(",");
            String breakfast = meals[0];
            String lunch = meals[1];
            String dinner = meals[2];
            String snack = meals[3];

            String[] mealCalories = currentDataList[2].split(",");
            String breakfastCal = mealCalories[0];
            String lunchCal = mealCalories[1];
            String dinnerCal = mealCalories[2];
            String snackCal = mealCalories[3];

            String[] exercises = currentDataList[3].split(",");

            
        }
    }

    private String getMonthName(int m) {
        if (m == 1) {
            return "January";
        } else if (m == 2) {
            return "February";
        } else if (m == 3) {
            return "March";
        } else if (m == 4) {
            return "April";
        } else if (m == 5) {
            return "May";
        } else if (m == 6) {
            return "June";
        } else if (m == 7) {
            return "July";
        } else if (m == 8) {
            return "August";
        } else if (m == 9) {
            return "September";
        } else if (m == 10) {
            return "October";
        } else if (m == 11) {
            return "November";
        } else {
            return "December";
        }
    }

    /**
     * Should end this activity and revert to the last activity
     * @param view the view provided by button listener
     */
    public void closeActivity(View view){
        finish();
    }
}
