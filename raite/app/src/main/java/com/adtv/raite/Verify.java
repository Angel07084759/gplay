package com.adtv.raite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Verify extends AppCompatActivity
{
    private TextView errMsg;
    private EditText fName, lName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify);
        errMsg = (TextView) findViewById(R.id.tv_verify_err_msg);
        fName = (EditText) findViewById(R.id.fname);
        lName = (EditText) findViewById(R.id.lname);

    }

    public void submit(View view)
    {
        String fname = fName.getText().toString().trim();
        String lname = lName.getText().toString().trim();
        errMsg.setText("");
        if (fname.isEmpty() || lname.isEmpty())
        {
            errMsg.setText("fields cannot be empty!");
            return;
        }
        if (!fname.matches("[a-zA-Z]+") || !lname.matches("[a-zA-Z]+"))
        {
            errMsg.setText("please enter letters only!");
            return;
        }
        if (fname.length() < 3 || lname.length() < 3)
        {
            errMsg.setText("please enter at least 3 characters in each field!");
            return;
        }
        new PHPConnect(new PHPConnect.PHPResponse() {
            @Override
            public void processFinish(String result)
            {
                Main.currentUser = new Const.User(result.split(","));
                startActivity(new Intent(Verify.this, Passenger.class));//.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                //overridePendingTransition(0, 0);
                finish();
            }
        }).execute(Const.VERIFY,
                Const.DBVar.fname.name(), fname,
                Const.DBVar.lname.name(), lname,
                Const.DBVar.phone.name(), Main.phoneNumber,
                Const.DBVar.ltime.name(), Main.timeMillis());

    }
}
