package com.adtv.logintest;

import android.os.AsyncTask;
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

public class PHPConnectAsync extends AsyncTask<String, String, String>
{
    public interface PHPResponse
    {
        void processFinish(String output);
    }


    private int count = 0;
    private boolean repeat;
    public PHPResponse delegate = null;

    public PHPConnectAsync(PHPResponse delegate, boolean repeat)
    {
        this.delegate = delegate;
        this.repeat = repeat;
    }

    public void setRepeat(boolean repeat)
    {
        this.repeat = repeat;
    }

    @Override
    protected String doInBackground(String... params)
    {
        try
        {
            String result = "";
            boolean repeatTemp = true;
            while (repeatTemp)
            {

                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

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
                Log.d("DEB", post_data.toString().length() + "reached post_data...." + post_data.toString());///////////

                //bufferedWriter.write((post_data.toString().length() <= 0 ? (URLEncoder.encode("post", "UTF-8")
                // + "=" + URLEncoder.encode("post", "UTF-8")) : post_data.toString()) );
                bufferedWriter.write((post_data.toString().length() <= 0 ? "post" :  post_data.toString()));
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

                publishProgress(result);

                repeatTemp = repeat;
                Log.d("DEB", "SUCCEED ....[" + result + "]" + repeatTemp);///////////
            }
            return result;
        }
        catch (MalformedURLException e)
        {
            Log.d("DEB", "e1" + e.getMessage());///////////
            return "e1" + e.getMessage();
        }
        catch (SocketTimeoutException e)
        {
            Log.d("DEB", "e2" + e.getMessage());///////////
            return "e2" + e.getMessage();
        }
        catch (IOException e)
        {
            Log.d("DEB", "e3" + e.getMessage());///////////
            return "e3" + e.getMessage();
        }
        catch (Exception e)
        {
            Log.d("DEB", "e4" + e.getMessage());///////////
            return "e4" + e.getMessage();
        }
        //return null;
    }



    @Override
    protected void onProgressUpdate(String... progress)
    {

        //Log.d("DEB", values[0] + "[" +count + "]");//a
        delegate.processFinish(progress[0]);//b
        super.onProgressUpdate(progress);
    }

    //@Override
    //protected void onPostExecute(String result)
    {
        //Log.d("DEB",  "[" + count + "]");//a
        //delegate.processFinish(result);//a
    }
}
