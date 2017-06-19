package cjcompany.nutridog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//For help storing credentials for Git command line:
//      $ git config credential.helper store
//      $ git push http://example.com/repo.git


public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
    }
}
