package chen.yy.com.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

;

/**
 * Game
 * Created by chenrongfa on 2017/3/20
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class ClipSurfaceViewTest extends SurfaceView implements SurfaceHolder.Callback
, Runnable{

	private SurfaceHolder sfh;
	private Paint mPaint;
	private static final String TAG = "SurfaceViewTest";
	private int lastX;
	private int lastY;
	private Thread thread;
	private boolean flag;
	private Bitmap bitmap;
	private Paint paint;

	public ClipSurfaceViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		sfh=getHolder();
		sfh.addCallback(this);
		mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.GREEN);
		mPaint.setTextSize(20);
	}

	public ClipSurfaceViewTest(Context context, AttributeSet attrs) {
		this(context, attrs,0);

	}

	private void draw() {
		Canvas canvas = sfh.lockCanvas();
		if(paint==null)
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.WHITE);
		canvas.drawColor(Color.WHITE);
		canvas.save();
		canvas.clipRect(0,0,bitmap.getWidth()/10,bitmap.getHeight());
		canvas.drawBitmap(bitmap,bitmap.getWidth()/10*(-currenFrame),0, paint);
		sfh.unlockCanvasAndPost(canvas);

	}

	public ClipSurfaceViewTest(Context context) {
		this(context,null);
		Log.d(TAG, "SurfaceViewTest() called with: context = [" + context + "]");
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fish);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d(TAG, "onDraw() called with: canvas = [" + canvas + "]");

	}
	private int leftX;
	private  int  bottomY;
	private int currenFrame=0;

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		Log.d(TAG, "surfaceCreated() called with: surfaceHolder = [" + surfaceHolder + "]");
//		leftX=getWidth()-bitmap.getWidth();
//		bottomY=getHeight()-bitmap.getHeight();
		thread=new Thread(this);
		thread.start();
		flag=true;
//		draw();
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
		Log.d(TAG, "surfaceChanged() called with: surfaceHolder = [" + surfaceHolder + "], i = [" + i + "], i1 = [" + i1 + "], i2 = [" + i2 + "]");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		flag=false;
		Log.d(TAG, "surfaceDestroyed() called with: surfaceHolder = [" + surfaceHolder + "]");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int eventX= (int) event.getX();
		int eventY= (int) event.getY();
		switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				lastX=eventX;
				lastY=eventY;
				break;
			case MotionEvent.ACTION_MOVE:
				lastX=eventX;
				lastY=eventY;
				draw();
				break;


		}

		return true;
	}

	@Override
	public void run() {
		while (flag){
			long start= SystemClock.currentThreadTimeMillis();
			currenFrame+=1;
//			lastY++;
			if(currenFrame>=10){
				currenFrame=0;
			}
			draw();
			long end=SystemClock.currentThreadTimeMillis();
			int during= (int) (end-start);
			if(during>50){
				SystemClock.sleep(during-50);
			}{
				SystemClock.sleep(50-during);
			}

		}

	}
}
