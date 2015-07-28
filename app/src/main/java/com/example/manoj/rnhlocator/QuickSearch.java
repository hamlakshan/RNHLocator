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


public class QuickSearch extends Activity {
    /*this activity locads the quick shrarch window which will show all the avialable places in the datadase
    * which are in 10KM readius to  the current location*/

    private GoogleMap googleMap;
    double latitude, longitude;
    String name, religion, description, id;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quick_search_map);

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
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f)); //change the zoom level

            markPlaces();
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    //this method palces the correct marker on the map

    public void placeMarker() {

// create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(name);

        // Changing marker icon according to the religious place
        if (religion == "Buddhist") {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.temple));
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
        googleMap.addMarker(marker);
    }

    public void markPlaces() {
        List<Location> locations = db.getAllLocations();    //list to get the location object
        for (Location ln : locations) {

            id = Integer.toString(ln.getId());
            name = ln.getName();
            religion = ln.getReligion();
            latitude = Double.parseDouble(ln.getLatitude());
            longitude = Double.parseDouble(ln.getLongitude());

            placeMarker();  //add the place marker each separate place


            // adding the location to the map


        }
    }


}
