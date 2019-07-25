package com.adtv.logintest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainTestB extends AppCompatActivity
{

    TextView textView;
    EditText userID, userName, userPass;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        textView =(TextView) findViewById(R.id.tv_test);
        userID = (EditText) findViewById(R.id.user_id);
        userName = (EditText) findViewById(R.id.user_name);
        userPass = (EditText) findViewById(R.id.user_pass);


    }

    public void onLogin(View view)
    {
        String user_id = userID.getText().toString().trim();
        String user_name = userName.getText().toString().trim();
        String user_pass = userPass.getText().toString().trim();
        String login_url = view.getId() == R.id.login
                ? "http://192.168.0.11/connLoginTest.php"
                : "http://192.168.0.11/connRegisterTest.php";
        new PHPConnectAsync(new PHPConnectAsync.PHPResponse()
        {
            @Override
            public void processFinish(String output)
            {
                textView.setText((output != null ? output : "NULL"));
            }
        }, false).execute(login_url, "user_id", user_id, "user_name", user_name, "user_pass", user_pass);
    }

    PHPConnectAsync phpConnect = null;
    public void startMonitor(View view)
    {
        String login_url = "http://192.168.0.11/connMonitorTest.php";

        if (phpConnect == null)
        {
            phpConnect = new PHPConnectAsync(new PHPConnectAsync.PHPResponse()
            {
                @Override
                public void processFinish(String output)
                {
                    //textView.setText((output != null ? Html.fromHtml(output) : "NULL"));//<br/>
                    ((TextView) findViewById(R.id.tv_test2)).setText((output != null ? output : "NULL"));
                }
            }, true);
            phpConnect.execute(login_url);
            ((Button) view).setText("stop");
        }
        else
        {
            ((Button) view).setText("start");
            phpConnect.setRepeat(false);
            phpConnect.cancel(true);
            phpConnect = null;
        }

    }

}
