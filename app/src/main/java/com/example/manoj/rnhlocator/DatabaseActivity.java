package com.example.manoj.rnhlocator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DatabaseActivity extends Activity {

    Button btnClose;
    Button btnView;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        btnClose = (Button) findViewById(R.id.btnexit);
        btnView = (Button) findViewById(R.id.btnview);
        btnAdd = (Button) findViewById(R.id.btnadd);

        // Click event to Button exit to exit the currrent window
        btnClose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Closing SecondScreen Activity
                finish();
            }
        });
        //this click event will open the list view of the religious places
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewLocations = new Intent(getApplicationContext(), ViewLocationsActivity.class);
                startActivity(viewLocations);
                finish();
            }
        });

        //this will open the add new location window
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addLocations = new Intent(getApplicationContext(), AddLocationsActivity.class);
                startActivity(addLocations);
            }
        });

    }

}
