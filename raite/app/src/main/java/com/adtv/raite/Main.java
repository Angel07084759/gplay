package com.adtv.raite;

import android.Manifest.permission;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Main extends AppCompatActivity
{
    final static String[] PERMISSIONS = {permission.ACCESS_FINE_LOCATION, permission.READ_PHONE_STATE};
    protected static String phoneNumber = null;

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

        new PHPConnect(new PHPConnect.PHPResponse()
        {
            @Override
            public void processFinish(String result)
            {
                if (result != null)
                {
                    String[] split = result.split(",");

                    if ((split[Const.DBVar.fname.ordinal()].equals("") || split[Const.DBVar.lname.ordinal()].equals("")))
                    {
                        startActivity(new Intent(Main.this, Verify.class));
                        finish();
                    }
                    else if ((split[Const.DBVar.driver.ordinal()].equals("1")))
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
        }).execute(Const.REGISTER,
                Const.DBVar.phone.name(), phoneNumber,
                Const.DBVar.ftime.name(), timeMillis());


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