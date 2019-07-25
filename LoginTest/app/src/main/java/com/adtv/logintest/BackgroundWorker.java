package com.adtv.logintest;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundWorker extends AsyncTask<String, Void, String>
{
    Context context;
    AlertDialog alertDialog;

    BackgroundWorker(Context context)
    {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params)
    {
        String type = params[0];
        String login_url = "http://192.168.0.11/connTest.php";
        if (type.equals("connTest"))
        {
            try
            {
                String user_id = params[1];
                String user_name = params[2];
                String user_pass = params[3];

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                //Main.textView.setText("\n1" + Main.textView.getText() + "");/////////////////

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8") + "&"
                        + URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                        + URLEncoder.encode("user_pass", "UTF-8") + "=" + URLEncoder.encode(user_pass, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //Log.e("ERR:", post_data);////////////////////////////////////

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }
                strT = post_data  + "[ok]:" + result;///////////////////////////
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;
            }
            catch (MalformedURLException e)
            {
                //strT += "[A]:" + e.getMessage();
                //Main.textView.setText("\nX1" + e.getMessage() + "");/////////////////e.printStackTrace();
            }
            catch (IOException e)
            {
                //strT += "[B]:" + e.getMessage();
                //Main.textView.setText("\nX2" + Main.textView.getText() + e.getMessage() + "");/////////////////e.printStackTrace();
            }
        }
       //Log.e("ERR:", "FAILE_10");////////////////////////////////////
        return null;
    }

    @Override
    protected void onPreExecute()
    {
        //super.onPreExecute();
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }
    static String strT= "";

    @Override
    protected void onPostExecute(String result)
    {
        //super.onPostExecute(aVoid);
        alertDialog.setMessage(result);
        alertDialog.show();
        //Main.textView.setText("\n" + Main.textView.getText() + "\n" + strT);/////////////////
    }

    @Override
    protected void onProgressUpdate(Void... values)
    {
        super.onProgressUpdate(values);
    }
}
