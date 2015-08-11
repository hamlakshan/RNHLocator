package com.example.manoj.rnhlocator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class ViewInMapActivity extends Activity {

    private GoogleMap googleMap;
    double latitude, longitude;
    String name, religion, description;
    Location location;
    DatabaseHandler db;
    TextView nameText;
    TextView religionText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_map);

        db = new DatabaseHandler(this); //db handler class

        //the intent to obtain the data recerivd from the previous window
        Intent main = getIntent();

        //String name = main.getStringExtra("name");
        String id = main.getStringExtra("id");
        TextView lid;
        nameText = (TextView) findViewById(R.id.locationName);
        religionText = (TextView) findViewById(R.id.locationType);

        //String religion = main.getStringExtra("religion");

        getPlaceDetails(Integer.parseInt(id) + 1);    //get all the details of the paces represented by id

        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            googleMap.setMyLocationEnabled(true); // false to disable  shows my current location on the map
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14.0f)); //change the zoom level
            placeMarker(religion);  //place marker on the location
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    //this method is used to get the location details of the selected place from the database
    public void getPlaceDetails(int id) {

        location = db.getLocationByID(id);
        latitude = Double.parseDouble(location.latitude);
        longitude = Double.parseDouble(location.longitude);
        name = location.name;
        religion = location.religion;

        //set values for the text views.
        nameText.setText(name);
        religionText.setText(religion);
        //description=

    }

    public void placeMarker(String religion) {

        // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(name);

        // Changing marker icon
        if (religion == "Buddhist") {
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        }
        if (religion == "Catholic") {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.church));
        }
        if (religion == "Islam") {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mosque));
        }
        if (religion == "Hindu") {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.kovil));
        }
        if (religion == "Other") {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        }
// adding marker
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        googleMap.addMarker(marker);
    }
}
