package com.adtv.logintest;

import android.content.SyncRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Main extends AppCompatActivity
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

    private final String SERVER_IP = "http://angelcpuparts.x10.mx/";//http://192.168.0.11/
    public void onLogin(View view)
    {
        String user_id = userID.getText().toString().trim();
        String user_name = userName.getText().toString().trim();
        String user_pass = userPass.getText().toString().trim();
        String login_url = SERVER_IP +  (view.getId() == R.id.login ? "connLoginTest.php" : "connRegisterTest.php");


        if (phpConnectLog == null)
        {
            phpConnectLog = new PHPConnect(new PHPConnect.PHPResponse()
            {
                @Override
                public void processFinish(String output)
                {
                    textView.setText((output != null ? output : "NULL") + " " + Thread.currentThread().getName());
                }
            }, false, login_url, "user_id", user_id, "user_name", user_name, "user_pass", user_pass);
        }
        if (phpConnectReg == null)
        {
            phpConnectReg = new PHPConnect(new PHPConnect.PHPResponse()
            {
                @Override
                public void processFinish(String output)
                {
                    textView.setText((output != null ? output : "NULL") + " " + Thread.currentThread().getName());
                }
            }, false, login_url, "user_id", user_id, "user_name", user_name, "user_pass", user_pass);
        }
        PHPConnect phpConnect = view.getId() == R.id.login ? phpConnectLog : phpConnectReg;
        phpConnect.start();

    }

    PHPConnect phpConnectMon = null;
    PHPConnect phpConnectLog = null;
    PHPConnect phpConnectReg = null;
    public void startMonitor(View view)
    {
        String login_url = SERVER_IP + "connMonitorTest.php";


        if (phpConnectMon == null)
        {
            phpConnectMon = new PHPConnect(new PHPConnect.PHPResponse()
            {
                @Override
                public void processFinish(String output)
                {
                    ((TextView) findViewById(R.id.tv_test2)).setText((output != null ? output : "NULL") + " " + Thread.currentThread().getName());
                }
            } , true, login_url);
            ((Button) view).setText("stop");
        }

        if (((Button) view).getText().toString().equalsIgnoreCase("start"))
        {
            ((Button) view).setText("stop");
            phpConnectMon.setRepeat(true);
            phpConnectMon.start();
        }
        else
        {
            ((Button) view).setText("start");
            phpConnectMon.setRepeat(false);
        }


    }


























/*    public void onLogin(View view)
    {
        String user_id = userID.getText().toString().trim();
        String user_name = userName.getText().toString().trim();
        String user_pass = userPass.getText().toString().trim();
        String login_url = view.getId() == R.id.login
                ? "http://192.168.0.11/connLoginTest.php"
                : "http://192.168.0.11/connRegisterTest.php";

        new Thread(new PHPConnect(new PHPConnect.PHPResponse()
        {
            @Override
            public void processFinish(String output)
            {
                textView.setText((output != null ? output : "NULL")  + " " + Thread.currentThread().getName());
            }
        } , false, login_url, "user_id", user_id, "user_name", user_name, "user_pass", user_pass)).start();

    }

    PHPConnect phpConnectMon = null;
    public void startMonitor(View view)
    {
        String login_url = "http://192.168.0.11/connMonitorTest.php";

        if (phpConnectMon == null)
        {
            new Thread(phpConnectMon = new PHPConnect(new PHPConnect.PHPResponse()
            {
                @Override
                public void processFinish(String output)
                {
                    ((TextView) findViewById(R.id.tv_test2)).setText((output != null ? output : "NULL"));
                }
            } , false, login_url)).start();
            ((Button) view).setText("stop");
        }
        else
        {
            ((Button) view).setText("start");
            phpConnectMon.setRepeat(false);
            phpConnectMon = null;
        }

    }
    */



}
