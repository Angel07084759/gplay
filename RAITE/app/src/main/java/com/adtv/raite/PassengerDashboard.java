package com.adtv.raite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PassengerDashboard extends AppCompatActivity
{
    private final String SERVER = "http://angelcpuparts.x10.mx/raite/";//http://192.168.0.11/
    private final String DRIVER = SERVER + "driver.php";

    private TextView errMsgTV;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passengerdashboard);
        errMsgTV = (TextView) findViewById(R.id.tv_passenger_err_msg);

    }

    public void driveStop(View view)
    {
        Log.d("DEB", "SERVICE CANCELLED");
        Alarm.cancelAlarm(this);
        Toast.makeText(this, "SERVICE CANCELLED", Toast.LENGTH_SHORT).show();
        errMsgTV.setText("" + Alarm.isSet(this));
    }
    public void drive(View view)
    {

        Log.d("DEB", "SERVICE STARTED");
        Alarm.setAlarm(this, 2000);
        Toast.makeText(this, "SERVICE STARTED", Toast.LENGTH_SHORT).show();
        errMsgTV.setText("" + Alarm.isSet(this));




/*        new PHPConnect(new PHPConnect.PHPResponse() {
            @Override
            public void processFinish(String result)
            {
                startActivity(new Intent(PassengerDashboard.this, DriverDashboard.class));//.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                //overridePendingTransition(0, 0);
                finish();
            }
        }).execute(DRIVER,
                Main.DBVar.driver.name(), "1",
                Main.DBVar.phone.name(), Main.phoneNumber,
                Main.DBVar.ltime.name(), Main.timeMillis());*/
    }


}
