package com.adtv.raite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class Driver extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver);

        ((Switch) findViewById(R.id.availability)).setChecked(MyLocation.isRunning());

    }

    public void available(View view)
    {
        if (((Switch) view).isChecked())
        {
            MyLocation.startService(this, "Waiting for passengers");
        }
        else
        {
            MyLocation.stopService(this);
        }
    }
}
