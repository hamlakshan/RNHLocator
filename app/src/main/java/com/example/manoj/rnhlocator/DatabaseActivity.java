package com.example.manoj.rnhlocator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DatabaseActivity extends Activity {

    Button btnClose;
    Button btnView;
    Button btnAdd;

    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        username = (TextView) findViewById(R.id.userName);
        btnClose = (Button) findViewById(R.id.btnexit);
        btnView = (Button) findViewById(R.id.btnview);
        btnAdd = (Button) findViewById(R.id.btnadd);

        Intent main = getIntent();
        final Intent viewLocaions = new Intent(getApplicationContext(), ViewLocationsActivity.class);
        final Intent addLocaions = new Intent(getApplicationContext(), AddLocationsActivity.class);

        // Receiving the Data frm previous window
        String name = main.getStringExtra("name");


        // Displaying Received data
        username.setText(name);
        //txtEmail.setText(email);

        // Click event to Button exit to exit the currrent window
        btnClose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Closing SecondScreen Activity
                finish();
            }
        });
        //this click enet will opent the list view of the religious places
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(viewLocaions);
            }
        });

        //this will open the add new location window
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(addLocaions);
            }
        });

    }

}
