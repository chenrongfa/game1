package com.mrc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

/**
 * 
 * @author Himi
 *
 */
public class MySurfaceView extends SurfaceView implements Callback, Runnable {
	private SurfaceHolder sfh;
	private Paint paint;
	private Thread th;
	private boolean flag;
	private Canvas canvas;
	private int screenW, screenH;
	//定义两个矩形图形的宽高坐标
	private int rectX1 = 10, rectY1 = 10, rectW1 = 40, rectH1 = 40;
	private int rectX2 = 100, rectY2 = 110, rectW2 = 40, rectH2 = 40;
	//便于观察是否发生了碰撞设置一个标识位
	private boolean isCollsion;
	//定义第一个矩形的矩形碰撞数组
	private Rect clipRect1 = new Rect(0, 0, 15, 15);
	private Rect clipRect2 = new Rect(rectW1 - 15, rectH1 - 15, rectW1, rectH1);
	private Rect[] arrayRect1 = new Rect[] { clipRect1, clipRect2 };
	//定义第二个矩形的矩形碰撞数组
	private Rect clipRect3 = new Rect(0, 0, 15, 15);
	private Rect clipRect4 = new Rect(rectW2 - 15, rectH2 - 15, rectW2, rectH2);
	private Rect[] arrayRect2 = new Rect[] { clipRect3, clipRect4 };

	/**
	 * SurfaceView初始化函数
	 */
	public MySurfaceView(Context context) {
		super(context);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		setFocusable(true);
	}

	/**
	 * SurfaceView视图创建，响应此函数
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenW = this.getWidth();
		screenH = this.getHeight();
		flag = true;
		//实例线程
		th = new Thread(this);
		//启动线程
		th.start();
	}

	/**
	 * 游戏绘图
	 */
	public void myDraw() {
		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.BLACK);
				paint.setColor(Color.WHITE);
				paint.setStyle(Style.FILL);
				if (isCollsion) {
					paint.setTextSize(20);
					canvas.drawText("Collision！", 0, 30, paint);
				}
				//绘制两个矩形
				canvas.drawRect(rectX1, rectY1, rectX1 + rectW1, rectY1 + rectH1, paint);
				canvas.drawRect(rectX2, rectY2, rectX2 + rectW2, rectY2 + rectH2, paint);
				//---绘制碰撞区域使用非填充，并设置画笔颜色白色
				paint.setStyle(Style.STROKE);
				paint.setColor(Color.RED);
				//绘制第一个矩形的所有矩形碰撞区域
				for (int i = 0; i < arrayRect1.length; i++) {
					canvas.drawRect(arrayRect1[i].left + this.rectX1, arrayRect1[i].top + this.rectY1, arrayRect1[i].right + this.rectX1, arrayRect1[i].bottom
							+ this.rectY1, paint);
				}
				//绘制第二个矩形的所有矩形碰撞区域
				for (int i = 0; i < arrayRect2.length; i++) {
					canvas.drawRect(arrayRect2[i].left + this.rectX2, arrayRect2[i].top + this.rectY2, arrayRect2[i].right + this.rectX2, arrayRect2[i].bottom
							+ rectY2, paint);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}

	/**
	 * 触屏事件监听
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//让矩形1随着触屏位置移动
		rectX1 = (int) event.getX() - rectW1 / 2;
		rectY1 = (int) event.getY() - rectH1 / 2;
		if (isCollsionWithRect(arrayRect1, arrayRect2)) {
			isCollsion = true;
		} else {
			isCollsion = false;
		}
		return true;
	}

	/**
	 * 按键事件监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 游戏逻辑
	 */
	private void logic() {

	}

	//Rect 类中的四个属性  top bottom left right
	//分别表示这个矩形的  	    上       下            左          右
	public boolean isCollsionWithRect(Rect[] rectArray, Rect[] rect2Array) {
		Rect rect = null;
		Rect rect2 = null;
		for (int i = 0; i < rectArray.length; i++) {
			//依次取出第一个矩形数组的每个矩形实例
			rect = rectArray[i];
			//获取到第一个矩形数组中每个矩形元素的属性值
			int x1 = rect.left + this.rectX1;
			int y1 = rect.top + this.rectY1;
			int w1 = rect.right - rect.left;
			int h1 = rect.bottom - rect.top;
			for (int j = 0; j < rect2Array.length; j++) {
				//依次取出第二个矩形数组的每个矩形实例
				rect2 = rect2Array[j];
				//获取到第二个矩形数组中每个矩形元素的属性值
				int x2 = rect2.left + this.rectX2;
				int y2 = rect2.top + this.rectY2;
				int w2 = rect2.right - rect2.left;
				int h2 = rect2.bottom - rect2.top;
				//进行循环遍历两个矩形碰撞数组所有元素之间的位置关系
				if (x1 >= x2 && x1 >= x2 + w2) {
				} else if (x1 <= x2 && x1 + w1 <= x2) {
				} else if (y1 >= y2 && y1 >= y2 + h2) {
				} else if (y1 <= y2 && y1 + h1 <= y2) {
				} else {
					//只要有一个碰撞矩形数组与另一碰撞矩形数组发生碰撞则认为碰撞
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void run() {
		while (flag) {
			long start = System.currentTimeMillis();
			myDraw();
			logic();
			long end = System.currentTimeMillis();
			try {
				if (end - start < 50) {
					Thread.sleep(50 - (end - start));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * SurfaceView视图状态发生改变，响应此函数
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	/**
	 * SurfaceView视图消亡时，响应此函数
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
	}
}
