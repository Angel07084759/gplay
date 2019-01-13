package com.adtv.tetrisapk;

import android.os.Handler;
import android.widget.TextView;
import java.util.Locale;

public class GameTimer extends Handler implements Runnable
{
    public  static final long DELAY_MILLIS = 100;

    private long startTime;
    private long elapsedTime;
    private boolean isRunning;
    private TextView timeView ;

    public GameTimer(TextView timeView)
    {
        this.timeView = timeView;
    }

    @Override
    public void run()
    {
        elapsedTime = System.currentTimeMillis() - startTime;
        timeView.setText(timeFormat(elapsedTime));
        postDelayed(this, DELAY_MILLIS);
    }

    public boolean isRunning()
    {
        return isRunning;
    }

    public void start()
    {
        if (!isRunning)
        {
            isRunning = true;
            startTime = System.currentTimeMillis() - elapsedTime;
            postDelayed(this, 0);
        }
    }

    public void stop()
    {
        if (isRunning)
        {
            removeCallbacks(this);
            isRunning = false;
        }
    }
    public void reset()
    {
        stop();
        startTime = 0;
        elapsedTime = 0;
        timeView.setText(timeFormat(elapsedTime));
    }

    /**
     * Formats the given timeTxt (in milliseconds) to h:m:s.
     *
     * @param millis the timeTxt in milliseconds to be converted.
     * @return s formatted timeTxt in string as 02dh:02dm:02ds:02ml
     */
    static String timeFormat(long millis)
    {
        int secs = (int) ((millis / 1000) % 60);
        int mins = (int) ((millis / (1000 * 60)) % 60);
        int hrs = (int) ((millis / (1000 * 60 * 60)));
        int mils = (int) ((millis % 1000) / 100);
        return String.format(Locale.US, "%02dh:%02dm:%02ds:%d", hrs, mins, secs, mils);
    }
}
