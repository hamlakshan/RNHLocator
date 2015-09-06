package com.example.manoj.rnhlocator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;


public class AddLocationsActivity extends Activity {

    Spinner dropDown;
    Button addnew;
    Button getCordinate;
    Button markOnmap;
    Button exit;

    EditText name;
    EditText description;
    EditText latitude;
    EditText longitude;

    DatabaseHandler db;
    private GoogleMap googleMap;

    private static final String TAG_LOG = "myview";

    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_locations_window);
        addSpinner();

        addnew = (Button) findViewById(R.id.btnadd);        //this button is used to add the new location
        getCordinate = (Button) findViewById(R.id.btnGetLocation);  //this button id used to add the location codrinates ising GPS tracker
        markOnmap = (Button) findViewById(R.id.gotomap);    //this button is used to add the location codinates using by touching the map
        exit = (Button) findViewById(R.id.btnexit);

        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.descriptionTxt);
        latitude = (EditText) findViewById(R.id.latitude);
        longitude = (EditText) findViewById(R.id.longitude);


        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG_LOG, "add button pressed");
                showAlert(AddLocationsActivity.this);    //opens the alert window

            }
        });
        //when user paress the generate Codinates button
        getCordinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create gps class object
                gps = new GPSTracker(AddLocationsActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

                latitude.setText(Double.toString(gps.getLatitude()));
                longitude.setText(Double.toString(gps.getLongitude()));

            }
        });

        //when user paress the mark on map function
        markOnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Loading map
                    initilizeMap();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        //when exit button is pressed the current window will be closed
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    //this method is used to add new user data to the database
    public void addData() {

        db = new DatabaseHandler(this);
        String religion = dropDown.getSelectedItem().toString();

        //add new data touple it to the data base as a Location object
        db.addLocation(new Location(name.getText().toString(), religion, description.getText().toString(), latitude.getText().toString(), longitude.getText().toString()));
        // showAlert();
    }


    //this method used to create the dropdown menu to select the religion
    public void addSpinner() {
        dropDown = (Spinner) findViewById(R.id.religion_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.religions, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        dropDown.setAdapter(adapter);

        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    //this method is used to show the map if user wants to mark the palce on the map
    public void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // check if map is created successfully or not
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            googleMap.setMyLocationEnabled(true); // false to disable  shows my current location on the map
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(6.7903917f, 79.9005859f), 14.0f));


            if (googleMap == null) {
                Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            } else {
                //the option to touch the maps and find mark the location

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng position) {
                        Toast.makeText(getApplicationContext(), position.latitude + " : " + position.longitude, Toast.LENGTH_SHORT).show();   //show the marked pace in the toast
                        latitude.setText(Double.toString(position.latitude));   //change the edit text
                        longitude.setText(Double.toString(position.longitude)); //change the edit text
                    }
                });


            }
        }
    }

    //this method is used to alert the user before entering the data to the database
    public void showAlert(Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Adding new data");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to add new location");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.alert);

        // On pressing Settings button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (name.getText() != null && latitude.getText() != null && longitude.getText() != null) {
                    addData();  //call the methd to add data to the database
                }

            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        Log.d(TAG_LOG, "im here");
        // alertDialog.show();
        AlertDialog alert11 = alertDialog.create();
        alert11.show();
    }


}
