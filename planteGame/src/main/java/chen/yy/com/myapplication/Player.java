package chen.yy.com.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Game
 * Created by chenrongfa on 2017/3/21
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class Player {
	private int hp = 3; //xueliang
	private Bitmap player; //people
	private Bitmap playerhp; //
	public int x, y;//人物的位置
	private int hpY;//血量位置
	private boolean isCollision;
	private int defeatTime=50;//无敌事件
	private int count;
	private int speed = 2; //速度
	private boolean isUp, isDown, isLeft, isRight;
	private static final String TAG = "Player";

	public Player(Bitmap player, Bitmap playerhp) {
		this.player = player;
		this.playerhp = playerhp;
		x = PlaneGameSurface.screenW / 2 - player.getWidth() / 2;
		y = PlaneGameSurface.screenH - player.getHeight();
		hpY = PlaneGameSurface.screenH - playerhp.getHeight();

	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void draw(Canvas canvas, Paint paint) {

		if (isCollision) {
			if (count % 2 == 0) {
				canvas.drawBitmap(player, x, y, paint);
			}
		} else {
			canvas.drawBitmap(player, x, y, paint);
		}
		for (int i = 0; i < hp; i++) {
			canvas.drawBitmap(playerhp, i * playerhp.getWidth(),
					0, paint);
		}
	}

	public void onKeyDown(KeyEvent ev) {
		switch (ev.getKeyCode()) {
			case KeyEvent.KEYCODE_DPAD_UP:
				isUp = true;
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				isDown = true;
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				Log.e("dd", "onKeyDown: left");
				isLeft = true;
				//				direction=LEFT;
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				isRight = true;
				//				direction=RIGHT;
				break;


		}


	}

	public void onKeyUp(KeyEvent ev) {
		switch (ev.getKeyCode()) {
			case KeyEvent.KEYCODE_DPAD_UP:
				isUp = false;
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				isDown = false;
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				isLeft = false;
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				isRight = false;
				break;


		}


	}

	/**
	 *  跟敌机碰撞
	 * @param enemy
	 * @return
	 */
	public boolean isCollision(Enemy enemy) {
		if (!isCollision) {
			int enemyX = enemy.x;
			int enemyY = enemy.y;
			int frameW = enemy.frameW;
			int frameH = enemy.frameH;
			if (x >= enemyX && (enemyX + frameW) <= x) {
				return false;
			} else if (x <= enemyX && (x + player.getWidth()) <= enemyX) {
				return false;
			} else if (y <= enemyY && (y + player.getHeight()) <= enemyY) {
				return false;
			} else if (y >= enemyY && (enemyY + frameH) <= y) {
				return false;
			}

			isCollision = true;

			return true;
		}else {
			return false;
		}
	}/**
	 *  跟子弹碰撞
	 * @param enemy
	 * @return
	 */
	public boolean isCollision(Bullet enemy) {
		if (!isCollision) {
			int enemyX = enemy.x;
			int enemyY = enemy.y;
			int frameW = enemy.bitmap.getWidth();
			int frameH = enemy.bitmap.getHeight();
			if (x >= enemyX && (enemyX + frameW) <= x) {
				return false;
			} else if (x <= enemyX && (x + player.getWidth()) <= enemyX) {
				return false;
			} else if (y <= enemyY && (y + player.getHeight()) <= enemyY) {
				return false;
			} else if (y >= enemyY && (enemyY + frameH) <= y) {
				return false;
			}

			isCollision = true;

			return true;
		}else {
			return false;
		}
	}

	public void run() {
		if (isLeft) {
			x -= speed;
		} else if (isRight) {
			x += speed;
		} else if (isDown) {
			y += speed;
		} else if (isUp) {
			y -= speed;
		}
		if (x + player.getWidth() >= PlaneGameSurface.screenW) {
			x = PlaneGameSurface.screenW - player.getWidth();

		} else if (x <= 0) {
			x = 0;
		}
		if (y + player.getHeight() >= PlaneGameSurface.screenH) {
			y = PlaneGameSurface.screenH - player.getHeight();
		} else if (y <= 0) {
			y = 0;
		}
		if (isCollision) {
			Log.e(TAG, "run: count" +count);
			count++;
			if (count > defeatTime) {
				count = 0;
				isCollision = false;
			}
		}


	}
}
