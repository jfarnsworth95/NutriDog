package cjcompany.nutridog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Jackson on 6/29/2017.
 */
public class MealPage extends AppCompatActivity {

    String meal;

    //TODO make sure info is saved when device moves away from app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO should be passed string var of "Breakfast", "Lunch", or "Dinner"
        Intent intent = getIntent();
        meal = intent.getStringExtra(PetInfoPage.MEAL);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
    }
}