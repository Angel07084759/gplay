package com.adtv.logintest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class MainTestA extends AppCompatActivity
{           static  TextView tvTest;//////////////
    EditText userID, userName, userPass;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tvTest =(TextView) findViewById(R.id.tv_test);
        userID = (EditText) findViewById(R.id.user_id);
        userName = (EditText) findViewById(R.id.user_name);
        userPass = (EditText) findViewById(R.id.user_pass);


    }
    public void onLogin(View view)
    {

    }
    public void onLoginC(View view)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                long longMill = System.currentTimeMillis();
                String login_url = "http://192.168.0.11/connTest.php";

                String user_id = userID.getText().toString().trim();
                String user_name = userName.getText().toString().trim();
                String user_pass = userPass.getText().toString().trim();
                try
                {


                    tvTest.setText("<2000a>"+ "[" +(System.currentTimeMillis() - longMill)+ "]");/////////////////////////////////
                    Thread.sleep(2000);/////////////////////////////////////////////

                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    tvTest.setText("<2000b>" + "[" + (System.currentTimeMillis() - longMill - 2000)+ "]");/////////////////////////////////
                    Thread.sleep(2000);////////////////////////////

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    tvTest.setText("<2000c>" + "[" + (System.currentTimeMillis() - longMill - 4000)+ "]");/////////////////////////////////
                    Thread.sleep(2000);////////////////////////////


                    String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                            + URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                            + URLEncoder.encode("user_pass", "UTF-8") + "=" + URLEncoder.encode(user_pass, "UTF-8");

                    tvTest.setText("<2000d>" + "[" + (System.currentTimeMillis() - longMill - 6000)/*+ "]\n" + post_data*/);/////////////////////////////////
                    Thread.sleep(2000);////////////////////////////

                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";

                    tvTest.setText("<2000e>" + "[" + (System.currentTimeMillis() - longMill - 8000)+ "]");/////////////////////////////////
                    Thread.sleep(2000);////////////////////////////

                    while ((line = bufferedReader.readLine()) != null)
                    {
                        result += line;
                    }

                    tvTest.setText("<2000f>" + "[" + (System.currentTimeMillis() - longMill - 10000)+ "]\n" + result);/////////////////////////////////
                    Thread.sleep(2000);////////////////////////////

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    //String str = result + "<\n" + post_data + ">";//////////

                    tvTest.setText("<2000g>" + "[" + (System.currentTimeMillis() - longMill - 12000)+ "]");/////////////////////////////////
                    Thread.sleep(2000);////////////////////////////

                    tvTest.setText(result + "<" +  (System.currentTimeMillis() - longMill - 14000) + ">" + httpURLConnection.getResponseCode());////////////////////////////
/*                }
                catch (MalformedURLException e)
                {
                    textView.setText("E_MSG1: " + e.getMessage());////////////////////////////
                }
                catch (IOException e)
                {
                    textView.setText("E_MSG2: " + e.getMessage());////////////////////////////
                */}
                catch (Exception e)
                {
                    tvTest.setText("E_MSG3: " + e.getMessage());////////////////////////////
                }
            }
        }).start();

    }
    public void onLoginB(final View view)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {


                ((Button) view).setText("AAAAA");//////////////////////
                String login_url = "http://192.168.0.11/connTest.php";

                //String type = "connTest";
                String user_id = userID.getText().toString().trim();
                String user_name = userName.getText().toString().trim();
                String user_pass = userPass.getText().toString().trim();

                //textView.setText("----");////////////////////////////
                try
                {
                    ((Button) view).setText("----");//////////////////////
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    //((Button) view).setText("Xaaaa");//////////////////////
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    //((Button) view).setText("Xbbbb");//////////////////////
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&" + URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&" + URLEncoder.encode("user_pass", "UTF-8") + "=" + URLEncoder.encode(user_pass, "UTF-8");
                    //((Button) view).setText("Xcccc");//////////////////////
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    //((Button) view).setText("Xdddd");//////////////////////
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    //((Button) view).setText("Xeeee");//////////////////////
                    while ((line = bufferedReader.readLine()) != null)
                    {
                        result += line;
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    //((Button) view).setAllCaps(false);/////////////////////
                    //((Button) view).setText(post_data);//////////////////////
                    tvTest.setText(result + "\n" + post_data);////////////////////////////
                }
                catch (Exception e)
                {
                    tvTest.setText("E_MSG: " + e.getMessage());////////////////////////////
                    //((Button) view).setText(e.getMessage());//////////////////////
                }
            }
        }).start();
    }


    public void onLoginA(View view)
    {
        String uid = userID.getText().toString().trim();
        String uname = userName.getText().toString().trim();
        String upass = userPass.getText().toString().trim();
        String type = "connTest";

        //textView.setText("0" + textView.getText() + "");/////////////////
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);

        backgroundWorker.execute(type,uid, uname, upass);
    }


}
