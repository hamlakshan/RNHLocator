package com.example.manoj.rnhlocator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    String name, category, description;

    Location location;
    DatabaseHandler db;

    TextView nameText;
    TextView categoryText;
    TextView descriptionText;

    Button update;
    Button delete;

    String location_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_map);

        update = (Button) findViewById(R.id.btnupdate);
        delete = (Button) findViewById(R.id.btndelete);

        db = new DatabaseHandler(this); //db handler class

        //the intent to obtain the data recerivd from the previous window
        Intent main = getIntent();

        //String name = main.getStringExtra("name");
        location_id = main.getStringExtra("id");

        nameText = (TextView) findViewById(R.id.locationName);
        categoryText = (TextView) findViewById(R.id.locationType);
        descriptionText = (TextView) findViewById(R.id.locationdescription);

        //String religion = main.getStringExtra("religion");

        getPlaceDetails(Integer.parseInt(location_id)+1);    //get all the details of the paces represented by id

        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//the location loaded will be deleted from the databse
                db.deleteLocation(location);

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateWindow = new Intent(getApplicationContext(), UpdateLocationActivity.class);
                updateWindow.putExtra("location_id", location_id);
                updateWindow.putExtra("name", name);
                updateWindow.putExtra("description", description);
                updateWindow.putExtra("lat", latitude);
                updateWindow.putExtra("lon", longitude);
                startActivity(updateWindow);
                finish();

            }
        });


    }

    public void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            googleMap.setMyLocationEnabled(true); // false to disable  shows my current location on the map
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14.0f)); //change the zoom level
            placeMarker(category);  //place marker on the location
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
        name = location.getName();
        category = location.getCategory();
        description = location.getDescription();

        //set values for the text views.
        nameText.setText(name);
        categoryText.setText(category);
        descriptionText.setText(description);
        //description=

    }

    public void placeMarker(String category) {

        // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(name);

        // Changing marker icon
        if (category == "Buddhist") {
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        }
        if (category == "Catholic") {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.church));
        }
        if (category == "Islam") {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.mosque));
        }
        if (category == "Hindu") {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.kovil));
        }
        if (category == "Other") {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
        }
// adding marker
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        googleMap.addMarker(marker);
    }
}
