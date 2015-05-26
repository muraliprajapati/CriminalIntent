package com.mdevs.criminalintent;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Murali on 10-04-2015.
 */
public class CrimeLab {
    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";

    static CrimeLab sCrimeLab;
    ArrayList<Crime> mCrimeList;
    Context mApplicationContext;
    private CriminalIntentJSONSerializer mSerializer;

    CrimeLab(Context context) {
        mApplicationContext = context;
        //mCrimeList = new ArrayList<Crime>();
        mSerializer = new CriminalIntentJSONSerializer(mApplicationContext, FILENAME);
        try {
            mCrimeList = mSerializer.loadCrimeList();
        } catch (Exception e) {
            mCrimeList = new ArrayList<Crime>();
            Log.e(TAG, "Error loading crime list: ", e);
        }
       /* for(int i = 0;i<100;i++){
            Crime c  = new Crime();
            c.setmTitle("Crime#"+i);
            c.setmSolved(i%2==0);
            mCrimeList.add(c);
        }*/

    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context.getApplicationContext());
        }
        return sCrimeLab;
    }

    public boolean saveCrimes() {
        try {
            mSerializer.saveCrimeList(mCrimeList);
            Log.d(TAG, "crimes saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving crimes: ", e);
            return false;
        }
    }

    public void addCrime(Crime c) {
        mCrimeList.add(c);
    }

    ArrayList getCrimeList() {
        return mCrimeList;
    }

    Crime getCrime(UUID uuid) {
        for (Crime crime : mCrimeList) {
            if (crime.getmId().equals(uuid)) {
                return crime;
            }
        }
        return null;
    }
}
