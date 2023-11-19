package com.example.myfirstapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MyDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database";
    private static final int VERSION = 1;
    private static final String TABLE_NAME = "TODO";
    private static final String KEYID = "ID";
    private static final String KEY_ACTIVITY = "ACTION";
    private static final String KEY_TIME = "ACT_TIME";
    private static final String KEY_DATE = "ACT_DATE";

    public MyDB(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "("+ KEYID + " INTEGER NOT NULL," + KEY_ACTIVITY + " TEXT NOT NULL," + KEY_TIME + " TEXT NOT NULL, "+ KEY_DATE +" TEXT NOT NULL);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE  IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertdb(int id,String act, String tim,String date) {
        SQLiteDatabase database = this.getWritableDatabase();
        ArrayList<Activity> actarray =fetching();
        ContentValues values = new ContentValues();
        values.put(KEYID,actarray.size()+1);
        values.put(KEY_ACTIVITY, act);
        values.put(KEY_TIME, tim);
        values.put(KEY_DATE,date);
        database.insert(TABLE_NAME, null, values);
    }

    public ArrayList<Activity> fetching() {
        SQLiteDatabase dataf = this.getReadableDatabase();
        Cursor cursor = dataf.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<Activity> actarray = new ArrayList<>();
        while (cursor.moveToNext()) {
            Activity object = new Activity();
            object.id=cursor.getInt(0);
            object.act=cursor.getString(1);
            object.time=cursor.getString(2);
            object.date=cursor.getString(3);
            actarray.add(object);
        }
        return actarray;
    }
    public void updateact(Activity obj){
        SQLiteDatabase dataupdate=this.getWritableDatabase();
        ContentValues value=new ContentValues();
        //value.put(KEYID,obj.id);
        value.put(KEY_ACTIVITY,obj.act);
        value.put(KEY_TIME,obj.time);
        value.put(KEY_DATE,obj.date);
        dataupdate.update(TABLE_NAME,value,KEYID+"="+obj.id,null);
    }
    public void deleteact(int id){
        SQLiteDatabase datadelete=this.getWritableDatabase();
        datadelete.delete(TABLE_NAME,KEYID+"="+id,null);
//        for(int i=id+1;i<=dataset.size()-1;++i){
//            ContentValues value=new ContentValues();
//            value.put(KEYID,i-1);
//            value.put(KEY_ACTIVITY,dataset.get(i).act);
//            value.put(KEY_TIME,dataset.get(i).time);
//            datadelete.update(TABLE_NAME,value,KEY_ACTIVITY+"="+dataset.get(i).act,null);
//        }
        ArrayList<Activity> data=fetching();
        for(int i=id;i<data.size();i++){
            ContentValues val=new ContentValues();
            val.put(KEYID,id-1);
            datadelete.update(TABLE_NAME,val,null,null);
        }
    }
//    public void updatedel(Activity obj){
//        SQLiteDatabase dataupdate=this.getWritableDatabase();
//        ContentValues value=new ContentValues();
//        value.put(KEYID,(obj.id)-1);
//        value.put(KEY_ACTIVITY,obj.act);
//        value.put(KEY_TIME,obj.time);
//        dataupdate.update(TABLE_NAME,value,KEYID+"="+obj.id,null);
//    }
}

