package com.mpp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * 
 * @author Himi
 *
 */
public class MySurfaceView extends SurfaceView implements Callback, Runnable,OnCompletionListener {
	private SurfaceHolder sfh;
	private Paint paint;
	private Thread th;
	private boolean flag;
	private Canvas canvas;
	private int screenW, screenH;
	//声明音乐的状态常量
	private final int MEDIAPLAYER_PAUSE = 0;//暂停
	private final int MEDIAPLAYER_PLAY = 1;//播放中
	private final int MEDIAPLAYER_STOP = 2;//停止
	//音乐的当前的状态
	private int mediaSate = 0;
	//声明一个音乐播放器
	private MediaPlayer mediaPlayer;
	//当前音乐播放的时间点
	private int currentTime;
	//当前音乐的总时间
	private int musicMaxTime;
	//当前音乐的音量大小
	private int currentVol;
	//快进、快退时间戳
	private int setTime = 5000;
	//播放器管理类
	private AudioManager am;

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
		//实例音乐播放器
		mediaPlayer = MediaPlayer.create(context, R.raw.bgmusic);
		//设置循环播放(设置了循环，“OnCompletionListener”监听器无法监听音乐是否播放完成)
		//mediaPlayer.setLooping(true);//设置循环播放  
		//获取音乐文件的总时间
		musicMaxTime = mediaPlayer.getDuration();
		//实例管理类
		am = (AudioManager) MainActivity.instance.getSystemService(Context.AUDIO_SERVICE);
		//设置当前调整音量大小只是针对媒体音乐进行调整
		MainActivity.instance.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		//绑定音乐完成监听器
		mediaPlayer.setOnCompletionListener(this);
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
				canvas.drawText("当前音量: " + currentVol, 50, 40, paint);
				canvas.drawText("当前播放的时间(毫秒)/总时间(毫秒)", 50, 70, paint);
				canvas.drawText(currentTime + "/" + musicMaxTime, 100, 100, paint);
				canvas.drawText("方向键中间按钮切换 暂停/开始", 50, 130, paint);
				canvas.drawText("方向键←键快退" + setTime / 1000 + "秒 ", 50, 160, paint);
				canvas.drawText("方向键→键快进" + setTime / 1000 + "秒 ", 50, 190, paint);
				canvas.drawText("方向键↑键增加音量 ", 50, 220, paint);
				canvas.drawText("方向键↓键减小音量", 50, 250, paint);
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
		//导航中键播放/暂停操作
		if (keyCode == KeyEvent.KEYCODE_0) {
			try {
				switch (mediaSate) {
				//当前处于播放的状态
				case MEDIAPLAYER_PLAY:
					mediaPlayer.pause();
					mediaSate = MEDIAPLAYER_PAUSE;
					break;
				//当前处于暂停的状态
				case MEDIAPLAYER_PAUSE:
					mediaPlayer.start();
					mediaSate = MEDIAPLAYER_PLAY;
					break;
				//当前处于停止的状态
				case MEDIAPLAYER_STOP:
					/*使用android MediaPlayer播放一段音乐时，有时会出现prepareasync called in state 8错误。
					以下方法可以避免这个异常出现,   //在播放之前先判断playerMusic是否被占用，这样就不会报错了*/
					if (mediaPlayer != null) {
						mediaPlayer.pause();
						mediaPlayer.stop();
					}
					mediaPlayer.prepare();
					mediaPlayer.start();
					mediaSate = MEDIAPLAYER_PLAY;
					break;
				}
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//导航上键调整音乐播放声音变大
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol + 1, AudioManager.FLAG_PLAY_SOUND);
			//导航下键调整音乐播放声音变小
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol - 1, AudioManager.FLAG_PLAY_SOUND);
			//导航左键调整音乐播放时间倒退五秒			
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if (currentTime - setTime <= 0) {
				mediaPlayer.seekTo(0);
			} else {
				mediaPlayer.seekTo(currentTime - setTime);
			}
			//导航右键调整音乐播放时间快进五秒		
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			if (currentTime + setTime >= musicMaxTime) {
				mediaPlayer.seekTo(musicMaxTime);
			} else {
				mediaPlayer.seekTo(currentTime + setTime);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 游戏逻辑
	 */
	private void logic() {
		if (mediaPlayer != null) {
			//获取当前音乐播放的时间
			currentTime = mediaPlayer.getCurrentPosition();
			//获取当前的音量值  
			currentVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
			//获取当前的音量最大值  
			//int valueMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		} else {
			currentTime = 0;
		}
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
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
	}
	/**
	 * 监听音乐是否播放完成
	 */
	@Override
	public void onCompletion(MediaPlayer arg0) {
		if (mediaPlayer == arg0) {
			Log.v("Himi", "Play Completed");
		}
	}
}
