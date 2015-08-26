package com.example.manoj.rnhlocator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

//this is the main activity which runs when the application starts.
public class MainActivity extends Activity {

    Button login;   //the login button
    Button quickSearch; //the quick search button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //button definition
        login=(Button)findViewById(R.id.btnLoginScreen);
        quickSearch=(Button)findViewById(R.id.btnQuickSearch);

        //when begin button pressed
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starting a new Intent
                Intent loginScreen = new Intent(getApplicationContext(),LoginWindowActivity.class);
                //loads the login window
                startActivity(loginScreen); //load the new window
                // finish();
            }
        });

        //when quic search activity pressed
        quickSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quickSearchWindow= new Intent(getApplicationContext(),QuickSearchActivity.class);
                //loads the quick search window
                startActivity(quickSearchWindow);
            }
        });
    }


}
