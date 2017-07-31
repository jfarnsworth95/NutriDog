package cjcompany.nutridog;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;

//For help storing credentials for Git command line:
//      $ git config credential.helper store
//      $ git push http://example.com/repo.git


public class StartPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if extra internal files exist
        //If does not exist, create file
        //https://developer.android.com/guide/topics/data/data-storage.html#filesInternal
        if(!fileExistence("cleanDogFood.csv")){
            createCleanDogFoodFile();
        }
        if(!fileExistence("pets.csv")){
            File file = new File(this.getFilesDir(), "pets.csv");
            try {
                file.createNewFile();
            }catch (IOException ex){
                System.out.println("ERROR IN CREATING pets.cvs FILE");
            }
        }

        setContentView(R.layout.activity_start);
    }

    /**
     * Creates cleanDogFood.csv if it does not exist using pre-filled list from assets folder
     * //https://developer.android.com/training/basics/data-storage/files.html
     */
    public void createCleanDogFoodFile(){
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput("cleanDogFood.csv", Context.MODE_PRIVATE);

            InputStream is = getAssets().open("cleanDogFood.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            while(line != null){
                outputStream.write(line.getBytes());
                line = br.readLine();
            }

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goTo_PetsPage(View view){
        Intent intent = new Intent(this, PetsPage.class);
        //intent.putExtra(EXTRA_MESSAGE, androidId);
        startActivity(intent);
    }

    public void goTo_VetsPage(View view){
        Intent intent = new Intent(this, VetsPage.class);
        startActivity(intent);
    }

    /**
     *Used to find if internal files already exist on start up. Method from: <p></p>
     * https://stackoverflow.com/questions/10576930/trying-to-check-if-a-file-exists-in-internal-storage
     *
     * @param fname file name
     * @return if file exits then true, else false
     */
    public boolean fileExistence(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }
}
