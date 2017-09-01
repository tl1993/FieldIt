package com.example.dell.fieldit.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dell on 17/08/2017.
 */

public class ReviewSql {
    final static String REVIEW_TABLE = "reviews";
    final static String REVIEW_ID = "_id";
    final static String REVIEW_TEXT = "text";
    final static String REVIEW_RATING = "rating";
    final static String REVIEW_FIELD_ID = "field_id";
    final static String REVIEW_USER_ID = "user_id";
    final static String REVIEW_USER_EMAIL = "user_email";

    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + REVIEW_TABLE + " (" +
                REVIEW_ID + " TEXT PRIMARY KEY," +
                REVIEW_TEXT + " TEXT," +
                REVIEW_RATING + " REAL," +
                REVIEW_FIELD_ID + " TEXT," +
                REVIEW_USER_ID + " TEXT, " +
                REVIEW_USER_EMAIL + " TEXT );");
    }

    public static void drop(SQLiteDatabase db) {

        // Drop the field table
        db.execSQL("drop table " + REVIEW_TABLE + ";");
    }

    public static List<Review> getFieldReviews(SQLiteDatabase db, String id) {

        String where = REVIEW_FIELD_ID + " = ?";
        String[] args = {id};
        // Get all fields
        Cursor cursor = db.query(REVIEW_TABLE, null, where, args, null, null, null);
        List<Review> reviews = new LinkedList<Review>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(REVIEW_ID);
            int textIndex = cursor.getColumnIndex(REVIEW_TEXT);
            int ratingIndex = cursor.getColumnIndex(REVIEW_RATING);
            int fieldIdIndex = cursor.getColumnIndex(REVIEW_FIELD_ID);
            int userIdIndex = cursor.getColumnIndex(REVIEW_USER_ID);
            int userEmailIndex = cursor.getColumnIndex(REVIEW_USER_EMAIL);
            do {
                String reviewId = cursor.getString(idIndex);
                String text = cursor.getString(textIndex);
                float rating = cursor.getFloat(ratingIndex);
                String fieldId = cursor.getString(fieldIdIndex);
                String userId = cursor.getString(userIdIndex);
                String userEmail = cursor.getString(userEmailIndex);
                Review review = new Review(reviewId, text, rating, fieldId, userId, userEmail);
                reviews.add(review);
            }
            while (cursor.moveToNext());
        }

        return reviews;
    }

        public static Review getReviewById(SQLiteDatabase db,String id)
    {
        String where = REVIEW_ID + " = ?";
        String[] args = {id};
        Cursor cursor = db.query(REVIEW_TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(REVIEW_ID);
            int textIndex = cursor.getColumnIndex(REVIEW_TEXT);
            int ratingIndex = cursor.getColumnIndex(REVIEW_RATING);
            int fieldIdIndex = cursor.getColumnIndex(REVIEW_FIELD_ID);
            int userIdIndex = cursor.getColumnIndex(REVIEW_USER_ID);
            int userEmailIndex = cursor.getColumnIndex(REVIEW_USER_EMAIL);
            String reviewId = cursor.getString(idIndex);
            String text = cursor.getString(textIndex);
            float rating = cursor.getFloat(ratingIndex);
            String fieldId = cursor.getString(fieldIdIndex);
            String userId = cursor.getString(userIdIndex);
            String userEmail = cursor.getString(userEmailIndex);
            Review review = new Review(reviewId, text, rating, fieldId, userId, userEmail);
            return review;

        }
        return null;


    }

    public static void addReview(SQLiteDatabase db, Review review) {
        // Add review to reviews table
        ContentValues values = new ContentValues();
        values.put(REVIEW_ID,review.getId());
        values.put(REVIEW_TEXT,review.getText());
        values.put(REVIEW_RATING,review.getStars());
        values.put(REVIEW_FIELD_ID,review.getField_id());
        values.put(REVIEW_USER_ID,review.getUser_id());
        values.put(REVIEW_USER_EMAIL,review.getUser_email());
        db.insertWithOnConflict(REVIEW_TABLE, REVIEW_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static void deleteReview(SQLiteDatabase db, String id){
        String where = REVIEW_FIELD_ID + " = ?";
        String[] args = {id};
        db.delete(REVIEW_TABLE, where, args);
    }

    public static double getLastUpdateDate(SQLiteDatabase db){

        // Get the last update time
        return LastUpdateSql.getLastUpdate(db, REVIEW_TABLE);
    }

    public static void setLastUpdateDate(SQLiteDatabase db, double date){

        // Set the last update time
        LastUpdateSql.setLastUpdate(db, REVIEW_TABLE, date);
    }
}
