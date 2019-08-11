package com.adtv.raite;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class Main extends AppCompatActivity
{
    private final String SERVER = "http://angelcpuparts.x10.mx/raite/";//http://192.168.0.11/
    private final String REGISTER = SERVER + "register.php";
    enum DBVar {status, driver, phone, fname, lname, latitude, longitude, ftime, ltime;}

    static String phoneNumber = "";

    private TextView errMsgTV;

    @Override
    protected void onCreate(final Bundle savedInstanceState)//PHPStatusServer
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        errMsgTV = (TextView) findViewById(R.id.tv_login_err_msg);

        phoneNumber =  getPhoneNumber();
        if (phoneNumber == null)
        {
            recreate();
            return;
        }


        new PHPConnect(new PHPConnect.PHPResponse() {
            @Override
            public void processFinish(String result)
            {
                //errMsgTV.setText( ("[" + (result == null ? "NULL" : result) + "]") );
                String[] split = new String[]{};
                if (result != null)
                {
                    split = result.split(",");

                    if ( (split[DBVar.fname.ordinal()].equals("") || split[DBVar.lname.ordinal()].equals("")) )
                    {
                        startActivity(new Intent(Main.this, Verify.class));//.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        //overridePendingTransition(0, 0);
                        finish();
                    }
                    else if ( (split[DBVar.driver.ordinal()].equals("1")) )
                    {
                        startActivity(new Intent(Main.this, DriverDashboard.class));//.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        //overridePendingTransition(0, 0);
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(Main.this, PassengerDashboard.class));//.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        //overridePendingTransition(0, 0);
                        finish();
                    }

                    errMsgTV.setText(split[DBVar.phone.ordinal()]);
                }

            }
        }).execute(REGISTER,
                DBVar.phone.name(), (true ? phoneNumber : timeMillis().substring(0, 11)),
                DBVar.ftime.name(), timeMillis());
    }

    static String split(String str)//TEST ONLY
    {
        String[] split = str.split(",");
        String result = "";
        for (String s: split)
        {
            result += "[" + s + "]";
        }
        return result;
    }

    static String timeMillis()//Test only
    {
        return "" + System.currentTimeMillis() ;
    }

    String getPhoneNumber()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            return null;
        }
        return ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
    }
}

