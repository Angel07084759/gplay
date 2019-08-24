package com.adtv.raite;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import java.util.List;

public class Main extends AppCompatActivity
{
    static final String[] PERMISSIONS = {permission.ACCESS_FINE_LOCATION, permission.READ_PHONE_STATE };
    public static String phoneNumber = null;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        for (String permit : PERMISSIONS)
        {
            if (ActivityCompat.checkSelfPermission(this, permit) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
                return;
            }
        }

        if ((phoneNumber = getPhoneNumber()) == null)
        {
            return;
        }

        Log.d("DEB", "Main.onCreate.phoneNumber: "  + phoneNumber);

        new LocationTask(this, new LocationTask.LocationTaskResponse()
        {
            @Override
            public void onProcessFinish(List<Address> addresses)
            {
                Log.d("DEB", "Main.onCreate.LocationTask.LocationTask.result: " + addresses);
                if (addresses != null && addresses.size() > 0)
                {
                    String latitude = addresses.get(0).getLatitude() + "";
                    String longitude = addresses.get(0).getLongitude() + "";

                    new PHPConnect(new PHPConnect.PHPResponse()
                    {
                        @Override
                        public void onProcessFinish(String result)
                        {
                            Log.d("DEB", "Main.onCreate.LocationTask.PHPConnect.result: " + result);

                            if (result != null)
                            {
                                String[] split = result.split(User.DELIMITER);
                                user = new User(split);

                                if (split[User.Field.fname.ordinal()].equals("0"))
                                {
                                    startActivity(new Intent(Main.this, Register.class));
                                    finish();
                                }
                                else if (split[User.Field.driver.ordinal()].equals("1"))
                                {
                                    startActivity(new Intent(Main.this, Driver.class));
                                    finish();
                                }
                                else
                                {
                                    startActivity(new Intent(Main.this, Passenger.class));
                                    finish();
                                }
                            }
                        }
                    }).execute(User.LOGIN,
                            User.Field.phone.name(), phoneNumber,
                            User.Field.ltime.name(), timestamp(),
                            User.Field.latitude.name(), latitude,
                            User.Field.longitude.name(), longitude);
                }
            }
        }).runTask();
    }
    public static String timestamp()
    {
        return Long.toString(System.currentTimeMillis()) ;
    }

    /** Returns null if READ_PHONE_STATE permission is NOT granted*/
    private String getPhoneNumber()
    {
        if (ActivityCompat.checkSelfPermission(this, permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{permission.READ_PHONE_STATE}, 1);
            return null;
        }
        return ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        boolean isPermissionGranted = true;

        for (int grant : grantResults)
        {
            if (grant != PackageManager.PERMISSION_GRANTED)
            {
                isPermissionGranted = false;
            }
        }
        if (isPermissionGranted)
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            recreate();
        }
    }

    public void openMap(View view)
    {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps"))) ;
        finish();
    }
}