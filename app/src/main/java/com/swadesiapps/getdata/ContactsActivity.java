package com.swadesiapps.getdata;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactsActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> StoreContacts;
    ArrayAdapter<String> arrayAdapter;
    Cursor cursor;
    private DatabaseReference mDataRef;
    String name, phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        listView = findViewById(R.id.listview1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StoreContacts = new ArrayList<String>();
        mDataRef = FirebaseDatabase.getInstance().getReference().child(Constants.deviceid).child("contact");

        GetContactsIntoArrayList();

        arrayAdapter = new ArrayAdapter<String>(
                ContactsActivity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, StoreContacts
        );

        listView.setAdapter(arrayAdapter);

    }


    public void GetContactsIntoArrayList() {

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            StoreContacts.add(name + " " + ":" + " " + phonenumber);
            Map<String,Object> call = new HashMap<>();
            call.put("name",name);
            call.put("number",phonenumber);
            mDataRef.child(id).updateChildren(call);
        }

        cursor.close();

    }

}