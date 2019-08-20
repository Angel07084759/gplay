package com.adtv.raite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Passenger extends AppCompatActivity
{
    private final String SERVER = "http://angelcpuparts.x10.mx/raite/";//http://192.168.0.11/
    private final String DRIVER = SERVER + "driver.php";

    private TextView errMsgTV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passenger);

        errMsgTV = (TextView) findViewById(R.id.tv_passenger_err_msg);
    }
}
