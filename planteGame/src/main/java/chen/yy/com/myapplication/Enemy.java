package chen.yy.com.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Game
 * Created by chenrongfa on 2017/3/21
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class Enemy {
	public int type;
	public static final  int TYPE_FLY=0;
	public static final  int TYPE_DUCKL=1;
	public static final  int TYPE_DUCKR=2;
	public Bitmap bitmap;
	public int x,y;//坐标
	public int frameW,frameH;
	private int speed;
	public boolean isDead;
	private int currentIndex;
	public Enemy(Bitmap bitmap,int type,int x,int y
	){

		this.bitmap=bitmap;
		this.x=x;
		this.y=y;
		this.type=type;

		switch (type){
			case TYPE_FLY:
				speed=20;
				break;case TYPE_DUCKL:
				speed=3;
				break;case TYPE_DUCKR:
				speed=5;
				break;




		}

		frameW=bitmap.getWidth()/10;
		frameH=bitmap.getHeight();
	}
    public void draw(Canvas canvas, Paint paint){

		canvas.save();
		canvas.clipRect(x,y,x+frameW,y+frameH);
		canvas.drawBitmap(bitmap,x-currentIndex*frameW
				,y,paint);
		canvas.restore();



	}
	public void run(){
		switch (type){
			case TYPE_FLY:
				if(isDead==false){
					speed-=1;
					y+=speed;
					if(y<-50){
						isDead=true;
					}
				}

				break;case TYPE_DUCKL:
				if (isDead==false){
					x+=speed/2;
					y+=speed;
					if(x>PlaneGameSurface.screenW){
						isDead=true;
					}

				}
				break;case TYPE_DUCKR:
				if (isDead==false){
					x-=speed/2;
					y+=speed;
					if(x<-20){
						isDead=true;
					}

				}
				break;




		}


	}
	public boolean isCollision(Bullet enemy) {
		if (!isDead) {
			int enemyX = enemy.x;
			int enemyY = enemy.y;
			int frameW = enemy.bitmap.getWidth();
			int frameH = enemy.bitmap.getHeight();
			if (x >= enemyX && (enemyX + frameW) <= x) {
				return false;
			} else if (x <= enemyX && (x + bitmap.getWidth()) <= enemyX) {
				return false;
			} else if (y <= enemyY && (y + bitmap.getHeight()) <= enemyY) {
				return false;
			} else if (y >= enemyY && (enemyY + frameH) <= y) {
				return false;
			}


		}
		isDead = true;

		return true;
	}
}
