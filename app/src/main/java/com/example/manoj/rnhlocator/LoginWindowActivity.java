package com.example.manoj.rnhlocator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class LoginWindowActivity extends Activity {

    //button definition
    Button authenticate;
    Button signup;
    Button toStandaloneApp;
    //edit text field definition
    EditText username;
    EditText password;
    //text field definiton
    TextView message;
    //used to encrypt the passwords
    private static MessageDigest md;

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object to make JSON communications
    JSONParser jParser = new JSONParser();
    //the link to the relevant php file in the web host
    private static String url_authenticate = "http://www.rnhlocator.site88.net/validate_user.php";
    //the TAGs used commonly in the code
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ID = "user_id";
    private static final String TAG_LOG = "myview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_window);

        //button innitialization
        authenticate = (Button) findViewById(R.id.btnAuthenticate);
        signup = (Button) findViewById(R.id.btnCloudSignup);
        toStandaloneApp = (Button) findViewById(R.id.btnToSandalondeApp);

        username = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);

        message = (TextView) findViewById(R.id.error_message);
        //on button click
        authenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this method will verify the entered data and provide access to the cloud storage
                message.setText("");
                new AuthenticateUser().execute();
            }
        });
        //on button click
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupwindow = new Intent(getApplicationContext(), CloudSignUpActivity.class);
                startActivity(signupwindow);
            }
        });
        //on button click
        toStandaloneApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create new intent to get the screen databaseActivity
                Intent databaseWindow = new Intent(getApplicationContext(), DatabaseActivity.class);
                startActivity(databaseWindow);

            }
        });


    }

    //this method will encrypt the password
    public static String cryptWithMD5(String pass) {
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] passBytes = pass.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digested.length; i++) {
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Log.d(TAG_LOG, "error encrypting the password");
            // Logger.getLogger(CryptWithMD5.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void clear() {
        Toast toast = Toast.makeText(getApplicationContext(), "username or the password is incorrect", Toast.LENGTH_SHORT);
        toast.show();
        message.setText("username or the password is incorrect");
        username.setText("");
        password.setText("");
    }

    //this inner class is used to validate the user and log in to the account
    class AuthenticateUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        int success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginWindowActivity.this);
            //this message will be shown in the loading window
            pDialog.setMessage("Validating user...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All locations from url
         */
        protected String doInBackground(String... args) {
            // Building Parameter

            String name = username.getText().toString();
            String pwd = cryptWithMD5(password.getText().toString());

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_name", name));
            params.add(new BasicNameValuePair("password", pwd));
            //output to get understanding of the process
            Log.d(TAG_LOG, "user name and pwd " + name + "  " + pwd);
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_authenticate, "POST", params);

            // Check your log cat for JSON reponse
            Log.d(TAG_LOG, json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    String id = json.getString(TAG_ID);
                    //if user name and password are correct the window cloud access will be opened
                    Intent cloudWindow = new Intent(getApplicationContext(), CloudDatabaseActivity.class);
                    cloudWindow.putExtra("id", id);
                    startActivity(cloudWindow);
                    // finish();

                } else if (success == 0) {
                    //if the system fails to authenticate
                    Log.d(TAG_LOG, "error username or pwd");
                }
            } catch (JSONException e) {
                Log.d(TAG_LOG, "exception occured");
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
            if (success == 0)
                clear();
        }

    }

}
