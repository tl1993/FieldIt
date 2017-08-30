package com.example.dell.fieldit.Model;

/**
 * Created by dell on 12/08/2017.
 */
import android.database.sqlite.SQLiteDatabase;
import java.util.LinkedList;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
public class FieldSql {
    final static String FIELD_TABLE = "fields";
    final static String FIELD_ID = "_id";
    final static String FIELD_NAME = "name";
    final static String FIELD_TYPE = "type";
    final static String FIELD_LONGITUDE = "longitude";
    final static String FIELD_LATITUDE = "latitude";
    final static String FIELD_DESCRIPTION = "description";
    final static String FIELD_IMAGE_NAME = "image_name";
    final static String FIELD_USER_ID = "user_id";
    final static String FIELD_IS_LIGHTED = "is_lighted";

    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + FIELD_TABLE + " (" +
                FIELD_ID + " TEXT PRIMARY KEY," +
                FIELD_NAME + " TEXT," +
                FIELD_TYPE + " TEXT," +
                FIELD_LONGITUDE + " TEXT," +
                FIELD_LATITUDE + " TEXT," +
                FIELD_DESCRIPTION + " TEXT," +
                FIELD_IMAGE_NAME + " TEXT," +
                FIELD_USER_ID + " TEXT," +
                FIELD_IS_LIGHTED + " INT);");
    }

    public static void drop(SQLiteDatabase db) {

        // Drop the field table
        db.execSQL("drop table " + FIELD_TABLE + ";");
    }

    public static List<Field> getAllFields(SQLiteDatabase db) {

        // Get all fields
        Cursor cursor = db.query(FIELD_TABLE, null, null , null, null, null, null);
        List<Field> fields = new LinkedList<Field>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(FIELD_ID);
            int nameIndex = cursor.getColumnIndex(FIELD_NAME);
            int typeIndex = cursor.getColumnIndex(FIELD_TYPE);
            int descriptionIndex = cursor.getColumnIndex(FIELD_DESCRIPTION);
            int latitudeIndex = cursor.getColumnIndex(FIELD_LATITUDE);
            int longitudeIndex = cursor.getColumnIndex(FIELD_LONGITUDE);
            int imageNameIndex = cursor.getColumnIndex(FIELD_IMAGE_NAME);
            int userIdIndex = cursor.getColumnIndex(FIELD_USER_ID);
            int isLightedIndex = cursor.getColumnIndex(FIELD_IS_LIGHTED);
            do {
                String id = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);
                String type = cursor.getString(typeIndex);
                String description = cursor.getString(descriptionIndex);
                String latitude = cursor.getString(latitudeIndex);
                String longitude = cursor.getString(longitudeIndex);
                String imageName = cursor.getString(imageNameIndex);
                String userId = cursor.getString(userIdIndex);
                boolean isLighted = (cursor.getInt(isLightedIndex)==1);
                Field field = new Field(id,name, type,latitude,longitude,description,isLighted);
                field.setImageName(imageName);
                field.setUser_id(userId);
                fields.add(field);

            } while (cursor.moveToNext());
        }
        return fields;
    }

    public static Field getFieldById(SQLiteDatabase db, String id) {

        // Return the field by  id if exists
        // Otherwise - return null
        String where = FIELD_ID + " = ?";
        String[] args = {id};
        Cursor cursor = db.query(FIELD_TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(FIELD_ID);
            int nameIndex = cursor.getColumnIndex(FIELD_NAME);
            int typeIndex = cursor.getColumnIndex(FIELD_TYPE);
            int descriptionIndex = cursor.getColumnIndex(FIELD_DESCRIPTION);
            int latitudeIndex = cursor.getColumnIndex(FIELD_LATITUDE);
            int longitudeIndex = cursor.getColumnIndex(FIELD_LONGITUDE);
            int imageNameIndex = cursor.getColumnIndex(FIELD_IMAGE_NAME);
            int userIdIndex = cursor.getColumnIndex(FIELD_USER_ID);
            int isLightedIndex = cursor.getColumnIndex(FIELD_IS_LIGHTED);
                String fieldId = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);
                String type = cursor.getString(typeIndex);
                String description = cursor.getString(descriptionIndex);
                String latitude = cursor.getString(latitudeIndex);
                String longitude = cursor.getString(longitudeIndex);
                String imageName = cursor.getString(imageNameIndex);
                String userId = cursor.getString(userIdIndex);
                boolean isLighted = (cursor.getInt(isLightedIndex)==1);
                Field field = new Field(id,name, type,latitude,longitude,description,isLighted);
                field.setImageName(imageName);
                field.setUser_id(userId);
                return  field;
        }
        return null;
    }

    public static void addField(SQLiteDatabase db, Field field) {

        // Add field to fields table if there is no already fields with the same id
        // Otherwise - update the field
        ContentValues values = new ContentValues();
        values.put(FIELD_ID, field.getId());
        values.put(FIELD_NAME, field.getName());
        values.put(FIELD_TYPE, field.getType());
        values.put(FIELD_DESCRIPTION, field.getDescription());
        values.put(FIELD_LATITUDE, field.getLatitude());
        values.put(FIELD_LONGITUDE, field.getLongitude());
        values.put(FIELD_USER_ID, field.getUser_id());
        values.put(FIELD_IMAGE_NAME, field.getImageName());
        if(field.getIslighted())
        {
            values.put(FIELD_IS_LIGHTED,1);
        }
        else
        {
            values.put(FIELD_IS_LIGHTED,0);
        }

        db.insertWithOnConflict(FIELD_TABLE, FIELD_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static void deleteField(SQLiteDatabase db, String id) {

        // Delete the field from fields table
        String where = FIELD_ID + " = ?";
        String[] args = {id};
        db.delete(FIELD_TABLE, where, args);
    }

    public static void editField(SQLiteDatabase db, Field field) {
        addField(db, field);
    }

    public static double getLastUpdateDate(SQLiteDatabase db){

        // Get the last update time
        return LastUpdateSql.getLastUpdate(db, FIELD_TABLE);
    }
    public static void setLastUpdateDate(SQLiteDatabase db, double date){

        // Set the last update time
        LastUpdateSql.setLastUpdate(db, FIELD_TABLE, date);
    }
}
