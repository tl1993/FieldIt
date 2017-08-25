package com.example.dell.fieldit.Model;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 17/08/2017.
 */

public class Review {
    private String id;
    private String text;
    private int stars;
    private String field_id;
    private String user_id;
    private boolean isDeleted;
    private double lastUpdated;

    public Review(String id,String text, int stars,String field_id,String user_id)
    {
        this.id = id;
        this.text = text;
        this.stars = stars;
        this.field_id = field_id;
        this.user_id = user_id;
    }

    public String getId() { return this.id;}
    public String getText() { return this.text;}
    public String getField_id() { return this.field_id;}
    public String getUser_id() { return this.user_id;}
    public int getStars() { return this.stars;}
    public boolean getIsDeleted(){return this.isDeleted;}
    public double getLastUpdated() {return this.lastUpdated;}

    public void setId(String id){ this.id = id;}

    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("text",this.text);
        result.put("stars",stars);
        result.put("field_id",field_id);
        result.put("user_id",user_id);
        result.put("lastUpdated", ServerValue.TIMESTAMP);
        result.put("is_deleted",this.isDeleted);
        return result;
    }
}
