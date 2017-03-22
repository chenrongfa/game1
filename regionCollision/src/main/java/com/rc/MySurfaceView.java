package com.rc;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

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
	//���ゆ�烽���ゆ�烽���ゆ�锋�����ゆ�烽���ゆ��
	private Rect rect = new Rect(0, 0, 50, 50);
	//���ゆ�烽���ゆ��Region���ゆ�峰�����ゆ��
	private Region r = new Region(rect);
	//���ゆ�风ず��瑙��ゆ�烽���ゆ�烽���ゆ�锋����渚ユ���疯��浣�
	private boolean isInclude;
	/**
	 * SurfaceView���ゆ�峰����ゆ�烽���ゆ�烽���ゆ��
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
	 * SurfaceView���ゆ�峰�鹃���ゆ�烽���ゆ�烽���ゆ�烽���ゆ�峰����跨�存�烽���ゆ��
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenW = this.getWidth();
		screenH = this.getHeight();
		flag = true;
		//瀹����ゆ�烽��绔�绛规��
		th = new Thread(this);
		//���ゆ�烽���ゆ�烽��绔�绛规��
		th.start();
	}

	/**
	 * ���ゆ�锋�����ゆ�峰��
	 */
	public void myDraw() {
		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.BLACK);
				//���ゆ�疯��浣�涓洪���ゆ�锋�堕���ゆ�烽���ゆ�烽���ゆ��icon��
				if (isInclude) {
					canvas.drawBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.icon), 100, 50, paint);
				}
				//���ゆ�烽���℃���烽���ゆ�烽���ゆ�烽��娲ワ����烽����瑙�瀵�锛�
				canvas.drawRect(rect, paint);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}

	/**
	 * ���ゆ�烽���ゆ�烽���扮》�烽���ゆ�烽���ゆ��
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//����璁规�烽����浼��烽���ゆ�烽���ゆ�烽���ゆ�烽���ゆ�烽���ゆ�烽���ゆ�锋�����ゆ�烽���ゆ�烽���惰�ф�烽���ゆ�烽���ゆ�烽��锟�
		if (r.contains((int) event.getX(), (int) event.getY())) {
			isInclude = true;
		} else {
			isInclude = false;
		}
		return true;
	}

	/**
	 * ���ゆ�烽���ゆ�烽���扮》�烽���ゆ�烽���ゆ��
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ���ゆ�锋����绔�纭锋��
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
	 * SurfaceView���ゆ�峰�剧�舵�����ゆ�烽���ゆ�烽��渚ュ��锛����ゆ�峰����跨�存�烽���ゆ��
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	/**
	 * SurfaceView���ゆ�峰�鹃���ゆ�烽���ゆ�锋�堕���ゆ�烽���ゆ�峰����跨�存�烽���ゆ��
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
	}
}
