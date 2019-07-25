package com.adtv.tetrisapk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity
{

    private static final int SPAWN_ROW = 0;
    private static final int SPAWN_COL = 4;

    private GameHandler gameHandler;
    private GameTimer gameTimer;
    private SoundPool soundPool;
    private GameAudioLoop gameAudioLoop;

    private Tetris tetris;
    private TextView timeTxt = null;
    private TextView score = null;
    private Button play;

    private long counter = 0;
    //private float audioSpeed = 1.0f;


    @SuppressLint("ClickableViewAccessibility")//////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        score = (TextView) findViewById(R.id.score);
        timeTxt = (TextView) findViewById(R.id.time);
        play = (Button) findViewById(R.id.play);

        gameHandler = new GameHandler();
        gameTimer = new GameTimer(timeTxt);
        gameAudioLoop = new GameAudioLoop(getResources().openRawResource(R.raw.tetrispcm));

        if (tetris == null)
        {
            play.post(new Runnable()
            {
                @Override
                public void run()
                {
                    tetris = newTetris(((TableLayout) (findViewById(R.id.tetris_board))), ((TableLayout) (findViewById(R.id.next_block))));
                    gameAudioLoop.play();
                    play.removeCallbacks(this);
                }
            });
        }


        findViewById(R.id.down).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                downClick(null);
                return false;
            }
        });

        ((TableLayout)findViewById(R.id.gesture_ctl)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                event.setAction(MotionEvent.ACTION_MOVE);
                onTouchEvent(event);
                return true;
            }
        });
    }

    protected class GameHandler extends Handler implements Runnable
    {
        @Override
        public void run()
        {
            downClick(null);
            if (gameTimer.isRunning())
            {
                gameHandler.postDelayed(this, 1000);//1000?
            }
        }
    }



    private Tetris newTetris(TableLayout tetris_board, TableLayout next_block)
    {
        return new Tetris(tetris_board, next_block);
    }

    public void leftClick(View view)
    {
        if(gameTimer.isRunning())
        {
            tetris.currentBlock.move(0,-1, false);
        }
        Log.e("*****************", "leftClick");
    }
    public void rightClick(View view)
    {
        if(gameTimer.isRunning())
        {
            tetris.currentBlock.move(0,1, false);
        }
        Log.e("*****************", "rightClick");
    }
    public void upClick(View view)
    {
        if(gameTimer.isRunning())
        {
            tetris.currentBlock.move(-1,0, false);
        }
        Log.e("*****************", "upClick");
    }
    public/* synchronized*/ void downClick(View view)
    {
        if (gameTimer.isRunning() && counter++ == 0)
        {
            tetris.spawn(SPAWN_ROW,SPAWN_COL);
        }
        else if (gameTimer.isRunning() && !tetris.currentBlock.move(1 ,0, false))
        {
            Log.e("*****************", "0downClick");
            if (!tetris.spawn(SPAWN_ROW,SPAWN_COL))
            {
                Log.e("*****************", "1downClick GAME OVER ==================" + counter);
                Block block = tetris.currentBlock;
                block.move(SPAWN_ROW, SPAWN_COL, true);
                gameTimer.stop();
                play.setText("play");//?
                counter = 0;
            }
            score.setText("" + tetris.rowsCleared);
        }
        Log.e("*****************", "2downClick");
    }
    public void rotateClick(View view)
    {
        if(gameTimer.isRunning())
        tetris.currentBlock.rotate();
    }

    public void playClick(View view)
    {
        if (play.getText().toString().toLowerCase().equals("play"))
        {
            if (counter == 0)
            {
                Button[] btns = {play, ((Button)findViewById(R.id.down))};
                gameAudioLoop.pause();
                gameAudioLoop.release();
                gameAudioLoop = new GameAudioLoop(getResources().openRawResource(R.raw.tetrispcm));
                tetris.reset(gameHandler, btns, gameAudioLoop, gameTimer, true);
            }
            else
            {
                gameHandler.post(gameHandler);
                gameAudioLoop.play();
                gameTimer.start();
            }
            play.setText("pause");//?
        }
        else
        {
            gameHandler.removeCallbacks(gameHandler);
            gameTimer.stop();
            gameAudioLoop.pause();
            play.setText("play");//?
        }
    }
    private void sleep(long delay)
    {
        try
        {
            Thread.sleep(delay);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    public void helpClick(View view)
    {
        Toast.makeText(this, "HELP is called", Toast.LENGTH_SHORT).show();
        gameTimer.stop();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        gameTimer.stop();
        gameHandler.removeCallbacks(gameHandler);
        gameAudioLoop.pause();
        play.setText("play");//?
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //soundPool.release();
        gameAudioLoop.release();
        gameAudioLoop = null;
        soundPool = null;
    }






    //TESTING========TESTING========TESTING========TESTING========TESTING========

    boolean isMoving = false;
    int x, y;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                Log.e("TAG", "moving(INIT): (" + x + ", " + y + ")");
                // reverse direction of rotation above the mid-line
/*                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

                mRenderer.setAngle(
                        mRenderer.getAngle() +
                                ((dx + dy) * TOUCH_SCALE_FACTOR));
                requestRender();
                */
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
/*    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TAG", "");
        int tempX = (int)event.getX();
        int tempY = (int)event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("TAG", "touched down");
                break;
            case MotionEvent.ACTION_MOVE:
                //if (!isMoving)
                {
                    x=tempX;
                    y=tempY;
                    isMoving = true;

                    Log.e("TAG", "moving(INIT): (" + tempX + ", " + tempY + ")");
                }

                break;
            case MotionEvent.ACTION_UP:
                isMoving = false;
*//*                Log.e("TAG", "");
                Log.e("TAG", "moving(FINAL1): (" + tempX + ", " + tempY + ")");
                Log.e("TAG", "moving(FINAL2): (" + x + ", " + y + ")");
                Log.e("TAG", "moving(FINAL3): (" + (x-tempX) + ", " + (y-tempY) + ")");*//*
                Log.e("TAG", "touched up");
                break;
        }
        return false;
    }*/













}