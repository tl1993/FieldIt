package com.example.dell.fieldit.Model;

/**
 * Created by dell on 12/08/2017.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LastUpdateSql {
    final static String LAST_UPDATE_TABLE = "last_update";
    final static String LAST_UPDATE_TABLE_TNAME = "table_name";
    final static String LAST_UPDATE_TABLE_DATE = "date";

    static public void create(SQLiteDatabase db) {

        // Create the last update time table
        db.execSQL("create table " + LAST_UPDATE_TABLE + " (" +
                LAST_UPDATE_TABLE_TNAME + " TEXT PRIMARY KEY," +
                LAST_UPDATE_TABLE_DATE + " NUMERIC);" );
    }

    public static void drop(SQLiteDatabase db) {

        // Drop the last update time table
        db.execSQL("drop table " + LAST_UPDATE_TABLE + ";");
    }

    public static double getLastUpdate(SQLiteDatabase db, String tableName) {

        // Get the last update time
        String[] args = {tableName};
        Cursor cursor = db.query(LAST_UPDATE_TABLE, null, LAST_UPDATE_TABLE_TNAME + " = ?",args , null, null, null);

        if (cursor.moveToFirst()) {
            return cursor.getDouble(cursor.getColumnIndex(LAST_UPDATE_TABLE_DATE));
        }
        return 0;
    }

    public static void setLastUpdate(SQLiteDatabase db, String table, double date) {

        // Set the last update time
        ContentValues values = new ContentValues();
        values.put(LAST_UPDATE_TABLE_TNAME, table);
        values.put(LAST_UPDATE_TABLE_DATE, date);

        db.insertWithOnConflict(LAST_UPDATE_TABLE,LAST_UPDATE_TABLE_TNAME,values,SQLiteDatabase.CONFLICT_REPLACE);
    }
}