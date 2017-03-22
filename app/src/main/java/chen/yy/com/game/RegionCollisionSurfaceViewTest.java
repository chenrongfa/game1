package chen.yy.com.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
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

public class RegionCollisionSurfaceViewTest extends SurfaceView implements SurfaceHolder.Callback
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
	private boolean isUp,isDown,isLeft,isRight,isStart,isEnd;
	private int width;
	private int heigth;
	private int col;
	private int direction;
	private int LEFT=1;
	private int RIGHT=0;
	private Paint paintRed;
	private boolean isCollision=false;
	private Rect rect;


	public RegionCollisionSurfaceViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		paintRed=new Paint(Paint.ANTI_ALIAS_FLAG);
		paintRed.setColor(Color.RED);


	}

	public RegionCollisionSurfaceViewTest(Context context, AttributeSet attrs) {
		this(context, attrs,0);

	}
	private int robot_x;
	private int  robot_y;

	private int x1=100,y1=100, w1=50,h1=50;
	private int x2=150,y2=150, w2=50,h2=50;

	private void draw() {

		Canvas canvas = sfh.lockCanvas();
		if(paint==null)
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.WHITE);
		canvas.drawColor(Color.BLACK);
		if(isCollision){
			canvas.drawRect(x1,y1,x1+w1,h1+y1,paintRed);
		}else{
			canvas.drawRect(x1,y1,w1+x1,h1+y1,paint);
		}
		if(rect==null)
			rect = new Rect(x2,y2,x2+w2,y2+h2);
		canvas.drawRect(rect,paint);
		sfh.unlockCanvasAndPost(canvas);


	}

	public RegionCollisionSurfaceViewTest(Context context) {
		this(context,null);
		Log.d(TAG, "SurfaceViewTest() called with: context = [" + context + "]");
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.robot);
		sfh=getHolder();
		sfh.addCallback(this);
		mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.GREEN);
		mPaint.setTextSize(20);
		setFocusable(true);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d(TAG, "onDraw() called with: canvas = [" + canvas + "]");

	}

	private int currentFrame=0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e(TAG, "onKeyDown: " );
		switch (keyCode){
			case KeyEvent.KEYCODE_DPAD_UP:
				isUp=true;
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				isDown=true;
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				Log.e(TAG, "onKeyDown: left" );
				isLeft=true;
				direction=LEFT;
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				isRight=true;
				direction=RIGHT;
				break;


		}


		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode){
			case KeyEvent.KEYCODE_DPAD_UP:
				isUp=false;
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				isDown=false;
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				isLeft=false;
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				isRight=false;
				break;


		}

		return super.onKeyUp(keyCode,event);
	}

	@Override
	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
		Log.e(TAG, "onKeyShortcut: " );
		return super.onKeyShortcut(keyCode, event);
	}

	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		Log.e(TAG, "onKeyMultiple: " );
		return super.onKeyMultiple(keyCode, repeatCount, event);
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {

		thread=new Thread(this);
		thread.start();
		flag=true;

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
		 x1= (int) event.getX()-w1;
		 y1= (int) event.getY()-h1;

				isCollision=isCollision();
				draw();





		return true;
	}


	private boolean isCollision() {
		Region region=new Region(rect);
		if(region.op(x1,y1,x1+w1,h1+y1, Region.Op.UNION))
			return true;

		return false;
	}

	@Override
	public void run() {
		while (flag){
			long start= SystemClock.currentThreadTimeMillis();
			isCollision=isCollision();
			draw();
			if(isLeft){
				x1-=5;
			}else if(isRight){
				x1+=5;
			}else if(isDown){
				y1+=5;
			}else if(isUp){
				y1-=5;
			}



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
