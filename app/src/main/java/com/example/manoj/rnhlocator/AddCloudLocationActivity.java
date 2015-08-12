package com.example.manoj.rnhlocator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AddCloudLocationActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    private static final String TAG_LOG = "myview";

    JSONParser jsonParser = new JSONParser();

    Spinner dropDown;

    Button getCordinate;
    Button markOnmap;
    Button exit;
    Button btnAddLocation;

    EditText inputName;
    EditText inputDesc;
    EditText inputLatitude;
    EditText inputLongitude;

    GoogleMap googleMap;

    GPSTracker gps;

    // url to create new product
    private static String url_create_location = "http://www.rnhlocator.site88.net/create_location.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_locations);
        addSpinner();
        Log.d(TAG_LOG, "on create finished");

        // Edit Text
        inputName = (EditText) findViewById(R.id.name);
        inputDesc = (EditText) findViewById(R.id.descriptionTxt);
        inputLatitude = (EditText) findViewById(R.id.latitude);
        inputLongitude = (EditText) findViewById(R.id.longitude);

        // Create button
        btnAddLocation = (Button) findViewById(R.id.btnadd);
        getCordinate = (Button) findViewById(R.id.btnGetLocation);  //this button id used to add the location codrinates ising GPS tracker
        markOnmap = (Button) findViewById(R.id.gotomap);    //this button is used to add the location codinates using by touching the map
        exit = (Button) findViewById(R.id.btnexit);

        // button click event
        btnAddLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new location in background thread
                Log.d(TAG_LOG, "add button pressed");
                new CreateNewLocation().execute();
            }
        });

        //when user paress the generate Codinates button
        getCordinate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create gps class object
                gps = new GPSTracker(AddCloudLocationActivity.this);

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

                inputLatitude.setText(Double.toString(gps.getLatitude()));
                inputLongitude.setText(Double.toString(gps.getLongitude()));

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
                        inputLatitude.setText(Double.toString(position.latitude));   //change the edit text
                        inputLongitude.setText(Double.toString(position.longitude)); //change the edit text
                    }
                });


            }
        }
    }

    /**
     * Background Async Task to Create new product
     */
    class CreateNewLocation extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            Log.d(TAG_LOG, "pre execute");
            super.onPreExecute();
            pDialog = new ProgressDialog(AddCloudLocationActivity.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {

            String name = inputName.getText().toString();
            String religion = dropDown.getSelectedItem().toString();
            String description = inputDesc.getText().toString();
            String latitude = inputLatitude.getText().toString();
            String longitude = inputLongitude.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("religion", religion));
            params.add(new BasicNameValuePair("description", description));
            params.add(new BasicNameValuePair("latitude", latitude));
            params.add(new BasicNameValuePair("longitude", longitude));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_location, "POST", params);

            // check log cat fro response
            Log.d(TAG_LOG, json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), ViewCloudLocationsActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
