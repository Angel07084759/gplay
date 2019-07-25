package com.adtv.logintest;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

public class PHPConnect implements Runnable
{
    public interface PHPResponse
    {
        void processFinish(String output);
    }


    private boolean started;
    private boolean repeat;
    private String[] params;
    private Thread thread;

    private PHPResponse delegate = null;

    public PHPConnect(PHPResponse delegate, boolean repeat, String... params)
    {
        this.delegate = delegate;
        this.repeat = repeat;
        this.params = params;

        thread = new Thread(this);
    }

    public void setRepeat(boolean repeat)
    {
        this.repeat = repeat;
    }

    public void start()
    {
        if (!started)
        {
            thread.start();
            started = true;
        }
        else
        {
            thread.interrupt();
            thread.run();
        }
    }

    public void run()
    {
        try
        {
            String result = "";
            boolean repeatTemp = true;

            while (repeatTemp)
            {
                Log.d("DEB", "reached while.... " + repeatTemp + " vs " + repeat + "..." + params[0]);///////////
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                Log.d("DEB", params.length + " reached (POST).... " + params.length);///////////
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                Log.d("DEB", "reached outputStream...");///////////

                StringBuilder post_data = new StringBuilder();

                for (int i = 1; i < params.length; i += 2)
                {
                    post_data.append(URLEncoder.encode(params[i], "UTF-8") + "=" + URLEncoder.encode(params[i + 1], "UTF-8"));
                    if (i + 2 < params.length)
                    {
                        post_data.append("&");
                    }
                }
                Log.d("DEB", post_data.toString().length() + "reached post_data...." + post_data.toString());///////////

                //bufferedWriter.write((post_data.toString().length() <= 0 ? (URLEncoder.encode("post", "UTF-8")
                // + "=" + URLEncoder.encode("post", "UTF-8")) : post_data.toString()) );
                bufferedWriter.write((post_data.toString().length() <= 0 ? "post" : post_data.toString()));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                Log.d("DEB", "reached flush....[" + httpURLConnection.getResponseCode() + "]");///////////

                InputStream inputStream = httpURLConnection.getResponseCode() == 500 ? httpURLConnection.getErrorStream() : httpURLConnection.getInputStream();

                Log.d("DEB", "reached getInputStream....");///////////
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                result = "";
                String line = "";

                Log.d("DEB", "reached inputStream....");///////////

                while ((line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                delegate.processFinish(result);//publishProgress(result);//////

                repeatTemp = repeat;
                Log.d("DEB", "SUCCEED ....[" + result + "]" + repeatTemp);///////////
            }
            //return result;
        }
        catch (MalformedURLException e)
        {
            Log.d("DEB", "e1" + e.getMessage());///////////
            //return "e1" + e.getMessage();
        }
        catch (SocketTimeoutException e)
        {
            Log.d("DEB", "e2" + e.getMessage());///////////
            // return "e2" + e.getMessage();
        }
        catch (IOException e)
        {
            Log.d("DEB", "e3" + e.getMessage());///////////
            //return "e3" + e.getMessage();
        }
        catch (Exception e)
        {
            Log.d("DEB", "e4" + e.getMessage());///////////
            //return "e4" + e.getMessage();
            e.printStackTrace();
        }
        //return null;
    }




}
