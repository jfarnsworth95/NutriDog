package cjcompany.nutridog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    ArrayList<String[]> collectedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        petName = intent.getStringExtra(PetInfoPage.PET_NAME);
        petID = intent.getStringExtra(PetInfoPage.PET_ID);

        setContentView(R.layout.activity_history);
        //collect data from file to display [if null, failure occured, abort with finish()]
        collectedData = collectData();
        if(collectedData.size() == 0){
            finish();
        }else{
            //inject data from file into view
            injectDates(collectedData);
        }

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

    public void injectDates(ArrayList<String[]> data){

        LinearLayout ll = (LinearLayout) findViewById(R.id.addItemHistoryHere);

        for(int i = data.size() -1; i > 0; i --){
            //set the date as the button text
            Button btn = (Button) findViewById(R.id.btn_itemHistory);
            String[] dateAndCal = data.get(i)[0].split(",");
            String month = getMonthName(Integer.getInteger(dateAndCal[1]));
            btn.setText(month + " " + dateAndCal[0] + ", " + dateAndCal[2]);
            final String[] dataForDay = data.get(i);

            //set a listener
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setContentView(R.layout.item_history);
                    detailedHistory(dataForDay);
                }
            });

            //add btn to linear layout inside scroll layout
            ll.addView(btn);
        }
    }

    public void detailedHistory(String[] dataForDay){
        //goes from most recent (bottom of doc) to least recent (top of doc)

        TextView tv = new TextView(this);

        String[] firstLine = dataForDay[0].split(",");
        String monthName = getMonthName(Integer.getInteger(firstLine[1]));
        String date = monthName + " " + firstLine[0] + " " + firstLine[2];

        //meal names
        String[] meals = dataForDay[1].split(",");
        String breakfast = meals[0];
        String lunch = meals[1];
        String dinner = meals[2];
        String snack = meals[3];

        //meal calories
        String[] mealCalories = dataForDay[2].split(",");
        String breakfastCal = mealCalories[0];
        String lunchCal = mealCalories[1];
        String dinnerCal = mealCalories[2];
        String snackCal = mealCalories[3];

        String[] exercises = dataForDay[3].split(",");

        //inject data into view
        TextView bTV = (TextView) findViewById(R.id.item_breakfast);
        bTV.setText(breakfast + ": " + breakfastCal);
        TextView lTV = (TextView) findViewById(R.id.item_lunch);
        bTV.setText(lunch + ": " + lunchCal);
        TextView dTV = (TextView) findViewById(R.id.item_dinner);
        bTV.setText(dinner + ": " + dinnerCal);
        TextView sTV = (TextView) findViewById(R.id.item_snack);
        bTV.setText(snack + ": " + snackCal);
        LinearLayout exLL = (LinearLayout) findViewById(R.id.item_exercises);
        for(int ex = 0; ex < exercises.length; ex ++){
            TextView eTV = new TextView(this);
            eTV.setText(exercises[ex]);
            exLL.addView(eTV);
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
