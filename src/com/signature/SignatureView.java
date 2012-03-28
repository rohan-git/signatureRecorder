package com.signature;

import java.util.ArrayList;
import java.util.List;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TableLayout.LayoutParams;

public class SignatureView extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = "MyActivity";

	PaintThread paintThread;
	Thread privateThread;
	SurfaceHolder holder;
	Bitmap initialBitmap;
	int width, height;

	public SignatureView(Context context) {
		super(context);

		doConstruction(context);

	}

	public SignatureView(Context context, AttributeSet as) {
		super(context, as);
		doConstruction(context);
	}

	public SignatureView(Context context, AttributeSet as, int defStyle) {
		super(context, as, defStyle);
		doConstruction(context);
	}

	private void doConstruction(Context context) {

		holder = getHolder();
		holder.addCallback(this);
		setFocusable(true);
		paintThread = new PaintThread(holder, context);
		privateThread = new Thread(paintThread);

	}

	public PaintThread getPaintThread() {
		return paintThread;
	}

	public Bitmap getBitmap() {
		// TODO Auto-generated method stub
		return this.initialBitmap;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		Log.w(TAG, "->> in surface chandeg");

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// event.get
		float oldX, oldY;
		oldX = event.getX();
		oldY = event.getY();

		float tempX = 0f, tempY = 0f;
		if (event.getAction() == MotionEvent.ACTION_MOVE
				|| event.getAction() == MotionEvent.ACTION_DOWN) {

			int p = event.getPointerCount();

			for (int i = 0; i < p; i++) {
				paintThread.setCoordinates(event.getX(i), event.getY(i));
			}
			Log.w(TAG, "->> touch event");
		}

		// Log.w(TAG, "->> touch event");
		return true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.w(TAG, "->> surface created");
		paintThread.setRunning(true);

		privateThread.start();
		// paintThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		paintThread.setRunning(false);

		// Log.e(TAG, "->> average time taken" + paintThread.getAverageTime());
		Log.e(TAG, "->> in surface destroyed");

	}

	class PaintThread implements Runnable {

		private static final float MIN_DISTANCE = 2;
		private static final float CIRCLE_RADIUS = 5;
		SurfaceHolder surfaceHolder;
		Context context;
		Bitmap background;

		Paint paint;
		int oldSize;

		List<Long> timeList;
		List<Float> XList;
		List<Float> YList;
		private boolean running = false;

		// private Canvas canvas;
		// private boolean saveFile;

		public PaintThread(SurfaceHolder surfaceHolder, Context context) {

			this.surfaceHolder = surfaceHolder;
			this.context = context;
			// this.canvas = new Canvas();

			paint = new Paint();
			paint.setColor(Color.WHITE);

			XList = new ArrayList<Float>();
			YList = new ArrayList<Float>();

			timeList = new ArrayList<Long>();

		}

		public List<Float> getXList() {
			return this.XList;
		}

		public List<Float> getYList() {
			return this.YList;
		}

/*
		// private void setCanvas(Canvas c) {
		//
		// synchronized (surfaceHolder) {
		// this.canvas = c;
		// }

		// Log.e("SURFACE"," h: "+this.canvas.getHeight()+" w: "+this.canvas.getWidth()
		// );
		// }

		// public Bitmap getBitmap() {
		// Bitmap temp;
		// //synchronized (surfaceHolder) {
		// temp = Bitmap.createBitmap(this.savedBitmap);
		// //}
		// return temp;
		//
		// // saveFile = true;
		//
		// // saveFile = false;
		//
		// // Canvas pCanvas;
		// // setRunning(false);
		// // Log.e("SURFACE", "<<< h: " + this.canvas.getHeight() + " w: "
		// // + this.canvas.getWidth());
		// // pCanvas = this.canvas;
		// // setRunning(true);
		// // return pCanvas;
		// }

		/*
		 * public String getAverageTime() {
		 * 
		 * Long sum = 0l; for (Long l : timeList) { sum += l; }
		 * 
		 * if (timeList.size() > 0) return (sum / timeList.size()) + ""; else
		 * return "0";
		 * 
		 * }
*/

		public void setCoordinates(float x, float y) {
			Log.w(TAG, "->> in set Coordinates");
			synchronized (surfaceHolder) {
				// if (XList.get(XList.size()-1) - x > MIN_DISTANCE
				// || YList.get(YList.size()-1) - y > MIN_DISTANCE) {
				XList.add(x);
				YList.add(y);
				// }
			}

		}

		public void setRunning(boolean running) {
			this.running = running;
		}

		@Override
		public void run() {
			// Log.w(TAG, " ->> in run/start method");
			while (running) {
				Canvas c = null;

				try {
					c = surfaceHolder.lockCanvas(null);
					synchronized (surfaceHolder) {
						updateCanvas(c);

						// setCanvas(c);
					}
				} finally {
					// do this in a finally so that if an exception is thrown
					// during the above, we don't leave the Surface in an
					// inconsistent state
					if (c != null) {
						surfaceHolder.unlockCanvasAndPost(c);
					}
				}
			}

		}

		private void updateCanvas(Canvas c) {

			// Log.w(TAG, "->> in update canvas");
			long startTime = System.nanoTime();
			int size = XList.size();

			if (initialBitmap == null) {
				initialBitmap = Bitmap.createBitmap(c.getWidth(), c.getHeight(),
						Bitmap.Config.ARGB_8888);				
			}
			
			Canvas tempCanvas = new Canvas(initialBitmap);

			if (size > oldSize) {
				for (int i = 0; i < size - 1; i++) {

					tempCanvas.drawCircle(XList.get(i), YList.get(i), CIRCLE_RADIUS,
							paint);
				}
				c.drawBitmap(initialBitmap, 0, 0, paint);
				oldSize = size;
			}

			long enTime = System.nanoTime();
			timeList.add(enTime - startTime);
		}

		// private void saveCanvas(Canvas c) {
		// // if (saveFile == true) {
		// c.drawBitmap(initialBitmap, null, null);
		// // }
		// }

	}

}
