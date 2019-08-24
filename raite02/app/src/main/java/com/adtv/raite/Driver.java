package com.adtv.raite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class Driver extends AppCompatActivity
{
    private Switch availability;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver);

        availability = (Switch) findViewById(R.id.availability);
        availability.setChecked(LocationService.isRunning());

    }

    public void switchAvailability(View view)
    {
        if (availability.isChecked())
        {
            LocationService.startService(this, "Waiting for passengers");
        }
        else
        {
            LocationService.stopService(this);
        }
    }
}
