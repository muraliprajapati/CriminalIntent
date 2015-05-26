package com.mdevs.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Murali on 07-04-2015.
 */
public class CrimeFragment extends Fragment {



    public static final String EXTRA_CRIME_ID = "com.mdevs.criminalintent.crime_id";
    public static final String DIALOG_DATE = "date";
    public static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 2;
    Crime mCrime;
    EditText mTitleField;
    Button mDateButton;
    CheckBox mSolvedCheckBox;
    Button mSuspectButton;
    Button mCallSuspectButton;
    String mSuspect;
    String mPhoneNumber;
    String contactID;
    Uri contactUri;
    CriminalIntentHelper helper;

    public static CrimeFragment newInstance(UUID crimeID) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_CRIME_ID, crimeID);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mCrime = new Crime();
        UUID crimeID = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            updateDate();
        } else if (requestCode == REQUEST_CONTACT) {
            contactUri = data.getData();

            getContactName();
            getContactPhoneNumber();


        }
    }

    private void getContactName() {
        String[] name = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
        Cursor cursor = getActivity().getContentResolver().query(contactUri, name, null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            return;
        }
        cursor.moveToFirst();
        mSuspect = cursor.getString(0);
        mCrime.setSuspect(mSuspect);
        mSuspectButton.setText(mSuspect);
        cursor.close();

    }

    void getContactPhoneNumber() {
        Cursor cursorID = getActivity().getContentResolver().query(contactUri, new String[]{ContactsContract.Contacts._ID}, null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        String[] phoneNumber = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor phoneCursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, phoneNumber,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);


        if (phoneCursor.getCount() == 0) {
            phoneCursor.close();
            return;
        }

        phoneCursor.moveToFirst();
        mPhoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        mSuspect = getString(R.string.call_suspect, mSuspect);
        mCallSuspectButton.setText(mSuspect);
        Log.e("NUMBER", "Phone no is:" + mPhoneNumber);

        phoneCursor.close();


    }

    void updateDate() {
        mDateButton.setText(mCrime.getmDate().toString());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        helper = new CriminalIntentHelper(getActivity());
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = (EditText) v.findViewById(R.id.crimeTitle);
        mTitleField.setText(mCrime.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mCrime.setmTitle(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mCrime.setmTitle(s.toString());
            }
        });

        mDateButton = (Button) v.findViewById(R.id.crimeDate);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getmDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fragmentManager, DIALOG_DATE);

            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crimeSolved);
        mSolvedCheckBox.setChecked(mCrime.ismSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setmSolved(isChecked);
            }
        });
        Button reportButton = (Button) v.findViewById(R.id.crimeReportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                startActivity(i);
            }
        });

        mSuspectButton = (Button) v.findViewById(R.id.crimeSuspectButton);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, REQUEST_CONTACT);
            }
        });
        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        mCallSuspectButton = (Button) v.findViewById(R.id.suspectCalltButton);
        mCallSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSuspect == null) {
                    Toast.makeText(getActivity(), "Please first choose a suspct", Toast.LENGTH_SHORT).show();
                } else {

                    Uri uri = Uri.parse("tel:" + mPhoneNumber);
                    Intent i = new Intent(Intent.ACTION_DIAL, uri);
                    i.putExtra(Intent.EXTRA_PHONE_NUMBER, mPhoneNumber);
                    // PackageManager pm = getActivity().getPackageManager();
                    //List<ResolveInfo> activities = pm.queryIntentActivities(i, 0);
                    //boolean isIntentSafe = activities.size() > 0;
                    startActivity(i);
                }

            }
        });
        return v;

    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity()).saveCrimes();
        //long id = helper.insertData(mCrime.getmId().toString(), mTitleField.getText().toString(), mSolvedCheckBox.getText().toString(), mDateButton.getText().toString(), mSuspect);
        //Toast.makeText(getActivity(),"Crime added",Toast.LENGTH_SHORT);
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.ismSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getmDate()).toString();
        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report, mCrime.getmTitle(), dateString, solvedString, suspect);


        return report;
    }
}
