package cjcompany.nutridog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

// Handle extra files with:
// https://developer.android.com/guide/topics/data/data-storage.html#filesInternal

/**
 * Created by Jackson on 6/20/2017.
 */
public class PetsPage extends AppCompatActivity {

    BufferedReader br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            FileInputStream fis = openFileInput("pets.csv");
            br = new BufferedReader(new InputStreamReader(fis));
            String str = br.readLine();

            //if pets.csv has no pets, use newPetForm()
            if(str == null){
                br.close();
                newPetForm();
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

    //TODO
    public void createPetButtons(String firstLine){
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

        //TODO creates square buttons in the two columns

        //TODO creates addNewPet button at bottom

        //TODO create RemovePetButton
    }

    //TODO
    public void newPetForm(){
        setContentView(R.layout.activity_pets_addnew);
    }

    //TODO
    private void onAddPet(View view){
        //TODO save dog info to pets.csv


        //reload page
        try {
            FileInputStream fis = openFileInput("pets.csv");
            br = new BufferedReader(new InputStreamReader(fis));
            String str = br.readLine();
            createPetButtons(str);
        }catch (FileNotFoundException ex){
            System.err.println("PET FILE MISSING: REMOVED SINCE STARTUP");
        }catch(IOException ex){
            System.err.println("UNEXPECTED IO EXCEPTION");
        }
    }

    //TODO
    private void onNewPetButton(){
        //clear previous injected XML code
            /* if no clear is needed, remove this method and set
            /  newPetForm as onClick method for addNewPet button
            */
    }

    //TODO
    public void goTo_PetInfo(View view){
        Intent intent = new Intent(this, VetsPage.class);
        //add pet reference to EXTRA_MESSAGE
        //intent.putExtra(EXTRA_MESSAGE, androidId);
        startActivity(intent);
    }
}

/*

    ******  EXAMPLE CODE FOR XML INJECTION  ******

    public void createTableView() {
        //make all list elements buttons that change color when pressed
        //insert totalPlayers variable
        int totalPlayers = 0;

        TableLayout tl = (TableLayout) findViewById(R.id.voteTable);

        for (int i = 0; i < 10; i++) {
            TableRow row = new TableRow(this);

            for (int e = 0; e < totalPlayers; e++) {

                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(acceptedAnswers.get(e));
                row.addView(checkBox);
            }

            tl.addView(row);
        }
    }
*/
