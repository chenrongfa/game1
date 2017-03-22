package chen.yy.com.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Game
 * Created by chenrongfa on 2017/3/21
 * email:18720979339@163.com
 * qq:952786280
 * company:yy
 */

public class Bullet {

	public int x,y;//子弹位置
	public int type;
	public Bitmap bitmap;
	public static final int BUTTLET_PLAYER=-1;
	public static final int BUTTLET_FlY=1;
	public static final int BUTTLET_BOSS=3;
	public static final int BUTTLET_DUCK=2;

	private int speed;
	public boolean isdead;
	//Boss疯狂状态下子弹相关成员变量
	private int dir;//当前Boss子弹方向
	//8方向常量
	public static final int DIR_UP = -1;
	public static final int DIR_DOWN = 2;
	public static final int DIR_LEFT = 3;
	public static final int DIR_RIGHT = 4;
	public static final int DIR_UP_LEFT = 5;
	public static final int DIR_UP_RIGHT = 6;
	public static final int DIR_DOWN_LEFT = 7;
	public static final int DIR_DOWN_RIGHT = 8;

	public Bullet( Bitmap bitmap,int type,int x,int y){
		this.x=x;
		this.y=y;
		this.type=type;
		this.bitmap=bitmap;
		switch (type){
			case BUTTLET_DUCK:
				speed=3;
				break;case BUTTLET_BOSS:
				speed=5;
				break;case BUTTLET_FlY:
				speed=3;
				break;case BUTTLET_PLAYER:
				speed=4;
				break;



		}

	}
	public Bullet( Bitmap bitmap,int type,int x,int y,int dir){
		this.x=x;
		this.y=y;
		this.type=type;
		this.bitmap=bitmap;
	    this.dir=dir;
		speed=5;



		}


	public  void draw(Canvas canvas, Paint paint){
		canvas.drawBitmap(bitmap,x,y,paint);


	}
  public void run() {
	  switch (type) {
		  case BUTTLET_FlY:
		  case BUTTLET_DUCK:
			  if (isdead == false) {
				  y += speed;
				  if (y > PlaneGameSurface.screenH) {
					  isdead = true;
				  }

			  }

			  break;
		  case BUTTLET_BOSS:
			  Log.e("boss", "run: dir"+dir );
			  speed = 5;
			  switch (dir){
				  case DIR_UP:
					  y-=speed;
					  break;
				  case DIR_DOWN:
					  y+=speed;
					  break;
				  case DIR_LEFT:
					  x-=speed;
					  break;
				  case DIR_RIGHT:
					  x+=speed;
					  break;
				  case DIR_DOWN_LEFT:
					  x-=speed;
					  y+=speed;
					  break;
				  case DIR_DOWN_RIGHT:
					  y+=speed;
					  x+=speed;
					  break;
				  case DIR_UP_LEFT:
					  x-=speed;
					  y-=speed;
					  break;
				  case DIR_UP_RIGHT:
					  x+=speed;
					  y-=speed;
					  break;

			  }
			  if (x<=-50||x>=PlaneGameSurface.screenW||y<=-50
					  ||y>PlaneGameSurface.screenH){
				  isdead=true;
			  }
			  break;

		  case BUTTLET_PLAYER:
			  if (isdead==false){
				 y-= speed;
				  if (y<-20){
					  isdead=true;
				  }
			  }
			  break;

	  }
  }
}
