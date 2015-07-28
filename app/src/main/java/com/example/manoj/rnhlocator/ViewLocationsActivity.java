package com.example.manoj.rnhlocator;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ViewLocationsActivity extends Activity {

    ListView listView;
    ArrayList<HashMap<String, String>> locationList;
    DatabaseHandler db;
    String id, name, religion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locaion_listview);

        db = new DatabaseHandler(this); //db handler class
        listView = (ListView) findViewById(R.id.list); //to repeset data in a list view
        locationList = new ArrayList<HashMap<String, String>>();    //the array lsit keep the details of each laocation


        //listView = getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting values from selected ListItem
                //String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
                // String lid = ((TextView) view.findViewById(R.id.lid)).getText().toString();
                //  String religion = ((TextView) view.findViewById(R.id.religion)).getText().toString();

                //name=String.valueOf(parent.getItemAtPosition(position));

                // Starting the activity to show the location in map
                Intent mapView = new Intent(getApplicationContext(), ViewInMapActivity.class);

                mapView.putExtra("id", Integer.toString(position));
                startActivity(mapView);

            }
        });

        showLocations();


    }

    public void showLocations() {
        List<Location> locations = db.getAllLocations();    //list to get the location object

        for (Location ln : locations) {
            HashMap<String, String> location = new HashMap<String, String>();


            id = Integer.toString(ln.getId());
            name = ln.getName();
            religion = ln.getReligion();


            // adding the lcation details to hash map list hash map
            location.put("id", id);
            location.put("name", name);
            location.put("religion", religion);

            locationList.add(location); //add the hashmap to the location list array list
        }


        //the adapter class to connect wit the list_view in locaion_listview.xml
        ListAdapter listAdapter = new SimpleAdapter(ViewLocationsActivity.this, locationList,
                R.layout.locaion_list_item, new String[]{"id", "name",
                "religion"}, new int[]{R.id.lid, R.id.name, R.id.religion});


        listView.setAdapter(listAdapter);   //pass the data in the adapter to the list view

    }

}
