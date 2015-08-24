package com.example.manoj.rnhlocator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;


public class MainActivity extends Activity {

    Button login;
    Button quickSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login=(Button)findViewById(R.id.btnLoginScreen);
        quickSearch=(Button)findViewById(R.id.btnQuickSearch);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starting a new Intent
                Intent loginScreen = new Intent(getApplicationContext(), LoginWindowActivity.class);
                startActivity(loginScreen); //load the new window
                // finish();
            }
        });

        quickSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quickSearchWindow= new Intent(getApplicationContext(),QuickSearchActivity.class);
                startActivity(quickSearchWindow);
            }
        });
    }


}
