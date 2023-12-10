package com.example.plantcareapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME = "Plant.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME1 = "users";
    private static final String TABLE_NAME2 = "details";


    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PLANT = "plant";
    private static final String COLUMN_DESC = "description";
    private static final String COLUMN_IMAGE = "image";


    public DBHelper(@Nullable Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = " CREATE TABLE "+ TABLE_NAME1 + "(" +
                COLUMN_EMAIL +" TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD +" TEXT)";
        db.execSQL(query1);

        String query2 = " CREATE TABLE "+ TABLE_NAME2 + "(" +
                COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_PLANT +" TEXT, "+
                COLUMN_DESC +" TEXT, "+
                COLUMN_IMAGE +" BLOB)";
        db.execSQL(query2);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop Table if exists " + TABLE_NAME1);
        db.execSQL("drop Table if exists " + TABLE_NAME2);
    }

    public Boolean insertData(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EMAIL,email);
        cv.put(COLUMN_PASSWORD,password);
        long result = db.insert(TABLE_NAME1,null,cv);
        if (result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Boolean checkEmail(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from users where email = ?", new String[]{email});

        if(cursor.getCount() >0){
            return true;
        }else{
            return false;
        }
    }

    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from users where email = ? and password = ?", new String[]{email, password});

        if(cursor.getCount() > 0){
            return true;
        }else {
            return false;
        }
    }

    public void addDetails(String name, String description, byte[] imageBytes){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PLANT, name);
        cv.put(COLUMN_DESC, description);
        cv.put(COLUMN_IMAGE, imageBytes);

        long result = db.insert(TABLE_NAME2,null,cv);
        if (result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
            updateIds();

        }
    }

    Cursor selectAddRecords(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = " SELECT * FROM "+TABLE_NAME2;

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query,null);
        }else{
            Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show();;
        }
        return cursor;
    }

    public void updateData(String id, String name, String desc, byte[] imageBytes){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PLANT,name);
        cv.put(COLUMN_DESC,desc);
        cv.put(COLUMN_IMAGE, imageBytes);

        long result = db.update(TABLE_NAME2,cv,COLUMN_ID + " = ?",new String[]{id});
        if(result==-1){
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void deletePlant(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME2, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        updateIds();
    }

    public void updateIds() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME2, null);

        if (cursor.moveToFirst()) {
            int newId = 1;
            do {
                int oldId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                ContentValues values = new ContentValues();
                values.put(COLUMN_ID, newId);
                db.update(TABLE_NAME2, values, COLUMN_ID + " = ?", new String[]{String.valueOf(oldId)});
                newId++;
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }


}

