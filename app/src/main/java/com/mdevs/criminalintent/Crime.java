package com.mdevs.criminalintent;

import android.text.format.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Murali on 07-04-2015.
 */
public class Crime {
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE = "date";
    private static final String JSON_SUSPECT = "suspect";

    UUID mId;
    String mTitle;
    DateFormat mDateFormat;
    Date mDate;
    boolean mSolved;
    String mSuspect;


    Crime() {
        mId = UUID.randomUUID();

        mDate = new Date();


    }

    public Crime(JSONObject jsonObject) throws JSONException {
        mId = UUID.fromString(jsonObject.getString(JSON_ID));
        if (jsonObject.has(JSON_TITLE)) {
            mTitle = jsonObject.getString(JSON_TITLE);
        }
        if (jsonObject.has(JSON_SUSPECT)) {
            mSuspect = jsonObject.getString(JSON_SUSPECT);
        }
        mSolved = jsonObject.getBoolean(JSON_SOLVED);
        mDate = new Date(jsonObject.getString(JSON_DATE));
    }


    @Override
    public String toString() {
        return mTitle;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_TITLE, mTitle);
        jsonObject.put(JSON_ID, mId.toString());
        jsonObject.put(JSON_DATE, mDate);
        jsonObject.put(JSON_SOLVED, mSolved);
        jsonObject.put(JSON_SUSPECT, mSuspect);
        return jsonObject;

    }

    public UUID getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getmDate() {
        return mDate;

    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;

    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }


}
