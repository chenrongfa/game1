package chen.yy.com.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;
import java.util.Vector;

import static chen.yy.com.myapplication.R.drawable.bullet;

/**
 * Game
 * Created by chenrongfa on 2017/3/21
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class PlaneGameSurface extends SurfaceView implements SurfaceHolder.Callback, Runnable {
	public static final int GAME_PLAYING = 1;
	private static final int GAME_MENU = 0;
	private static final int GAME_PAUSE = 2;
	private static final int GAME_LOST = 3;
	private static final int GAME_WIN = 4;
	private static final String TAG = "PlaneGameSurface";
	public static int gameState = 0;
	public static int screenW;
	public static int screenH;
	private SurfaceHolder sfh;
	private boolean flag;// 线程结束标志
	private Resources rc;
	private Bitmap bgmenu; //游戏菜单
	private Bitmap bgPlaying; //游戏背景
	private Bitmap bgBoom; //爆炸效果
	private Bitmap bgBossBoom; //boss爆炸效果
	private Bitmap bgBtn; // 游戏开始
	private Bitmap bgBtn_press; //游戏btn被按下
	private Bitmap bgDuck;
	private Bitmap bgFly;
	private Bitmap bgPig;
	private Bitmap bgWin;
	private Bitmap bgLog;
	private Bitmap bgPlayer;
	private Bitmap bgPlayerHp;  //血量
	private Bitmap bgBullet;    //子弹
	private Bitmap bgEnemyBullet; //敌机子弹
	public static Bitmap bgBossBullet; //boss 子弹
	private GameMenu gameMenu;
	private Gamebg gamebg;
	private Player player;
	private Paint paint;
	private Thread thread;
	private boolean isFirst;
	private boolean isRest;
	private Vector<Enemy> enemies;
	private int createTime = 50;
	private int count;
	private Context context;
	private int enemyArray[][] = {{1, 2}, {1, 1}, {1, 3, 1, 2}, {1, 2}, {2, 3}, {3, 1, 3}, {2, 2}, {1, 2}, {2, 2}, {1, 3, 1, 1}, {2, 1},
			{1, 3}, {2, 1}, {-1}};
	private int enemyIndex;
	private boolean isBoss;
	private Random random;
	public static Vector<Bullet> enemyBullet;
	private int countBullet;
	private Vector<Bullet> playerbullet;
	private int  countPlayerBullet;
	private Vector<Boom> booms;
	public static Vector<Bullet> vcBulletBoss;
	private Boss boss;

	public PlaneGameSurface(Context context) {
		this(context, null);

		//初始化


	}

	public PlaneGameSurface(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PlaneGameSurface(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		sfh = getHolder();
		sfh.addCallback(this);
		setFocusable(true);//要设置  不然 按键事件没有用
		rc = this.getResources();
		setFocusableInTouchMode(true);
		Log.e(TAG, "PlaneGameSurface:screenW " + screenW);
		Log.e(TAG, "PlaneGameSurface:screenH " + screenH);
		initFrame();
		this.context=context;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenH = this.getHeight();
		screenW = getWidth();
		initFrame();
		thread = new Thread(this);
		thread.start();
		flag = true;
		isRest = true;

		//shuapin
		draw();


	}
	//绘图

	public void draw() {
		Canvas canvas = null;
		try {
			canvas = sfh.lockCanvas();
			canvas.drawColor(Color.WHITE);
			switch (gameState) {
				case GAME_MENU:
					//					if(isFirst||isRest){
					gameMenu.draw(canvas);
					isFirst = false;
					isRest = false;
					//					}
					break;
				case GAME_PLAYING:
					gamebg.draw(canvas, paint);
					player.draw(canvas, paint);
					if (!isBoss) {
						//绘制敌机
						for (int i = 0; i < enemies.size(); i++) {
							enemies.get(i).draw(canvas, paint);
						}
						//绘制子弹
						for (int i = 0; i <enemyBullet.size() ; i++) {
							enemyBullet.get(i).draw(canvas,paint);
						}


					} else {
						//boss
						boss.draw(canvas,paint);
						for (int i = 0; i < vcBulletBoss.size(); i++) {
							vcBulletBoss.get(i).draw(canvas,paint);
						}

					}
					//主角子弹
					for (int i = 0; i <playerbullet.size() ; i++) {
						playerbullet.get(i).draw(canvas,paint);
					}
					//绘制爆炸
					for (int i = 0; i <booms.size() ; i++) {
					booms.get(i).draw(canvas,paint);
				}
					break;
				case GAME_PAUSE:
					break;
				case GAME_LOST:
					canvas.drawBitmap(bgLog,0,0,paint);
					break;
				case GAME_WIN:
					canvas.drawBitmap(bgWin,0,0,paint);
					break;
				default:
					break;
			}
		} catch (Exception e) {

		} finally {
			sfh.unlockCanvasAndPost(canvas);
		}


	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e("dd", "onKeyDown:dd " + keyCode);
		Log.e("dd", "onKeyDown: " + gameState);
		if (keyCode==KeyEvent.KEYCODE_BACK){
			if (gameState==GAME_PLAYING||gameState==
					GAME_LOST||gameState==GAME_WIN){
				enemyIndex=0;
				isBoss=false;
				gameState=GAME_MENU;
				initFrame();
				return true;

			}else if (gameState==GAME_MENU){
				((MainActivity)context).finish();
				System.exit(0);
			}
		}
		switch (gameState) {
			case GAME_MENU:
				break;
			case GAME_PLAYING:
				player.onKeyDown(event);
				break;
			case GAME_PAUSE:
				break;
			case GAME_LOST:
				break;
			case GAME_WIN:
				break;
			default:
				break;

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (gameState) {
			case GAME_MENU:
				break;
			case GAME_PLAYING:
				player.onKeyUp(event);
				break;
			case GAME_PAUSE:
				break;
			case GAME_LOST:
				break;
			case GAME_WIN:
				break;
			default:
				break;

		}
		return super.onKeyUp(keyCode, event);
	}

	public void logic() {
		switch (gameState) {
			case GAME_MENU:
				break;
			case GAME_PLAYING:
				break;
			case GAME_PAUSE:
				break;
			case GAME_LOST:
				break;
			case GAME_WIN:
				break;
			default:
				break;

		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (gameState) {
			case GAME_MENU:
				gameMenu.ontouch(event);
				break;
			case GAME_PLAYING:
				break;
			case GAME_PAUSE:
				break;
			case GAME_LOST:
				break;
			case GAME_WIN:
				break;
			default:
				break;

		}
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {



	}
 private Bitmap bgBoss;
	/**
	 * 加载图片资源
	 */
	private void initFrame() {
		if (gameState == GAME_MENU) {
			bgBoom = BitmapFactory.decodeResource(rc, R.drawable.boom);
			bgWin=BitmapFactory.decodeResource(rc,R.drawable.gamewin);
			bgLog=BitmapFactory.decodeResource(rc,R.drawable.gamelost);
			bgBossBoom = BitmapFactory.decodeResource(rc, R.drawable.boos_boom);
			bgBossBullet = BitmapFactory.decodeResource(rc, R.drawable.boosbullet);
			bgPlaying = BitmapFactory.decodeResource(rc, R.drawable.background);
			bgmenu = BitmapFactory.decodeResource(rc, R.drawable.menu);
			bgPlayerHp = BitmapFactory.decodeResource(rc, R.drawable.hp);
			bgBtn = BitmapFactory.decodeResource(rc, R.drawable.button);
			bgBtn_press = BitmapFactory.decodeResource(rc, R.drawable.button_press);
			bgBullet = BitmapFactory.decodeResource(rc, bullet);
			bgBossBullet = BitmapFactory.decodeResource(rc, R.drawable.boosbullet);
			bgEnemyBullet = BitmapFactory.decodeResource(rc, R.drawable.bullet_enemy);
			bgPig = BitmapFactory.decodeResource(rc, R.drawable.enemy_pig);
			bgDuck = BitmapFactory.decodeResource(rc, R.drawable.enemy_duck);
			bgFly = BitmapFactory.decodeResource(rc, R.drawable.enemy_fly);
			bgPlayer = BitmapFactory.decodeResource(rc, R.drawable.player);
			bgPlayerHp = BitmapFactory.decodeResource(rc, R.drawable.hp);
			gameMenu = new GameMenu(bgmenu, bgBtn, bgBtn_press);
			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			gamebg = new Gamebg(bgPlaying);
			player = new Player(bgPlayer, bgPlayerHp);
			isFirst = true;
			enemies = new Vector<>();
			random = new Random();
			enemyBullet=new Vector<>();
			playerbullet=new Vector<>();
			booms=new Vector<>();
			boss=new Boss(bgPig);
			vcBulletBoss=new Vector<>();
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		flag = false;
	}

	@Override
	public void run() {
		while (flag) {
			Log.e(TAG, "run: ");

			switch (gameState) {
				case GAME_MENU:

					break;
				case GAME_PLAYING:
					gamebg.run();
					player.run();
					if (!isBoss) {
						for (int i = 0; i < enemies.size(); i++) {
							Enemy enemy = enemies.get(i);
							if (enemy.isDead) {
								enemies.removeElementAt(i);
							} else {
								enemy.run();
							}
						}


					//生成敌机
					count++;
					if (count % createTime == 0) {
						for (int i = 0; i < enemyArray[enemyIndex].length; i++) {
							//苍蝇
							if (enemyArray[enemyIndex][i] == 1) {
								int x = random.nextInt(screenW - 100) + 50;
								enemies.addElement(new Enemy(bgFly, 0, x, -10));
								//鸭子左
							} else if (enemyArray[enemyIndex][i] == 2) {
								int y = random.nextInt(20);
								int x=random.nextInt(screenW);
								enemies.addElement(new Enemy(bgDuck, 1, x, y));
								//鸭子右
							} else if (enemyArray[enemyIndex][i] == 3) {
								int y = random.nextInt(20);
								enemies.addElement(new Enemy(bgDuck, 2, screenW + 50, y));
							}
						}
						//这里判断下一组是否为最后一组(Boss)
						if (enemyIndex == enemyArray.length - 1) {
							isBoss = true;
						} else {
							enemyIndex++;
						}
						//处理敌机与主角的碰撞
						for (int i = 0; i < enemies.size(); i++) {
							if (player.isCollision(enemies.elementAt(i))) {
								//发生碰撞，主角血量-1
								Log.e("dd", "run: 123" );
								player.setHp(player.getHp() - 1);
								//当主角血量小于0，判定游戏失败
								if (player.getHp() <= -1) {
									gameState = GAME_LOST;
								}
							}
						}
						//处理敌机子弹与主角的碰撞
						for (int i = 0; i < enemyBullet.size(); i++) {
							if (player.isCollision(enemyBullet.elementAt(i))) {
								//发生碰撞，主角血量-1
								Log.e("dd", "run: 123" );
								player.setHp(player.getHp() - 1);
								//当主角血量小于0，判定游戏失败
								if (player.getHp() <= -1) {
									gameState = GAME_LOST;
								}
							}
						}


					}
						//添加子弹
					countBullet++;
						if (countBullet%40==0){
							for (int i = 0; i <enemies.size() ; i++) {
								Enemy bullet = enemies.get(i);
								int type=0;
								switch (bullet.type){
									case Enemy.TYPE_FLY:
										type=Bullet.BUTTLET_FlY;
									case Enemy.TYPE_DUCKL:
									case Enemy.TYPE_DUCKR:
										type=Bullet.BUTTLET_DUCK;
								}
								enemyBullet.add(new Bullet(bgEnemyBullet
								,type,bullet.x+10,bullet.y));

							}

						}


						//处理逻辑
						for (int i = 0; i <enemyBullet.size() ; i++) {
							Bullet bullet = enemyBullet.get(i);
							if (bullet.isdead){
								enemyBullet.removeElementAt(i);
							}else{
								bullet.run();
							}

						}

					}else {//Boss相关逻辑
						//每0.5秒添加一个主角子弹
						boss.logic();
						if (countPlayerBullet % 10 == 0) {
							//Boss的没发疯之前的普通子弹
							vcBulletBoss.add(new Bullet(bgBossBullet,  Bullet.BUTTLET_BOSS,boss.x + 35, boss.y + 40,
									Bullet.DIR_DOWN));
						}
						//Boss子弹逻辑
						for (int i = 0; i < vcBulletBoss.size(); i++) {
							Bullet b = vcBulletBoss.elementAt(i);
							if (b.isdead) {
								vcBulletBoss.removeElement(b);
							} else {
								b.run();
							}
						}
						//Boss子弹与主角的碰撞
						for (int i = 0; i < vcBulletBoss.size(); i++) {
							if (player.isCollision(vcBulletBoss.elementAt(i))) {
								//发生碰撞，主角血量-1
								player.setHp(player.getHp() - 1);
								//当主角血量小于0，判定游戏失败
								if (player.getHp() <= -1) {
									gameState = GAME_LOST;
								}
							}
						}
						//Boss被主角子弹击中，产生爆炸效果
						for (int i = 0; i < playerbullet.size(); i++) {
							Bullet b = playerbullet.elementAt(i);
							if (boss.isCollsionWith(b)) {
								if (boss.hp <= 0) {
									//游戏胜利
									gameState = GAME_WIN;
								} else {
									//及时删除本次碰撞的子弹，防止重复判定此子弹与Boss碰撞、
									b.isdead = true;
									//Boss血量减1
									boss.setHp(boss.hp - 1);
									//在Boss上添加三个Boss爆炸效果
									booms.add(new Boom(bgBossBoom, boss.x + 25, boss.y + 30, 5));
									booms.add(new Boom(bgBossBoom, boss.x + 35, boss.y + 40, 5));
									booms.add(new Boom(bgBossBoom, boss.x + 45, boss.y + 50, 5));
								}
							}
						}
					}
					countPlayerBullet++;
					if (countPlayerBullet%20==0){

							playerbullet.add(new Bullet(bgBullet,Bullet.BUTTLET_PLAYER,player.x+10,player.y));

					}
					for (int i = 0; i <playerbullet.size() ; i++) {
						Bullet bullet = playerbullet.get(i);
						if (bullet.isdead){
							playerbullet.removeElementAt(i);
						}else{
							bullet.run();
						}

					}



					//player子弹与敌机碰撞处理
					for (int i = 0; i < playerbullet.size(); i++) {
						for (int j = 0; j < enemies.size(); j++) {
							if(enemies.get(j).isCollision(playerbullet
							.get(i))){
								//爆炸
								booms.add(new Boom(bgBoom,enemies.get(j).x,
										enemies.get(j).y,7));

							}

						}

					}
					for (int i = 0; i <booms.size() ; i++) {
						Boom boom = booms.get(i);
						if (boom.isPlayEnd()){
							booms.removeElementAt(i);
						}else{
							boom.run();
						}

					}


					break;
				case GAME_PAUSE:
					break;
				case GAME_LOST:
					break;
				case GAME_WIN:
					break;
				default:
					break;

			}

			draw();

		}


	}

}
