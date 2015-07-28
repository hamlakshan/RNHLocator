package com.example.manoj.rnhlocator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Manoj on 7/17/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;



    // Database Name
    private static final String DATABASE_NAME = "rnhLocator.db";

    // Contacts table name
    private static final String TABLE_LOCATIONS = "location";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_RELIGION = "religion";
    private static final String KEY_LATITUDE="latitude";
    private static final String KEY_LONGITUDE="longitude";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_LOCATIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_RELIGION + " TEXT," +KEY_LATITUDE + " TEXT," +KEY_LONGITUDE + " TEXT" + ");";
        db.execSQL(CREATE_LOCATION_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);

        // Create tables again
        onCreate(db);
    }



    // Adding new touple or location data to the database
    void addLocation(Location location) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, location.getName()); // assign location name
        values.put(KEY_RELIGION, location.getReligion()); // religion
        values.put(KEY_LATITUDE,location.getLatitude());
        values.put(KEY_LONGITUDE,location.getLongitude());

        // Inserting Row
        db.insert(TABLE_LOCATIONS,null,values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public Location getLocationByID(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOCATIONS, new String[] { KEY_ID,
                        KEY_NAME, KEY_RELIGION, KEY_LATITUDE, KEY_LONGITUDE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Location location = new Location(Integer.parseInt(cursor.getString(0)),cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4));
        // return contact
        return location;
    }

    // Getting All Contacts
    public List<Location> getAllLocations() {
        List<Location> locationList = new ArrayList<Location>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LOCATIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Location location = new Location();
                location.setId(Integer.parseInt(cursor.getString(0)));
                location.setName(cursor.getString(1));
                location.setReligion(cursor.getString(2));
                location.setLatitude(cursor.getString(3));
                location.setLongitude(cursor.getString(4));
                // Adding contact to list
                locationList.add(location);
            } while (cursor.moveToNext());
        }

        // return contact list
        return locationList;
    }

    // Updating single contact
    public int updateLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, location.getName());
        values.put(KEY_RELIGION, location.getReligion()); // religion
        values.put(KEY_LATITUDE,location.getLatitude());
        values.put(KEY_LONGITUDE,location.getLongitude());

        // updating row
        return db.update(TABLE_LOCATIONS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(location.getId()) });
    }

    // Deleting single contact
    public void deleteLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCATIONS, KEY_ID + " = ?", new String[] { String.valueOf(location.getId()) });
        db.close();
    }


    // Getting contacts Count
    public int getLocationCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOCATIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
