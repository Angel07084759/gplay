package com.adtv.raite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DriverDashboard extends AppCompatActivity
{
    private final String SERVER = "http://angelcpuparts.x10.mx/raite/";//http://192.168.0.11/
    private final String DRIVER = SERVER + "driver.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_dashboard);
    }

    public void drive(View view)
    {
        new PHPConnect(new PHPConnect.PHPResponse() {
            @Override
            public void processFinish(String result)
            {
                startActivity(new Intent(DriverDashboard.this, PassengerDashboard.class));//.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                //overridePendingTransition(0, 0);
                finish();
            }
        }).execute(DRIVER,
                Main.DBVar.driver.name(), "0",
                Main.DBVar.phone.name(), Main.phoneNumber,
                Main.DBVar.ltime.name(), Main.timeMillis());
    }
}
