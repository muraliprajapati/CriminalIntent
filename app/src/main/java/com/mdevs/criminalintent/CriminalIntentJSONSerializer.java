package com.mdevs.criminalintent;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Murali on 20-05-2015.
 */
public class CriminalIntentJSONSerializer {
    private Context mContext;
    private String mFilename;

    public CriminalIntentJSONSerializer(Context c, String f) {
        mContext = c;
        mFilename = f;
    }

    public ArrayList<Crime> loadCrimeList() throws IOException, JSONException {
        ArrayList<Crime> mCrimeList = new ArrayList<Crime>();
        BufferedReader reader = null;
        try {
// Open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
// Line breaks are omitted and irrelevant
                jsonString.append(line);
            }
// Parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue();
// Build the array of crimes from JSONObjects
            for (int i = 0; i < array.length(); i++) {
                mCrimeList.add(new Crime(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
// Ignore this one; it happens when starting fresh
        } finally {
            if (reader != null)
                reader.close();
        }
        return mCrimeList;
    }

    public void saveCrimeList(ArrayList<Crime> mCrimeList)
            throws JSONException, IOException {

        JSONArray jsonArray = new JSONArray();
        for (Crime c : mCrimeList)
            jsonArray.put(c.toJSON());

        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);

            writer.write(jsonArray.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
