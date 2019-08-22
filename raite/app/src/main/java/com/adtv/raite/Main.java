package com.adtv.raite;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class Main extends AppCompatActivity
{
    final static String[] PERMISSIONS = {permission.ACCESS_FINE_LOCATION, permission.READ_PHONE_STATE};
    protected static String phoneNumber = null;
    protected static Const.User currentUser;

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
            Log.d("DEB", "Could NOT get phone number");
            return;
        }


        new LocationTask(getApplicationContext(), new LocationTask.LocationTaskResponse()
        {
            @Override
            public void onProcessFinish(List<Address> addresses)
            {
                Log.d("DEB", "MAIN[a]:LOCATION " + (addresses != null));
                if (addresses != null && addresses.size() > 0)
                {
                    String latitude = addresses.get(0).getLatitude() + "";
                    String longitude = addresses.get(0).getLongitude() + "";
                    Log.d("DEB", phoneNumber + "MAIN[a]:LOCATION[if] " + latitude + "," + longitude);
                    new PHPConnect(new PHPConnect.PHPResponse()
                    {
                        @Override
                        public void processFinish(String result)
                        {
                            Log.d("DEB", "MAIN[b]:RESULT " + result);

                            if (result != null)
                            {
                                String[] split = result.split(",");

                                if (split[Const.DBVar.fname.ordinal()].equals("0") || split[Const.DBVar.lname.ordinal()].equals("0"))
                                {
                                    startActivity(new Intent(Main.this, Verify.class));
                                    finish();
                                }
                                else if (split[Const.DBVar.driver.ordinal()].equals("1"))
                                {
                                    startActivity(new Intent(Main.this, Driver.class));
                                    finish();
                                }
                                else
                                {
                                    startActivity(new Intent(Main.this, Passenger.class));
                                    finish();
                                }
                                currentUser = new Const.User(result.split(","));
                            }
                        }
                    }).execute(Const.REGISTER,
                            Const.DBVar.phone.name(), phoneNumber,
                            Const.DBVar.ftime.name(), timeMillis(),
                            Const.DBVar.latitude.name(), latitude,//just added
                            Const.DBVar.longitude.name(), longitude);//just added


                }


            }
        }).runTask();



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
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

    /** Returns null if READ_PHONE_STATE permission is NOT granted*/
    private String getPhoneNumber()
    {
        Log.d("DEB", "getting phone number");
        if (ActivityCompat.checkSelfPermission(this, permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{permission.READ_PHONE_STATE}, 1);
            return null;
        }
        return ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
    }

    static String timeMillis()
    {
        return "" + System.currentTimeMillis() ;
    }
}