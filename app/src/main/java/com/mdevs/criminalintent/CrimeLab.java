package com.mdevs.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Murali on 10-04-2015.
 */
public class CrimeLab {
    static CrimeLab sCrimeLab;
    ArrayList<Crime> mCrimeList;
    Context mApplicationContext;

    CrimeLab(Context context) {
        mApplicationContext = context;
        mCrimeList = new ArrayList<Crime>();
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
    public void addCrime(Crime c){
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
