package cjcompany.nutridog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 * Created by Jackson on 6/20/2017.
 */
public class PetInfoPage extends AppCompatActivity {

    public final static String MEAL = "com.cjcompany.nutridog.MEAL";
    public final static String PET_NAME = "com.cjcompany.nutridog.PET_NAME";
    public final static String PET_ID = "com.cjcompany.nutridog.PET_ID";
    public final static String DATE = "com.cjcompany.nutridog.DATE";
    String petName;
    String petID;
    String[] petInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        petName = intent.getStringExtra(PetsPage.PET_NAME);
        petID = intent.getStringExtra(PetsPage.PET_ID);
        petInfo = intent.getStringArrayExtra(PetsPage.PET_INFO);

        setContentView(R.layout.activity_pets_list);
    }

    public void collectScreenData(){

        //find current date
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //establish String vars for later
        String calorieLimit = "";
        String caloriesConsumed = "";
        String bf = "";
        String lnh = "";
        String dnr = "";
        String snk = "";

        //check if data exists for current date
        BufferedReader br = null;
        try{
            //File name is DogName_Data_UniqueID.csv
            FileInputStream fis = openFileInput(petName + "_Data_" + petID + ".csv");
            br = new BufferedReader(new InputStreamReader(fis));
            String str = br.readLine();


            while(str != null) {
                if (!str.equals("")) {
                    String[] split = str.split(",");
                    if (new Integer(split[0]) == day && new Integer(split[1]) == month && new Integer(split[2]) == year) {
                        calorieLimit = split[3];

                        str = br.readLine(); //moves to meal names
                        str = br.readLine(); //moves to meal calories

                        String[] calorieList = str.split(",");
                        bf = calorieList[0];
                        lnh = calorieList[1];
                        dnr = calorieList[2];
                        snk = calorieList[3];

                        Integer calConsumed = new Integer(bf) + new Integer(lnh) + new Integer(dnr) + new Integer(snk);
                        caloriesConsumed = calConsumed.toString();
                        break;

                    }
                    str = br.readLine(); //moves to meal names
                    str = br.readLine(); //moves to meal calories
                    str = br.readLine(); //moves to exercises
                    str = br.readLine(); //moves to dates and total calories
                }
                //if the day does not exist in file, taken care of below
                br.close();
                fis.close();
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

        if(calorieLimit.equals("")){
            //create new entry in petName_data_uniqueID.csv for today
            addTodayToPetData(day,month,year);
        }

        //Apply data to layout
        setScreenData(calorieLimit,caloriesConsumed,bf,lnh,dnr,snk);
    }

    /**
     * Method to update total used calories for the day and meal calories
     */
    public void setScreenData(String calorieLimit, String caloriesConsumed, String bf, String lnh, String dnr, String snk){
        //set calorie bar, if no entry, set to zero
        TextView caloriesUsed = (TextView) findViewById(R.id.currentPetCalories);
        caloriesUsed.setText(caloriesConsumed);
        TextView totalCalories = (TextView) findViewById(R.id.totalPetCalories);
        totalCalories.setText(calorieLimit);

        //set breakfast, if no entry, set to zero
        Button breakfastBtn = (Button) findViewById(R.id.btn_petBreakfast);
        breakfastBtn.setText(bf);

        //set lunch, if no entry, set to zero
        Button lunchBtn = (Button) findViewById(R.id.btn_petLunch);
        lunchBtn.setText(lnh);

        //set dinner, if no entry, set to zero
        Button dinnerBtn = (Button) findViewById(R.id.btn_petDinner);
        dinnerBtn.setText(dnr);

        //set snacks, if no entry, set to zero
        Button snackBtn = (Button) findViewById(R.id.btn_petSnack);
        snackBtn.setText(snk);
    }

    public void addTodayToPetData(int day, int month, int year){
        BufferedReader br1 = null;
        try{
            FileInputStream fis = openFileInput(petName + "_Data_" + petID + ".csv");
            br1 = new BufferedReader(new InputStreamReader(fis));
            String str = br1.readLine();

            FileOutputStream outputStream = openFileOutput("tempFile.csv", Context.MODE_PRIVATE);

            while(str != null) {
                outputStream.write(str.getBytes());
            }

            //calculate total calories for dog to consume
            double rer = findCalorieRequirement(Integer.valueOf(petInfo[3]));

            //add entry for today to file
            String dateAndCalories = Integer.toString(day)+ "," + Integer.toString(month)
                    + "," + Integer.toString(year)+ "," + Double.toString(rer) + "\n";
            outputStream.write(dateAndCalories.getBytes());
            outputStream.write("\n".getBytes()); //meal names
            outputStream.write("0,0,0,0 \n".getBytes()); //meal calories

            outputStream.close();
            br1.close();
            fis.close();

            //delete old file, rename new file
            File oldFile = new File(getFilesDir(),petName + "_Data_" + petID + ".csv");
            oldFile.delete();
            File newFile = new File(this.getFilesDir(), "tempFile.csv");
            newFile.renameTo(new File(petName + "_Data_" + petID + ".csv"));


        }catch (FileNotFoundException ex){
            System.err.println("INDIVIDUAL PET DATA FILE MISSING: REMOVED SINCE STARTUP");
        }catch(IOException ex1){
            System.err.println("UNEXPECTED IO EXCEPTION");
        }finally {
            try{
                if(br1 != null){
                    br1.close();
                }
            }catch(IOException ex){
                System.err.println("Could not close buffered reader");
            }
        }
    }

    /**
     * Provides caloric requirement based on Resting Energy Requirement
     * https://www.platopettreats.com/connect/dogs-daily-calorie-calculator/
     */
    private double findCalorieRequirement(int wt){
        double wt_inKilos = wt/2.2046226218;
        double rer = 70 * Math.pow(wt_inKilos,(3/4));
        return rer;
    }


    public void goTo_PetHistory(View view){
        Intent intent = new Intent(this, PetHistory.class);
        intent.putExtra(PET_NAME, petName);
        intent.putExtra(PET_ID, petID);
        startActivity(intent);
    }

    public void goTo_MealPage(View view){
        Intent intent = new Intent(this, MealPage.class);
        //Tag can be (Breakfast, Lunch, Dinner, Snack)
        String meal = view.getTag().toString();
        Calendar c = Calendar.getInstance();
        String year = Integer.toString(c.get(Calendar.YEAR));
        String month = Integer.toString(c.get(Calendar.MONTH));
        String day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        String date = day + "," + month + "," + year;

        intent.putExtra(MEAL, meal);
        intent.putExtra(DATE, date);
        intent.putExtra(PET_NAME, petName);
        intent.putExtra(PET_ID, petID);
        startActivity(intent);
    }


}
