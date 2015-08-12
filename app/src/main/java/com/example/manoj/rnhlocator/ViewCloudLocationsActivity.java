package com.example.manoj.rnhlocator;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewCloudLocationsActivity extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> locationList;

    // url to get all locations list from cloud database
    private static String url_all_locations = "http://www.rnhlocator.site88.net/view_location.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_LOCATIONS = "location";
    private static final String TAG_LID = "lid";
    private static final String TAG_NAME = "name";
    private static final String TAG_RELIGION = "religion";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_LATITUDES = "latitudes";
    private static final String TAG_LONGITUDES = "longitudes";

    // products JSONArray
    JSONArray location = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locaion_listview);

        // Hashmap for ListView
        locationList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
        new LoadAllLocations().execute();

        // Get listview
        ListView lv = getListView();

        // on seleting single location
        // launching the single location activity
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String lid = ((TextView) view.findViewById(R.id.lid)).getText().toString();
                String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
                String religion = ((TextView) view.findViewById(R.id.religion)).getText().toString();
               // String description = ((TextView) view.findViewById(R.id.description)).getText().toString();
             //   String latitude = ((TextView) view.findViewById(R.id.latitude)).getText().toString();
             //   String longitude = ((TextView) view.findViewById(R.id.longitude)).getText().toString();

                // Starting new intent
             //   Intent in = new Intent(getApplicationContext(), EditProductActivity.class);
                // sending pid to next activity
             //   in.putExtra(TAG_LID, lid);

                // starting new activity and expecting some response back
              //  startActivityForResult(in, 100);
            }
        });

    }

    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    /**
     * Background Async Task to Load all locations by making HTTP Request
     */
    class LoadAllLocations extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewCloudLocationsActivity.this);
            //this message will be shown in the loading window
            pDialog.setMessage("Loading locations. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All locations from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_locations, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All locations: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    location = json.getJSONArray(TAG_LOCATIONS);

                    // looping through All Products
                    for (int i = 0; i < location.length(); i++) {
                        JSONObject c = location.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_LID);
                        String name = c.getString(TAG_NAME);
                        String religion = c.getString(TAG_RELIGION);
                        String description = c.getString(TAG_DESCRIPTION);
                        String latitude = c.getString(TAG_LATITUDES);
                        String longitude = c.getString(TAG_LONGITUDES);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_LID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_RELIGION, religion);
                        map.put(TAG_DESCRIPTION, description);
                        map.put(TAG_LATITUDES, latitude);
                        map.put(TAG_LONGITUDES, longitude);

                        // adding HashList to ArrayList
                        locationList.add(map);
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(), AddCloudLocationActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
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
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            ViewCloudLocationsActivity.this, locationList, R.layout.locaion_list_item, new String[]{TAG_LID,
                            TAG_NAME,TAG_RELIGION}, new int[]{R.id.lid, R.id.name,R.id.religion});
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }

}
