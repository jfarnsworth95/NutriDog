package cjcompany.nutridog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

// Handle extra files with:
// https://developer.android.com/guide/topics/data/data-storage.html#filesInternal

/**
 * Created by Jackson on 6/20/2017.
 */
public class PetsPage extends AppCompatActivity {

    BufferedReader br;
    public final static String PET_NAME = "com.cjcompany.nutridog.PET_NAME";
    public final static String PET_ID = "com.cjcompany.nutridog.PET_ID";
    public final static String PET_INFO = "com.cjcompany.nutridog.PET_INFO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            FileInputStream fis = openFileInput("pets.csv");
            br = new BufferedReader(new InputStreamReader(fis));
            String str = br.readLine();

            //if pets.csv has no pets, use setContentView(R.layout.activity_pets_addnew);
            if(str == null){
                br.close();
                setContentView(R.layout.activity_pets_addnew);
                Button submitBtn = (Button) findViewById(R.id.btn_submitNewPet);
                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSubmitNewPet(v);
                    }
                });
            }
            //if pets.csv has entries, use createPetButtons()
            else{
                createPetButtons(str);
            }
        }catch (FileNotFoundException ex){
            Log.e("PetsPage","PET FILE MISSING: REMOVED SINCE STARTUP");
        }catch(IOException ex){
            Log.e("PetsPage","UNEXPECTED IO EXCEPTION");
        }finally {
            try{
                if(br != null){
                    br.close();
                }
            }catch(IOException ex){
                System.err.println("UNEXPECTED ERROR");
            }
        }
    }

    public void createPetButtons(){
        Log.i("PetsPage","Creating buttons"); //TODO REMOVE AFTER TESTING
        try {
            FileInputStream fis = openFileInput("pets.csv");
            br = new BufferedReader(new InputStreamReader(fis));
            String str = br.readLine();

            //if pets.csv has no pets, use newPetForm()
            if(str == null){
                br.close();
                setContentView(R.layout.activity_pets_addnew);
            }
            //if pets.csv has entries, use createPetButtons()
            else{
                createPetButtons(str);
            }
        }catch (FileNotFoundException ex){
            System.err.println("PET FILE MISSING: REMOVED SINCE STARTUP");
        }catch(IOException ex){
            System.err.println("UNEXPECTED IO EXCEPTION");
        }finally {
            try{
                if(br != null){
                    br.close();
                }
            }catch(IOException ex){
                System.err.println("UNEXPECTED ERROR");
            }
        }
    }

    public void createPetButtons(String firstLine){
        Log.i("PetsPage","File not empty, making buttons"); //TODO REMOVE AFTER TESTING
        setContentView(R.layout.activity_pets_list);
        ArrayList<String[]> dogInfos = new ArrayList<String[]>();
        dogInfos.add(firstLine.split(","));

        //find pets from sheet
        try{
            String str = br.readLine();
            while(str != null){
                dogInfos.add(str.split(","));
                str= br.readLine();
            }

        }catch(IOException ex){
            System.err.println("UNEXPECTED IO EXCEPTION");
        }finally{
            if(br != null){
                try {
                    br.close();
                }catch(IOException ex){
                    System.err.println("VERY WEIRD ERROR IN: createPetButtons");
                }
            }
        }

        //creates square buttons in the two columns
        LinearLayout leftColumn = (LinearLayout) findViewById(R.id.pets_leftColumn);
        LinearLayout rightColumn = (LinearLayout) findViewById(R.id.pets_rightColumn);

        for(int even = 0; even < dogInfos.size(); even +=2){
            Button newLeftButton = createButton(dogInfos.get(even)[0],dogInfos.get(even)[1],dogInfos.get(even));
            leftColumn.addView(newLeftButton);
        }
        if(dogInfos.size() > 1){
            for(int odd = 1; odd < dogInfos.size(); odd += 2){
                Button newRightButton = createButton(dogInfos.get(odd)[0],dogInfos.get(odd)[1],dogInfos.get(odd));
                rightColumn.addView(newRightButton);
            }
        }
    }

    /**
     * Creates the button object to be placed in View. Also establishes the connection to PetInfoPage.class.
     * @param name name of the pet
     * @param uniqueID id of the pet
     * @return the button representing a pet
     */
    private Button createButton(final String name, final String uniqueID, final String[] dogInfo){
        //sets height, width, text size, and margin in R.id.btn_pet
        final Button btn = (Button) findViewById(R.id.btn_pet);
        btn.setText(name);

        //set button onClick and onLongClick methods
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PetInfoPage.class);
                //intent.putExtra(EXTRA_MESSAGE, androidId);
                intent.putExtra(PET_NAME,name);
                intent.putExtra(PET_ID,uniqueID);
                intent.putExtra(PET_INFO,dogInfo);
                startActivity(intent);
            }
        });

        //https://www.javatpoint.com/android-popup-menu-example
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //https://www.mkyong.com/android/android-alert-dialog-example/
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PetsPage.this);
                // set title
                alertDialogBuilder.setTitle("Are you sure you want to remove pet data?");
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Delete",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, delete pet info
                                deletePetInfo(name,uniqueID);
                                Toast.makeText(PetsPage.this,"Deleted data for " + btn.getText(),Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close the dialog box and do nothing
                                dialog.cancel();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
                return true;
            }
        });

        return btn;
    }

    private void deletePetInfo(String name, String uniqueID){
        BufferedReader br = null;
        try {

            //delete pet from pets.csv
            FileInputStream fis = openFileInput("pets.csv");
            br = new BufferedReader(new InputStreamReader(fis));

            FileOutputStream outputStream = openFileOutput("tempFile.csv", Context.MODE_PRIVATE);
            String str = br.readLine();

            while(str != null) {

                if (str.split(",")[0].equals(name) && str.split(",")[1].equals(uniqueID)) {

                }else{
                    outputStream.write(str.getBytes());
                }
                str = br.readLine();
            }
            outputStream.close();

            //delete old pets.csv and set tempFile to be new pets.csv
            File oldFile = new File(getFilesDir(),"pets.cvs");
            oldFile.delete();
            File newFile = new File(this.getFilesDir(), "tempFile.csv");
            newFile.renameTo(new File("pets.csv"));

            //delete Dog's personal info file
            File dogDataFile = new File(getFilesDir(),name + "_Data_" + uniqueID);
            dogDataFile.delete();

        }catch (FileNotFoundException ex){
            System.err.println("PET FILE MISSING: REMOVED SINCE STARTUP");
        }catch(IOException ex){
            System.err.println("UNEXPECTED IO EXCEPTION");
        }finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                System.err.println("UNEXPECTED ERROR");
            }
        }

        //Restarts view to show changes
        createPetButtons();
    }

    public void onNewPetBtnClicked(View view){
        Intent i = new Intent(this, PetsPage.class);
        setContentView(R.layout.activity_pets_addnew);
        Button btn = (Button) findViewById(R.id.btn_submitNewPet);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitNewPet(v);
            }
        });
    }

    private void onSubmitNewPet(View view) {
        Log.i("PetsPage","Clicked Submit"); //TODO REMOVE AFTER TESTING
        //save dog info to pets.csv
        //Name,UniqueID,Age,Weight,Height,Breed,Gender
        String name;
        String id;
        try{
            FileInputStream fis = openFileInput("pets.csv");
            br = new BufferedReader(new InputStreamReader(fis));

            FileOutputStream outputStream = openFileOutput("tempFile.csv", Context.MODE_PRIVATE);
            String str = br.readLine();

            while(str != null) {
                outputStream.write(str.getBytes());
                str = br.readLine();
            }

            //TODO check for empty values
            String newPetData = ""; //Name,UniqueID,Age,Weight,Height,Breed,Gender
            EditText petName = (EditText) findViewById(R.id.petName);
            newPetData += "," + cleanInput(petName.getText().toString());
            name = cleanInput(petName.getText().toString());
            Long petID = Math.abs(new Random().nextLong());
            newPetData += "," + new Long(petID).toString();
            id = new Long(petID).toString();
            EditText petAge = (EditText) findViewById(R.id.petAge);
            newPetData += "," + petAge.getText().toString();
            EditText petWeight = (EditText) findViewById(R.id.petWeight);
            newPetData += "," + petWeight.getText().toString();
            EditText petHeight = (EditText) findViewById(R.id.petHeightInches);
            newPetData += "," + petHeight.getText().toString();
            EditText petBreed = (EditText) findViewById(R.id.petBreed);
            newPetData += "," + cleanInput(petBreed.getText().toString());
            RadioButton maleBtn = (RadioButton) findViewById(R.id.radioBtn_male);
            if(maleBtn.isChecked()){
                newPetData += ",Male";
            }else{
                newPetData += ",Female";
            }
            outputStream.write(newPetData.getBytes());

            outputStream.close();

            File oldFile = new File(getFilesDir(),"pets.cvs");
            oldFile.delete();
            File newFile = new File(this.getFilesDir(), "tempFile.csv");
            newFile.renameTo(new File("pets.csv"));

            //create dog's info file (Named it: DogName_Data_UniqueID.csv)
            FileOutputStream fos = openFileOutput(name + "_Data_" + id + ".csv", Context.MODE_PRIVATE);
            fos.write("".getBytes());
            fos.close();

        }catch(IOException ex){
            System.err.println("UNEXPECTED ERROR");
        }finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                System.err.println("UNEXPECTED ERROR");
            }
        }

        //reload page
        Log.i("PetsPage","Entered Pet Info"); //TODO REMOVE AFTER TEST
        createPetButtons();
    }

    /**
     * Removes any "," from user inputed values to prevent issues with the csv docs
     * @param str String to clean
     * @return String with no commas
     */
    private String cleanInput(String str){
        while(str.contains(",")){
            str = str.replace(",","");
        }
        return str;
    }

}