package com.example.manoj.rnhlocator;

/**
 * Created by Manoj on 7/22/2015.
 */
public class Location {

    //private variables or the attributes of the talbe
    int id;
    String name;
    String category;
    String description;
    String latitude;
    String longitude;

    // Empty constructor
    public Location() {

    }

    // constructor
    public Location(int id, String name, String category, String description, String latitude, String longitude) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // constructor to auto incremnet the id
    public Location(String name, String category, String description, String latitude, String longitude) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
