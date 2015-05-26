package com.mdevs.criminalintent;

import android.app.Fragment;

/**
 * Created by Murali on 10-04-2015.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
