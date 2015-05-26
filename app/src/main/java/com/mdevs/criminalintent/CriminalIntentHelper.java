package com.mdevs.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;

/**
 * Created by Murali on 23-05-2015.
 */
public class CriminalIntentHelper {
     CrimeDatabase crimeDatabase;
    Context context;

    CriminalIntentHelper(Context context){
        crimeDatabase = new CrimeDatabase(context);

    }


    public long insertData(String uuid,String title,String solved,String date,String suspect){
        SQLiteDatabase db = crimeDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(crimeDatabase.CRIME_UUID,uuid);
        cv.put(crimeDatabase.CRIME_TITLE,title);
        cv.put(crimeDatabase.CRIME_SOLVED,solved);
        cv.put(crimeDatabase.CRIME_DATE,date);
        cv.put(crimeDatabase.CRIME_SUSPECT,suspect);
        long id = db.insert(crimeDatabase.TABLE_NAME,null,cv);
        return id;
    }

         class CrimeDatabase extends SQLiteOpenHelper{

            private static final String DATABASE_NAME = "CrimeDatabase";
            private static final String TABLE_NAME = "CrimeTable";
            private static final int DATABASE_VER = 1;
            private static final String UID = "_id";
            private static final String CRIME_UUID = "CrimeUUID";
            private static final String CRIME_TITLE = "CrimeTitle";
            private static final String CRIME_SOLVED = "CrimeSolved";
            private static final String CRIME_DATE = "CrimeDate";
            private static final String CRIME_SUSPECT = "CrimeSuspect";
            private static final String CREATE_TABLE ="CREATE TABLE "+TABLE_NAME+" ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    CRIME_UUID+" TEXT, "+
                    CRIME_TITLE+" TEXT, "+
                    CRIME_SOLVED+" TEXT, "+
                    CRIME_DATE+" LONG, "+
                    CRIME_SUSPECT+" TEXT);";
            private static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME+" ";



            public CrimeDatabase(Context context){

                super(context,DATABASE_NAME,null,DATABASE_VER);

            }
            @Override
            public void onCreate(SQLiteDatabase db) {
                //CREATE TABLE CrimeTable (_id INTEGER PRIMARY KEY AUTOINCREMENT, CrimeUUID UUID, CrimeTitle TEXT, CrimeSolved TEXT, CrimeDate LONG, CrimeSuspect TEXT);
                //String CREATE_TABLE ="CREATE TABLE "+TABLE_NAME+" ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CRIME_UUID+" TEXT, "+CRIME_TITLE+" TEXT, "+CRIME_SOLVED+" TEXT, "+CRIME_DATE+" LONG, "+CRIME_SUSPECT+" TEXT);";
                db.execSQL(CREATE_TABLE);


                Toast.makeText(context,"Table created",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL(DROP_TABLE);
                onCreate(db);
                Toast.makeText(context,"Table updated",Toast.LENGTH_SHORT).show();

            }

        }
}
