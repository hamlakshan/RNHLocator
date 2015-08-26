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
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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

    String user_id;

    // url to get all locations list from cloud database
    private static String url_all_locations = "http://www.rnhlocator.site88.net/view_location.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_LOCATIONS = "location";
    private static final String TAG_LID = "lid";
    private static final String TAG_NAME = "name";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_USER = "user_id";
    private static final String TAG_LATITUDES = "latitudes";
    private static final String TAG_LONGITUDES = "longitudes";


    private static final String TAG_LOG = "myview";

    // products JSONArray
    JSONArray location = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        //the intent to obtain the data recerivd from the previous window
        Intent main = getIntent();
        //String name = main.getStringExtra("name");
        user_id = main.getStringExtra("id");
        Log.d(TAG_LOG, "user id: " + user_id);

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

                // Starting new intent
                Intent viewinmap = new Intent(getApplicationContext(), ViewCloudInMapActivity.class);
                // sending pid to next activity
                viewinmap.putExtra("id", lid);
                Log.d(TAG_LOG, "sent location_id is: " + lid);
                // starting new activity and expecting some response back
                startActivity(viewinmap);

            }
        });

    }

    /**
     * Background Async Task to Load all locations by making HTTP Request
     */
    class LoadAllLocations extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        int success;

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
            params.add(new BasicNameValuePair("user_id", user_id));

            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_locations, "POST", params);

            // Check your log cat for JSON reponse
            Log.d(TAG_LOG, json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    location = json.getJSONArray(TAG_LOCATIONS);

                    //looping through All Products
                    for (int i = 0; i < location.length(); i++) {

                        JSONObject c = location.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_LID);
                        String name = c.getString(TAG_NAME);
                        String category = c.getString(TAG_CATEGORY);
                        String description = c.getString(TAG_DESCRIPTION);
                        String latitude = c.getString(TAG_LATITUDES);
                        String longitude = c.getString(TAG_LONGITUDES);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_LID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_CATEGORY, category);
                        map.put(TAG_DESCRIPTION, description);
                        map.put(TAG_LATITUDES, latitude);
                        map.put(TAG_LONGITUDES, longitude);
                        // adding HashList to ArrayList
                        locationList.add(map);
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    Log.d(TAG_LOG, "no location result found");
                    Intent moveback = new Intent(getApplicationContext(), CloudDatabaseActivity.class);
                    // Closing all previous activities
                    moveback.putExtra("id", user_id);
                    moveback.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(moveback);
                }
            } catch (JSONException e) {
                Log.d(TAG_LOG, "exception occur in viewcloudlocationActivity");
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
            if (success == 0) {
                Toast toast = Toast.makeText(getApplicationContext(), "no location found", Toast.LENGTH_SHORT);
                toast.show();
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            ViewCloudLocationsActivity.this, locationList,
                            R.layout.cloud_locations_list_item, new String[]{TAG_LID,
                            TAG_NAME, TAG_CATEGORY},
                            new int[]{R.id.lid, R.id.name, R.id.category});
                    // updating listview
                    setListAdapter(adapter);
                }
            });
        }

    }

}
