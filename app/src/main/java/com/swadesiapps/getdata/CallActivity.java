package com.swadesiapps.getdata;

import android.database.Cursor;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CallActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> StoreCall;
    ArrayAdapter<String> arrayAdapter;
    private DatabaseReference mDataRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listview2);
        mDataRef = FirebaseDatabase.getInstance().getReference().child(Constants.deviceid).child("call");
        StoreCall = new ArrayList<String>();
        fetchCalls();
        arrayAdapter = new ArrayAdapter<String>(
                CallActivity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, StoreCall
        );

        listView.setAdapter(arrayAdapter);
    }
    public void fetchCalls(){
        String[] projection = new String[] {
                CallLog.Calls._ID,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE
        };
        Cursor c = getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null,
        null, CallLog.Calls.DATE + " DESC");

        if (c.getCount() > 0)
        {
            c.moveToFirst();
            do{
                String callerID = c.getString(c.getColumnIndex(CallLog.Calls._ID));
                String callerNumber = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
                long callDateandTime = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(callDateandTime);
                String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
                long callDuration = c.getLong(c.getColumnIndex(CallLog.Calls.DURATION));
                int callType = c.getInt(c.getColumnIndex(CallLog.Calls.TYPE));
                String type = null;
                if(callType == CallLog.Calls.INCOMING_TYPE)
                {
                    type = "incoming";
                }
                else if(callType == CallLog.Calls.OUTGOING_TYPE)
                {
                    type = "outgoing";
                }
                else if(callType == CallLog.Calls.MISSED_TYPE)
                {
                    type = "missed";
                }
                StoreCall.add(callerNumber+"\n"+date+" : "+callDuration+" sec\n"+type);
                Map<String,Object> call = new HashMap<>();
                call.put("caller_no",callerNumber);
                call.put("call_duration",callDuration+" sec");
                call.put("call_time",date);
                call.put("call_type",type);
                mDataRef.child(callerID).updateChildren(call);
            }while(c.moveToNext());

        }
    }
}
