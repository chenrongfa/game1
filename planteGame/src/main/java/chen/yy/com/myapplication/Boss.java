/**
 * 
 */
package chen.yy.com.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Boss
 * @author Himi
 *
 */
public class Boss {
	//Boss的血量
	public int hp = 50;
	//Boss的图片资源
	private Bitmap bmpBoss;
	//Boss坐标
	public int x, y;
	//Boss每帧的宽高
	public int frameW, frameH;
	//Boss当前帧下标
	private int frameIndex;
	//Boss运动的速度
	private int speed = 5;
	//Boss的运动轨迹
	//一定时间会向着屏幕下方运动，并且发射大范围子弹，（是否狂态）
	//正常状态下 ，子弹垂直朝下运动
	private boolean isCrazy;
	//进入疯狂状态的状态时间间隔
	private int crazyTime = 200;
	//计数器
	private int count;

	//Boss的构造函数
	public Boss(Bitmap bmpBoss) {
		this.bmpBoss = bmpBoss;
		frameW = bmpBoss.getWidth() / 10;
		frameH = bmpBoss.getHeight();
		//Boss的X坐标居中
		x = PlaneGameSurface.screenW / 2 - frameW / 2;
		y = 0;
	}

	//Boss的绘制
	public void draw(Canvas canvas, Paint paint) {
		canvas.save();
		canvas.clipRect(x, y, x + frameW, y + frameH);
		canvas.drawBitmap(bmpBoss, x - frameIndex * frameW, y, paint);
		canvas.restore();
	}

	//Boss的逻辑
	public void logic() {
		//不断循环播放帧形成动画
		frameIndex++;
		if (frameIndex >= 10) {
			frameIndex = 0;
		}
		//没有疯狂的状态
		if (isCrazy == false) {
			x += speed;
			if (x + frameW >= PlaneGameSurface.screenW) {
				speed = -speed;
			} else if (x <= 0) {
				speed = -speed;
			}
			count++;
			if (count % crazyTime == 0) {
				isCrazy = true;
				speed = 24;
			}
			//疯狂的状态
		} else {
			speed -= 1;
			//当Boss返回时创建大量子弹
			if (speed == 0) {
				//添加8方向子弹
				PlaneGameSurface.vcBulletBoss.add(new Bullet(PlaneGameSurface.bgBossBullet,  Bullet.BUTTLET_BOSS,x+40, y+10, Bullet.DIR_UP));
				PlaneGameSurface.vcBulletBoss.add(new Bullet(PlaneGameSurface.bgBossBullet,  Bullet.BUTTLET_BOSS,x+40, y+10, Bullet.DIR_DOWN));
				PlaneGameSurface.vcBulletBoss.add(new Bullet(PlaneGameSurface.bgBossBullet,  Bullet.BUTTLET_BOSS, x+40, y+10,Bullet.DIR_LEFT));
				PlaneGameSurface.vcBulletBoss.add(new Bullet(PlaneGameSurface.bgBossBullet,  Bullet.BUTTLET_BOSS,x+40, y+10, Bullet.DIR_RIGHT));
				PlaneGameSurface.vcBulletBoss.add(new Bullet(PlaneGameSurface.bgBossBullet,  Bullet.BUTTLET_BOSS,x+40, y+10, Bullet.DIR_UP_LEFT));
				PlaneGameSurface.vcBulletBoss.add(new Bullet(PlaneGameSurface.bgBossBullet, Bullet.BUTTLET_BOSS, x+40, y+10, Bullet.DIR_UP_RIGHT));
				PlaneGameSurface.vcBulletBoss.add(new Bullet(PlaneGameSurface.bgBossBullet, Bullet.BUTTLET_BOSS, x+40, y+10, Bullet.DIR_DOWN_LEFT));
				PlaneGameSurface.vcBulletBoss.add(new Bullet(PlaneGameSurface.bgBossBullet,  Bullet.BUTTLET_BOSS,x+40, y+10, Bullet.DIR_DOWN_RIGHT));
				
				
				
			}
			y += speed;
			if (y <= 0) {
				//恢复正常状态
				isCrazy = false;
				speed = 5;
			}
		}
	}

	//判断碰撞(Boss被主角子弹击中)
	public boolean isCollsionWith(Bullet bullet) {
		int x2 = bullet.x;
		int y2 = bullet.x;
		int w2 = bullet.bitmap.getWidth();
		int h2 = bullet.bitmap.getHeight();
		if (x >= x2 && x >= x2 + w2) {
			return false;
		} else if (x <= x2 && x + frameW <= x2) {
			return false;
		} else if (y >= y2 && y >= y2 + h2) {
			return false;
		} else if (y <= y2 && y + frameH <= y2) {
			return false;
		}
		return true;
	}

	//设置Boss血量
	public void setHp(int hp) {
		this.hp = hp;
	}
}
