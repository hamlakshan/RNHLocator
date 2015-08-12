package com.example.manoj.rnhlocator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CloudDatabaseActivity extends Activity {

    Button btnViewLocatios;
    Button btnNewLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cloud_database);

        btnViewLocatios = (Button) findViewById(R.id.btnViewLocations);
        btnNewLocation = (Button) findViewById(R.id.btnCreateLocation);

        // view products click event
        btnViewLocatios.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent viewlocation = new Intent(getApplicationContext(), ViewCloudLocationsActivity.class);
                startActivity(viewlocation);

            }
        });

        // view products click event
        btnNewLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent addnew = new Intent(getApplicationContext(), AddLocationsActivity.class);
                startActivity(addnew);

            }
        });
    }
}
