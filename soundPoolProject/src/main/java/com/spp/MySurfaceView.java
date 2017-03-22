package com.spp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
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
	//声明SoundPool
	private SoundPool sp;
	//记录长音乐文件id
	private int soundId_long;
	//记录断短音乐文件id
	private int soundId_short;

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
			//实例SoundPool播放器
			sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
			//加载音乐文件获取其数据ID
			soundId_long = sp.load(context, R.raw.himi_long, 1);
			//加载音乐文件获取其数据ID
			soundId_short = sp.load(context, R.raw.himi_short, 1);
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
				paint.setColor(Color.RED);
				paint.setTextSize(15);
				canvas.drawText("点击导航键的上键：播放断音效", 50, 50, paint);
				canvas.drawText("点击导航键的下键：播放长音效", 50, 80, paint);
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
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
			sp.play(soundId_long, 1f, 1f, 0, 0, 1);
		else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
			sp.play(soundId_short, 2, 2, 0, 0, 1);
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
