package com.adtv.raite;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
    private final String EXIST = SERVER + "exist.php";
    enum DBVar { verified, driver, phone, fname, lname, ftime, ltime;}

    private TextView errMsgTV;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        errMsgTV = (TextView) findViewById(R.id.tv_login_err_msg);

        String phoneNumber =  getPhoneNumber();
        if (phoneNumber == null)
        {
            recreate();
            return;
        }

/*        new PHPConnect(new PHPConnect.PHPResponse() {
            @Override
            public void processFinish(String result)
            {
                String s = "";
                if (result != null)
                {
                    String[] split = result.split(",");
                    for (int i = 0; i < split.length; i++)
                    {
                        s += "[" + split[i]+"]{" + split[i].length() + "}";
                    }
                }
                errMsgTV.setText((result == null ? "NULL" : s));
            }
        }).execute(EXIST, DB_PHONE, phoneNumber);*/

        new PHPConnect(new PHPConnect.PHPResponse() {
            @Override
            public void processFinish(String result)
            {
                //errMsgTV.setText( ("[" + (result == null ? "NULL" : result) + "]") );
                String[] split = new String[]{};
                if (result != null)
                {
                    if ( (split = result.split(","))[DBVar.verified.ordinal()].equals("0") )
                    {
                        //startActivity(new Intent(this, DisplayMessageActivity.class));
                    }

                    errMsgTV.setText(split(result));
                }

            }
        }).execute(REGISTER, DBVar.phone.name(), (true ? phoneNumber : timeMillis().substring(0, 11)),DBVar.ftime.name(), timeMillis());
    }

    String split(String str)//TEST ONLY
    {
        String[] split = str.split(",");
        String result = "";
        for (String s: split)
        {
            result += "[" + s + "]";
        }
        return result;
    }

    String timeMillis()//Test only
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

