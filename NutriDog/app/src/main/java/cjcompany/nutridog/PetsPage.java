package cjcompany.nutridog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Jackson on 6/20/2017.
 */
public class PetsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        //if pets.csv exists, use createPetButtons

        //if pets.csv does not exist, use newPetForm
    }

    public void createPetButtons(){
        //find pets from sheet
        //creates square buttons in two columns
        //creates addNewPet button at bottom
    }

    public void newPetForm(){
        //following textEdit fields:
            //name
            //breed
            //height

        //maybe allow input picture of dog? (limit resolution, save only whats req.)
        //keep pictures for timeline?

        //then submit button

        //from breed, generate:
            //envrionment? waiting on Cary for these.
            //excersises?
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
