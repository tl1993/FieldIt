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
    private double longitude;
    private double latitude;
    private String description;

    public Field(String id,String name,String type,double latitude,double longitude,String description)
    {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description=description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public  String getId(){return this.id;}
    public  String getName() { return this.name;}
    public  String getType() {return this.type;}
    public double getLongitude(){return this.longitude;}
    public double getLatitude() {return this.latitude;}
    public String  getDescription() {return this.description;}
     public Map<String, Object> toMap() {
         HashMap<String, Object> result = new HashMap<>();
         result.put("name",this.name);
         result.put("type",this.type);
         result.put("description",this.description);
         result.put("latitude",this.latitude);
         result.put("longitude",this.longitude);
         return result;
     }
}
