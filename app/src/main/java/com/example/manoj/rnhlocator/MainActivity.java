package com.example.manoj.rnhlocator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

//this is the main activity which runs when the application starts.
public class MainActivity extends ActionBarActivity {


    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;

    Button login;   //the login button
    Button quickSearch; //the quick search button

    private static final String TAG_LOG = "myview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());
        checkInternet();    //check weather internet connection is available

        //button definition
        login = (Button) findViewById(R.id.btnLoginScreen);
        quickSearch = (Button) findViewById(R.id.btnQuickSearch);

        //when begin button pressed
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starting a new Intent
                Intent loginScreen = new Intent(getApplicationContext(), LoginWindowActivity.class);
                //loads the login window
                Log.d(TAG_LOG, "started");
                startActivity(loginScreen); //load the new window
                // finish();
            }
        });

        //when quic search activity pressed
        quickSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quickSearchWindow = new Intent(getApplicationContext(), QuickSearchActivity.class);
                //loads the quick search window
                startActivity(quickSearchWindow);
            }
        });
    }

    public void checkInternet() {
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (!isInternetPresent) {
            // Internet connection is not present
            // Ask user to connect to Internet
            showAlertDialog(MainActivity.this, "No Internet Connection", "You need active internet to use this app.Please turn on your internet", false);
        }
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon(R.drawable.alert);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //text.setText("chahnge text");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
