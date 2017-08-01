package cjcompany.nutridog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Jackson on 6/20/2017.
 */
public class RecommendedVetsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_vets);

        BufferedReader br = null;
        ScrollView sv = (ScrollView) findViewById(R.id.recVetLst_SV);

        try {
            InputStream is = getAssets().open("recommendedVets.csv");
            br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();

            while(line != null){
                String[] vetInfo = line.split(",");
                final String vetName = vetInfo[0];
                final String vetStAddress = vetInfo[1];
                final String cityStateZIP = vetInfo[2] + ", " + vetInfo[3] + ", " + vetInfo[4];
                String vetPhone = vetInfo[5];

                RelativeLayout rl = (RelativeLayout) findViewById(R.id.vetItemListElement);

                TextView vetName_layout = (TextView) rl.getChildAt(R.id.txt_vetName);
                vetName_layout.setText(vetName);
                TextView vetStAddress_layout = (TextView) rl.getChildAt(R.id.txt_St_Address);
                vetStAddress_layout.setText(vetStAddress);
                TextView vetGeneralAddress_layout = (TextView) rl.getChildAt(R.id.txt_ZIP_Address);
                vetGeneralAddress_layout.setText(cityStateZIP);
                TextView vetPhone_layout = (TextView) rl.getChildAt(R.id.txt_vetPhone);
                vetPhone_layout.setText(vetPhone);

                rl.isClickable();
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //https://developers.google.com/maps/documentation/urls/android-intents
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + vetName +","+ vetStAddress +","+ cityStateZIP);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });


                sv.addView(rl);
            }

        }catch (FileNotFoundException ex1){
            System.err.println("Asset: recommendedVets.csv not found");
        }catch(IOException ex2){
            System.err.println("Trouble reading csv file: aborting");
        }finally {
            if(br != null){
                try {
                    br.close();
                }catch (IOException ex){
                    System.err.println("Couldnt close Buffered Reader for reading vet csv");
                }
            }
        }
    }
}
