package cjcompany.nutridog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

            //if pets.csv has no pets, use setContentView(R.layout.activity_pets_addnew);
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

    public void createPetButtons(){
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
            Button newLeftButton = createButton(dogInfos.get(even)[0],new Integer(dogInfos.get(even)[1]));
            leftColumn.addView(newLeftButton);
        }
        if(dogInfos.size() > 1){
            for(int odd = 1; odd < dogInfos.size(); odd += 2){
                Button newRightButton = createButton(dogInfos.get(odd)[0],new Integer(dogInfos.get(odd)[1]));
                rightColumn.addView(newRightButton);
            }
        }
    }

    private Button createButton(final String name, final int uniqueID){
        final Button btn = new Button(this);
        btn.setText(name);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PetInfoPage.class);
                //add pet reference to EXTRA_MESSAGE
                //intent.putExtra(EXTRA_MESSAGE, androidId);
                intent.putExtra("DOGS_NAME",name);
                startActivity(intent);
            }
        });

        //https://www.javatpoint.com/android-popup-menu-example
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(PetsPage.this, btn);
                popup.getMenuInflater().inflate(R.menu.popup_pet_delete, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(PetsPage.this,"Deleted data for " + btn.getText(),Toast.LENGTH_SHORT).show();

                        //TODO Make confirmation window/popup
                        deletePetInfo(name,uniqueID);
                        return true;
                    }
                });
                popup.show();//showing popup menu
                return true;
            }
        });

        //TODO set button appearence (ex: size, text size, padding, ...)
        //TODO set button onClick and onLongClick methods

        return btn;
    }

    private void deletePetInfo(String name, int uniqueID){
        BufferedReader br = null;
        try {

            //delete pet from pets.csv
            FileInputStream fis = openFileInput("pets.csv");
            br = new BufferedReader(new InputStreamReader(fis));

            FileOutputStream outputStream = openFileOutput("tempFile.csv", Context.MODE_PRIVATE);

            String str = br.readLine();

            while(str != null) {

                if (str.split(",")[0] != name) {
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
    }


    //TODO CONNECT TO XML FILE
    private void onSubmitNewPet(View view) {
        Intent i = new Intent(this, PetsPage.class);
        //TODO save dog info to pets.csv

        //TODO create dog's info file (Named it: DogName_Data_UniqueID.csv)


        //reload page
        try {
            FileInputStream fis = openFileInput("pets.csv");
            br = new BufferedReader(new InputStreamReader(fis));
            String str = br.readLine();
            createPetButtons(str);
        } catch (FileNotFoundException ex) {
            System.err.println("PET FILE MISSING: REMOVED SINCE STARTUP");
        } catch (IOException ex) {
            System.err.println("UNEXPECTED IO EXCEPTION");
        }
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
