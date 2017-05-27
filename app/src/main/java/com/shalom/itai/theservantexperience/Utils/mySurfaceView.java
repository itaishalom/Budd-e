package com.shalom.itai.theservantexperience.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.shalom.itai.theservantexperience.R;

/**
 * Created by Itai on 26/05/2017.
 */

public class mySurfaceView extends SurfaceView implements
        SurfaceHolder.Callback {

   // private TutorialThread _thread;
    public boolean startFromOutside = false;
    public mySurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
  //      _thread = new TutorialThread(getHolder(), this);
  //      _thread.setPriority(Thread.MAX_PRIORITY);
    }


    public mySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    //    _thread = new TutorialThread(getHolder(), this);
        // TODO Auto-generated constructor stub
    }

    public mySurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getHolder().addCallback(this);
   //     _thread = new TutorialThread(getHolder(), this);
        // TODO Auto-generated constructor stub
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap _scratch = BitmapFactory.decodeResource(getResources(), R.drawable.angry);
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(_scratch, 10, 10, new Paint());

    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
       /*
        Canvas canvas = null;
        try {
            canvas = holder.lockCanvas(null);
            synchronized (holder) {
                onDraw(canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
        */
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
       /*
        boolean retry = true;
        _thread.setRunning(false);
        while (retry) {
            try {
                _thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
        */
    }
/*
    class TutorialThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private mySurfaceView _panel;
        private boolean _run = false;

        public TutorialThread(SurfaceHolder surfaceHolder, mySurfaceView panel) {
            _surfaceHolder = surfaceHolder;
            _panel = panel;
        }

        public void setRunning(boolean run) {
            _run = run;
        }

        @Override
        public void run() {
           // if (!startFromOutside)
          //      return;
            Canvas c;
            while (_run) {
                c = null;
                try {
                    c = _surfaceHolder.lockCanvas();
                    synchronized (_surfaceHolder) {
                        _panel.onDraw(c);
                    }
                } finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }*/
}