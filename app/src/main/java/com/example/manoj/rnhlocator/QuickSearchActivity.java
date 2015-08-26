package com.example.manoj.rnhlocator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;


public class QuickSearchActivity extends Activity {
    /*this activity locads the quick shrarch window which will show all the avialable places in the datadase
    * which are in 10KM readius to  the current location*/

    private GoogleMap googleMap;
    double latitude, longitude,my_latitude,my_longitude;
    String name, category, description, id;
    DatabaseHandler db;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_search_map);
        gps = new GPSTracker(QuickSearchActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            my_latitude = gps.getLatitude();
            my_longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + my_latitude + "\nLong: " + my_longitude, Toast.LENGTH_LONG).show();

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        db = new DatabaseHandler(this); //db handler class

        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //the method to innitialize the map
    public void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            googleMap.setMyLocationEnabled(true); // false to disable  shows my current location on the map
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(6.7903917f, 79.9005859f), 14.0f)); //innitial zoom level

            markPlaces();
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //this method palces the correct marker on the map

    public void placeMarker() {

// create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(name);

        // Changing marker icon according to the religious place
        if (category == "Buddhist") {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.temple));
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
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        googleMap.addMarker(marker);
    }

    public void markPlaces() {
        List<Location> locations = db.getAllLocations();    //list to get the location object
        for (Location ln : locations) {

            id = Integer.toString(ln.getId());
            name = ln.getName();
            category = ln.getCategory();
            latitude = Double.parseDouble(ln.getLatitude());
            longitude = Double.parseDouble(ln.getLongitude());


            placeMarker();  //add the place marker each separate place

            // adding the location to the map


        }
    }

    public long getDistanceMeters(double lat1, double lng1) {

        double l1 = toRadians(lat1);
        double l2 = toRadians(my_latitude);
        double g1 = toRadians(lng1);
        double g2 = toRadians(my_longitude);

        double dist = acos(sin(l1) * sin(l2) + cos(l1) * cos(l2) * cos(g1 - g2));
        if (dist < 0) {
            dist = dist + Math.PI;
        }

        return Math.round(dist * 6378100);
    }


}
