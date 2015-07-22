package com.example.manoj.rnhlocator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;


public class MainActivity extends Activity {

    Button next;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=(EditText)findViewById(R.id.name);
        next=(Button)findViewById(R.id.btnNextScreen);

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), DatabaseActivity.class);

                //Sending data to another Activity
                nextScreen.putExtra("name", name.getText().toString());
               //nextScreen.putExtra("email", inputEmail.getText().toString());
                startActivity(nextScreen);
               // finish();
            }
        });
    }


}
