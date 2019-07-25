package com.adtv.tetrisapk;

import android.os.Handler;
import android.widget.TextView;
import java.util.Locale;

/**
 * GameTimer uses the system time to implement a timer.
 * This timer can be run, stopped, paused, and reset.
 * This timer uses the TextView given in the constructor
 * to display the current time value.
 */
public class GameTimer extends Handler implements Runnable
{
    /**
     * The time in milliseconds to check for a time change.
     */
    public  static final long DELAY_IN_MILLIS = 100;

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
        postDelayed(this, DELAY_IN_MILLIS);
    }

    /**
     * Getter for isRunning.
     * @return the value of isRunning.
     */
    public boolean isRunning()
    {
        return isRunning;
    }

    /**
     * Getter for elapsedTime.
     *
     * @return the value of elapedTime.
     */
    public long getElapsedTime()
    {
        return elapsedTime;
    }

    /**
     * If the timer is not running, calls postDelayed, sets isRunning to true
     * and start time to System.currentTimeMillis() - elapsedTime.
     */
    public void start()
    {
        if (!isRunning)
        {
            isRunning = true;
            startTime = System.currentTimeMillis() - elapsedTime;
            postDelayed(this, 0);
        }
    }

    /**
     * If the timer is running, this calls removeCallbacks
     * and sets isRunning to false.
     */
    public void stop()
    {
        if (isRunning)
        {
            removeCallbacks(this);
            isRunning = false;
        }
    }

    /**
     * Calls stop and sets variable to the default values.
     */
    public void reset()
    {
        stop();
        startTime = 0;
        elapsedTime = 0;
        timeView.setText(timeFormat(elapsedTime));
    }

    /**
     * Formats the given time(in milliseconds) to h:m:s (String).
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
