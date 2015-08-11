package com.example.manoj.rnhlocator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class LoginWindowActivity extends Activity {

    Button authenticate;
    Button toStandaloneApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_window);

        authenticate=(Button)findViewById(R.id.btnAuthenticate);
        toStandaloneApp=(Button)findViewById(R.id.btnToSandalondeApp);

        toStandaloneApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create new intent to get the screen databaseActivity
                Intent databaseWindow= new Intent(getApplicationContext(),DatabaseActivity.class);
                startActivity(databaseWindow);

            }
        });

        authenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });


    }
//this method will verify the entered data and provide access to the cloud storage
    public void verifyUser(){
        
    }

}
