package com.mdevs.criminalintent;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Murali on 07-04-2015.
 */
public class Crime {

    UUID mId;
    String mTitle;
    DateFormat mDateFormat;
    Date mDate;
    boolean mSolved;


    Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();


    }

    @Override
    public String toString() {
        return mTitle;
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


}
