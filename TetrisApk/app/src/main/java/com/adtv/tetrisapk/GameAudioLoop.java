package com.adtv.tetrisapk;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class GameAudioLoop extends AudioTrack
{
    public static final int[] SAMPLE_RATES = new int[]{8000, 11025,
            16000, 22050, 32000, 37800, 44056, 44100, 47250, 48000, 50000,
            50400, 88200, 96000, 176400, 192000, 352800, 2822400, 5644800};
    public static final int PLAYBACK_RATE = 76800;
    private static byte[] audioBuffer = new byte[1];

    public GameAudioLoop(int streamType, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes, int mode)
    {
        super(streamType, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, mode);
    }

    public GameAudioLoop(InputStream inputStream)
    {
        this(AudioManager.STREAM_MUSIC, getValidSampleRate(),
                AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT,
                ((audioBuffer = inputStreamToBytes(inputStream)).length * 2) , AudioTrack.MODE_STATIC);
        write(audioBuffer, 0, audioBuffer.length);
        setLoopPoints(0, audioBuffer.length / 2, -1);
        setPlaybackRate(PLAYBACK_RATE);
    }

    public void setPlaybackRate(float playbackRate)
    {
        setPlaybackRate((int) (PLAYBACK_RATE * playbackRate));
    }


    public static byte[] inputStreamToBytes(InputStream inputStream)
    {
        byte[] buffer = new byte[0];
        try
        {
            buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
        }
        catch (IOException e)
        {
            Log.d("GameAudioLoop",  "GameAudioLoop(InputStream):" + e.getMessage());
        }
        return buffer;
    }

    public static int getValidSampleRate()
    {
        for (int rate : SAMPLE_RATES)
        {
            try
            {
                return new AudioTrack(AudioManager.STREAM_MUSIC, rate,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT, rate,
                        AudioTrack.MODE_STATIC).getSampleRate();

            }
            catch (Exception e)
            {
                Log.e("GameAudioLoop", "getValidSampleRates():" + e.getMessage());
            }
        }
        return -1;
    }

}
