package com.dan.naari;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.telephony.SmsManager;

public class TrigActivity extends AppCompatActivity {

    private static final int REQUEST_CALL = 1;
    private DatabaseHelper myDB;
    private boolean volumeButtonPressed = false;
    private long pressStartTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trig);
        myDB = new DatabaseHelper(this);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keycode = event.getKeyCode();

        switch (keycode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    if (!volumeButtonPressed) {
                        volumeButtonPressed = true;
                        pressStartTime = System.currentTimeMillis();
                    }
                } else if (action == KeyEvent.ACTION_UP) {
                    long pressDuration = System.currentTimeMillis() - pressStartTime;
                    if (volumeButtonPressed && pressDuration >= 5000) { // 5000 milliseconds = 5 seconds
                        initiateSOS();
                    }
                    volumeButtonPressed = false;
                }
                return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void initiateSOS() {
        Cursor cursor = myDB.getListContents();
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DatabaseHelper.COL2); // Get the index of the COL2 column
            if (columnIndex != -1) { // Check if the column index is valid
                do {
                    String contactNumber = cursor.getString(columnIndex); // Retrieve contact number using column index
                    sendSOSMessage(contactNumber);
                } while (cursor.moveToNext());
            } else {
                // Handle the case where the column index is not found
                Toast.makeText(this, "Column index not found!", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }
    }


    private void sendSOSMessage(String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CALL);
        } else {
            // Get the last known location
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            String message = "SOS: Please help! My location is: ";
            if (lastKnownLocation != null) {
                double latitude = lastKnownLocation.getLatitude();
                double longitude = lastKnownLocation.getLongitude();
                message += "Latitude: " + latitude + ", Longitude: " + longitude;

                // Generate Google Maps link
                String mapsLink = "http://maps.google.com/maps?q=" + latitude + "," + longitude;
                message += "\nGoogle Maps Link: " + mapsLink;
            } else {
                message += "Location not available";
            }

            SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SOS message sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send SOS message
                initiateSOS();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
