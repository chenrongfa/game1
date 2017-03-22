package com.spfp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
	//记录当前圆形所在九宫格的位置下标
	private int creentTileIndex;
	//声明一个SharedPreferences对象
	private SharedPreferences sp;

	/**
	 * SurfaceView初始化函数
	 */
	public MySurfaceView(Context context) {
		super(context);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		setFocusable(true);
		//通过Context获取SharedPreference实例
		sp = context.getSharedPreferences("SaveName", Context.MODE_PRIVATE);
		//每次程序运行时获取圆形的下标
		int tempIndex = sp.getInt("CirCleIndex", -1);
		//判定如果返回-1 说明没有找到，就不对当前记录圆形的变量进行赋值
		if (tempIndex != -1) {
			creentTileIndex = tempIndex;
		}
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
				canvas.drawColor(Color.WHITE);
				paint.setColor(Color.BLACK);
				paint.setStyle(Style.STROKE);
				//绘制九宫格(将屏幕九等份)
				//得到每个方格的宽高
				int tileW = screenW / 3;
				int tileH = screenH / 3;
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						canvas.drawRect(i * tileW, j * tileH, (i + 1) * tileW, (j + 1) * tileH, paint);
					}
				}
				//根据得到的圆形下标位置进行绘制相应的方格中
				paint.setStyle(Style.FILL);
				canvas.drawCircle(creentTileIndex % 3 * tileW + tileW / 2, creentTileIndex / 3 * tileH + tileH / 2, 30, paint);
				//操作说明
				canvas.drawText("上键：保存游戏", 0, 20, paint);
				canvas.drawText("下键：读取游戏", 110, 20, paint);
				canvas.drawText("左右键：移动圆形", 215, 20, paint);
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
		return true;
	}

	/**
	 * 按键事件监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//上键保存游戏状态
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			sp.edit().putInt("CirCleIndex", creentTileIndex).commit();
			//下键读取游戏状态
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			int tempIndex = sp.getInt("CirCleIndex", -1);
			if (tempIndex != -1) {
				creentTileIndex = tempIndex;
			}
			//圆形的移动
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if (creentTileIndex > 0) {
				creentTileIndex -= 1;
			}
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			if (creentTileIndex < 8) {
				creentTileIndex += 1;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 游戏逻辑
	 */
	private void logic() {
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
