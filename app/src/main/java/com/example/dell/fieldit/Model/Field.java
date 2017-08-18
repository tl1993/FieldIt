package com.example.dell.fieldit.Model;

/**
 * Created by dell on 26/07/2017.
 */
import android.widget.TimePicker;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
public class Field {

    private String id;
    private String name;
    private String type;
    private String userId;
    private String longitude;
    private String latitude;
    private String description;
    private String imageName;
    private boolean isLighted;
    private double lastUpdated;
    private boolean isDeleted;

    public Field() {

    }

    public Field(String name,String type,String latitude,String longitude,String description,boolean isLighted)
    {
        this.name = name;
        this.type = type;
        this.description=description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isLighted = isLighted;
    }

    public Field(String id,String name,String type,String latitude,String longitude,String description,boolean isLighted)
    {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description=description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isLighted = isLighted;
    }
    // Getters
    public  String getId(){return this.id;}
    public  String getName() { return this.name;}
    public  String getType() {return this.type;}
    public String getLongitude(){return this.longitude;}
    public String getLatitude() {return this.latitude;}
    public String  getDescription() {return this.description;}
    public String getImageName() { return this.imageName;}
    public String getUserId() { return this.userId;}
    public boolean getIslighted() { return this.isLighted;}
    public double getLastUpdated() { return this.lastUpdated;}
    public boolean getIsDeleted() {return this.isDeleted;}
    // Setters
    public void setId(String id) { this.id = id;}
    public void setName(String name) { this.name = name;}
    public void setType(String type) { this.type = type;}
    public void setLongitude(String longitude) { this.longitude = longitude;}
    public void setLatitude(String latitude) { this.latitude = latitude;}
    public void setDescription(String description) { this.description = description;}
    public void setImageName(String imageName){ this.imageName = imageName;}
    public void setLastUpdated(double lastUpdated) { this.lastUpdated = lastUpdated;}
    public void setIsDeleted(boolean isDeleted){ this.isDeleted = isDeleted;}
    public void setUserId(String userId) { this.userId = userId;}
    public void setLighted(boolean isLighted){this.isLighted = isLighted;}

     public Map<String, Object> toMap() {
         HashMap<String, Object> result = new HashMap<>();
         result.put("name",this.name);
         result.put("type",this.type);
         result.put("description",this.description);
         result.put("latitude",this.latitude);
         result.put("longitude",this.longitude);
         result.put("user_id",this.userId);
         result.put("is_lighted",this.isLighted);
         return result;
     }
}
