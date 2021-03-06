package cjcompany.nutridog;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
            Log.i("StartPage","Creating pets.csv"); //TODO REMOVE AFTER TESTING
            File file = new File(this.getFilesDir(), "pets.csv");
            try {
                file.createNewFile();
            }catch (IOException ex){
                Log.i("StartPage","ERROR CREATING pets.csv FILE");
            }
        }

        setContentView(R.layout.activity_start);
    }

    /**
     * Creates cleanDogFood.csv if it does not exist using pre-filled list from assets folder
     * //https://developer.android.com/training/basics/data-storage/files.html
     */
    public void createCleanDogFoodFile(){
        Log.i("StartPage","Creating cleanDogFood.csv"); //TODO REMOVE AFTER TESTING
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
            Log.i("StartPage","ERROR CREATING cleanDogFood.csv FILE");
            e.printStackTrace();
        }
    }

    public void goTo_PetsPage(View view){
        Intent intent = new Intent(this, PetsPage.class);
        //intent.putExtra(EXTRA_MESSAGE, androidId);
        startActivity(intent);
    }

    public void goTo_VetsGoogleMaps(View view){

        double longitude = 0;
        double latitude = 0;

        //get latitude and longitude
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }catch(SecurityException ex){

            Context context = getApplicationContext();
            CharSequence text = "Application requires GPS to search via Google Maps";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            return;
        }

        //start google maps
        //https://developers.google.com/maps/documentation/urls/android-intents
        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=veterinarian");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void goTo_RecommendedVetsPage(View view){
        Intent intent = new Intent(this, RecommendedVetsPage.class);
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
        Log.i("StartPage",fname + " existence = " + file.exists());
        return file.exists();
    }
}
