package com.adtv.tetrisapk;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class GameAudioLoop extends AudioTrack
{
    /**
     *
     */
    public static final int PLAYBACK_RATE = 78000;//76800

    /**
     * List of the most used sample rates.
     */
    public static final int[] SAMPLE_RATES = new int[]{8000, 11025,
            16000, 22050, 32000, 37800, 44056, 44100, 47250, 48000, 50000,
            50400, 88200, 96000, 176400, 192000, 352800, 2822400, 5644800};
    private static byte[] audioBuffer = new byte[1];

    /**
     * super constructor to be used as a wrapper constructor.
     */
    private GameAudioLoop(int streamType, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes, int mode)
    {
        super(streamType, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, mode);
    }

    /**
     * Creates a new GameAudioLoop using an inputStream which is an audio file of PCM format.
     * Precondition: file must be a PCM format.
     * @param inputStream
     */
    public GameAudioLoop(InputStream inputStream)
    {
        this(AudioManager.STREAM_MUSIC, getValidSampleRate(),
                AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT,
                ((audioBuffer = inputStreamToBytes(inputStream)).length * 2) , AudioTrack.MODE_STATIC);
        write(audioBuffer, 0, audioBuffer.length);
        setLoopPoints(0, audioBuffer.length / 2, -1);
        setPlaybackRate(PLAYBACK_RATE);
    }

    /**
     * Sets the audio to a given speed rate.
     * playbackRate must be > 0. if playbackRate == 2,
     * the audio will be played twice as fast than the original speed.
     *
     * @param playbackRate The speed rate to play the audio.
     */
    public void setPlaybackRate(float playbackRate)
    {
        if (playbackRate != 0)
        {
            setPlaybackRate((int) (PLAYBACK_RATE * Math.abs(playbackRate)));
        }
    }

    /**
     * Converts the given inputStream to an array of bytes.
     *
     * @param inputStream the stream to be converted.
     * @return an array of bytes which contains values of the given inputStream.
     *         If fails to convert, the returned array length is zero.
     */
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

    /**
     * Finds a valid sample rate from a list of the most used sample rates.
     * @return a valid sample rate or -1 if no valid sample rate is found in the list.
     */
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
