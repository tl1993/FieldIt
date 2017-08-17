package com.example.dell.fieldit.Model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 17/08/2017.
 */

public class Review {
    private String id;
    private String text;
    private double stars;
    private String field_id;
    private String user_id;

    public Review(String id,String text, double stars,String field_id,String user_id)
    {
        this.id = id;
        this.text = text;
        this.stars = stars;
        this.field_id = field_id;
        this.user_id = user_id;
    }

    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("text",this.text);
        result.put("stars",stars);
        result.put("field_id",field_id);
        result.put("user_id",user_id);
        return result;
    }
}
