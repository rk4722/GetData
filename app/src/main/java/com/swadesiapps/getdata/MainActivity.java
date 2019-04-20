package com.swadesiapps.getdata;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    int PERMISSION_ALL = 199;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE
    };
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.device_id);

        Constants.deviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        textView.setText(Constants.deviceid);
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getContacts(View view) {
        if (checkSelfPermission( Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( new String[]{
                    Manifest.permission.READ_CONTACTS}, 1);
            return;
        }else{
            startActivity(new Intent(MainActivity.this,ContactsActivity.class));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getCalls(View view) {
        if (checkSelfPermission( Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_CALL_LOG},2);
            return;
        }else{
            startActivity(new Intent(MainActivity.this,CallActivity.class));
        }
    }

    public void getPhotos(View view) {
    }

    public void getLocation(View view) {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getImeiOtherDetails(View view) {
        if (checkSelfPermission( Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_PHONE_STATE},5);
            return;
        }else{
            startActivity(new Intent(MainActivity.this,PhoneDetailsActivity.class));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getMessages(View view) {
        if (checkSelfPermission( Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_SMS},6);
            return;
        }else{
            startActivity(new Intent(MainActivity.this,SMSActivity.class));
        }
    }
}
