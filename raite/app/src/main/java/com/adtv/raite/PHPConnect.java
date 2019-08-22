package com.adtv.raite;

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

public class PHPConnect extends AsyncTask<String, Void, String>
{

    public interface PHPResponse{void processFinish(String result);}
    public PHPResponse delegate = null;

    public PHPConnect(PHPResponse delegate)
    {
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(String s)
    {
        delegate.processFinish(s);
    }

    /**
     *
     * @param params url, var_name1, var_value1, ...
     * @return null if connection fails or an error occurs, empty: false; 1: true
     */
    @Override
    protected String doInBackground(String... params)
    {
        try
        {
            String result = "";
            URL url = new URL(params[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            Log.d("DEB", "url[" + url + "]");///////////
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

            bufferedWriter.write((post_data.toString().length() <= 0 ? "force_post" : post_data.toString()));
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getResponseCode()
                    == 500 ? httpURLConnection.getErrorStream() : httpURLConnection.getInputStream();


            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "iso-8859-1"));

            result = "";
            String line = "";

            while ((line = bufferedReader.readLine()) != null)
            {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            Log.d("DEB", "PHPConnect:doInBackground:result[" + result + "]");///////////
            return result;
        }
        catch (Exception e)
        {
            Log.d("DEB", "PHPConnect:doInBackground:Exception:" + e.getMessage());///////////
            return null;//"e" + e.getMessage();
        }
    }

}
