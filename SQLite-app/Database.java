package com.example.helloworld;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static final String TAG = "DATABASE";
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VER = 1;

    private static final String TABLE_NAME = "dziennik";
    private static final String COLUMN_NAME = "nazwisko";

    private static final String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s (%s TEXT)",TABLE_NAME,COLUMN_NAME);
    private static final String DEL_TABLE = String.format("DROP TABLE IF EXISTS %s",TABLE_NAME);

    private static final String GET_TABLE = String.format("SELECT * FROM %s",TABLE_NAME);
    private final Context context;

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER); 
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DEL_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void insertText(String text){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME,text);
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);

        updateLastModifiedDate();
        Log.e(TAG,String.format("INSERTED DATA %s",text));
    }


    public ArrayList<String> getNames(){
        ArrayList<String> names = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(GET_TABLE,null);

        while(cursor.moveToNext()){
            int i = cursor.getColumnIndex(COLUMN_NAME);
            String name = cursor.getString(i);
            names.add(name);
        }

        return names;
    }

    public String getLastModifiedDate() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        long lastModifiedMillis = sharedPreferences.getLong("lastModified", 0);
        if (lastModifiedMillis == 0) {
            return "Brak danych";
        } else {
            Date lastModifiedDate = new Date(lastModifiedMillis);
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            return dateFormat.format(lastModifiedDate);
        }
    }

    public void updateLastModifiedDate() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("lastModified", System.currentTimeMillis());
        editor.apply();
    }
}
