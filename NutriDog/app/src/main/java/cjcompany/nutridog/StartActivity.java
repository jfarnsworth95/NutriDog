package cjcompany.nutridog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//For help storing credentials for Git command line:
//      $ git config credential.helper store
//      $ git push http://example.com/repo.git


public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void goTo_PetsPage(){
        Intent intent = new Intent(this, PetsPage.class);
        //intent.putExtra(EXTRA_MESSAGE, androidId);
        startActivity(intent);
    }

    public void goTo_VetsPage(){
        Intent intent = new Intent(this, VetsPage.class);
        //intent.putExtra(EXTRA_MESSAGE, androidId);
        startActivity(intent);
    }

    public void goTo_ToysPage(){
        Intent intent = new Intent(this, ToysPage.class);
        //intent.putExtra(EXTRA_MESSAGE, androidId);
        startActivity(intent);
    }
}
