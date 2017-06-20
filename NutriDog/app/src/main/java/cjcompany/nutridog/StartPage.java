package cjcompany.nutridog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

//For help storing credentials for Git command line:
//      $ git config credential.helper store
//      $ git push http://example.com/repo.git


public class StartPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void goTo_PetsPage(View view){
        Intent intent = new Intent(this, PetsPage.class);
        //intent.putExtra(EXTRA_MESSAGE, androidId);
        startActivity(intent);
    }

    public void goTo_VetsPage(View view){
        Intent intent = new Intent(this, VetsPage.class);
        //intent.putExtra(EXTRA_MESSAGE, androidId);
        startActivity(intent);
    }

    public void goTo_ToysPage(View view){
        Intent intent = new Intent(this, ToysPage.class);
        //intent.putExtra(EXTRA_MESSAGE, androidId);
        startActivity(intent);
    }
}
