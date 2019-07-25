package com.adtv.tetrisapk;


import android.view.MotionEvent;
import android.view.View;

public interface SwipeControl extends View.OnTouchListener
{
    @Override
    boolean onTouch(View v, MotionEvent event);


}
