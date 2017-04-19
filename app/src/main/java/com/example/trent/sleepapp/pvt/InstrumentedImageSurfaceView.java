package com.example.trent.sleepapp.pvt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class InstrumentedImageSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	public RunTest runTest = null;	//hack to access timing from RunTest for delay calculation
	public boolean measure = false;	//should we record timings?

	public volatile int color = Color.WHITE;
	
	private SurfaceHolder surfaceHolder;
	private Surface surface;
	private Rect surfaceFrame = new Rect();
	private Paint colorPaint = new Paint();
  
  public InstrumentedImageSurfaceView(Context context) {
		super(context);
		init();
	}

	public InstrumentedImageSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
    getHolder().addCallback(this);
    surfaceHolder = getHolder();
    surfaceHolder.setFormat(PixelFormat.OPAQUE);
    surface = surfaceHolder.getSurface();
    setFocusable(true);
	}

  @Override
  public void onDraw(Canvas canvas) {
  	canvas.drawColor(Color.WHITE);
  	colorPaint.setColor(color);
  	float w = canvas.getWidth() / 5;
  	float h = canvas.getHeight() / 6;
  	canvas.drawRect(0, 0, w, h, colorPaint);
  	canvas.drawRect(2*w, 0, 3*w, h, colorPaint);
  	canvas.drawRect(4*w, 0, 5*w, h, colorPaint);

  	canvas.drawRect(w, h, 2*w, 2*h, colorPaint);
  	canvas.drawRect(3*w, h, 4*w, 2*h, colorPaint);
  	
  	canvas.drawRect(0, 2*h, w, 3*h, colorPaint);
  	canvas.drawRect(2*w, 2*h, 3*w, 3*h, colorPaint);  	
  	canvas.drawRect(4*w, 2*h, 5*w, 3*h, colorPaint);
  	
  	canvas.drawRect(w, 3*h, 2*w, 4*h, colorPaint);
  	canvas.drawRect(3*w, 3*h, 4*w, 4*h, colorPaint);

  	canvas.drawRect(0, 4*h, w, 5*h, colorPaint);
  	canvas.drawRect(2*w, 4*h, 3*w, 5*h, colorPaint);  	
  	canvas.drawRect(4*w, 4*h, 5*w, 5*h, colorPaint);

  	if (measure) {
  		runTest.t_2 = System.nanoTime();	//TODO: remove this stuff after verifying that timing is correct
//  		long measuredDelay = t_2 - runTest.t_0;
//  		long epsilon = measuredDelay - runTest.stimulusDelay * 1000000;
//  		Log.d(runTest.TAG, "t_2 = " + t_2 + ", measured d = " + measuredDelay + ", epsilon = " + epsilon);
  		measure = false;
  	}
  }
  
  /**
   * Redraw this surface immediately
   */
  @SuppressLint("WrongCall")		//suppress WrongCall for call to onDraw()
  public void draw() {
  	Canvas c = null;
  	try {
  		c = surface.lockCanvas(surfaceFrame);
  		if (c != null) {
  			onDraw(c);
  		}
  	} catch (Surface.OutOfResourcesException e) {
  		e.printStackTrace();
  	} catch (IllegalArgumentException e) {
  		e.printStackTrace();
  	} finally {
  		// do this in a finally so that if an exception is thrown
  		// during the above, we don't leave the Surface in an
  		// inconsistent state
  		if (c != null) {
  			surface.unlockCanvasAndPost(c);
  		}
  	}
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
  	surfaceFrame = holder.getSurfaceFrame();
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
  }
}
