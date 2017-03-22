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

public class Gamebg {
	private Bitmap back;
	private Bitmap back1;
	private int positionY;
	private int positionY1;
	public Gamebg(Bitmap backGround){
		back=backGround;
		positionY=-Math.abs(back.getHeight()-PlaneGameSurface.screenH);
		positionY1=positionY-back.getHeight()+222;
	}
	public void draw(Canvas canvas, Paint paint){
		canvas.drawBitmap(back,0,positionY,paint);
		canvas.drawBitmap(back,0,positionY1,paint);

	}
	public void run(){
		positionY++;
		positionY1++;
		if (positionY>PlaneGameSurface.screenH-100){
			positionY=positionY1-back.getHeight()+222;
		}
		if(positionY1>PlaneGameSurface.screenH-100){
			positionY1=positionY-back.getHeight()+222;
		}


	}
}
