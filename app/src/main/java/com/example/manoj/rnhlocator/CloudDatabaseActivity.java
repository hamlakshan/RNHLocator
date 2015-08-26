package com.example.manoj.rnhlocator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

// /this window will provide the access to the cloud database activities
public class CloudDatabaseActivity extends Activity {
    //button definition
    Button btnViewLocatios;
    Button btnNewLocation;
    Button btnViewQuickSuggestions;

    //the TAG to uniquely identify the print log messages
    private static final String TAG_LOG = "myview";
    String user_id; //save the passed user id

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cloud_database_window);

        //the intent to obtain the data recerivd from the previous window
        Intent main = getIntent();
        //String name = main.getStringExtra("name");
        user_id = main.getStringExtra("id");
        Log.d(TAG_LOG, "cloud database activity user id is : " + user_id);
        btnViewLocatios = (Button) findViewById(R.id.btnViewLocations);
        btnNewLocation = (Button) findViewById(R.id.btnCreateLocation);
        btnViewQuickSuggestions = (Button) findViewById(R.id.btnViewLocationsInMap);

        // view products click event
        btnViewLocatios.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent viewlocation = new Intent(getApplicationContext(), ViewCloudLocationsActivity.class);
                viewlocation.putExtra("id", user_id);
                startActivity(viewlocation);
                Log.d(TAG_LOG, "passed user id: " + user_id);
            }
        });

        // view products click event
        btnNewLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent addnew = new Intent(getApplicationContext(), AddCloudLocationActivity.class);
                addnew.putExtra("id", user_id);
                startActivity(addnew);
                Log.d(TAG_LOG, "new locatin adding window opened");

            }
        });

        btnViewQuickSuggestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quickmap = new Intent(getApplicationContext(), QuickCloudMapSearchActivity.class);
                startActivity(quickmap);
            }
        });
    }
}
